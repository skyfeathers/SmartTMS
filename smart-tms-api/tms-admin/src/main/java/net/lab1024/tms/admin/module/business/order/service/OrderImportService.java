package net.lab1024.tms.admin.module.business.order.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.CarCostBasicInfoDao;
import net.lab1024.tms.admin.module.business.carcost.cashpay.CarCostCashPayManager;
import net.lab1024.tms.admin.module.business.carcost.category.CarCostCategoryDao;
import net.lab1024.tms.admin.module.business.carcost.ureapay.CarCostUreaPayManager;
import net.lab1024.tms.admin.module.business.driver.service.DriverDataTracerService;
import net.lab1024.tms.admin.module.business.driver.service.DriverManager;
import net.lab1024.tms.admin.module.business.material.businesstype.BusinessTypeDao;
import net.lab1024.tms.admin.module.business.material.cabinet.CabinetDao;
import net.lab1024.tms.admin.module.business.material.cost.CostItemDao;
import net.lab1024.tms.admin.module.business.material.transportroute.TransportRouteDao;
import net.lab1024.tms.admin.module.business.material.transportroute.TransportRoutePathDao;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.dto.TransportRoutePathDTO;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderImportDTO;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderPathImportForm;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderImportResultVO;
import net.lab1024.tms.admin.module.business.order.manager.OrderGoodsManager;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.admin.module.business.order.manager.OrderPathManager;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperManager;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperPrincipalManager;
import net.lab1024.tms.admin.module.business.shipper.service.ShipperDataTracerService;
import net.lab1024.tms.admin.module.business.vehicle.service.VehicleDataTracerService;
import net.lab1024.tms.admin.module.business.vehicle.service.VehicleManager;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillCostManager;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillGoodsManager;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillReceiveCostManager;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillCommonService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.*;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.material.businesstype.BusinessTypeEntity;
import net.lab1024.tms.common.module.business.material.cabinet.CabinetEntity;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;
import net.lab1024.tms.common.module.business.material.cost.domain.CostItemEntity;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.material.transportroute.domain.TransportRouteEntity;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperNatureEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperPrincipalEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillGoodsEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillReceiveCostEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 长途短驳订单导入
 *
 * @author lidoudou
 * @date 2022/11/19 下午5:02
 */
@Slf4j
@Service
public class OrderImportService {

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private BusinessTypeDao businessTypeDao;
    @Autowired
    private CabinetDao cabinetDao;
    @Autowired
    private TransportRouteDao transportRouteDao;
    @Autowired
    private TransportRoutePathDao transportRoutePathDao;
    @Autowired
    private ShipperManager shipperManager;
    @Autowired
    private ShipperDataTracerService shipperDataTracerService;
    @Autowired
    private VehicleManager vehicleManager;
    @Autowired
    private DriverManager driverManager;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private OrderGoodsManager orderGoodsManager;
    @Autowired
    private OrderPathManager orderPathManager;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private WaybillCommonService waybillCommonService;
    @Autowired
    private WaybillGoodsManager waybillGoodsManager;
    @Autowired
    private WaybillReceiveCostManager waybillReceiveCostManager;
    @Autowired
    private WaybillCostManager waybillCostManager;
    @Autowired
    private CostItemDao costItemDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private DriverDataTracerService driverDataTracerService;
    @Autowired
    private VehicleDataTracerService vehicleDataTracerService;
    @Autowired
    private OrderDataTracerService orderDataTracerService;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Autowired
    private ShipperPrincipalManager shipperPrincipalManager;
    @Resource
    private CarCostBasicInfoDao carCostBasicInfoDao;
    @Resource
    private CarCostCategoryDao carCostCategoryDao;
    @Resource
    private CarCostCashPayManager carCostCashPayManager;
    @Resource
    private CarCostUreaPayManager carCostUreaPayManager;
    @Autowired
    private SystemConfigService systemConfigService;
    /**
     * 验证数据准确性，返回数据列表。
     *
     * @return
     */
    public ResponseDTO<List<OrderImportResultVO>> validateImportData(Long enterpriseId, MultipartFile uploadFile) {
        try {
            // 文件最大 1M
            long maxSize = 1 * 1024 * 1024L;
            if (uploadFile.getSize() > maxSize) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "上传文件最大1M，如果超过1M，请删除Excel无用Sheet或对Excel进行拆分");
            }
            List<OrderImportDTO> importList = importData(uploadFile);
            if (CollectionUtils.isEmpty(importList)) {
                return ResponseDTO.userErrorParam("导入内容不能为空");
            }
            importList = importList.stream().filter(e -> SmartStringUtil.isNotBlank(e.getConsignor())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(importList)) {
                return ResponseDTO.userErrorParam("导入内容不能为空");
            }

            // 清除列的前后空格
            this.clearExcelBlank(importList);

            // 运输方式
            Set<String> transportModeSet = importList.stream().map(OrderImportDTO::getTransportModeName).collect(Collectors.toSet());
            for (String transportModeName : transportModeSet) {
                WaybillTransportModeEnum transportModeEnum = SmartBaseEnumUtil.getEnumByDesc(transportModeName, WaybillTransportModeEnum.class);
                if (null == transportModeEnum) {
                    return ResponseDTO.userErrorParam("运输方式【"+ transportModeName+"】不存在");
                }
            }

