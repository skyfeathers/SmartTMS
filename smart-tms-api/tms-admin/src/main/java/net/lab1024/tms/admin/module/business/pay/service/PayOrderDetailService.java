package net.lab1024.tms.admin.module.business.pay.service;

import com.google.common.collect.Maps;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderVO;
import net.lab1024.tms.admin.module.business.pay.dao.*;
import net.lab1024.tms.admin.module.business.pay.domain.bo.PayOrderWaybillBO;
import net.lab1024.tms.admin.module.business.pay.domain.vo.*;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillVO;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillDetailService;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.business.pay.domain.*;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 3:06 下午
 */
@Service
public class PayOrderDetailService {

    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private FleetDao fleetDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillDetailService waybillDetailService;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private PayOrderCostDao payOrderCostDao;
    @Autowired
    private PayOrderReceiveDao payOrderReceiveDao;
    @Autowired
    private PayOrderPaymentDao payOrderPaymentDao;
    @Autowired
    private PayOrderVerificationDao payOrderVerificationDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private FlowInstanceDao flowInstanceDao;

    /**
     * 付款单详情
     *
     * @param payOrderId
     * @return
     */
    public PayOrderDetailVO getDetail(Long payOrderId) {
        PayOrderEntity payOrderEntity = payOrderDao.selectById(payOrderId);
        if (payOrderEntity == null) {
            return null;
        }
        PayOrderDetailVO payOrderDetailVO = SmartBeanUtil.copy(payOrderEntity, PayOrderDetailVO.class);
        List<PayOrderVO> buildVoList = Lists.newArrayList(payOrderDetailVO);
        //订单信息
        this.buildOrderInfo(buildVoList);
        //企业信息
        this.buildPayOrderEnterprise(buildVoList);
        //结算对象信息
        this.buildPayOrderSettleObject(buildVoList);
        //收款银行信息
        this.buildPayOrderReceive(buildVoList);
        //付款银行信息
        this.buildPayOrderPayment(buildVoList);
        //付款油卡
        this.buildPayOrderOilCard(buildVoList);
        //核销信息
        this.buildPayOrderVerification(buildVoList);
        //运单信息
        this.buildWayBillInfo(buildVoList);
        //费用信息
        List<PayOrderCostVO> costList = this.getPayOrderCost(payOrderId);
        payOrderDetailVO.setCostList(costList);
        return payOrderDetailVO;
    }

