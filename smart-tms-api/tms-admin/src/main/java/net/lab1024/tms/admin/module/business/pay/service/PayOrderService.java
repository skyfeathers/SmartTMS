package net.lab1024.tms.admin.module.business.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.driver.dao.DriverBankDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetBankDao;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.oa.bank.BankDao;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardBalanceChangeForm;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardService;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderCostDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderPaymentDao;
import net.lab1024.tms.admin.module.business.pay.domain.form.*;
import net.lab1024.tms.admin.module.business.pay.domain.vo.*;
import net.lab1024.tms.admin.module.business.pay.manager.PayOrderManager;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillCostDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillDetailService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.*;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetBankEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.oa.bank.BankEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderTypeEnum;
import net.lab1024.tms.common.module.business.pay.domain.*;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.file.service.FileService;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 3:06 下午
 */
@Slf4j
@Service
public class PayOrderService {

    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private DriverBankDao driverBankDao;
    @Autowired
    private FleetBankDao fleetBankDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillCostDao waybillCostDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private PayOrderManager payOrderManager;
    @Autowired
    private PayOrderDetailService payOrderDetailService;
    @Autowired
    private PayOrderDataTracerService payOrderDataTracerService;
    @Autowired
    private BankDao bankDao;
    @Autowired
    private PayOrderCostDao payOrderCostDao;
    @Autowired
    private WaybillDetailService waybillDetailService;
    @Autowired
    private PayOrderPaymentDao payOrderPaymentDao;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private FlowInstanceDao flowInstanceDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardService oilCardService;
    /**
     * 创建付款单
     *
     * @param payOrderCreateForm
     * @param dataTracerRequestForm
     */
    public ResponseDTO<String> payOrderCreate(PayOrderCreateForm payOrderCreateForm, DataTracerRequestForm dataTracerRequestForm) {
        if (PayOrderTypeEnum.OIL_CARD.equalsValue(payOrderCreateForm.getPayOrderType())) {
            if (payOrderCreateForm.getOilCardId() == null) {
                return ResponseDTO.userErrorParam("油卡ID不能为空");
            }
            OilCardEntity oilCardEntity = oilCardDao.selectById(payOrderCreateForm.getOilCardId());
            if (!oilCardEntity.getEnterpriseId().equals(payOrderCreateForm.getEnterpriseId())) {
                return ResponseDTO.userErrorParam("数据异常");
            }
        }
        if (PayOrderTypeEnum.CASH.equalsValue(payOrderCreateForm.getPayOrderType()) && payOrderCreateForm.getBankId() == null) {
            return ResponseDTO.userErrorParam("银行卡ID不能为空");
        }
        Long waybillId = payOrderCreateForm.getWaybillId();
        PayOrderEntity dbPayOrderEntity = payOrderDao.selectByWaybillIdAndPayType(waybillId, payOrderCreateForm.getPayOrderType(), PayOrderStatusEnum.CANCEL.getValue());
        if (dbPayOrderEntity != null) {
            return ResponseDTO.userErrorParam("已存在相关类型的付款信息，请勿重复申请");
        }
        //运单信息
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        if (waybillEntity == null) {
            return ResponseDTO.userErrorParam("运单不存在");
        }
        if (!payOrderCreateForm.getEnterpriseId().equals(waybillEntity.getEnterpriseId())) {
            return ResponseDTO.userErrorParam("运单数据异常");
        }
        //运单费用项
        List<WaybillCostEntity> waybillCostEntityList = waybillCostDao.selectByWaybillId(waybillId);
        if (PayOrderTypeEnum.CASH.equalsValue(payOrderCreateForm.getPayOrderType())) {
            waybillCostEntityList = waybillCostEntityList.stream().filter(e->!CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory())).collect(Collectors.toList());
        } else {
            waybillCostEntityList = waybillCostEntityList.stream().filter(e->CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory())).collect(Collectors.toList());
        }
        //付款单-收款银行信息
        PayOrderReceiveEntity payOrderReceiveEntity = null;
        if (PayOrderTypeEnum.CASH.equalsValue(payOrderCreateForm.getPayOrderType())) {
            payOrderReceiveEntity = this.payOrderReceiveBank(waybillEntity.getSettleType(), payOrderCreateForm.getBankId());
        }
        //应付总金额
        BigDecimal payableTotalAmount = waybillCostEntityList.stream().map(e -> e.getCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //付款单号
        String payOderNumber = serialNumberService.generate(SerialNumberIdEnum.PAY_ORDER);
        //付款单信息
        PayOrderEntity payOrderEntity = new PayOrderEntity();
        payOrderEntity.setWaybillId(waybillId);
        payOrderEntity.setOrderId(waybillEntity.getOrderId());
        payOrderEntity.setShipperId(waybillEntity.getShipperId());
        payOrderEntity.setPayOrderNumber(payOderNumber);
        payOrderEntity.setPayOrderType(payOrderCreateForm.getPayOrderType());
        payOrderEntity.setEnterpriseId(payOrderCreateForm.getEnterpriseId());
        payOrderEntity.setDriverId(waybillEntity.getDriverId());
        payOrderEntity.setVehicleId(waybillEntity.getVehicleId());
        payOrderEntity.setFleetId(waybillEntity.getFleetId());
        payOrderEntity.setOilCardId(payOrderCreateForm.getOilCardId());
        payOrderEntity.setPayableTotalAmount(payableTotalAmount);
        payOrderEntity.setPaidTotalAmount(BigDecimal.ZERO);
        payOrderEntity.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        payOrderEntity.setPayOrderStatus(PayOrderStatusEnum.NO_PAY.getValue());
        payOrderEntity.setVerificationFlag(false);
        payOrderEntity.setRemark(payOrderCreateForm.getRemark());
        payOrderEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        payOrderEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        // 非网络货运
        payOrderEntity.setOrderType(OrderTypeEnum.ORDINARY.getValue());
        //持久化
        payOrderManager.payOrderCreateHandle(payOrderEntity, payOrderReceiveEntity, waybillCostEntityList, dataTracerRequestForm);

        payOrderDataTracerService.saveLog(payOrderEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 结算对象银行信息
     *
     * @param settleType
     * @param bankId
     * @return
     */
    private PayOrderReceiveEntity payOrderReceiveBank(Integer settleType, Long bankId) {
        WaybillSettleTypeEnum settleTypeEnum = SmartBaseEnumUtil.getEnumByValue(settleType, WaybillSettleTypeEnum.class);
        if (WaybillSettleTypeEnum.DRIVER == settleTypeEnum) {
            return this.payOrderReceiveBankByDriver(bankId);
        }
        return this.payOrderReceiveBankByFleet(bankId);
    }

    /**
     * 结算对象-司机银行信息
     *
     * @param settleObjectBankId
     * @return
     */
    private PayOrderReceiveEntity payOrderReceiveBankByDriver(Long settleObjectBankId) {
        DriverBankEntity driverBankEntity = driverBankDao.selectById(settleObjectBankId);
        if (driverBankEntity == null) {
            return null;
        }
        PayOrderReceiveEntity payOrderReceiveEntity = new PayOrderReceiveEntity();
        payOrderReceiveEntity.setReceiveBankType(driverBankEntity.getBankType());
        payOrderReceiveEntity.setReceiveBankName(driverBankEntity.getBankName());
        payOrderReceiveEntity.setReceiveBankBranchName(driverBankEntity.getBankBranchName());
        payOrderReceiveEntity.setReceiveAccountName(driverBankEntity.getAccountName());
        payOrderReceiveEntity.setReceiveBankAccount(driverBankEntity.getBankAccount());
        return payOrderReceiveEntity;
    }

    /**
     * 结算对象-车队银行信息
     *
     * @param settleObjectBankId
     * @return
     */
    private PayOrderReceiveEntity payOrderReceiveBankByFleet(Long settleObjectBankId) {
        FleetBankEntity fleetBankEntity = fleetBankDao.selectById(settleObjectBankId);
        if (fleetBankEntity == null) {
            return null;
        }
        PayOrderReceiveEntity payOrderReceiveEntity = new PayOrderReceiveEntity();
        payOrderReceiveEntity.setReceiveBankType(fleetBankEntity.getBankType());
        payOrderReceiveEntity.setReceiveBankName(fleetBankEntity.getBankName());
        payOrderReceiveEntity.setReceiveBankBranchName(fleetBankEntity.getBankBranchName());
        payOrderReceiveEntity.setReceiveAccountName(fleetBankEntity.getAccountName());
        payOrderReceiveEntity.setReceiveBankAccount(fleetBankEntity.getBankAccount());
        return payOrderReceiveEntity;
    }

    /**
     * 付款单详情
     *
     * @param payOrderId
     * @return
     */
    public ResponseDTO<PayOrderDetailVO> detail(Long payOrderId) {
        PayOrderDetailVO payOrderDetailVO = payOrderDetailService.getDetail(payOrderId);
        if (payOrderDetailVO == null) {
            return ResponseDTO.userErrorParam("付款单不存在");
        }
        return ResponseDTO.ok(payOrderDetailVO);
    }

    /**
     * 充值
     *
     * @param rechargeForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> oilCardRecharge(OilCardRechargeForm rechargeForm, DataTracerRequestForm dataTracerRequestForm) {
        PayOrderEntity payOrderEntity = payOrderDao.selectById(rechargeForm.getPayOrderId());
        if (payOrderEntity == null) {
            return ResponseDTO.userErrorParam("油卡充值申请单不存在");
        }
        if (!FlowAuditStatusEnum.PASS.equalsValue(payOrderEntity.getAuditStatus())) {
            return ResponseDTO.userErrorParam("只能对审核通过的单据进行充值");
        }
        if (PayOrderStatusEnum.PAID.equalsValue(payOrderEntity.getPayOrderStatus())) {
            return ResponseDTO.userErrorParam("该单据已经充值，无法再次充值");
        }
        //修改油卡归属
        OilCardEntity oilCardUpdateEntity = new OilCardEntity();
        oilCardUpdateEntity.setOilCardId(payOrderEntity.getOilCardId());
        oilCardUpdateEntity.setUseDriverId(payOrderEntity.getDriverId());
        oilCardUpdateEntity.setUseVehicleId(payOrderEntity.getVehicleId());
        oilCardUpdateEntity.setUseTime(LocalDateTime.now());

        //油卡充值
        OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
        changeForm.setOilCardId(payOrderEntity.getOilCardId());
        changeForm.setChangeAmount(payOrderEntity.getPayableTotalAmount());
        changeForm.setRemark(payOrderEntity.getRemark());
        changeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        changeForm.setOperatorName(dataTracerRequestForm.getOperatorName());

        PayOrderEntity payOrderUpdateEntity = new PayOrderEntity();
        payOrderUpdateEntity.setPayOrderId(rechargeForm.getPayOrderId());
        payOrderUpdateEntity.setPayOrderStatus(PayOrderStatusEnum.PAID.getValue());
        payOrderUpdateEntity.setPaidTotalAmount(payOrderEntity.getPayableTotalAmount());
        payOrderUpdateEntity.setAttachment(rechargeForm.getAttachment());
        payOrderUpdateEntity.setPayTime(rechargeForm.getRechargeTime());

        payOrderManager.oilCardRecharge(oilCardUpdateEntity, changeForm,  payOrderUpdateEntity, dataTracerRequestForm);
        payOrderDataTracerService.oilCardPayLog(rechargeForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 付款
     * 一次性全支付完
     *
     * @param payOrderPaymentForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> pay(PayOrderPaymentForm payOrderPaymentForm, DataTracerRequestForm dataTracerRequestForm) {
        Long payOrderId = payOrderPaymentForm.getPayOrderId();
        PayOrderEntity payOrderEntity = payOrderDao.selectById(payOrderId);
        if (payOrderEntity == null) {
            return ResponseDTO.userErrorParam("付款单不存在");
        }
        ResponseDTO<String> checkResp = this.batchPayCheck(Lists.newArrayList(payOrderEntity));
        if (!checkResp.getOk()) {
            return checkResp;
        }
        BankEntity bankEntity = bankDao.selectById(payOrderPaymentForm.getBankId());
        if (bankEntity == null || bankEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("付款银行信息不存在");
        }
        // 付款金额 = 应付金额 - 车辆费用总金额
        BigDecimal payAmount = payOrderEntity.getPayableTotalAmount();
        //付款银行信息
        PayOrderPaymentEntity payOrderPaymentEntity = new PayOrderPaymentEntity();
        payOrderPaymentEntity.setPayOrderId(payOrderId);
        payOrderPaymentEntity.setPayBankName(bankEntity.getBankName());
        payOrderPaymentEntity.setPayAccountName(bankEntity.getAccountName());
        payOrderPaymentEntity.setPayBankAccount(bankEntity.getAccountNumber());

        payOrderPaymentEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        payOrderPaymentEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());

        PayOrderEntity payOrderUpdateEntity = new PayOrderEntity();
        payOrderUpdateEntity.setPayOrderId(payOrderId);
        payOrderUpdateEntity.setPayOrderStatus(PayOrderStatusEnum.PAID.getValue());
        payOrderUpdateEntity.setPaidTotalAmount(payOrderEntity.getPayableTotalAmount());
        payOrderUpdateEntity.setSequenceCode(payOrderPaymentForm.getSequenceCode());
        payOrderUpdateEntity.setAttachment(payOrderPaymentForm.getAttachment());
        payOrderUpdateEntity.setPayTime(payOrderPaymentForm.getPayTime());

        payOrderManager.payHandle(payOrderUpdateEntity, payOrderPaymentEntity, dataTracerRequestForm);

        payOrderDataTracerService.payLog(payOrderPaymentEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量付款
     *
     * @param batchPaymentForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchPay(PayOrderBatchPaymentForm batchPaymentForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> payOrderIdList = batchPaymentForm.getPayOrderIdList();
        List<PayOrderEntity> payOrderEntityList = payOrderDao.selectBatchIds(payOrderIdList);
        ResponseDTO<String> checkResp = this.batchPayCheck(payOrderEntityList);
        if (!checkResp.getOk()) {
            return checkResp;
        }
        //付款银行信息
        BankEntity bankEntity = bankDao.selectById(batchPaymentForm.getBankId());
        if (bankEntity == null || bankEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("付款银行信息不存在");
        }
        List<PayOrderPaymentEntity> payOrderPaymentEntityList = Lists.newArrayList();
        List<PayOrderEntity> payOrderUdateEntityList = Lists.newArrayList();

        for (PayOrderEntity payOrderEntity : payOrderEntityList) {
            PayOrderPaymentEntity payOrderPaymentEntity = new PayOrderPaymentEntity();
            payOrderPaymentEntity.setPayOrderId(payOrderEntity.getPayOrderId());
            payOrderPaymentEntity.setPayBankName(bankEntity.getBankName());
            payOrderPaymentEntity.setPayAccountName(bankEntity.getAccountName());
            payOrderPaymentEntity.setPayBankAccount(bankEntity.getAccountNumber());
            payOrderPaymentEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
            payOrderPaymentEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
            payOrderPaymentEntityList.add(payOrderPaymentEntity);

            PayOrderEntity payOrderUpdateEntity = new PayOrderEntity();
            payOrderUpdateEntity.setPayOrderId(payOrderEntity.getPayOrderId());
            payOrderUpdateEntity.setPayOrderStatus(PayOrderStatusEnum.PAID.getValue());
            payOrderUpdateEntity.setPaidTotalAmount(payOrderEntity.getPayableTotalAmount());
            payOrderUpdateEntity.setSequenceCode(batchPaymentForm.getSequenceCode());
            payOrderUpdateEntity.setAttachment(batchPaymentForm.getAttachment());
            payOrderUpdateEntity.setPayTime(batchPaymentForm.getPayTime());
            payOrderUdateEntityList.add(payOrderUpdateEntity);
        }
        payOrderManager.batchPayHandle(payOrderUdateEntityList, payOrderPaymentEntityList, dataTracerRequestForm);

        payOrderDataTracerService.batchPayLog(payOrderPaymentEntityList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 付款校验
     *
     * @param payOrderEntityList
     * @return
     */
    private ResponseDTO<String> batchPayCheck(List<PayOrderEntity> payOrderEntityList) {
        if (CollectionUtils.isEmpty(payOrderEntityList)) {
            return ResponseDTO.userErrorParam("付款单不存在");
        }
        // 校验
        for (PayOrderEntity payOrderEntity : payOrderEntityList) {
            if (!PayOrderStatusEnum.NO_PAY.equalsValue(payOrderEntity.getPayOrderStatus())) {
                return ResponseDTO.userErrorParam("存在已支付数据，请重新选择");
            }
            if (payOrderEntity.getVerificationFlag()) {
                return ResponseDTO.userErrorParam("存在已核销数据，请重新选择");
            }
            if (!FlowAuditStatusEnum.PASS.equalsValue(payOrderEntity.getAuditStatus())) {
                return ResponseDTO.userErrorParam("存在未审核通过，请重新选择");
            }

        }
        return ResponseDTO.ok();
    }


    /**
     * 核销
     *
     * @param payOrderVerificationForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> verification(PayOrderVerificationForm payOrderVerificationForm, DataTracerRequestForm dataTracerRequestForm) {
        PayOrderEntity payOrderEntity = payOrderDao.selectById(payOrderVerificationForm.getPayOrderId());
        if (payOrderEntity == null) {
            return ResponseDTO.userErrorParam("付款单不存在");
        }
        if (!PayOrderStatusEnum.PAID.equalsValue(payOrderEntity.getPayOrderStatus())) {
            return ResponseDTO.userErrorParam("只能对已支付的单据进行核销");
        }
        if (payOrderEntity.getVerificationFlag()) {
            return ResponseDTO.userErrorParam("该单据已经核销，无法再此核销");
        }
        if (!FlowAuditStatusEnum.PASS.equalsValue(payOrderEntity.getAuditStatus())) {
            return ResponseDTO.userErrorParam("该单据尚未审核通过，无法核销");
        }
        //付款银行信息
        PayOrderVerificationEntity payOrderVerificationEntity = new PayOrderVerificationEntity();
        payOrderVerificationEntity.setPayOrderId(payOrderVerificationForm.getPayOrderId());
        payOrderVerificationEntity.setVerificationAttachment(payOrderVerificationForm.getVerificationAttachment());
        payOrderVerificationEntity.setVerificationRemark(payOrderVerificationForm.getVerificationRemark());
        payOrderVerificationEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        payOrderVerificationEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());

        payOrderManager.verificationHandle(payOrderEntity, payOrderVerificationEntity);

        payOrderDataTracerService.verificationLog(payOrderVerificationEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量核销
     *
     * @param batchVerificationForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchVerification(PayOrderBatchVerificationForm batchVerificationForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> payOrderIdList = batchVerificationForm.getPayOrderIdList();
        List<PayOrderEntity> payOrderEntityList = payOrderDao.selectBatchIds(payOrderIdList);
        ResponseDTO<String> checkResp = this.batchVerificationCheck(payOrderEntityList);
        if (!checkResp.getOk()) {
            return checkResp;
        }
        //核销信息
        List<PayOrderVerificationEntity> verificationEntityList = Lists.newArrayList();
        for (Long payOrderId : payOrderIdList) {
            PayOrderVerificationEntity payOrderVerificationEntity = new PayOrderVerificationEntity();
            payOrderVerificationEntity.setPayOrderId(payOrderId);
            payOrderVerificationEntity.setVerificationAttachment(batchVerificationForm.getVerificationAttachment());
            payOrderVerificationEntity.setVerificationRemark(batchVerificationForm.getVerificationRemark());
            payOrderVerificationEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
            payOrderVerificationEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
            verificationEntityList.add(payOrderVerificationEntity);
        }

        payOrderManager.batchVerificationHandle(payOrderEntityList, verificationEntityList);
        payOrderDataTracerService.batchVerificationLog(verificationEntityList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 付款校验
     *
     * @param payOrderEntityList
     * @return
     */
    private ResponseDTO<String> batchVerificationCheck(List<PayOrderEntity> payOrderEntityList) {
        if (CollectionUtils.isEmpty(payOrderEntityList)) {
            return ResponseDTO.userErrorParam("付款单不存在");
        }
        // 校验
        for (PayOrderEntity payOrderEntity : payOrderEntityList) {
            if (!PayOrderStatusEnum.PAID.equalsValue(payOrderEntity.getPayOrderStatus())) {
                return ResponseDTO.userErrorParam("只能对已支付的单据进行核销");
            }
            if (payOrderEntity.getVerificationFlag()) {
                return ResponseDTO.userErrorParam("存在已经核销的单据，无法再此核销");
            }
            if (!FlowAuditStatusEnum.PASS.equalsValue(payOrderEntity.getAuditStatus())) {
                return ResponseDTO.userErrorParam("存在尚未审核通过的单据，无法核销");
            }
        }
        return ResponseDTO.ok();
    }


    /**
     * 作废应收
     *
     * @param payOrderCancelForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> cancel(PayOrderCancelForm payOrderCancelForm, DataTracerRequestForm dataTracerRequestForm) {
        Long payOrderId = payOrderCancelForm.getPayOrderId();
        PayOrderEntity payOrderEntity = payOrderDao.selectById(payOrderId);
        if (payOrderEntity == null) {
            return ResponseDTO.userErrorParam("付款单不存在");
        }
        if (!FlowAuditStatusEnum.PASS.equalsValue(payOrderEntity.getAuditStatus())) {
            return ResponseDTO.userErrorParam("该单据尚未审核通过，无法作废");
        }
        if (payOrderEntity.getVerificationFlag()) {
            return ResponseDTO.userErrorParam("只能对未核销的单据进行作废");
        }
        if (!PayOrderStatusEnum.NO_PAY.equalsValue(payOrderEntity.getPayOrderStatus()) && !PayOrderStatusEnum.PAID.equalsValue(payOrderEntity.getPayOrderStatus())) {
            return ResponseDTO.userErrorParam("支付中的单据无法作废，请在未支付或支付完成后作废");
        }
        if (payOrderEntity.getVerificationFlag()) {
            return ResponseDTO.userErrorParam("该单据已经核销，无法再此作废");
        }
        payOrderManager.cancelPayOrder(payOrderId);
        payOrderDataTracerService.cancelLog(payOrderId, payOrderCancelForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }



    /**
     * 分页查询付款单
     *
     * @param payOrderQueryForm
     * @return
     */
    public ResponseDTO<PageResult<PayOrderVO>> query(PayOrderQueryForm payOrderQueryForm) {
        if (null != payOrderQueryForm.getWaitAuditByMeFlag() && payOrderQueryForm.getWaitAuditByMeFlag()) {
            payOrderQueryForm.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        }
        payOrderQueryForm.setWaybillNumberList(SmartStringUtil.joinIrregularString(payOrderQueryForm.getWaybillNumbers()));
        Page page = SmartPageUtil.convert2PageQuery(payOrderQueryForm);
        List<PayOrderEntity> list = payOrderDao.query(page, payOrderQueryForm);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseDTO.ok(SmartPageUtil.convert2PageResult(page, Lists.newArrayList()));
        }
        List<PayOrderVO> payOrderVOList = SmartBeanUtil.copyList(list, PayOrderVO.class);
        payOrderDetailService.buildListVo(payOrderVOList, payOrderQueryForm.getRequestUserId());
        PageResult<PayOrderVO> pageResult = SmartPageUtil.convert2PageResult(page, payOrderVOList);
        return ResponseDTO.ok(pageResult);
    }


    /**
     * 统计付款单的应付、已付金额
     *
     * @param payOrderQueryForm
     * @return
     */
    public ResponseDTO<PayOrderAmountStatisticVO> queryAmountStatistics(PayOrderQueryForm payOrderQueryForm) {
        if (null != payOrderQueryForm.getWaitAuditByMeFlag() && payOrderQueryForm.getWaitAuditByMeFlag()) {
            payOrderQueryForm.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        }
        PayOrderAmountStatisticVO amountStatistics = payOrderDao.queryAmountStatistic(payOrderQueryForm);
        return ResponseDTO.ok(amountStatistics);
    }

    /**
     * 根据运单id查付款单列表
     *
     * @param waybillId
     * @param requestUserId
     * @return
     */
    public ResponseDTO<List<PayOrderVO>> queryByWaybill(Long waybillId, Long requestUserId) {
        List<Object> statusList = SmartBaseEnumUtil.differenceValueList(PayOrderStatusEnum.class, PayOrderStatusEnum.CANCEL);
        List<PayOrderEntity> list = payOrderDao.queryByWaybillId(waybillId, statusList);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<PayOrderVO> payOrderVOList = SmartBeanUtil.copyList(list, PayOrderVO.class);
        payOrderDetailService.buildListVo(payOrderVOList, requestUserId);
        return ResponseDTO.ok(payOrderVOList);
    }

    public List<PayOrderExportVO> queryByExport(PayOrderQueryForm payOrderQueryForm) {
        Boolean noCreateTimeSearchFlag = payOrderQueryForm.getStartTime() == null || payOrderQueryForm.getEndTime() == null;
        int maxSearchDays = 0;
        if (!noCreateTimeSearchFlag) {
            maxSearchDays = (int) ChronoUnit.DAYS.between(payOrderQueryForm.getStartTime(), payOrderQueryForm.getEndTime()) + 1;
        }
        if (maxSearchDays > 62) {
            throw new BusinessException("导出时间错误,最大支持两个月的订单导出");
        }
        if (null != payOrderQueryForm.getWaitAuditByMeFlag() && payOrderQueryForm.getWaitAuditByMeFlag()) {
            payOrderQueryForm.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        }

        List<PayOrderEntity> payOrderEntityList = payOrderDao.query(null, payOrderQueryForm);
        if (CollectionUtils.isEmpty(payOrderEntityList)) {
            return Lists.newArrayList();
        }

        List<Long> payOrderIdList = payOrderEntityList.stream().map(PayOrderEntity::getPayOrderId).collect(Collectors.toList());
        List<PayOrderCostEntity> payOrderCostEntityList = payOrderCostDao.selectByPayOrderIdList(payOrderIdList);
        if (CollectionUtils.isEmpty(payOrderCostEntityList)) {
            return Lists.newArrayList();
        }
        Map<Long, PayOrderEntity> payOrderMap = payOrderEntityList.stream().collect(Collectors.toMap(PayOrderEntity::getPayOrderId, Function.identity()));

        List<PayOrderExportVO> costVOList = SmartBeanUtil.copyList(payOrderCostEntityList, PayOrderExportVO.class);
        //运单信息
        List<Long> waybillIdList = costVOList.stream().map(PayOrderExportVO::getWaybillId).collect(Collectors.toList());
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        // 构建司机、车辆
        Map<Long, WaybillEntity> waybillMap = waybillEntityList.stream().collect(Collectors.toMap(WaybillEntity::getWaybillId, Function.identity()));

        // 查询付款信息
        List<PayOrderPaymentEntity> paymentEntityList = payOrderPaymentDao.selectByPayOrderIdList(payOrderIdList);
        Map<Long, PayOrderPaymentEntity> paymentMap = paymentEntityList.stream().collect(Collectors.toMap(PayOrderPaymentEntity::getPayOrderId, Function.identity()));

        // 获取审批人
        List<Long> instanceIdList = payOrderEntityList.stream().filter(e -> FlowAuditStatusEnum.WAIT.equalsValue(e.getAuditStatus())).map(PayOrderEntity::getFlowInstanceId).collect(Collectors.toList());
        Map<Long, String> handlerMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(instanceIdList)) {
            List<FlowInstanceEntity> flowInstanceEntityList = flowInstanceDao.selectBatchIds(instanceIdList);
            handlerMap = flowInstanceEntityList.stream().collect(Collectors.toMap(FlowInstanceEntity::getInstanceId, FlowInstanceEntity::getCurrentHandlerNames));
        }
        // 司机信息
        Map<Long, DriverEntity> driverMap = getDriverMap(waybillEntityList);
        // 车辆信息
        Map<Long, VehicleEntity> vehicleMap = getVehicleMap(waybillEntityList);
        // 订单
        Map<Long, OrderEntity> orderMap = getOrderMap(waybillEntityList);
        // 客户名称
        Map<Long, String> shipperNameMap = getShipperName(orderMap);
        // 获取企业信息
        Map<Long, String> enterpriseNameMap = getEnterpriseName(waybillEntityList);
        // 调度名字
        Map<Long, String> scheduleNameMap = getScheduleName(orderMap.values());

        // 合并数据
        for (int i = 0; i < costVOList.size(); i++) {
            PayOrderExportVO exportVO = costVOList.get(i);
            WaybillEntity waybillEntity = waybillMap.get(exportVO.getWaybillId());
            if (waybillEntity == null) {
                continue;
            }

            PayOrderEntity payOrderEntity = payOrderMap.get(exportVO.getPayOrderId());
            if (payOrderEntity == null) {
                continue;
            }

            exportVO.setIndex(i + 1);
            exportVO.setPayableAmount(SmartBigDecimalUtil.setScale(exportVO.getPayableAmount(), 2));
            // 设置付款单信息
            exportVO.setPayOrderNumber(payOrderEntity.getPayOrderNumber());
            exportVO.setCreateTime(payOrderEntity.getCreateTime());
            exportVO.setAuditStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(payOrderEntity.getAuditStatus(), FlowAuditStatusEnum.class));
            exportVO.setPayOrderRemark(payOrderEntity.getRemark());
            exportVO.setEnterpriseName(enterpriseNameMap.getOrDefault(payOrderEntity.getEnterpriseId(), StringConst.EMPTY));
            //当前审批人名称
            exportVO.setCurrentAuditor(handlerMap.getOrDefault(payOrderEntity.getFlowInstanceId(), StringConst.EMPTY));
            exportVO.setPayOrderStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(payOrderEntity.getPayOrderStatus(), PayOrderStatusEnum.class));

            // 设置付款信息
            if (paymentMap.containsKey(exportVO.getPayOrderId())) {
                PayOrderPaymentEntity paymentEntity = paymentMap.get(exportVO.getPayOrderId());
                exportVO.setPayAccountName(paymentEntity.getPayAccountName());
            }

            // 设置运单信息
            exportVO.setSettleTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(waybillEntity.getSettleType(), WaybillSettleTypeEnum.class));
            exportVO.setWaybillNumber(waybillEntity.getWaybillNumber());
            exportVO.setContainerNumber(waybillEntity.getContainerNumber());
            exportVO.setDeliverGoodsTime(waybillEntity.getDeliverGoodsTime());
            exportVO.setReceiveGoodsTime(waybillEntity.getReceiveGoodsTime());
            exportVO.setWaybillCreateTime(waybillEntity.getCreateTime());
            exportVO.setReceiveAmount(waybillEntity.getReceiveAmount());
            exportVO.setProfitAmount(waybillEntity.getProfitAmount());

            // 设置司机信息
            if (driverMap.containsKey(waybillEntity.getDriverId())) {
                DriverEntity driverEntity = driverMap.get(waybillEntity.getDriverId());
                exportVO.setDriverName(driverEntity.getDriverName());
            }

            // 设置车辆信息
            if (vehicleMap.containsKey(waybillEntity.getVehicleId())) {
                VehicleEntity vehicleEntity = vehicleMap.get(waybillEntity.getVehicleId());
                exportVO.setBusinessModeDesc(SmartBaseEnumUtil.getEnumDescByValue(vehicleEntity.getBusinessMode(), BusinessModeEnum.class));
                exportVO.setVehicleNumber(vehicleEntity.getVehicleNumber());
            }

            // 设置订单信息
            if (orderMap.containsKey(waybillEntity.getOrderId())) {
                OrderEntity orderEntity = orderMap.get(waybillEntity.getOrderId());
                exportVO.setContainerLocation(orderEntity.getContainerLocation());
                exportVO.setPlacingLocation(orderEntity.getPlacingLocation());
                exportVO.setUnloadingLocation(orderEntity.getUnloadingLocation());
                exportVO.setReturnContainerLocation(orderEntity.getReturnContainerLocation());
                exportVO.setConsignor(shipperNameMap.getOrDefault(orderEntity.getShipperId(), StringConst.EMPTY));
                exportVO.setLatestPackingTime(orderEntity.getLatestPackingTime());
                exportVO.setLoadTime(orderEntity.getLoadTime());
                exportVO.setCustomerService(orderEntity.getCreateUserName());
                exportVO.setScheduleName(scheduleNameMap.getOrDefault(orderEntity.getScheduleId(), StringConst.EMPTY));

                // 利润 应收 - 应付 - 税金
                // 税金  = 应收 / (1+ 税点) * 税点
                BigDecimal taxPoint = orderEntity.getTaxPoint();
                BigDecimal taxAmount = exportVO.getReceiveAmount().divide(BigDecimal.valueOf(100).add(taxPoint), 10, RoundingMode.HALF_UP);
                taxAmount = SmartBigDecimalUtil.Amount.multiply(taxAmount, taxPoint);
                exportVO.setTaxAmount(taxAmount);
            }

        }

        return costVOList;
    }

    /**
     * 获取司机信息
     *
     * @param waybillList
     * @return
     */
    private Map<Long, DriverEntity> getDriverMap(List<WaybillEntity> waybillList) {
        Set<Long> driverIdSet = waybillList.stream().map(WaybillEntity::getDriverId).collect(Collectors.toSet());
        return waybillDetailService.getDriverMap(driverIdSet);
    }

    /**
     * 获取车辆信息
     *
     * @param waybillList
     * @return
     */
    private Map<Long, VehicleEntity> getVehicleMap(List<WaybillEntity> waybillList) {
        Set<Long> vehicleIdSet = waybillList.stream().map(WaybillEntity::getVehicleId).collect(Collectors.toSet());
        return waybillDetailService.getVehicleMap(vehicleIdSet);
    }

    /**
     * 获取订单信息
     *
     * @param waybillList
     * @return
     */
    private Map<Long, OrderEntity> getOrderMap(List<WaybillEntity> waybillList) {
        Set<Long> orderIdSet = waybillList.stream().map(WaybillEntity::getOrderId).collect(Collectors.toSet());
        return waybillDetailService.getOrderMap(orderIdSet);
    }

    /**
     * 获取客户名称
     *
     * @param orderMap
     * @return
     */
    private Map<Long, String> getShipperName(Map<Long, OrderEntity> orderMap) {
        Set<Long> shipperIdList = orderMap.values().stream().map(OrderEntity::getShipperId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return Maps.newHashMap();
        }
        List<ShipperEntity> shipperEntityList = shipperDao.selectBatchIds(shipperIdList);
        return shipperEntityList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getConsignor));
    }

    /**
     * 获取客户名称
     *
     * @param waybillList
     * @return
     */
    private Map<Long, String> getEnterpriseName(List<WaybillEntity> waybillList) {
        Set<Long> enterpriseIdList = waybillList.stream().map(WaybillEntity::getEnterpriseId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(enterpriseIdList)) {
            return Maps.newHashMap();
        }
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdList);
        return enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
    }

    /**
     * 获取调度名称
     *
     * @param orderList
     * @return
     */
    private Map<Long, String> getScheduleName(Collection<OrderEntity> orderList) {
        if (CollectionUtils.isEmpty(orderList)) {
            return Maps.newHashMap();
        }
        Set<Long> scheduleIdSet = orderList.stream().map(OrderEntity::getScheduleId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(scheduleIdSet);
        return employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
    }


    /**
     * 获取运单信息
     *
     * @param payOrderQueryForm
     * @return
     */
    public List<PayOrderWaybillExportVO> exportByWaybill(PayOrderQueryForm payOrderQueryForm) {
        // 付款单查询
        Boolean noCreateTimeSearchFlag = payOrderQueryForm.getStartTime() == null || payOrderQueryForm.getEndTime() == null;
        int maxSearchDays = 0;
        if (!noCreateTimeSearchFlag) {
            maxSearchDays = (int) ChronoUnit.DAYS.between(payOrderQueryForm.getStartTime(), payOrderQueryForm.getEndTime()) + 1;
        }
        if (maxSearchDays > 62) {
            throw new BusinessException("导出时间错误,最大支持两个月的订单导出");
        }
        if (null != payOrderQueryForm.getWaitAuditByMeFlag() && payOrderQueryForm.getWaitAuditByMeFlag()) {
            payOrderQueryForm.setAuditStatus(FlowAuditStatusEnum.WAIT.getValue());
        }
        List<PayOrderEntity> payOrderList = payOrderDao.query(null, payOrderQueryForm);
        if (CollectionUtils.isEmpty(payOrderList)) {
            return Lists.newArrayList();
        }

        // 付款项查询，根据运单去重
        List<Long> payOrderIdList = payOrderList.stream().map(PayOrderEntity::getPayOrderId).collect(Collectors.toList());
        List<PayOrderCostEntity> costList = payOrderCostDao.selectByPayOrderIdList(payOrderIdList);
        if (CollectionUtils.isEmpty(costList)) {
            return Lists.newArrayList();
        }
        List<PayOrderWaybillExportVO> exportVOList = costList.stream()
                .collect(Collectors.toMap(
                        e -> e.getPayOrderId() + "_" + e.getWaybillId(),  // 复合键
                        e -> {
                            PayOrderWaybillExportVO exportVO = new PayOrderWaybillExportVO();
                            exportVO.setPayOrderId(e.getPayOrderId());
                            exportVO.setWaybillId(e.getWaybillId());
                            return exportVO;
                        },
                        (existing, replacement) -> existing
                ))
                .values().stream().collect(Collectors.toList());

        // 付款单信息
        Map<Long, PayOrderEntity> payOrderMap = payOrderList.stream().collect(Collectors.toMap(PayOrderEntity::getPayOrderId, Function.identity()));
        //运单信息
        List<Long> waybillIdList = exportVOList.stream().map(PayOrderWaybillExportVO::getWaybillId).collect(Collectors.toList());
        List<WaybillEntity> waybillEntityList = waybillDao.selectBatchIds(waybillIdList);
        Map<Long, WaybillEntity> waybillMap = waybillEntityList.stream().collect(Collectors.toMap(WaybillEntity::getWaybillId, Function.identity()));
        // 司机信息
        Map<Long, DriverEntity> driverMap = getDriverMap(waybillEntityList);
        // 车辆信息
        Map<Long, VehicleEntity> vehicleMap = getVehicleMap(waybillEntityList);

        for (PayOrderWaybillExportVO exportVO : exportVOList) {
            Long payOrderId = exportVO.getPayOrderId();
            Long waybillId = exportVO.getWaybillId();
            Long driverId = null;
            Long vehicleId = null;
            WaybillEntity waybillEntity = null;
            PayOrderEntity payOrderEntity = payOrderMap.get(payOrderId);
            if (waybillMap.containsKey(waybillId)) {
                waybillEntity = waybillMap.get(waybillId);
                driverId = waybillEntity.getDriverId();
                vehicleId = waybillEntity.getVehicleId();
            }
            exportVO.setPayOrderNumber(payOrderEntity.getPayOrderNumber());
            exportVO.setWaybillNumber(waybillEntity == null ? null : waybillEntity.getWaybillNumber());
            if (driverId != null) {
                exportVO.setDriverId(driverId);
                DriverEntity driverEntity = driverMap.get(driverId);
                if (driverEntity != null) {
                    exportVO.setDriverName(driverEntity.getDriverName());
                    exportVO.setDrivingLicense(driverEntity.getDrivingLicense());
                    exportVO.setIdCardFrontPic(driverEntity.getIdCardFrontPic());
                    exportVO.setIdCardFrontPicUrl(fileService.getCacheUrl(driverEntity.getIdCardFrontPic()));
                    exportVO.setIdCardBackPic(driverEntity.getIdCardBackPic());
                    exportVO.setIdCardBackPicUrl(fileService.getCacheUrl(driverEntity.getIdCardBackPic()));
                    exportVO.setTelephone(driverEntity.getTelephone());
                    exportVO.setDrivingLicenseFrontPic(driverEntity.getDrivingLicenseFrontPic());
                    exportVO.setDrivingLicenseFrontPicUrl(fileService.getCacheUrl(driverEntity.getDrivingLicenseFrontPic()));
                    exportVO.setDrivingLicenseBackPic(driverEntity.getDrivingLicenseBackPic());
                    exportVO.setDrivingLicenseBackPicUrl(fileService.getCacheUrl(driverEntity.getDrivingLicenseBackPic()));
                    exportVO.setDrivingLicenseNo(driverEntity.getDrivingLicenseNo());
                    exportVO.setQualificationCertificatePic(driverEntity.getQualificationCertificatePic());
                    exportVO.setQualificationCertificatePicUrl(fileService.getCacheUrl(driverEntity.getQualificationCertificatePic()));
                    exportVO.setQualificationCertificate(driverEntity.getQualificationCertificate());
                }
            }
            if (vehicleId != null) {
                exportVO.setVehicleId(vehicleId);
                VehicleEntity vehicleEntity = vehicleMap.get(vehicleId);
                if (vehicleEntity != null) {
                    exportVO.setRoadTransportCertificateNumber(vehicleEntity.getRoadTransportCertificateNumber());
                    exportVO.setVehicleNumber(vehicleEntity.getVehicleNumber());
                    exportVO.setVehicleType(vehicleEntity.getVehicleType());
                    exportVO.setVehicleTypeDesc(dictCacheService.selectValueNameByValueCode(vehicleEntity.getVehicleType()));
                    exportVO.setVehiclePlateColorCode(vehicleEntity.getVehiclePlateColorCode());
                    exportVO.setVehiclePlateColorDesc(SmartBaseEnumUtil.getEnumDescByValue(vehicleEntity.getVehiclePlateColorCode(), VehiclePlateColorEnum.class));
                    exportVO.setVehicleEnergyType(vehicleEntity.getVehicleEnergyType());
                    exportVO.setVehicleEnergyTypeDesc(dictCacheService.selectValueNameByValueCode(vehicleEntity.getVehicleEnergyType()));
                    exportVO.setGabarite(vehicleEntity.getGabarite());
                    exportVO.setVehicleTonnage(vehicleEntity.getVehicleTonnage());
                    exportVO.setGrossMass(vehicleEntity.getGrossMass());
                    exportVO.setDrivingLicenseAttachment(vehicleEntity.getDrivingLicenseAttachment());
                    exportVO.setDrivingLicenseAttachmentUrl(fileService.getCacheUrl(vehicleEntity.getDrivingLicenseAttachment()));
                    exportVO.setDrivingLicenseEctypeAttachment(vehicleEntity.getDrivingLicenseEctypeAttachment());
                    exportVO.setDrivingLicenseEctypeAttachmentUrl(fileService.getCacheUrl(vehicleEntity.getDrivingLicenseEctypeAttachment()));
                    exportVO.setRoadTransportCertificateAttachment(vehicleEntity.getRoadTransportCertificateAttachment());
                    exportVO.setRoadTransportCertificateAttachmentUrl(fileService.getCacheUrl(vehicleEntity.getRoadTransportCertificateAttachment()));
                }
            }
        }
        return exportVOList;
    }
}