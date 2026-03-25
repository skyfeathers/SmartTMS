package net.lab1024.tms.admin.module.business.pay.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.instance.FlowInstanceService;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceSubmitBO;
import net.lab1024.tms.admin.module.business.oilcard.dao.BalanceRecordDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardBalanceChangeForm;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardService;
import net.lab1024.tms.admin.module.business.pay.dao.*;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardBalanceRecordEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderTypeEnum;
import net.lab1024.tms.common.module.business.pay.domain.*;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 3:38 下午
 */
@Service
public class PayOrderManager extends ServiceImpl<PayOrderDao, PayOrderEntity> {

    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private PayOrderReceiveDao payOrderReceiveDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private PayOrderPaymentDao payOrderPaymentDao;
    @Autowired
    private PayOrderPaymentManager payOrderPaymentManager;
    @Autowired
    private PayOrderCostManager payOrderCostManager;
    @Autowired
    private PayOrderVerificationDao payOrderVerificationDao;
    @Autowired
    private PayOrderVerificationManager payOrderVerificationManager;
    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardService oilCardService;
    @Autowired
    private BalanceRecordDao balanceRecordDao;
    @Autowired
    private FlowInstanceService flowInstanceService;

    /**
     * 付款单创建
     *
     * @param payOrderEntity
     * @param payOrderReceiveEntity
     * @param waybillCostEntityList
     * @param requestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void payOrderCreateHandle(PayOrderEntity payOrderEntity,
                                     PayOrderReceiveEntity payOrderReceiveEntity,
                                     List<WaybillCostEntity> waybillCostEntityList,
                                     DataTracerRequestForm requestForm) {
        payOrderDao.insert(payOrderEntity);
        //收款银行信息
        Long payOrderId = payOrderEntity.getPayOrderId();
        if (payOrderReceiveEntity != null) {
            payOrderReceiveEntity.setPayOrderId(payOrderId);
            payOrderReceiveDao.insert(payOrderReceiveEntity);
        }
        //费用信息
        List<PayOrderCostEntity> costEntityList = SmartBeanUtil.copyList(waybillCostEntityList, PayOrderCostEntity.class);
        costEntityList = costEntityList.stream().filter(e->e.getCostAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        costEntityList.forEach(e -> {
            e.setPayOrderId(payOrderId);
        });
        if (CollectionUtils.isNotEmpty(costEntityList)) {
            payOrderCostManager.saveBatch(costEntityList);
        }
        //同步运单提交付款标识
        this.updateWaybillPayStatusByPayOrderId(payOrderId);
        // 提交审核
        this.submitFlow(payOrderEntity, requestForm);
    }

    /**
     * 提交审核
     *
     * @param payOrderEntity
     * @return
     */
    private Long submitFlow(PayOrderEntity payOrderEntity, DataTracerRequestForm requestForm) {
        FlowInstanceSubmitBO flowInstanceSubmitBO = new FlowInstanceSubmitBO();
        if (PayOrderTypeEnum.CASH.equalsValue(payOrderEntity.getPayOrderType())) {
            flowInstanceSubmitBO.setFlowTypeEnum(FlowTypeEnum.PAY_AUDIT);
        } else {
            flowInstanceSubmitBO.setFlowTypeEnum(FlowTypeEnum.OIL_CARD_AUDIT);
        }
        flowInstanceSubmitBO.setEnterpriseId(payOrderEntity.getEnterpriseId());
        flowInstanceSubmitBO.setBusinessId(payOrderEntity.getPayOrderId());
        flowInstanceSubmitBO.setBusinessCode(payOrderEntity.getPayOrderNumber());
        flowInstanceSubmitBO.setInitiatorId(payOrderEntity.getCreateUserId());
        flowInstanceSubmitBO.setRemark(payOrderEntity.getRemark());
        ResponseDTO<Long> flowResp = flowInstanceService.startFlowInstance(flowInstanceSubmitBO, requestForm);
        if (!flowResp.getOk()) {
            throw new BusinessException(flowResp.getMsg());
        }
        Long flowInstanceId = flowResp.getData();
        return flowInstanceId;
    }