    /**
     * 油卡
     * @param buildVoList
     */
    private void buildPayOrderOilCard(List<PayOrderVO> buildVoList) {
        List<Long> oilCardIds = buildVoList.stream().filter(e->e.getOilCardId() != null).map(PayOrderVO::getOilCardId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oilCardIds)) {
            return;
        }
        List<OilCardEntity> oilCardEntityList = oilCardDao.selectBatchIds(oilCardIds);
        Map<Long, OilCardEntity> oilCardEntityMap = oilCardEntityList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardId, Function.identity()));
        for (PayOrderVO payOrderVO : buildVoList) {
            Long oilCardId = payOrderVO.getOilCardId();
            if (oilCardId == null ) {
                continue;
            }
            OilCardEntity oilCardEntity = oilCardEntityMap.get(oilCardId);
            payOrderVO.setOilCardNo(oilCardEntity.getOilCardNo());
        }
    }

    /**
     * 列表
     * @param payOrderVOList
     */
    public void buildListVo(List<PayOrderVO> payOrderVOList, Long requestUserId) {
        if (CollectionUtils.isEmpty(payOrderVOList)) {
            return;
        }
        //企业信息
        this.buildPayOrderEnterprise(payOrderVOList);
        //结算对象信息
        this.buildPayOrderSettleObject(payOrderVOList);
        //收款银行信息
        this.buildPayOrderReceive(payOrderVOList);
        //付款银行信息
        this.buildPayOrderPayment(payOrderVOList);
        //油卡信息
        this.buildPayOrderOilCard(payOrderVOList);
        //核销信息
        this.buildPayOrderVerification(payOrderVOList);
        //运单
        this.buildWayBillInfo(payOrderVOList);
        // 设置审批人信息
        buildAudit(payOrderVOList, requestUserId);
    }

    /**
     * 设置审批人信息
     *
     * @param payOrderVOList
     * @param requestUserId
     */
    private void buildAudit(List<PayOrderVO> payOrderVOList, Long requestUserId) {
        List<Long> instanceIdList = payOrderVOList.stream().filter(e -> FlowAuditStatusEnum.WAIT.equalsValue(e.getAuditStatus())).map(PayOrderVO::getFlowInstanceId).collect(Collectors.toList());
        Map<Long, List<Long>> handlerIdsMap = Maps.newHashMap();
        Map<Long, String> handlerMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(instanceIdList)) {
            List<FlowInstanceEntity> flowInstanceEntityList = flowInstanceDao.selectBatchIds(instanceIdList);
            handlerIdsMap = flowInstanceEntityList.stream().collect(Collectors.toMap(FlowInstanceEntity::getInstanceId, e -> {
                if (FlowAuditStatusEnum.PROCESSING.equalsValue(e.getAuditStatus())) {
                    return SmartStringUtil.splitConverToLongList(e.getCurrentHandlers(), ",");
                }
                return com.google.common.collect.Lists.newArrayList();
            }));
            handlerMap = flowInstanceEntityList.stream().collect(Collectors.toMap(FlowInstanceEntity::getInstanceId, FlowInstanceEntity::getCurrentHandlerNames));
        }
        for (PayOrderVO payOrderVO : payOrderVOList) {
            //当前审批人
            payOrderVO.setAuditFlag(Boolean.FALSE);
            if (handlerIdsMap.containsKey(payOrderVO.getFlowInstanceId())) {
                List<Long> handlerIds = handlerIdsMap.getOrDefault(payOrderVO.getFlowInstanceId(), com.google.common.collect.Lists.newArrayList());
                payOrderVO.setAuditFlag(handlerIds.contains(requestUserId));
            }
            //当前审批人名称
            payOrderVO.setCurrentAuditor(handlerMap.getOrDefault(payOrderVO.getFlowInstanceId(), StringConst.EMPTY));
        }
    }

    /**
     * 企业信息
     *
     * @param payOrderVOList
     */
    public void buildOrderInfo(List<PayOrderVO> payOrderVOList) {
        Set<Long> orderIdList = payOrderVOList.stream().filter(e -> null != e.getOrderId()).map(PayOrderVO::getOrderId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(orderIdList)) {
            return;
        }
        Map<Long, OrderVO> orderMap = this.getOrderMap(orderIdList);
        for (PayOrderVO payOrderVO : payOrderVOList) {
            if (null == payOrderVO.getOrderId()) {
                continue;
            }
            OrderVO orderVO = orderMap.get(payOrderVO.getOrderId());
            if (orderVO == null) {
                continue;
            }
            payOrderVO.setShipperName(orderVO.getConsignor());
            payOrderVO.setScheduleId(orderVO.getScheduleId());
            payOrderVO.setScheduleName(orderVO.getScheduleName());
            payOrderVO.setLatestPackingTime(orderVO.getLatestPackingTime());
        }
    }

    /**
     * 企业信息
     * @param payOrderVOList
     */
    public void buildPayOrderEnterprise(List<PayOrderVO> payOrderVOList) {
        //所属企业
        Set<Long> enterpriseIdList = payOrderVOList.stream().map(PayOrderVO::getEnterpriseId).collect(Collectors.toSet());
        //nft 企业
        Set<Long> nftEnterpriseIdList = payOrderVOList.stream().filter(e->e.getNftEnterpriseId() != null).map(PayOrderVO::getNftEnterpriseId).collect(Collectors.toSet());
        enterpriseIdList.addAll(nftEnterpriseIdList);
        Map<Long, EnterpriseEntity> enterpriseMap = this.getEnterpriseMap(enterpriseIdList);
        //组装
        for (PayOrderVO payOrderVO : payOrderVOList) {
            EnterpriseEntity enterpriseEntity = enterpriseMap.get(payOrderVO.getEnterpriseId());
            if(enterpriseEntity != null){
                payOrderVO.setEnterpriseName(enterpriseEntity.getEnterpriseName());
            }
            if(payOrderVO.getNftEnterpriseId() != null){
                EnterpriseEntity nftEnterpriseEntity = enterpriseMap.get(payOrderVO.getNftEnterpriseId());
                if(nftEnterpriseEntity != null){
                    payOrderVO.setNftEnterpriseName(nftEnterpriseEntity.getEnterpriseName());
                }
            }

        }
    }

    /**
     * 公司信息
     *
     * @param enterpriseIdList
     * @return
     */
    private Map<Long, EnterpriseEntity> getEnterpriseMap(Collection<Long> enterpriseIdList) {
        Map<Long, EnterpriseEntity> companyMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(enterpriseIdList)) {
            return companyMap;
        }
        List<EnterpriseEntity> enterpriseEntityList = enterpriseDao.selectBatchIds(enterpriseIdList);
        if (CollectionUtils.isNotEmpty(enterpriseEntityList)) {
            companyMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, Function.identity()));
        }
        return companyMap;
    }

    /**
     * 订单信息
     *
     * @param orderIdList
     * @return
     */
    private Map<Long, OrderVO> getOrderMap(Collection<Long> orderIdList) {
        Map<Long, OrderVO> orderMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(orderIdList)) {
            return orderMap;
        }
        List<OrderVO> orderList = orderDao.selectByIdList(orderIdList);
        if (CollectionUtils.isNotEmpty(orderList)) {
            orderMap = orderList.stream().collect(Collectors.toMap(OrderVO::getOrderId, Function.identity()));
        }
        return orderMap;
    }

    /**
     * 付款单费用项
     *
     * @param payOrderId
     * @return
     */
    private List<PayOrderCostVO> getPayOrderCost(Long payOrderId) {

        List<PayOrderCostEntity> payOrderCostEntityList = payOrderCostDao.selectByPayOrderId(payOrderId);
        if (CollectionUtils.isEmpty(payOrderCostEntityList)) {
            return Lists.newArrayList();
        }
        List<PayOrderCostVO> costVOList = SmartBeanUtil.copyList(payOrderCostEntityList, PayOrderCostVO.class);
        //运单信息
        List<Long> waybillIdList = costVOList.stream().map(PayOrderCostVO::getWaybillId).collect(Collectors.toList());
        List<WaybillVO> waybillVOList = waybillDao.selectByWaybillIdList(waybillIdList);
        // 构建司机、车辆、车队、订单信息
        waybillDetailService.buildDriverVehicleFleetOrder(waybillVOList);
        Map<Long, WaybillVO> waybillMap = waybillVOList.stream().collect(Collectors.toMap(WaybillVO::getWaybillId, Function.identity()));
        //合并数据
        for (PayOrderCostVO payOrderCostVO : costVOList) {
            WaybillVO waybillVO = waybillMap.get(payOrderCostVO.getWaybillId());
            if(waybillVO == null){
                continue;
            }
        }
        return costVOList;
    }


    /**
     * 付款信息
     *
     * @param payOrderVOList
     */
    public void buildPayOrderPayment(List<PayOrderVO> payOrderVOList) {
        if (CollectionUtils.isEmpty(payOrderVOList)) {
            return;
        }
        List<Long> payOrderIdList = payOrderVOList.stream().map(PayOrderVO::getPayOrderId).collect(Collectors.toList());
        //查询银行信息
        List<PayOrderPaymentEntity> payOrderPaymentEntityList = payOrderPaymentDao.selectByPayOrderIdList(payOrderIdList);
        List<PayOrderPaymentVO> paymentVOList = SmartBeanUtil.copyList(payOrderPaymentEntityList, PayOrderPaymentVO.class);
        Map<Long, PayOrderPaymentVO> paymentVOMap = paymentVOList.stream().collect(Collectors.toMap(PayOrderPaymentVO::getPayOrderId, Function.identity()));
        //组装
        for (PayOrderVO payOrderVO : payOrderVOList) {
            PayOrderPaymentVO paymentVO = paymentVOMap.get(payOrderVO.getPayOrderId());
            payOrderVO.setPaymentVO(paymentVO);
        }
    }

    /**
     * 收款信息
     *
     * @param payOrderVOList
     */
    public void buildPayOrderReceive(List<PayOrderVO> payOrderVOList) {
        if (CollectionUtils.isEmpty(payOrderVOList)) {
            return;
        }
        List<Long> payOrderIdList = payOrderVOList.stream().map(PayOrderVO::getPayOrderId).collect(Collectors.toList());
        //查询银行信息
        List<PayOrderReceiveEntity> payOrderReceiveEntityList = payOrderReceiveDao.selectByPayOrderIdList(payOrderIdList);
        List<PayOrderReceiveVO> receiveVOList = SmartBeanUtil.copyList(payOrderReceiveEntityList, PayOrderReceiveVO.class);
        Map<Long, PayOrderReceiveVO> receiveVOMap = receiveVOList.stream().collect(Collectors.toMap(PayOrderReceiveVO::getPayOrderId, Function.identity()));
        //组装
        for (PayOrderVO payOrderVO : payOrderVOList) {
            PayOrderReceiveVO receiveVO = receiveVOMap.get(payOrderVO.getPayOrderId());
            payOrderVO.setReceiveVO(receiveVO);
        }
    }

    /**
     * 核销信息
     *
     * @param payOrderVOList
     */
    public void buildPayOrderVerification(List<PayOrderVO> payOrderVOList) {
        if (CollectionUtils.isEmpty(payOrderVOList)) {
            return;
        }
        List<Long> payOrderIdList = payOrderVOList.stream().map(PayOrderVO::getPayOrderId).collect(Collectors.toList());
        //查询银行信息
        List<PayOrderVerificationEntity> payOrderVerificationEntityList = payOrderVerificationDao.selectByPayOrderIdList(payOrderIdList);
        List<PayOrderVerificationVO> verificationVOList = SmartBeanUtil.copyList(payOrderVerificationEntityList, PayOrderVerificationVO.class);
        Map<Long, PayOrderVerificationVO> verificationVOMap = verificationVOList.stream().collect(Collectors.toMap(PayOrderVerificationVO::getPayOrderId, Function.identity()));
        //组装
        for (PayOrderVO payOrderVO : payOrderVOList) {
            PayOrderVerificationVO verificationVO = verificationVOMap.get(payOrderVO.getPayOrderId());
            payOrderVO.setVerificationVO(verificationVO);
        }
    }

    /**
     * 构建付款单结算对象信息
     *
     * @param payOrderVOList
     */
    public void buildPayOrderSettleObject(List<PayOrderVO> payOrderVOList) {
        if (CollectionUtils.isEmpty(payOrderVOList)) {
            return;
        }
        Map<Long, FleetEntity> fleetEntityMap = Maps.newHashMap();
        Map<Long, DriverEntity> driverEntityMap = Maps.newHashMap();
        Map<Long, VehicleEntity> vehicleEntityMap = Maps.newHashMap();

        List<Long> fleetIdList = payOrderVOList.stream().filter(e->e.getFleetId() != null).map(PayOrderVO::getFleetId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fleetIdList)) {
            List<FleetEntity> fleetEntityList = fleetDao.selectBatchIds(fleetIdList);
            fleetEntityMap = fleetEntityList.stream().collect(Collectors.toMap(FleetEntity::getFleetId, Function.identity()));

        }
        List<Long> driverIdList = payOrderVOList.stream().filter(e->e.getDriverId() != null).map(PayOrderVO::getDriverId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(driverIdList)) {
            List<DriverEntity> driverEntityList = driverDao.selectBatchIds(driverIdList);
            driverEntityMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, Function.identity()));

        }

        List<Long> vehicleIdList = payOrderVOList.stream().filter(e->e.getVehicleId() != null).map(PayOrderVO::getVehicleId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(driverIdList)) {
            List<VehicleEntity> driverEntityList = vehicleDao.selectBatchIds(vehicleIdList);
            vehicleEntityMap = driverEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, Function.identity()));

        }
        for (PayOrderVO payOrderVO : payOrderVOList) {
            Long driverId = payOrderVO.getDriverId();
            Long vehicleId = payOrderVO.getVehicleId();
            Long fleetId = payOrderVO.getFleetId();

            DriverEntity driverEntity = driverEntityMap.get(driverId);
            VehicleEntity vehicleEntity = vehicleEntityMap.get(vehicleId);
            FleetEntity fleetEntity = fleetEntityMap.get(fleetId);

            if (driverEntity != null) {
                payOrderVO.setDriverName(driverEntity.getDriverName());
                payOrderVO.setDriverTelephone(driverEntity.getTelephone());
            }

            if (vehicleEntity != null) {
                payOrderVO.setVehicleNumber(vehicleEntity.getVehicleNumber());
            }

            if (fleetEntity != null) {
                payOrderVO.setFleetName(fleetEntity.getFleetName());
                payOrderVO.setFleetCaptainName(fleetEntity.getCaptainName());
                payOrderVO.setFleetCaptainPhone(fleetEntity.getCaptainPhone());
            }
        }

    }


    /**
     * 构建网络货运付款单信息
     * @param payOrderVOList
     */
    public void buildWayBillInfo(List<PayOrderVO> payOrderVOList) {
        if (CollectionUtils.isEmpty(payOrderVOList)) {
            return;
        }
        List<Long> payOrderIdList = payOrderVOList.stream().map(PayOrderVO::getPayOrderId).collect(Collectors.toList());
        // 查询付款单管理的运单信息
        List<PayOrderWaybillBO> payOrderWaybillBOList = payOrderDao.selectWayBillByPayOrderId(payOrderIdList);
        Map<Long,PayOrderWaybillBO> payOrderWaybillMap = payOrderWaybillBOList.stream().collect(Collectors.toMap(PayOrderWaybillBO::getPayOrderId, Function.identity()));

        for (PayOrderVO payOrderVO : payOrderVOList) {
            PayOrderWaybillBO waybillBO = payOrderWaybillMap.get(payOrderVO.getPayOrderId());
            if (waybillBO == null) {
                continue;
            }
            payOrderVO.setWaybillId(waybillBO.getWaybillId());
            payOrderVO.setWaybillNumber(waybillBO.getWaybillNumber());
            payOrderVO.setShipperName(waybillBO.getShipperName());
            payOrderVO.setContainerNumber(waybillBO.getContainerNumber());
            payOrderVO.setReceiptAttachment(waybillBO.getReceiptAttachment());
            payOrderVO.setTruckOrderAttachment(waybillBO.getTruckOrderAttachment());

        }
    }
}