            // 查询货主
            Set<String> searchSet = importList.stream().map(e -> e.getConsignor().trim()).collect(Collectors.toSet());

            List<ShipperEntity> shipperEntityList = shipperManager.getBaseMapper().selectByConsignorListAndEnterpriseId(searchSet, enterpriseId, Boolean.FALSE);
            Map<String, Long> shipperIdMap = shipperEntityList.stream().collect(Collectors.toMap(ShipperEntity::getConsignor, ShipperEntity::getShipperId));

            // 调度员
            Set<String> employeeNameSet = importList.stream().map(OrderImportDTO::getScheduleName).collect(Collectors.toSet());
            importList.forEach(item -> {
                employeeNameSet.add(item.getCreateUserName());
            });

            List<EmployeeEntity> employeeEntityList = employeeDao.selectByActualNameList(enterpriseId, employeeNameSet, Boolean.FALSE, Boolean.FALSE);
            List<String> actualNameList = employeeEntityList.stream().map(EmployeeEntity::getActualName).collect(Collectors.toList());
            Set<String> actualNameSet = actualNameList.stream().collect(Collectors.toSet());
            if (actualNameSet.size() != actualNameList.size()) {
                return ResponseDTO.userErrorParam("存在同名员工，请处理后重新导入");
            }
            Map<String, Long> employeeIdMap = employeeEntityList.stream().collect(Collectors.toMap(EmployeeEntity::getActualName, EmployeeEntity::getEmployeeId));

            // 业务类型
            Set<String> businessTypeNameList = importList.stream().map(OrderImportDTO::getContainerBusinessTypeName).collect(Collectors.toSet());
            List<BusinessTypeEntity> businessTypeEntityList = businessTypeDao.selectByBusinessTypeNameList(enterpriseId, businessTypeNameList, Boolean.FALSE, Boolean.FALSE);
            Map<String, Long> businessTypeIdMap = businessTypeEntityList.stream().collect(Collectors.toMap(BusinessTypeEntity::getBusinessTypeName, BusinessTypeEntity::getBusinessTypeId));

            // 柜型
            Set<String> cabinetNameList = importList.stream().map(OrderImportDTO::getCabinetName).collect(Collectors.toSet());
            List<CabinetEntity> cabinetEntityList = cabinetDao.selectByCabinetNameList(enterpriseId, cabinetNameList, Boolean.FALSE, Boolean.FALSE);
            Map<String, Long> cabinetIdMap = cabinetEntityList.stream().collect(Collectors.toMap(CabinetEntity::getCabinetName, CabinetEntity::getCabinetId));

            // 货物类型列表
            List<DictValueVO> goodsTypeList = dictCacheService.selectByKeyCode("cargoTypeClassificationCode");
            Map<String, String> goodsTypeMap = goodsTypeList.stream().collect(Collectors.toMap(DictValueVO::getValueName, DictValueVO::getValueCode));

            // 路线
            Set<String> transportNameList = importList.stream().map(OrderImportDTO::getTransportRouteName).collect(Collectors.toSet());
            List<TransportRouteEntity> transportRouteEntityList = transportRouteDao.selectByTransportNameList(enterpriseId, transportNameList, Boolean.FALSE, Boolean.FALSE);
            List<String> dbTransportRouteNameList = transportRouteEntityList.stream().map(TransportRouteEntity::getTransportRouteName).collect(Collectors.toList());
            Set<String> dbTransportRouteNameSet = dbTransportRouteNameList.stream().collect(Collectors.toSet());
            if (dbTransportRouteNameSet.size() != dbTransportRouteNameList.size()) {
                return ResponseDTO.userErrorParam("存在同名路线，请处理后重新导入");
            }
            Map<String, Long> transportRouteIdMap = transportRouteEntityList.stream().collect(Collectors.toMap(TransportRouteEntity::getTransportRouteName, TransportRouteEntity::getTransportRouteId));

