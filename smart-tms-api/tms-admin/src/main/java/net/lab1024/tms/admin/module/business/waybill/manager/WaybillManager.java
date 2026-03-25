package net.lab1024.tms.admin.module.business.waybill.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleDao;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.instance.FlowInstanceService;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceSubmitBO;
import net.lab1024.tms.admin.module.business.material.cost.CostItemDao;
import net.lab1024.tms.admin.module.business.order.service.OrderGoodsService;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillSplitTransportDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillVoucherDao;
import net.lab1024.tms.admin.module.business.waybill.domain.form.*;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillCommonService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;
import net.lab1024.tms.common.module.business.material.cost.domain.CostItemEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.*;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:18 下午
 */
@Slf4j
@Service
public class WaybillManager extends ServiceImpl<WaybillDao, WaybillEntity> {

    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillGoodsManager waybillGoodsManager;
    @Autowired
    private FlowInstanceService flowInstanceService;
    @Autowired
    private CostItemDao costItemDao;
    @Autowired
    private WaybillCostManager waybillCostManager;
    @Autowired
    private WaybillReceiveCostManager waybillReceiveCostManager;
    @Autowired
    private DriverVehicleDao driverVehicleDao;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private WaybillSplitTransportDao waybillSplitTransportDao;
    @Autowired
    private WaybillPathManager waybillPathManager;
    @Autowired
    private WaybillVoucherDao waybillVoucherDao;
    @Autowired
    private WaybillCommonService waybillCommonService;

    /**
     * 创建运单
     *
     * @param waybillEntity
     * @param orderGoodsEntityList
     * @param goodsFormList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void createWaybillHandle(WaybillEntity waybillEntity,
                                    List<OrderGoodsEntity> orderGoodsEntityList,
                                    List<WayBillCreateGoodsForm> goodsFormList,
                                    WayBillCreateForm wayBillCreateForm,
                                    WaybillSplitTransportEntity waybillSplitTransportEntity,
                                    List<WaybillPathEntity> waybillPathEntityList,
                                    DataTracerRequestForm dataTracerRequestForm) {

        waybillDao.insert(waybillEntity);
        Long waybillId = waybillEntity.getWaybillId();
        // 保存路线
        if (CollectionUtils.isNotEmpty(waybillPathEntityList)) {
            waybillPathEntityList.forEach(e -> e.setWaybillId(waybillId));
            waybillPathManager.saveBatch(waybillPathEntityList);
        }
        if (waybillSplitTransportEntity != null) {
            waybillSplitTransportEntity.setWaybillId(waybillId);
            waybillSplitTransportEntity.setDriverId(waybillEntity.getDriverId());
            waybillSplitTransportDao.insert(waybillSplitTransportEntity);
        }
        //货物信息
        this.saveWaybillGoods(orderGoodsEntityList, goodsFormList, waybillId);
        //费用信息
        this.saveWaybillCost(waybillId, wayBillCreateForm.getCashAmount(), wayBillCreateForm.getOilCardAmount());
        //应收费用信息
        this.saveWaybillReceiveCost(waybillId, waybillEntity.getReceiveAmount());
        //保存司机、车辆关联关系
        this.saveDriverVehicle(waybillEntity.getDriverId(), waybillEntity.getVehicleId());
        //更新订单分配状态
        orderGoodsService.updateOrderScheduleFlag(waybillEntity.getOrderId());
        // 提交审核
        this.submitFlow(waybillEntity, dataTracerRequestForm);
    }


    /**
     * 保存司机-车辆关联关系
     *
     * @param driverId
     * @param vehicleId
     */
    private void saveDriverVehicle(Long driverId, Long vehicleId) {
        DriverVehicleEntity driverVehicleEntity = driverVehicleDao.selectByDriverAndVehicle(driverId, vehicleId);
        if (null != driverVehicleEntity) {
            return;
        }
        DriverVehicleEntity insertEntity = new DriverVehicleEntity();
        insertEntity.setDriverId(driverId);
        insertEntity.setVehicleId(vehicleId);
        driverVehicleDao.insert(insertEntity);
    }

