package net.lab1024.tms.admin.module.business.waybill.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.contract.basic.ContractDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.material.businesstype.BusinessTypeService;
import net.lab1024.tms.admin.module.business.material.cabinet.CabinetService;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderCostDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderWaybillDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderWaybillDTO;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperManagerDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.*;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.*;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.contract.constant.ContractStatusEnum;
import net.lab1024.tms.common.module.business.contract.domain.ContractEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.*;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @author yandy
 * @description:
 * @date 2022/8/13 12:28 上午
 */
@Service
public class WaybillDetailService {


    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private FleetDao fleetDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillGoodsDao waybillGoodsDao;
    @Autowired
    private WaybillCostDao waybillCostDao;
    @Autowired
    private WaybillReceiveCostDao waybillReceiveCostDao;
    @Autowired
    private WaybillPathDao waybillPathDao;
    @Autowired
    private WaybillVoucherDao waybillVoucherDao;
    @Autowired
    private CabinetService cabinetService;
    @Autowired
    private BusinessTypeService businessTypeService;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Resource
    private WaybillExceptionDao waybillExceptionDao;

    /**
     * 运单详情
     *
     * @param waybillId
     * @return
     */
    public WaybillDetailVO getDetail(Long waybillId) {
        WaybillVO waybillVO = waybillDao.detail(waybillId);
        if (waybillVO == null) {
            return null;
        }
        WaybillDetailVO waybillDetailVO = SmartBeanUtil.copy(waybillVO, WaybillDetailVO.class);
        // 构建司机、车辆、车队、订单信息
        this.buildDriverVehicleFleetOrder(Lists.newArrayList(waybillDetailVO));
        this.buildPoundListAttachment(Lists.newArrayList(waybillDetailVO));
        // 商品信息
        this.buildGoodsInfo(Lists.newArrayList(waybillDetailVO));
        // 应付费用信息
        this.buildCostInfo(Lists.newArrayList(waybillDetailVO));
        // 应收费用
        this.buildReceiveCostInfo(Lists.newArrayList(waybillDetailVO));
        return waybillDetailVO;
    }