    /**
     * 油卡充值
     * @param oilCardUpdateEntity
     * @param changeForm
     * @param payOrderUpdateEntity
     * @param dataTracerRequestForm
     */
    public void oilCardRecharge(OilCardEntity oilCardUpdateEntity, OilCardBalanceChangeForm changeForm, PayOrderEntity payOrderUpdateEntity, DataTracerRequestForm dataTracerRequestForm) {
        oilCardDao.updateById(oilCardUpdateEntity);
        ResponseDTO<Long> resp = oilCardService.slaveDistribute(changeForm, dataTracerRequestForm);
        if (!resp.getOk()) {
            throw new BusinessException(resp.getMsg());
        }
        Long recordId = resp.getData();
        OilCardBalanceRecordEntity oilCardBalanceRecordEntity = balanceRecordDao.selectById(recordId);
        payOrderUpdateEntity.setSequenceCode(oilCardBalanceRecordEntity.getBalanceRecordNo());
        payOrderDao.updateById(payOrderUpdateEntity);
        //同步运单提交付款标识
        Long payOrderId = payOrderUpdateEntity.getPayOrderId();
        this.updateWaybillPayStatusByPayOrderId(payOrderId);
    }

    /**
     * 付款
     *
     * @param payOrderEntity
     * @param payOrderPaymentEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void payHandle(PayOrderEntity payOrderEntity, PayOrderPaymentEntity payOrderPaymentEntity, DataTracerRequestForm dataTracerRequestForm) {
        //更新付款单
        payOrderDao.updateById(payOrderEntity);
        //付款信息
        payOrderPaymentDao.insert(payOrderPaymentEntity);
        //同步运单提交付款标识
        this.updateWaybillPayStatusByPayOrderId(payOrderEntity.getPayOrderId());
    }


    /**
     * 批量付款
     *
     * @param payOrderEntityList
     * @param payOrderPaymentEntityList
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void batchPayHandle(List<PayOrderEntity> payOrderEntityList, List<PayOrderPaymentEntity> payOrderPaymentEntityList, DataTracerRequestForm dataTracerRequestForm) {
        //更新付款单
        this.updateBatchById(payOrderEntityList);
        //付款信息
        payOrderPaymentManager.saveBatch(payOrderPaymentEntityList);

        List<Long> payOrderIdList = payOrderEntityList.stream().map(PayOrderEntity::getPayOrderId).collect(Collectors.toList());
        this.updateWaybillPayStatusByPayOrderIdList(payOrderIdList);
    }

    /**
     * 核销
     *
     * @param payOrderEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void verificationHandle(PayOrderEntity payOrderEntity, PayOrderVerificationEntity payOrderVerificationEntity) {
        Long payOrderId = payOrderEntity.getPayOrderId();
        //更新付款单
        PayOrderEntity payOrderUpdateEntity = new PayOrderEntity();
        payOrderUpdateEntity.setPayOrderId(payOrderId);
        payOrderUpdateEntity.setVerificationFlag(true);
        payOrderDao.updateById(payOrderUpdateEntity);
        payOrderVerificationDao.insert(payOrderVerificationEntity);
    }

    /**
     * 批量核销
     *
     * @param payOrderEntityList
     * @param verificationEntityList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void batchVerificationHandle(List<PayOrderEntity> payOrderEntityList, List<PayOrderVerificationEntity> verificationEntityList) {
        List<PayOrderEntity> payOrderUpdateEntityList = Lists.newArrayList();
        for (PayOrderEntity payOrderEntity : payOrderEntityList) {
            PayOrderEntity payOrderUpdateEntity = new PayOrderEntity();
            payOrderUpdateEntity.setPayOrderId(payOrderEntity.getPayOrderId());
            payOrderUpdateEntity.setVerificationFlag(true);
            payOrderUpdateEntityList.add(payOrderUpdateEntity);
        }
        this.updateBatchById(payOrderUpdateEntityList);
        payOrderVerificationManager.saveBatch(verificationEntityList);
    }

    /**
     * 审批通过
     *
     * @param payOrderId
     */
    public void payOrderAuditPass(Long payOrderId) {
        PayOrderEntity updatePayOrderEntity = new PayOrderEntity();
        updatePayOrderEntity.setPayOrderId(payOrderId);
        updatePayOrderEntity.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
        payOrderDao.updateById(updatePayOrderEntity);
    }