    /**
     * 运单费用信息
     *
     * @param waybillId
     */
    private void saveWaybillCost(Long waybillId, BigDecimal cashAmount, BigDecimal oilCardAmount) {
        List<Integer> typeList = Lists.newArrayList(CostItemTypeEnum.PAY.getValue());
        List<CostItemEntity> costItemEntityList = costItemDao.selectByType(typeList, false);
        if (CollectionUtils.isNotEmpty(costItemEntityList)) {
            List<WaybillCostEntity> waybillCostEntityList = Lists.newArrayList();
            for (CostItemEntity costItemEntity : costItemEntityList) {
                WaybillCostEntity waybillCostEntity = new WaybillCostEntity();
                waybillCostEntity.setWaybillId(waybillId);
                waybillCostEntity.setCostItemId(costItemEntity.getCostItemId());
                waybillCostEntity.setCostItemCategory(costItemEntity.getCategory());
                waybillCostEntity.setCostItemType(costItemEntity.getType());
                waybillCostEntity.setCostItemName(costItemEntity.getName());
                waybillCostEntity.setCostAmount(BigDecimal.ZERO);
                if (CostItemCategoryEnum.FREIGHT_COST.equalsValue(costItemEntity.getCategory())) {
                    waybillCostEntity.setCostAmount(cashAmount);
                }
                if (CostItemCategoryEnum.OIL_CARD_COST.equalsValue(costItemEntity.getCategory())) {
                    waybillCostEntity.setCostAmount(oilCardAmount);
                }
                waybillCostEntityList.add(waybillCostEntity);
            }
            waybillCostManager.saveBatch(waybillCostEntityList);
        }
    }

    /**
     * 运单应收费用信息
     *
     * @param waybillId
     */
    private void saveWaybillReceiveCost(Long waybillId, BigDecimal receiveAmount) {
        List<Integer> typeList = Lists.newArrayList(CostItemTypeEnum.RECEIVE.getValue());
        List<CostItemEntity> costItemEntityList = costItemDao.selectByType(typeList, false);
        List<WaybillReceiveCostEntity> waybillReceiveCostEntityList = Lists.newArrayList();
        for (CostItemEntity costItemEntity : costItemEntityList) {
            WaybillReceiveCostEntity waybillReceiveCostEntity = new WaybillReceiveCostEntity();
            waybillReceiveCostEntity.setWaybillId(waybillId);
            waybillReceiveCostEntity.setCostItemId(costItemEntity.getCostItemId());
            waybillReceiveCostEntity.setCostItemCategory(costItemEntity.getCategory());
            waybillReceiveCostEntity.setCostItemType(costItemEntity.getType());
            waybillReceiveCostEntity.setCostItemName(costItemEntity.getName());
            if (CostItemCategoryEnum.FREIGHT_COST.equalsValue(costItemEntity.getCategory())) {
                waybillReceiveCostEntity.setCostAmount(receiveAmount);
            } else {
                waybillReceiveCostEntity.setCostAmount(BigDecimal.ZERO);
            }
            waybillReceiveCostEntityList.add(waybillReceiveCostEntity);
        }
        waybillReceiveCostManager.saveBatch(waybillReceiveCostEntityList);
    }