    /**
     * 根据收款单查询运单
     *
     * @param waybillIdList
     * @return
     */
    public List<WaybillCostDetailVO> getWaybillCostByIdList(List<Long> waybillIdList) {
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Lists.newArrayList();
        }
        List<WaybillVO> waybillVOList = waybillDao.selectByWaybillIdList(waybillIdList);
        return this.getWaybillCostByVoList(waybillVOList);
    }

    /**
     * 根据收款单查询运单
     *
     * @param waybillVOList
     * @return
     */
    public List<WaybillCostDetailVO> getWaybillCostByVoList(List<WaybillVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return Lists.newArrayList();
        }
        List<Long> waybillIdList = waybillVOList.stream().map(WaybillVO::getWaybillId).collect(Collectors.toList());
        // 构建司机、车辆、车队、订单信息
        this.buildDriverVehicleFleetOrder(waybillVOList);
        List<WaybillCostDetailVO> waybillCostDetailVOList = SmartBeanUtil.copyList(waybillVOList, WaybillCostDetailVO.class);
        // 费用明细
        List<WaybillReceiveCostEntity> receiveCostEntityList = waybillReceiveCostDao.selectByWaybillIdList(waybillIdList);
        List<WaybillCostEntity> payCostEntityList = waybillCostDao.selectByWaybillIdList(waybillIdList);

        Map<Long, List<WaybillReceiveCostEntity>> receiveCostEntityListMap = receiveCostEntityList.stream().collect(groupingBy(WaybillReceiveCostEntity::getWaybillId));
        Map<Long, List<WaybillCostEntity>> payCostEntityListMap = payCostEntityList.stream().collect(groupingBy(WaybillCostEntity::getWaybillId));

        Set<Long> scheduleIdList = waybillVOList.stream().map(WaybillVO::getScheduleId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(scheduleIdList);
        Map<Long, String> employeeNameMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));

        // 组装费用明细
        for (WaybillCostDetailVO waybillCostDetailVO : waybillCostDetailVOList) {
            Long waybillId = waybillCostDetailVO.getWaybillId();
            List<WaybillReceiveCostEntity> waybillReceiveCostEntityList = receiveCostEntityListMap.getOrDefault(waybillId, Lists.newArrayList());
            List<WaybillCostEntity> waybillPayCostEntityList = payCostEntityListMap.getOrDefault(waybillId, Lists.newArrayList());
            waybillCostDetailVO.setScheduleName(employeeNameMap.getOrDefault(waybillCostDetailVO.getScheduleId(), StringConst.EMPTY));
            waybillCostDetailVO.setCostList(SmartBeanUtil.copyList(waybillPayCostEntityList, WaybillCostVO.class));
            waybillCostDetailVO.setReceiveCostList(SmartBeanUtil.copyList(waybillReceiveCostEntityList, WaybillReceiveCostVO.class));
        }
        return waybillCostDetailVOList;
    }


    /**
     * 商品信息
     *
     * @param waybillVOList
     */
    public void buildGoodsInfo(List<WaybillDetailVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        List<Long> waybillIdList = waybillVOList.stream().map(WaybillVO::getWaybillId).collect(Collectors.toList());
        // 运单货物信息
        List<WaybillGoodsEntity> waybillGoodsEntityList = waybillGoodsDao.selectByWaybillIdList(waybillIdList);
        if (CollectionUtils.isEmpty(waybillGoodsEntityList)) {
            waybillVOList.forEach(e -> e.setGoodsList(Lists.newArrayList()));
            return;
        }
        //合并数据
        List<WaybillGoodsVO> waybillGoodsVOList = SmartBeanUtil.copyList(waybillGoodsEntityList, WaybillGoodsVO.class);
        Map<Long, List<WaybillGoodsVO>> waybillGoodsListMap = waybillGoodsVOList.stream().collect(groupingBy(WaybillGoodsVO::getWaybillId));

        for (WaybillDetailVO waybillDetailVO : waybillVOList) {
            Long waybillId = waybillDetailVO.getWaybillId();
            //运单货物
            List<WaybillGoodsVO> goodsList = waybillGoodsListMap.getOrDefault(waybillId, Lists.newArrayList());

            for (WaybillGoodsVO waybillGoodsVO : goodsList) {
                waybillGoodsVO.setGoodsTypeName(dictCacheService.selectValueNameByValueCode(waybillGoodsVO.getGoodsType()));
            }
            waybillDetailVO.setGoodsList(goodsList);
        }
    }

    /**
     * 费用信息
     *
     * @param waybillVOList
     */
    public void buildCostInfo(List<WaybillDetailVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        List<Long> waybillIdList = waybillVOList.stream().map(WaybillVO::getWaybillId).collect(Collectors.toList());
        // 运单费用信息
        List<WaybillCostEntity> waybillCostEntityList = waybillCostDao.selectByWaybillIdList(waybillIdList);
        if (CollectionUtils.isEmpty(waybillCostEntityList)) {
            waybillVOList.forEach(e -> e.setCostList(Lists.newArrayList()));
            return;
        }
        List<WaybillCostVO> waybillCostVOList = SmartBeanUtil.copyList(waybillCostEntityList, WaybillCostVO.class);
        //待支付的费用信息
        Map<Long, List<WaybillCostVO>> waybillCostListMap = waybillCostVOList.stream().collect(groupingBy(WaybillCostVO::getWaybillId));
        waybillVOList.forEach(e -> e.setCostList(waybillCostListMap.getOrDefault(e.getWaybillId(), Lists.newArrayList())));
    }

    /**
     * 费用信息
     *
     * @param waybillVOList
     */
    public void buildReceiveCostInfo(List<WaybillDetailVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        List<Long> waybillIdList = waybillVOList.stream().map(WaybillVO::getWaybillId).collect(Collectors.toList());
        // 运单费用信息
        List<WaybillReceiveCostEntity> waybillReceiveCostEntityList = waybillReceiveCostDao.selectByWaybillIdList(waybillIdList);
        if (CollectionUtils.isEmpty(waybillReceiveCostEntityList)) {
            waybillVOList.forEach(e -> e.setReceiveCostList(Lists.newArrayList()));
            return;
        }
        List<WaybillReceiveCostVO> waybillReceiveCostVOList = SmartBeanUtil.copyList(waybillReceiveCostEntityList, WaybillReceiveCostVO.class);
        Map<Long, List<WaybillReceiveCostVO>> waybillReceiveCostListMap = waybillReceiveCostVOList.stream().collect(groupingBy(WaybillReceiveCostVO::getWaybillId));
        waybillVOList.forEach(e -> e.setReceiveCostList(waybillReceiveCostListMap.getOrDefault(e.getWaybillId(), Lists.newArrayList())));
    }

    /**
     * 构建司机、车辆、车队、订单、货主信息
     *
     * @param waybillVOList
     */
    public void buildDriverVehicleFleetOrder(List<WaybillVO> waybillVOList) {
        if (CollectionUtils.isEmpty(waybillVOList)) {
            return;
        }
        Set<Long> orderManagerIdSet = waybillVOList.stream().filter(e->e.getOrderManagerId() != null).map(WaybillVO::getOrderManagerId).collect(Collectors.toSet());
        Map<Long, String> orderManagerEmployeeNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(orderManagerIdSet)) {
            List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(orderManagerIdSet);
            orderManagerEmployeeNameMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));
        }
        //司机信息
        Set<Long> driverIdList = waybillVOList.stream().map(WaybillVO::getDriverId).collect(Collectors.toSet());
        Map<Long, DriverEntity> driverMap = this.getDriverMap(driverIdList);
        //车辆
        Set<Long> vehicleIdList = waybillVOList.stream().map(WaybillVO::getVehicleId).collect(Collectors.toSet());
        Map<Long, VehicleEntity> vehicleMap = this.getVehicleMap(vehicleIdList);
        //车队
        Set<Long> fleetIdList = waybillVOList.stream().filter(e -> e.getFleetId() != null).map(WaybillVO::getFleetId).collect(Collectors.toSet());
        Map<Long, FleetEntity> fleetMap = this.getFleetMap(fleetIdList);
        //货主
        Set<Long> shipperIdList = waybillVOList.stream().map(WaybillVO::getShipperId).collect(Collectors.toSet());
        Map<Long, ShipperManagerDTO> shipperMap = this.getShipperMap(shipperIdList);
        //运单所属企业
        Set<Long> enterpriseIdList = waybillVOList.stream().map(WaybillVO::getEnterpriseId).collect(Collectors.toSet());
        //合同
        List<Long> waybillIdList = waybillVOList.stream().map(WaybillVO::getWaybillId).collect(Collectors.toList());
        Map<Long, ContractEntity> contractMap = this.getContractMap(waybillIdList);
        Map<Long, EnterpriseEntity> enterpriseMap = this.getEnterpriseMap(enterpriseIdList);
        // 柜型
        List<Long> cabinetIdList = waybillVOList.stream().filter(e -> e.getCabinetId() != null).map(WaybillVO::getCabinetId).collect(Collectors.toList());
        Map<Long, String> cabinetNameMap = cabinetService.getCabinetMap(cabinetIdList);
        // 获取业务类型
        Set<Long> businessTypeIdList = waybillVOList.stream().map(WaybillVO::getContainerBusinessTypeId).collect(Collectors.toSet());
        Map<Long, String> businessTypeNameMap = businessTypeService.getBusinessTypeMap(businessTypeIdList);
        // 运单异常信息
        List<WaybillExceptionEntity> waybillExceptionEntities = waybillExceptionDao.selectByWaybillIdList(waybillIdList, false);
        Map<Long, List<WaybillExceptionVO>> waybillExceptionMap = waybillExceptionEntities.stream().collect(groupingBy(WaybillExceptionEntity::getWaybillId, mapping(e -> SmartBeanUtil.copy(e, WaybillExceptionVO.class), toList())));

        Set<Long> scheduleIdList = waybillVOList.stream().map(WaybillVO::getScheduleId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(scheduleIdList);
        Map<Long, String> employeeNameMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));

        // 路线
        List<WaybillPathEntity> pathList = waybillPathDao.selectByWaybillIdList(waybillIdList);
        Map<Long, List<WaybillPathEntity>> pathMap = pathList.stream().collect(groupingBy(WaybillPathEntity::getWaybillId));
        //进行注入
        for (WaybillVO waybillVO : waybillVOList) {

            waybillVO.setOrderManagerName(orderManagerEmployeeNameMap.getOrDefault(waybillVO.getOrderManagerId(), ""));
            //设置调度人姓名
            waybillVO.setScheduleName(employeeNameMap.get(waybillVO.getScheduleId()));
            // 结算类型描述
            waybillVO.setSettleTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(waybillVO.getSettleType(), WaybillSettleTypeEnum.class));
            // 利润 应收 - 应付 - 税金
            // 税金  = 应收 / (1+ 税点) * 税点
            BigDecimal taxPoint = waybillVO.getTaxPoint();

            BigDecimal taxAmount = waybillVO.getReceiveAmount().divide(BigDecimal.valueOf(100).add(taxPoint), 10, RoundingMode.HALF_UP);
            taxAmount = SmartBigDecimalUtil.Amount.multiply(taxAmount, taxPoint);
            waybillVO.setTaxAmount(taxAmount);
            //柜型
            waybillVO.setCabinetName(cabinetNameMap.get(waybillVO.getCabinetId()));
            //业务类型名称
            waybillVO.setContainerBusinessTypeName(businessTypeNameMap.get(waybillVO.getContainerBusinessTypeId()));
            // 异常信息
            waybillVO.setExceptionList(waybillExceptionMap.getOrDefault(waybillVO.getWaybillId(), Lists.newArrayList()));
            // 合同文件
            ContractEntity contractEntity = contractMap.get(waybillVO.getWaybillId());
            if (contractEntity != null) {
                waybillVO.setContractFile(contractEntity.getContractFile());
                waybillVO.setContractCode(contractEntity.getContractCode());
            }
            // 订单相关信息
            this.buildOrderInfo(waybillVO, shipperMap, enterpriseMap);
            // 路线
            List<WaybillPathEntity> waybillPathList = pathMap.getOrDefault(waybillVO.getWaybillId(), Lists.newArrayList());
            waybillPathList.sort((Comparator.comparing(WaybillPathEntity::getType)));
            waybillVO.setPathList(SmartBeanUtil.copyList(waybillPathList, WaybillPathVO.class));
            // 司机、车辆、车队
            DriverEntity driverEntity = driverMap.get(waybillVO.getDriverId());
            if (driverEntity != null) {
                waybillVO.setDriverName(driverEntity.getDriverName());
                waybillVO.setDriverTelephone(driverEntity.getTelephone());
            }
            VehicleEntity vehicleEntity = vehicleMap.get(waybillVO.getVehicleId());
            if (vehicleEntity != null) {
                waybillVO.setVehicleNumber(vehicleEntity.getVehicleNumber());
            }

            if (waybillVO.getFleetId() == null) {
                continue;
            }
            FleetEntity fleetEntity = fleetMap.get(waybillVO.getFleetId());
            if (fleetEntity != null) {
                waybillVO.setFleetName(fleetEntity.getFleetName());
                waybillVO.setFleetCaptainPhone(fleetEntity.getCaptainPhone());
            }

        }
    }


    /**
     * 榜单
     * @param list
     */
    public void buildPoundListAttachment(List<WaybillVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> waybillIdList = list.stream().map(WaybillVO::getWaybillId).collect(Collectors.toList());
        // 运单凭证信息
        List<WaybillVoucherEntity> voucherEntityList = waybillVoucherDao.selectByWaybillIdList(waybillIdList);
        if (CollectionUtils.isEmpty(voucherEntityList)) {
            return;
        }
        //合并数据
        Map<Long,WaybillVoucherEntity> waybillLoadVoucherMap = voucherEntityList.stream().filter(e-> WaybillVoucherTypeEnum.LOAD.equalsValue(e.getType())).collect(toMap(WaybillVoucherEntity::getWaybillId, Function.identity()));

        Map<Long,WaybillVoucherEntity> waybillUnloadVoucherMap = voucherEntityList.stream().filter(e-> WaybillVoucherTypeEnum.UNLOAD.equalsValue(e.getType())).collect(toMap(WaybillVoucherEntity::getWaybillId, Function.identity()));
        for (WaybillVO waybillDetailVO : list) {
            Long waybillId = waybillDetailVO.getWaybillId();

            WaybillVoucherEntity waybillLoadVoucher = waybillLoadVoucherMap.get(waybillId);
            if (waybillLoadVoucher != null) {
                waybillDetailVO.setLoadPoundListAttachment(waybillLoadVoucher.getPoundListAttachment());
            }

            WaybillVoucherEntity waybillUnloadVoucher = waybillUnloadVoucherMap.get(waybillId);
            if (waybillUnloadVoucher != null) {
                waybillDetailVO.setUnloadPoundListAttachment(waybillUnloadVoucher.getPoundListAttachment());
            }
        }
    }


    private void buildOrderInfo(WaybillVO waybillVO, Map<Long, ShipperManagerDTO> shipperMap, Map<Long, EnterpriseEntity> enterpriseMap) {
        ShipperManagerDTO shipperManagerDTO = shipperMap.get(waybillVO.getShipperId());
        if (shipperManagerDTO != null) {
            waybillVO.setShipperId(shipperManagerDTO.getShipperId());
            waybillVO.setShipperName(shipperManagerDTO.getConsignor());
            waybillVO.setShortName(shipperManagerDTO.getShortName());
            waybillVO.setShipperManagerName(shipperManagerDTO.getManagerName());
        }
        EnterpriseEntity enterpriseEntity = enterpriseMap.get(waybillVO.getEnterpriseId());
        if (enterpriseEntity != null) {
            waybillVO.setEnterpriseName(enterpriseEntity.getEnterpriseName());
        }
    }

    /**
     * 订单信息
     *
     * @param orderIdList
     * @return
     */
    public Map<Long, OrderEntity> getOrderMap(Collection<Long> orderIdList) {
        Map<Long, OrderEntity> orderMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(orderIdList)) {
            return orderMap;
        }
        List<OrderEntity> orderEntityList = orderDao.selectBatchIds(orderIdList);
        if (CollectionUtils.isNotEmpty(orderEntityList)) {
            orderMap = orderEntityList.stream().collect(Collectors.toMap(OrderEntity::getOrderId, Function.identity()));
        }
        return orderMap;
    }

    /**
     * 应收单号
     *
     * @param waybillIdList
     * @return
     */
    private Map<Long, List<String>> getReceiveOrderMap(Collection<Long> waybillIdList) {
        Map<Long, List<String>> receiveOrderMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return receiveOrderMap;
        }
        List<ReceiveOrderWaybillDTO> waybillDTOList = receiveOrderDao.selectByWaybillIdList(waybillIdList, CheckStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isNotEmpty(waybillDTOList)) {
            receiveOrderMap = waybillDTOList.stream().collect(Collectors.groupingBy(ReceiveOrderWaybillDTO::getWaybillId, Collectors.mapping(ReceiveOrderWaybillDTO::getReceiveOrderNumber, Collectors.toList())));
        }
        return receiveOrderMap;
    }


    /**
     * 合同
     *
     * @param waybillIdList
     * @return
     */
    private Map<Long, ContractEntity> getContractMap(Collection<Long> waybillIdList) {
        Map<Long, ContractEntity> contractMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return contractMap;
        }
        List<ContractEntity> contractEntityList = contractDao.selectByWaybillIdListAndExcludeStatus(waybillIdList, ContractStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            contractEntityList.forEach(e -> {
                if (StringUtils.isBlank(e.getContractFile())) {
                    e.setContractFile("");
                }
            });
            contractMap = contractEntityList.stream().collect(Collectors.toMap(ContractEntity::getWaybillId, Function.identity()));
        }
        return contractMap;
    }

    /**
     * 货主信息
     *
     * @param shipperIdList
     * @return
     */
    private Map<Long, ShipperManagerDTO> getShipperMap(Collection<Long> shipperIdList) {
        Map<Long, ShipperManagerDTO> shipperMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(shipperIdList)) {
            return shipperMap;
        }
        List<ShipperEntity> shipperEntityList = shipperDao.selectBatchIds(shipperIdList);

        List<ShipperPrincipalDTO> shipperPrincipalList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdList, PrincipalTypeEnum.MANAGER.getValue());
        Map<Long, List<String>> managerNameMap = shipperPrincipalList.stream().collect(Collectors.groupingBy(ShipperPrincipalDTO::getShipperId, Collectors.mapping(ShipperPrincipalDTO::getEmployeeName, Collectors.toList())));

        for (ShipperEntity shipperEntity : shipperEntityList) {
            ShipperManagerDTO shipperManagerDTO = SmartBeanUtil.copy(shipperEntity, ShipperManagerDTO.class);
            shipperManagerDTO.setManagerName(SmartStringUtil.join(CommonConst.SEPARATOR, managerNameMap.getOrDefault(shipperEntity.getShipperId(), Lists.newArrayList())));
            shipperMap.put(shipperEntity.getShipperId(),shipperManagerDTO);
        }
        return shipperMap;
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
     * 司机信息
     *
     * @param driverIdList
     * @return
     */
    public Map<Long, DriverEntity> getDriverMap(Collection<Long> driverIdList) {
        Map<Long, DriverEntity> driverMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(driverIdList)) {
            return driverMap;
        }
        List<DriverEntity> driverEntityList = driverDao.selectBatchIds(driverIdList);
        if (CollectionUtils.isNotEmpty(driverEntityList)) {
            driverMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, Function.identity()));

        }
        return driverMap;
    }

    /**
     * 车辆信息
     *
     * @param vehicleIdList
     * @return
     */
    public Map<Long, VehicleEntity> getVehicleMap(Collection<Long> vehicleIdList) {
        Map<Long, VehicleEntity> vehicleMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            return vehicleMap;
        }
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        if (CollectionUtils.isNotEmpty(vehicleEntityList)) {
            vehicleMap = vehicleEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, Function.identity()));
        }
        return vehicleMap;
    }

    /**
     * 车队信息
     *
     * @param fleetIdList
     * @return
     */
    private Map<Long, FleetEntity> getFleetMap(Collection<Long> fleetIdList) {
        Map<Long, FleetEntity> fleetMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(fleetIdList)) {
            return fleetMap;
        }
        List<FleetEntity> fleetEntityList = fleetDao.selectBatchIds(fleetIdList);
        if (CollectionUtils.isNotEmpty(fleetEntityList)) {
            fleetMap = fleetEntityList.stream().collect(Collectors.toMap(FleetEntity::getFleetId, Function.identity()));
        }
        return fleetMap;
    }

}