    /**
     * 审批驳回
     *
     * @param payOrderId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void payOrderAuditReject(Long payOrderId) {
        PayOrderEntity updatePayOrderEntity = new PayOrderEntity();
        updatePayOrderEntity.setPayOrderId(payOrderId);
        updatePayOrderEntity.setAuditStatus(FlowAuditStatusEnum.REJECT.getValue());
        updatePayOrderEntity.setPayOrderStatus(PayOrderStatusEnum.CANCEL.getValue());
        payOrderDao.updateById(updatePayOrderEntity);
        //同步运单提交付款标识
        this.updateWaybillPayStatusByPayOrderId(payOrderId);
    }

    /**
     * 审批撤销
     *
     * @param payOrderId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void payOrderAuditCancel(Long payOrderId) {
        PayOrderEntity updatePayOrderEntity = new PayOrderEntity();
        updatePayOrderEntity.setPayOrderId(payOrderId);
        updatePayOrderEntity.setAuditStatus(FlowAuditStatusEnum.CANCEL.getValue());
        updatePayOrderEntity.setPayOrderStatus(PayOrderStatusEnum.CANCEL.getValue());
        payOrderDao.updateById(updatePayOrderEntity);
        //同步运单提交付款标识
        this.updateWaybillPayStatusByPayOrderId(payOrderId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void cancelPayOrder(Long payOrderId) {
        // 更新付款单为作废i状态
        PayOrderEntity updateEntity = new PayOrderEntity();
        updateEntity.setPayOrderId(payOrderId);
        updateEntity.setPayOrderStatus(PayOrderStatusEnum.CANCEL.getValue());
        payOrderDao.updateById(updateEntity);
        this.updateWaybillPayStatusByPayOrderId(payOrderId);
    }

    public void updateWaybillPayStatusByPayOrderIdList(List<Long> payOrderIdList) {
        List<PayOrderEntity> payOrderEntity = payOrderDao.selectBatchIds(payOrderIdList);
        List<Long> waybillIdList = payOrderEntity.stream().map(PayOrderEntity::getPayOrderId).collect(Collectors.toList());
        this.batchUpdateWaybillPayStatus(waybillIdList);
    }
    /**
     * 更新运单支付状态
     * @param payOrderId
     */
    public void updateWaybillPayStatusByPayOrderId(Long payOrderId) {
        PayOrderEntity payOrderEntity = payOrderDao.selectById(payOrderId);
        this.updateWaybillPayStatus(payOrderEntity.getWaybillId());
    }

    /**
     * 更新运单支付状态
     * @param waybillId
     */
    public void updateWaybillPayStatus(Long waybillId) {
        this.batchUpdateWaybillPayStatus(Lists.newArrayList(waybillId));
    }