    /**
     * 运单货物信息
     *
     * @param orderGoodsEntityList
     * @param goodsFormList
     * @param waybillId
     */
    private List<WaybillGoodsEntity> saveWaybillGoods(List<OrderGoodsEntity> orderGoodsEntityList, List<WayBillCreateGoodsForm> goodsFormList, Long waybillId) {
        Map<Long, OrderGoodsEntity> orderGoodsMap = orderGoodsEntityList.stream().collect(Collectors.toMap(OrderGoodsEntity::getOrderGoodsId, Function.identity()));

        List<WaybillGoodsEntity> waybillGoodsEntityList = SmartBeanUtil.copyList(goodsFormList, WaybillGoodsEntity.class);
        waybillGoodsEntityList.forEach(e -> {
            e.setWaybillId(waybillId);

            OrderGoodsEntity orderGoodsEntity = orderGoodsMap.get(e.getOrderGoodsId());
            e.setOrderId(orderGoodsEntity.getOrderId());
            e.setGoodsType(orderGoodsEntity.getGoodsType());
            e.setGoodsName(orderGoodsEntity.getGoodsName());
            e.setGoodsUnit(orderGoodsEntity.getGoodsUnit());
        });
        waybillGoodsManager.saveBatch(waybillGoodsEntityList);
        return waybillGoodsEntityList;
    }

    /**
     * 提交审核
     *
     * @param waybillEntity
     * @return
     */
    private Long submitFlow(WaybillEntity waybillEntity, DataTracerRequestForm dataTracerRequestForm) {
        FlowInstanceSubmitBO flowInstanceSubmitBO = new FlowInstanceSubmitBO();
        flowInstanceSubmitBO.setFlowTypeEnum(FlowTypeEnum.WAY_BILL_AUDIT);
        flowInstanceSubmitBO.setEnterpriseId(waybillEntity.getEnterpriseId());
        flowInstanceSubmitBO.setBusinessId(waybillEntity.getWaybillId());
        flowInstanceSubmitBO.setBusinessCode(waybillEntity.getWaybillNumber());
        flowInstanceSubmitBO.setInitiatorId(waybillEntity.getCreateUserId());
        flowInstanceSubmitBO.setRemark(waybillEntity.getRemark());
        ResponseDTO<Long> flowResp = flowInstanceService.startFlowInstance(flowInstanceSubmitBO, dataTracerRequestForm);
        if (!flowResp.getOk()) {
            throw new BusinessException(flowResp.getMsg());
        }
        Long flowInstanceId = flowResp.getData();
        return flowInstanceId;
    }


    @Transactional(rollbackFor = Throwable.class)
    public void cancelHandle(WaybillEntity waybillEntity, DataTracerRequestForm requestForm) {
        //撤销审批
        if (FlowAuditStatusEnum.WAIT.equalsValue(waybillEntity.getAuditStatus()) && waybillEntity.getFlowInstanceId() != null) {
            ResponseDTO<String> cancelFlowInstance = flowInstanceService.cancelFlowInstance(waybillEntity.getFlowInstanceId(), requestForm);
            if (!cancelFlowInstance.getOk()) {
                throw new BusinessException(cancelFlowInstance.getMsg());
            }
        }
        waybillDao.updateWaybillStatus(waybillEntity.getWaybillId(), WaybillStatusEnum.CANCEL.getValue());
        //更新订单分配状态
        orderGoodsService.updateOrderScheduleFlag(waybillEntity.getOrderId());
    }