            Map<Long, List<TransportRoutePathDTO>> transportRoutePathMap = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(transportRouteEntityList)) {
                List<Long> transportRouteIdList = transportRouteEntityList.stream().map(TransportRouteEntity::getTransportRouteId).collect(Collectors.toList());
                List<TransportRoutePathDTO> transportRoutePathList = transportRoutePathDao.selectByTransportRouteIdList(transportRouteIdList);
                transportRoutePathMap = transportRoutePathList.stream().collect(Collectors.groupingBy(TransportRoutePathDTO::getTransportRouteId));
            }

            // 司机
            Set<String> driverPhoneSet = importList.stream().map(OrderImportDTO::getDriverPhone).collect(Collectors.toSet());
            List<DriverEntity> driverEntityList = driverManager.getBaseMapper().selectByPhone(enterpriseId, driverPhoneSet, Boolean.FALSE);
            Map<String, Long> driverIdMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getTelephone, DriverEntity::getDriverId));

            // 车牌
            Set<String> vehicleNumberSet = importList.stream().map(OrderImportDTO::getVehicleNumber).collect(Collectors.toSet());
            List<VehicleEntity> vehicleEntityList = vehicleManager.getBaseMapper().selectByVehicleNumber(enterpriseId, vehicleNumberSet, Boolean.FALSE);
            Map<String, Long> vehicleIdMap = vehicleEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleNumber, VehicleEntity::getVehicleId));

            List<OrderImportResultVO> importResultList = Lists.newArrayList();
            // 验证数据是否为空
            for (OrderImportDTO item : importList) {
                OrderImportResultVO data = SmartBeanUtil.copy(item, OrderImportResultVO.class);
                List<String> errorMsgList = Lists.newArrayList();
                // 校验参数
                String verify = SmartBeanUtil.verify(item);
                if (null != verify) {
                    errorMsgList.add(verify.replace("[", StringConst.EMPTY).replace("]", StringConst.EMPTY));
                }
                if (shipperIdMap.containsKey(data.getConsignor())) {
                    data.setShipperId(shipperIdMap.get(data.getConsignor().trim()));
                }
                if (employeeIdMap.containsKey(data.getScheduleName())) {
                    data.setScheduleId(employeeIdMap.get(data.getScheduleName()));
                } else {
                    errorMsgList.add("未匹配到所属调度");
                }

                TransportationTypeEnum transportationTypeEnum = SmartBaseEnumUtil.getEnumByDesc(data.getTransportationTypeName(), TransportationTypeEnum.class);
                // 运输类型为空，则默认普货运输
                if (null != transportationTypeEnum) {
                    data.setBusinessTypeCode(transportationTypeEnum.getValue());
                } else {
                    errorMsgList.add("运输类型错误");
                }

                WaybillSettleModeEnum waybillSettleModeEnum = SmartBaseEnumUtil.getEnumByDesc(data.getSettleModeName(), WaybillSettleModeEnum.class);
                if (null != waybillSettleModeEnum) {
                    data.setSettleMode(waybillSettleModeEnum.getValue());
                } else {
                    errorMsgList.add("结算方式错误");
                }

                WaybillTransportModeEnum transportModeEnum = SmartBaseEnumUtil.getEnumByDesc(data.getTransportModeName(), WaybillTransportModeEnum.class);
                data.setTransportMode(WaybillTransportModeEnum.LONG_DISTANCE.getValue());
                // 业务类型为空，则默认长途
                if (null != transportModeEnum) {
                    data.setTransportMode(transportModeEnum.getValue());
                }
                // 集装箱运输，验证业务类型、柜型
                if (null != transportationTypeEnum && TransportationTypeEnum.CONTAINER_TRANSPORT.equalsValue(transportationTypeEnum.getValue())) {
                    if (businessTypeIdMap.containsKey(data.getContainerBusinessTypeName())) {
                        data.setContainerBusinessTypeId(businessTypeIdMap.get(data.getContainerBusinessTypeName()));
                    }
                    data.setCabinetId(cabinetIdMap.get(data.getCabinetName()));
                }

                // 设置商品信息
                String goodsType = goodsTypeMap.get(data.getGoodsTypeName());
                data.setGoodsType(goodsType);
                if (SmartStringUtil.isBlank(goodsType)) {
                    errorMsgList.add("货物类型不存在");
                }
                if (SmartStringUtil.isBlank(data.getGoodsName())) {
                    errorMsgList.add("货物名称不能为空");
                }

                GoodsUnitTypeEnum goodsUnitTypeEnum = SmartBaseEnumUtil.getEnumByDesc(data.getGoodsUnitName(), GoodsUnitTypeEnum.class);
                if (null != goodsUnitTypeEnum) {
                    data.setGoodsUnit(goodsUnitTypeEnum.getValue());
                } else {
                    errorMsgList.add("货物单位不存在");
                }

                // 设置路线信息
                Long transportRouteId = transportRouteIdMap.get(data.getTransportRouteName());
                if (null != transportRouteId) {
                    data.setTransportRouteId(transportRouteId);
                    List<TransportRoutePathDTO> pathList = transportRoutePathMap.getOrDefault(transportRouteId, Lists.newArrayList());
                    List<OrderPathImportForm> importPathList = SmartBeanUtil.copyList(pathList, OrderPathImportForm.class);
                    data.setPathList(importPathList);
                } else {
                    errorMsgList.add("运输路线不存在");
                }
                data.setCreateUserId(employeeIdMap.get(data.getCreateUserName()));
                if (!employeeIdMap.containsKey(data.getCreateUserName())) {
                    errorMsgList.add("业务操作人不存在");
                }

                /**
                 * 运单信息 start
                 */
                data.setDriverId(driverIdMap.get(data.getDriverPhone()));
                data.setVehicleId(vehicleIdMap.get(data.getVehicleNumber()));

                // 设置业务时间
//                data.setBusinessDate(SmartLocalDateUtil.parseYMD(item.getBusinessDateStr()));

                if (null == data.getOilCardAmount()) {
                    data.setOilCardAmount(BigDecimal.ZERO);
                }
                data.setErrorMsg(SmartStringUtil.join(StringConst.SEPARATOR, errorMsgList));
                importResultList.add(data);
            }
            return ResponseDTO.ok(importResultList);
        } catch (Exception e) {
            log.error("订单导入失败", e);
            return ResponseDTO.userErrorParam("导入模版内容有误");
        }
    }

    private List<OrderImportDTO> importData(MultipartFile uploadFile) throws Exception {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        List<OrderImportDTO> orderImportList = ExcelImportUtil.importExcel(uploadFile.getInputStream(), OrderImportDTO.class, params);
        return orderImportList;
    }

    /**
     * 确认导入
     *
     * @param importList
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> confirmImportData(List<OrderImportResultVO> importList, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        // 保存货主信息
        this.batchSaveShipper(importList, enterpriseId, dataTracerRequestForm);
        // 保存车辆信息
        this.batchSaveVehicle(importList, dataTracerRequestForm);
        // 保存司机信息
        this.batchSaveDriver(importList, dataTracerRequestForm);
        if (CollectionUtils.isNotEmpty(importList.stream().filter(e -> null == e.getShipperId() || null == e.getVehicleId() || null == e.getDriverId()).collect(Collectors.toList()))) {
            throw new BusinessException("数据验证未通过，请联系技术人员");
        }
        // 保存订单信息
        this.batchSaveOrder(importList, enterpriseId, dataTracerRequestForm);
        // 保存运单信息
        this.batchSaveWaybill(importList, enterpriseId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 创建货主，设置货主简称、全称，所属公司，货主类型（默认为企业）
     *
     * @param importList
     * @param dataTracerRequestForm
     */
    private void batchSaveShipper(List<OrderImportResultVO> importList, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        Set<String> waitAddShortNameSet = importList.stream().filter(e -> null == e.getShipperId()).map(OrderImportResultVO::getConsignor).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(waitAddShortNameSet)) {
            return;
        }

        List<String> shipperNameList = Lists.newArrayList();
        List<ShipperEntity> insertList = Lists.newArrayList();
        for (OrderImportResultVO item : importList) {
            if (null != item.getShipperId() || shipperNameList.contains(item.getConsignor())) {
                continue;
            }
            shipperNameList.add(item.getConsignor());
            ShipperEntity shipperEntity = SmartBeanUtil.copy(item, ShipperEntity.class);
            shipperEntity.setShortName(item.getConsignor());
            shipperEntity.setEnterpriseId(enterpriseId);
            shipperEntity.setShipperNature(ShipperNatureEnum.ENTERPRISE.getValue());
            shipperEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
            insertList.add(shipperEntity);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            shipperManager.saveBatch(insertList);
            List<ShipperPrincipalEntity> shipperPrincipalEntityList = buildManagerList(insertList, dataTracerRequestForm.getOperatorId());
            shipperPrincipalManager.saveBatch(shipperPrincipalEntityList);
            shipperDataTracerService.saveByImportLog(insertList, dataTracerRequestForm);
            Map<String, Long> shipperShortNameMap = insertList.stream().collect(Collectors.toMap(ShipperEntity::getShortName, ShipperEntity::getShipperId));
            for (OrderImportResultVO item : importList) {
                if (null != item.getShipperId()) {
                    continue;
                }
                item.setShipperId(shipperShortNameMap.get(item.getConsignor()));
            }
        }
    }

    private List<ShipperPrincipalEntity> buildManagerList(List<ShipperEntity> shipperEntityList,Long createUserId) {
        List<ShipperPrincipalEntity> shipperPrincipalEntityList = shipperEntityList.stream().map(e->{
            ShipperPrincipalEntity shipperPrincipalEntity = new ShipperPrincipalEntity();
            shipperPrincipalEntity.setShipperId(e.getShipperId());
            shipperPrincipalEntity.setEmployeeId(createUserId);
            shipperPrincipalEntity.setType(PrincipalTypeEnum.MANAGER.getValue());
            return shipperPrincipalEntity;
        }).collect(Collectors.toList());
        return shipperPrincipalEntityList;
    }

    /**
     * 创建车辆，经营方式默认外派，车牌号
     *
     * @param importList
     * @param dataTracerRequestForm
     * @return
     */
    private void batchSaveVehicle(List<OrderImportResultVO> importList, DataTracerRequestForm dataTracerRequestForm) {
        Set<String> waitAddVehicleNumberSet = importList.stream().filter(e -> null == e.getVehicleId()).map(OrderImportResultVO::getVehicleNumber).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(waitAddVehicleNumberSet)) {
            return;
        }

        List<String> vehicleNumberList = Lists.newArrayList();
        List<VehicleEntity> vehicleInsertList = Lists.newArrayList();
        for (OrderImportResultVO importData : importList) {
            if (null != importData.getVehicleId() || vehicleNumberList.contains(importData.getVehicleNumber())) {
                continue;
            }
            vehicleNumberList.add(importData.getVehicleNumber());

            VehicleEntity vehicleEntity = new VehicleEntity();
            vehicleEntity.setVehicleNumber(importData.getVehicleNumber());
            vehicleEntity.setCreateUserId(importData.getCreateUserId());
            vehicleEntity.setCreateUserName(importData.getCreateUserName());
            vehicleInsertList.add(vehicleEntity);
        }
        if (CollectionUtils.isNotEmpty(vehicleInsertList)) {
            vehicleManager.saveBatch(vehicleInsertList);
            vehicleDataTracerService.saveByImportLog(vehicleInsertList, dataTracerRequestForm);
            Map<String, Long> vehicleNumberMap = vehicleInsertList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleNumber, VehicleEntity::getVehicleId));
            for (OrderImportResultVO item : importList) {
                if (null != item.getVehicleId()) {
                    continue;
                }
                item.setVehicleId(vehicleNumberMap.get(item.getVehicleNumber()));
            }
        }
    }

    /**
     * 创建司机，经营方式默认外派，设置司机电话，姓名
     *
     * @param importList
     * @param dataTracerRequestForm
     * @return
     */
    private void batchSaveDriver(List<OrderImportResultVO> importList, DataTracerRequestForm dataTracerRequestForm) {
        Set<String> driverPhoneSet = importList.stream().filter(e -> null == e.getDriverId()).map(OrderImportResultVO::getDriverPhone).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(driverPhoneSet)) {
            return;
        }
        List<String> driverPhoneList = Lists.newArrayList();
        List<DriverEntity> driverInsertList = Lists.newArrayList();
        for (OrderImportResultVO item : importList) {
            if (null != item.getDriverId() || driverPhoneList.contains(item.getDriverPhone())) {
                continue;
            }
            driverPhoneList.add(item.getDriverPhone());

            DriverEntity driverEntity = new DriverEntity();
            driverEntity.setDriverName(item.getDriverName());
            driverEntity.setTelephone(item.getDriverPhone());
            driverEntity.setCreateUserId(item.getCreateUserId());
            driverEntity.setCreateUserName(item.getCreateUserName());
            driverInsertList.add(driverEntity);
        }
        if (CollectionUtils.isNotEmpty(driverInsertList)) {
            driverManager.saveBatch(driverInsertList);
            driverDataTracerService.saveByImportLog(driverInsertList, dataTracerRequestForm);
            Map<String, Long> driverPhoneMap = driverInsertList.stream().collect(Collectors.toMap(DriverEntity::getTelephone, DriverEntity::getDriverId));
            for (OrderImportResultVO item : importList) {
                if (null != item.getDriverId()) {
                    continue;
                }
                item.setDriverId(driverPhoneMap.get(item.getDriverPhone()));
            }
        }
    }

    /**
     * 创建订单
     *
     * @param importList
     * @param dataTracerRequestForm
     */
    private void batchSaveOrder(List<OrderImportResultVO> importList, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        List<OrderPathEntity> orderPathInsertList = Lists.newArrayList();
        List<OrderGoodsEntity> orderGoodsInsertList = Lists.newArrayList();

        Set<Long> shipperIdSet = importList.stream().map(OrderImportResultVO::getShipperId).collect(Collectors.toSet());
        List<ShipperPrincipalDTO> shipperPrincipalList =   shipperPrincipalDao.selectByShipperIdListAndType(shipperIdSet, PrincipalTypeEnum.MANAGER.getValue());
        Map<Long, Long> shipperManagerMap = shipperPrincipalList.stream().collect(Collectors.toMap(ShipperPrincipalDTO::getShipperId, ShipperPrincipalDTO::getEmployeeId));

        importList.forEach(item -> {
            // 路线信息
            List<OrderPathEntity> pathList = SmartBeanUtil.copyList(item.getPathList(), OrderPathEntity.class);

            OrderEntity orderEntity = SmartBeanUtil.copy(item, OrderEntity.class);
            orderEntity.setEnterpriseId(enterpriseId);
            orderEntity.setOrderNo(serialNumberService.generate(SerialNumberIdEnum.ORDER));
            orderEntity.setStatus(OrderStatusEnum.TRANSPORT.getValue());
            orderEntity.setSingleTripReceiveAmount(item.getReceiveAmount());
            orderEntity.setSingleTripFreightAmount(item.getPayAmount());
            orderEntity.setOrderType(OrderTypeEnum.ORDINARY.getValue());
            orderEntity.setScheduleFlag(Boolean.TRUE);
            orderEntity.setManagerId(shipperManagerMap.getOrDefault(item.getShipperId(),NumberConst.DEFAULT_PARENT_ID));
            orderManager.updateLocation(orderEntity, SmartBeanUtil.copyList(pathList, OrderPathDTO.class));
            orderManager.getBaseMapper().insert(orderEntity);

            Long orderId = orderEntity.getOrderId();
            item.setOrderId(orderId);

            pathList.forEach(e -> e.setOrderId(orderId));
            orderPathInsertList.addAll(pathList);

            // 货物信息
            OrderGoodsEntity orderGoodsEntity = SmartBeanUtil.copy(item, OrderGoodsEntity.class);
            orderGoodsEntity.setRemark(StringConst.EMPTY);
            orderGoodsEntity.setOrderId(orderId);
            orderGoodsInsertList.add(orderGoodsEntity);
        });
        orderGoodsManager.saveBatch(orderGoodsInsertList);
        orderPathManager.saveBatch(orderPathInsertList);
        orderDataTracerService.importLog(orderGoodsInsertList.stream().map(OrderGoodsEntity::getOrderId).collect(Collectors.toSet()), dataTracerRequestForm);
    }


    // 创建运单 运费、油卡费用、货物、订单、结算方式/类型
    private void batchSaveWaybill(List<OrderImportResultVO> importList, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> orderIdList = importList.stream().map(OrderImportResultVO::getOrderId).collect(Collectors.toList());
        List<OrderGoodsEntity> orderGoodsEntityList = orderGoodsManager.getBaseMapper().selectByOrderIdList(orderIdList);
        Map<Long, List<OrderGoodsEntity>> orderGoodsMap = orderGoodsEntityList.stream().collect(Collectors.groupingBy(OrderGoodsEntity::getOrderId));
        List<WaybillCostEntity> defaultPayCostList = selectWaybillOrderPayCostItem();

        // 保存订单的费用信息
        List<Integer> typeList = Lists.newArrayList(CostItemTypeEnum.RECEIVE.getValue());
        List<CostItemEntity> costItemEntityList = costItemDao.selectByType(typeList, false);

        List<WaybillGoodsEntity> goodsInsertList = Lists.newArrayList();
        List<WaybillCostEntity> payCostInsertList = Lists.newArrayList();
        List<WaybillReceiveCostEntity> receiveCostInsertList = Lists.newArrayList();
        for (OrderImportResultVO orderImportResultVO : importList) {
            WaybillEntity waybillEntity = SmartBeanUtil.copy(orderImportResultVO, WaybillEntity.class);
            waybillEntity.setEnterpriseId(enterpriseId);
            waybillEntity.setWaybillNumber(serialNumberService.generate(SerialNumberIdEnum.WAYBILL));
            waybillEntity.setOrderType(OrderTypeEnum.ORDINARY.getValue());
            waybillEntity.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
            waybillEntity.setWaybillStatus(WaybillStatusEnum.TRANSPORT.getValue());
            waybillEntity.setReceiveAmount(orderImportResultVO.getReceiveAmount());
            waybillEntity.setTransportMode(orderImportResultVO.getTransportMode());

            BigDecimal payAmount = orderImportResultVO.getPayAmount();
            BigDecimal oilCardAmount = orderImportResultVO.getOilCardAmount();
            BigDecimal carCostAmount = BigDecimal.ZERO;
            if (orderImportResultVO.getUreaCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getUreaCost()) < 0) {
                carCostAmount = carCostAmount.add(orderImportResultVO.getUreaCost());
            }
            if (orderImportResultVO.getMaintenanceCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getMaintenanceCost()) < 0) {
                carCostAmount = carCostAmount.add(orderImportResultVO.getMaintenanceCost());
            }
            if (orderImportResultVO.getOtherCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getOtherCost()) < 0) {
                carCostAmount = carCostAmount.add(orderImportResultVO.getOtherCost());
            }
            BigDecimal salaryAmount = BigDecimal.ZERO;
            if (orderImportResultVO.getSalaryCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getSalaryCost()) < 0) {
                salaryAmount = salaryAmount.add(orderImportResultVO.getSalaryCost());
            }
            BigDecimal profitAmount = waybillCommonService.calculateProfitAmount(waybillEntity.getReceiveAmount(), BigDecimal.ZERO, salaryAmount, carCostAmount, orderImportResultVO.getTaxPoint());

            waybillEntity.setPayableCashAmount(payAmount);
            waybillEntity.setPayableOilCardAmount(oilCardAmount);
            waybillEntity.setPayableAmount(SmartBigDecimalUtil.Amount.add(payAmount, oilCardAmount));
            waybillEntity.setSettleType(WaybillSettleTypeEnum.DRIVER.getValue());
            waybillEntity.setSettleMode(orderImportResultVO.getSettleMode());
            waybillEntity.setProfitAmount(profitAmount);
            waybillManager.getBaseMapper().insert(waybillEntity);
            Long waybillId = waybillEntity.getWaybillId();

            // 设置商品信息
            List<OrderGoodsEntity> orderGoodsList = orderGoodsMap.getOrDefault(waybillId, Lists.newArrayList());
            orderGoodsList.forEach(e -> {
                WaybillGoodsEntity waybillGoodsEntity = SmartBeanUtil.copy(e, WaybillGoodsEntity.class);
                waybillGoodsEntity.setWaybillId(waybillId);
                goodsInsertList.add(waybillGoodsEntity);
            });

            // 设置应付费用信息
            defaultPayCostList.forEach(e -> {
                WaybillCostEntity waybillCostEntity = SmartBeanUtil.copy(e, WaybillCostEntity.class);
                waybillCostEntity.setWaybillId(waybillId);
                if (CostItemCategoryEnum.FREIGHT_COST.equalsValue(e.getCostItemCategory())) {
                    waybillCostEntity.setCostAmount(orderImportResultVO.getPayAmount());
                }
                if (CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory())) {
                    waybillCostEntity.setCostAmount(orderImportResultVO.getOilCardAmount());
                }
                payCostInsertList.add(waybillCostEntity);
            });


            // 设置应收费用信息
            costItemEntityList.forEach(costItem -> {
                WaybillReceiveCostEntity waybillReceiveCostEntity = new WaybillReceiveCostEntity();
                waybillReceiveCostEntity.setWaybillId(waybillId);
                waybillReceiveCostEntity.setCostItemId(costItem.getCostItemId());
                waybillReceiveCostEntity.setCostItemCategory(costItem.getCategory());
                waybillReceiveCostEntity.setCostItemType(costItem.getType());
                waybillReceiveCostEntity.setCostItemName(costItem.getName());
                waybillReceiveCostEntity.setCostAmount(BigDecimal.ZERO);
                if (CostItemCategoryEnum.FREIGHT_COST.equalsValue(costItem.getCategory())) {
                    waybillReceiveCostEntity.setCostAmount(orderImportResultVO.getReceiveAmount());
                }
                receiveCostInsertList.add(waybillReceiveCostEntity);
            });
            // 自有车尿素费用
            if (orderImportResultVO.getUreaCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getUreaCost()) < 0) {
                String ureaCategoryId = systemConfigService.getConfigValue(SystemConfigKeyEnum.CAR_COST_UREA_CATEGORY_ID);
                Long categoryId = Long.valueOf(ureaCategoryId);
                CarCostCategoryEntity categoryEntity = carCostCategoryDao.selectById(categoryId);

                CarCostUreaPayEntity ureaPayEntity = new CarCostUreaPayEntity();
                ureaPayEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                ureaPayEntity.setWaybillId(waybillId);
                ureaPayEntity.setDriverId(waybillEntity.getDriverId());
                ureaPayEntity.setCategoryId(categoryId);
                ureaPayEntity.setCostType(categoryEntity.getCostType());
                ureaPayEntity.setCategoryName(categoryEntity.getCategoryName());
                ureaPayEntity.setPayMode(categoryEntity.getPayMode());
                ureaPayEntity.setAmount(orderImportResultVO.getUreaCost());
                ureaPayEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
                ureaPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
                ureaPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
                ureaPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
                ureaPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

                CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
                tabulationEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                tabulationEntity.setWaybillId(waybillId);
                tabulationEntity.setModuleType(CarCostModuleTypeEnum.UREA_PAY.getValue());
                tabulationEntity.setAmount(orderImportResultVO.getUreaCost());
                tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
                tabulationEntity.setDriverId(waybillEntity.getDriverId());
                tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
                tabulationEntity.setCategoryId(categoryId);
                tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
                tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

                carCostUreaPayManager.handleAdd(waybillEntity.getDriverId(), ureaPayEntity, tabulationEntity, dataTracerRequestForm);
            }
            // 自有车维修费用
            if (orderImportResultVO.getMaintenanceCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getMaintenanceCost()) < 0) {
                String maintenanceCategoryId = systemConfigService.getConfigValue(SystemConfigKeyEnum.CAR_COST_MAINTENANCE_CATEGORY_ID);
                Long categoryId = Long.valueOf(maintenanceCategoryId);
                CarCostCategoryEntity categoryEntity = carCostCategoryDao.selectById(categoryId);

                CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
                cashPayEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                cashPayEntity.setWaybillId(waybillId);
                cashPayEntity.setDriverId(waybillEntity.getDriverId());
                cashPayEntity.setVehicleId(waybillEntity.getVehicleId());
                cashPayEntity.setCategoryId(categoryId);
                cashPayEntity.setCostType(categoryEntity.getCostType());
                cashPayEntity.setCategoryName(categoryEntity.getCategoryName());
                cashPayEntity.setPayMode(categoryEntity.getPayMode());
                cashPayEntity.setAmount(orderImportResultVO.getMaintenanceCost());
                cashPayEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
                cashPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
                cashPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
                cashPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
                cashPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

                CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
                tabulationEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                tabulationEntity.setWaybillId(waybillId);
                tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_PAY.getValue());
                tabulationEntity.setAmount(orderImportResultVO.getMaintenanceCost());
                tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
                tabulationEntity.setDriverId(waybillEntity.getDriverId());
                tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
                tabulationEntity.setCategoryId(categoryId);
                tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
                tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

                carCostCashPayManager.handleAdd(cashPayEntity, tabulationEntity);
            }
            // 自有车工资
            if (orderImportResultVO.getSalaryCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getSalaryCost()) < 0) {
                CarCostBasicInfoEntity basicInfoEntity = new CarCostBasicInfoEntity();
                basicInfoEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                basicInfoEntity.setWaybillId(waybillId);
                basicInfoEntity.setHighSpeedMileage(BigDecimal.ZERO);
                basicInfoEntity.setLowSpeedMileage(BigDecimal.ZERO);
                basicInfoEntity.setGpsMileage(BigDecimal.ZERO);
                basicInfoEntity.setOilConsumption(BigDecimal.ZERO);
                basicInfoEntity.setCommissionSalary(orderImportResultVO.getSalaryCost());
                basicInfoEntity.setAttendanceDays(0);
                basicInfoEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                basicInfoEntity.setConfirmFlag(true);
                carCostBasicInfoDao.insert(basicInfoEntity);
            }
            // 自有车其他费用
            if (orderImportResultVO.getOtherCost() != null && BigDecimal.ZERO.compareTo(orderImportResultVO.getOtherCost()) < 0) {
                String otherCategoryConfigId = systemConfigService.getConfigValue(SystemConfigKeyEnum.CAR_COST_OTHER_CATEGORY_ID);
                Long categoryId = Long.valueOf(otherCategoryConfigId);
                CarCostCategoryEntity categoryEntity = carCostCategoryDao.selectById(categoryId);

                CarCostCashPayEntity cashPayEntity = new CarCostCashPayEntity();
                cashPayEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                cashPayEntity.setWaybillId(waybillId);
                cashPayEntity.setDriverId(waybillEntity.getDriverId());
                cashPayEntity.setVehicleId(waybillEntity.getVehicleId());
                cashPayEntity.setCategoryId(categoryId);
                cashPayEntity.setCostType(categoryEntity.getCostType());
                cashPayEntity.setCategoryName(categoryEntity.getCategoryName());
                cashPayEntity.setPayMode(categoryEntity.getPayMode());
                cashPayEntity.setAmount(orderImportResultVO.getMaintenanceCost());
                cashPayEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
                cashPayEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
                cashPayEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
                cashPayEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
                cashPayEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

                CarCostTabulationEntity tabulationEntity = new CarCostTabulationEntity();
                tabulationEntity.setEnterpriseId(waybillEntity.getEnterpriseId());
                tabulationEntity.setWaybillId(waybillId);
                tabulationEntity.setModuleType(CarCostModuleTypeEnum.CASH_PAY.getValue());
                tabulationEntity.setAmount(orderImportResultVO.getMaintenanceCost());
                tabulationEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
                tabulationEntity.setDriverId(waybillEntity.getDriverId());
                tabulationEntity.setVehicleId(waybillEntity.getVehicleId());
                tabulationEntity.setCategoryId(categoryId);
                tabulationEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
                tabulationEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());

                carCostCashPayManager.handleAdd(cashPayEntity, tabulationEntity);
            }
        }

        // 持久化
        waybillGoodsManager.saveBatch(goodsInsertList);
        waybillCostManager.saveBatch(payCostInsertList);
        waybillReceiveCostManager.saveBatch(receiveCostInsertList);
    }

    /**
     * 查询运单的应付费用明细
     *
     * @return
     */
    private List<WaybillCostEntity> selectWaybillOrderPayCostItem() {
        // 保存订单的费用信息
        List<Integer> typeList = Lists.newArrayList(CostItemTypeEnum.PAY.getValue());
        List<CostItemEntity> costItemEntityList = costItemDao.selectByType(typeList, Boolean.FALSE);
        List<WaybillCostEntity> waybillOrderCostEntityList = Lists.newArrayList();
        for (CostItemEntity costItemEntity : costItemEntityList) {
            WaybillCostEntity waybillCostEntity = new WaybillCostEntity();
            waybillCostEntity.setCostItemId(costItemEntity.getCostItemId());
            waybillCostEntity.setCostItemCategory(costItemEntity.getCategory());
            waybillCostEntity.setCostItemType(costItemEntity.getType());
            waybillCostEntity.setCostItemName(costItemEntity.getName());
            waybillCostEntity.setCostAmount(BigDecimal.ZERO);
            waybillOrderCostEntityList.add(waybillCostEntity);
        }
        return waybillOrderCostEntityList;
    }

    /**
     * 清除前后空格
     *
     * @param importList
     */
    private void clearExcelBlank(List<OrderImportDTO> importList){
        importList.forEach(item -> {
            item.setConsignor(replaceBlank(item.getConsignor()));
            item.setScheduleName(replaceBlank(item.getScheduleName()));
            item.setTransportationTypeName(replaceBlank(item.getTransportationTypeName()));
            item.setTransportModeName(replaceBlank(item.getTransportModeName()));
            item.setContainerBusinessTypeName(replaceBlank(item.getContainerBusinessTypeName()));
            item.setCabinetName(replaceBlank(item.getCabinetName()));
            item.setGoodsTypeName(replaceBlank(item.getGoodsTypeName()));
            item.setGoodsUnitName(replaceBlank(item.getGoodsUnitName()));
            item.setCreateUserName(replaceBlank(item.getCreateUserName()));
            item.setTransportRouteName(replaceBlank(item.getTransportRouteName()));
            item.setDriverName(replaceBlank(item.getDriverName()));
            item.setDriverPhone(replaceBlank(item.getDriverPhone()));
            item.setVehicleNumber(replaceBlank(item.getVehicleNumber()));
            item.setContainerNumber(replaceBlank(item.getContainerNumber()));
            item.setSettleModeName(replaceBlank(item.getSettleModeName()));
        });
    }

    private String replaceBlank(String keyWords) {
        if (StringUtils.isBlank(keyWords)) {
            return StringConst.EMPTY;
        }
        return keyWords.trim();
    }
}