    /**
     * 更新运单支付状态
     *
     * @param waybillIdList
     */
    public void batchUpdateWaybillPayStatus(List<Long> waybillIdList) {
        List<PayOrderEntity> payOrderEntityList = payOrderDao.selectByWaybillIdList(waybillIdList, PayOrderStatusEnum.CANCEL.getValue());

        if (CollectionUtils.isEmpty(payOrderEntityList)) {
            List<WaybillEntity> updateWaybillList = Lists.newArrayList();
            for (Long waybillId : waybillIdList) {
                WaybillEntity updateEntity = new WaybillEntity();
                updateEntity.setWaybillId(waybillId);
                updateEntity.setPayStatus(PayOrderStatusEnum.NO_APPLY.getValue());
                updateEntity.setCashPayStatus(PayOrderStatusEnum.NO_APPLY.getValue());
                updateEntity.setOilCardPayStatus(PayOrderStatusEnum.NO_APPLY.getValue());
                updateWaybillList.add(updateEntity);
            }
            waybillManager.updateBatchById(updateWaybillList);
            return;
        }
        // 存在付款单的运单
        Map<Long, List<PayOrderEntity>> payOrderEntityListMap = payOrderEntityList.stream().collect(groupingBy(PayOrderEntity::getWaybillId));
        Set<Long> existWaybillIdSet = payOrderEntityListMap.keySet();
        Set<Long> noPayOrderWaybillIdSet = waybillIdList.stream().filter(e->!existWaybillIdSet.contains(e)).collect(Collectors.toSet());
        // 处理无付款单数据
         if (CollectionUtils.isNotEmpty(noPayOrderWaybillIdSet)) {
            List<WaybillEntity> updateWaybillList = Lists.newArrayList();
            for (Long waybillId : noPayOrderWaybillIdSet) {
                WaybillEntity updateEntity = new WaybillEntity();
                updateEntity.setWaybillId(waybillId);
                updateEntity.setPayStatus(PayOrderStatusEnum.NO_APPLY.getValue());
                updateEntity.setCashPayStatus(PayOrderStatusEnum.NO_APPLY.getValue());
                updateEntity.setOilCardPayStatus(PayOrderStatusEnum.NO_APPLY.getValue());
                updateWaybillList.add(updateEntity);
            }
            waybillManager.updateBatchById(updateWaybillList);
        }
        // 处理存在付款单数据
        if (CollectionUtils.isNotEmpty(existWaybillIdSet)) {
            List<WaybillEntity> updateWaybillList = Lists.newArrayList();
            List<WaybillEntity> dbWaybillEntityList = waybillDao.selectBatchIds(existWaybillIdSet);
            Map<Long,WaybillEntity> dbWaybillEntityMap = dbWaybillEntityList.stream().collect(Collectors.toMap(WaybillEntity::getWaybillId, Function.identity()));
            for (Long waybillId : existWaybillIdSet) {
                WaybillEntity dbWaybillEntity = dbWaybillEntityMap.get(waybillId);
                List<PayOrderEntity> payOrderList = payOrderEntityListMap.get(waybillId);
                Map<Integer,PayOrderEntity> payMap = payOrderList.stream().collect(Collectors.toMap(PayOrderEntity::getPayOrderType, Function.identity()));

                PayOrderEntity oilCardPayOrderEntity = payMap.get(PayOrderTypeEnum.OIL_CARD.getValue());
                PayOrderEntity cashPayOrderEntity = payMap.get(PayOrderTypeEnum.CASH.getValue());
                // 油卡是否需要支付
                Boolean needPayOilCard = dbWaybillEntity.getPayableOilCardAmount().compareTo(BigDecimal.ZERO) > 0;
                // 现金是否需要支付
                Boolean needPayCash = dbWaybillEntity.getPayableOilCardAmount().compareTo(BigDecimal.ZERO) > 0;
                // 油卡支付状态
                Integer oilCardPayStatus = null != oilCardPayOrderEntity ? oilCardPayOrderEntity.getPayOrderStatus() : PayOrderStatusEnum.NO_APPLY.getValue();
                // 现金支付状态
                Integer cashPayStatus = null != cashPayOrderEntity ? cashPayOrderEntity.getPayOrderStatus() : PayOrderStatusEnum.NO_APPLY.getValue();
                // 合计状态
                Integer payStatus = PayOrderStatusEnum.NO_APPLY.getValue();
                // 不需要支付油卡
                if (!needPayOilCard && needPayCash) {
                    payStatus = cashPayStatus;
                }
                // 不需要支付现金
                if (needPayOilCard && !needPayCash) {
                    payStatus = oilCardPayStatus;
                }
                if (needPayOilCard && needPayCash ) {
                    if (oilCardPayStatus < cashPayStatus) {
                        payStatus = oilCardPayStatus;
                    }else {
                        payStatus = cashPayStatus;
                    }
                }
                // 更新数据
                WaybillEntity updateEntity = new WaybillEntity();
                updateEntity.setWaybillId(waybillId);
                updateEntity.setOilCardPayStatus(oilCardPayStatus);
                updateEntity.setCashPayStatus(cashPayStatus);
                updateEntity.setPayStatus(payStatus);
                updateWaybillList.add(updateEntity);
            }
            waybillManager.updateBatchById(updateWaybillList);
        }
    }

}