    /**
     * 费用维护
     *
     * @param waybillCostForm
     * @param waybillEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void costHandle(WaybillCostForm waybillCostForm, WaybillEntity waybillEntity, BigDecimal taxPoint, DataTracerRequestForm dataTracerRequestForm) {
        this.payCostHandle(waybillCostForm);
        this.receiveCostHandle(waybillCostForm);

        Long waybillId = waybillEntity.getWaybillId();
        //计算合计应收金额
        BigDecimal totalReceiveAmount = waybillCostForm.getReceiveCostItemList().stream().map(e -> e.getCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //计算合计应付金额
        List<WaybillCostEntity> waybillCostEntityList = waybillCostManager.getBaseMapper().selectByWaybillIdList(Lists.newArrayList(waybillId));
        BigDecimal oilCardAmount = waybillCostEntityList.stream().filter(e->CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory())).map(e -> e.getCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cashAmount = waybillCostEntityList.stream().filter(e->!CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory())).map(e -> e.getCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPayableAmount = waybillCostEntityList.stream().map(e -> e.getCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 利润
        BigDecimal profitAmount = waybillCommonService.calculateProfitAmount(totalReceiveAmount, totalPayableAmount, waybillEntity.getSalaryAmount(), waybillEntity.getCarCostAmount(), taxPoint);

        WaybillEntity updateEntity = new WaybillEntity();
        updateEntity.setWaybillId(waybillId);
        updateEntity.setPayableOilCardAmount(oilCardAmount);
        updateEntity.setPayableCashAmount(cashAmount);
        updateEntity.setPayableAmount(totalPayableAmount);
        updateEntity.setReceiveAmount(totalReceiveAmount);
        updateEntity.setProfitAmount(profitAmount);
        waybillDao.updateById(updateEntity);
    }

    /**
     * 应付费用维护
     * @param waybillCostForm
     */
    private void payCostHandle(WaybillCostForm waybillCostForm) {
        List<WaybillCostItemForm> costItemList = waybillCostForm.getCostItemList();
        //更新运单费用项
        List<WaybillCostEntity> updateCostEntityList = Lists.newArrayList();
        for (WaybillCostItemForm waybillCostItemForm : costItemList) {
            WaybillCostEntity waybillCostEntity = new WaybillCostEntity();
            waybillCostEntity.setWaybillCostId(waybillCostItemForm.getWaybillCostId());
            waybillCostEntity.setCostAmount(waybillCostItemForm.getCostAmount());
            waybillCostEntity.setRemark(waybillCostItemForm.getRemark());
            updateCostEntityList.add(waybillCostEntity);
        }
        waybillCostManager.updateBatchById(updateCostEntityList);

    }

    /**
     * 应收费用维护
     * @param waybillCostForm
     */
    private void receiveCostHandle(WaybillCostForm waybillCostForm) {
        //更新运单应收费用项
        List<WaybillReceiveCostItemForm> receiveCostItemList = waybillCostForm.getReceiveCostItemList();
        List<WaybillReceiveCostEntity> updateReceiveCostEntityList = Lists.newArrayList();
        for (WaybillReceiveCostItemForm receiveCostItemForm : receiveCostItemList) {
            WaybillReceiveCostEntity waybillReceiveCostEntity = new WaybillReceiveCostEntity();
            waybillReceiveCostEntity.setWaybillReceiveCostId(receiveCostItemForm.getWaybillReceiveCostId());
            waybillReceiveCostEntity.setCostAmount(receiveCostItemForm.getCostAmount());
            waybillReceiveCostEntity.setRemark(receiveCostItemForm.getRemark());
            updateReceiveCostEntityList.add(waybillReceiveCostEntity);
        }
        waybillReceiveCostManager.updateBatchById(updateReceiveCostEntityList);
    }

    /**
     * 运单审批通过
     *
     * @param waybillId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void waybillAuditPass(Long waybillId) {
        WaybillEntity updateEntity = new WaybillEntity();
        updateEntity.setWaybillId(waybillId);
        updateEntity.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
        updateEntity.setWaybillStatus(WaybillStatusEnum.TRANSPORT.getValue());
        waybillDao.updateById(updateEntity);
    }

    /**
     * 运单审批驳回
     *
     * @param waybillId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void waybillAuditReject(Long waybillId) {
        WaybillEntity updateWaybillEntity = new WaybillEntity();
        updateWaybillEntity.setWaybillId(waybillId);
        updateWaybillEntity.setAuditStatus(FlowAuditStatusEnum.REJECT.getValue());
        updateWaybillEntity.setWaybillStatus(WaybillStatusEnum.CANCEL.getValue());
        waybillDao.updateById(updateWaybillEntity);
        //更新订单分配状态
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        orderGoodsService.updateOrderScheduleFlag(waybillEntity.getOrderId());
    }

    /**
     * 运单审批撤销
     *
     * @param waybillId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void waybillAuditCancel(Long waybillId) {
        WaybillEntity updateWaybillEntity = new WaybillEntity();
        updateWaybillEntity.setWaybillId(waybillId);
        updateWaybillEntity.setAuditStatus(FlowAuditStatusEnum.CANCEL.getValue());
        updateWaybillEntity.setWaybillStatus(WaybillStatusEnum.CANCEL.getValue());
        waybillDao.updateById(updateWaybillEntity);
        //更新订单分配状态
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        orderGoodsService.updateOrderScheduleFlag(waybillEntity.getOrderId());
    }


    /**
     * 计算利润信息 应收金额 - 短驳金额 - 应付金额 - 税金
     *
     * @param receiveAmount    运单信息
     * @param taxPoint 税点
     * @return
     */
    public BigDecimal calculateProfitAmount(BigDecimal receiveAmount, BigDecimal payableAmount, BigDecimal salaryAmount, BigDecimal carCostAmount, BigDecimal taxPoint) {
        // 税金  = 应收 / (1+ 税点) * 税点
        BigDecimal taxAmount = receiveAmount.divide(new BigDecimal("100").add(taxPoint), 10, RoundingMode.HALF_UP);
        taxAmount = SmartBigDecimalUtil.Amount.multiply(taxAmount, taxPoint);

        BigDecimal profitAmount = receiveAmount.subtract(payableAmount).subtract(taxAmount).setScale(SmartBigDecimalUtil.PRICE_DECIMAL_POINT, RoundingMode.HALF_UP);
        return profitAmount;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateWaybill(WaybillUpdateForm updateForm, WaybillEntity waybillEntity, DataTracerRequestForm dataTracerRequestForm){
        waybillDao.updateBasic(updateForm);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addVoucherHandle(WaybillVoucherEntity waybillVoucherEntity, WaybillEntity waybillEntity, BigDecimal poundListQuantity, DataTracerRequestForm dataTracerRequestForm) {
        waybillVoucherDao.insert(waybillVoucherEntity);
        Integer voucherType = waybillVoucherEntity.getType();
        Long waybillId = waybillEntity.getWaybillId();
        if (WaybillVoucherTypeEnum.LOAD.equalsValue(voucherType)) {
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(waybillId);
            updateWaybillEntity.setDeliverGoodsTime(waybillVoucherEntity.getCreateTime());
            updateWaybillEntity.setWaybillStatus(WaybillStatusEnum.TRANSPORT.getValue());
            updateWaybillEntity.setLoadPoundListQuantity(poundListQuantity);
            waybillDao.updateById(updateWaybillEntity);
        }
        if (WaybillVoucherTypeEnum.UNLOAD.equalsValue(voucherType)) {
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(waybillId);
            updateWaybillEntity.setReceiveGoodsTime(waybillVoucherEntity.getCreateTime());
            updateWaybillEntity.setWaybillStatus(WaybillStatusEnum.COMPLETE.getValue());
            updateWaybillEntity.setUnloadPoundListQuantity(poundListQuantity);
            waybillDao.updateById(updateWaybillEntity);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateVoucherHandle(WaybillVoucherEntity waybillVoucherEntity, WaybillEntity waybillEntity, BigDecimal poundListQuantity, DataTracerRequestForm dataTracerRequestForm) {
        waybillVoucherDao.updateById(waybillVoucherEntity);
        Integer voucherType = waybillVoucherEntity.getType();
        Long waybillId = waybillEntity.getWaybillId();
        if (WaybillVoucherTypeEnum.LOAD.equalsValue(voucherType)) {
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(waybillId);
            updateWaybillEntity.setDeliverGoodsTime(waybillVoucherEntity.getCreateTime());
            updateWaybillEntity.setLoadPoundListQuantity(poundListQuantity);
            waybillDao.updateById(updateWaybillEntity);
        }
        if (WaybillVoucherTypeEnum.UNLOAD.equalsValue(voucherType)) {
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(waybillId);
            updateWaybillEntity.setReceiveGoodsTime(waybillVoucherEntity.getCreateTime());
            updateWaybillEntity.setUnloadPoundListQuantity(poundListQuantity);
            waybillDao.updateById(updateWaybillEntity);
        }
    }
}