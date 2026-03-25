package net.lab1024.tms.admin.module.business.vehicle.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.bracket.BracketDao;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleDao;
import net.lab1024.tms.admin.module.business.drivervehicle.domain.DriverVehicleVO;
import net.lab1024.tms.admin.module.business.insurance.InsuranceService;
import net.lab1024.tms.admin.module.business.maintenance.dao.MaintenanceDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.domain.dto.VehicleWaybillCountDTO;
import net.lab1024.tms.admin.module.business.vehicle.domain.form.*;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleDetailVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleExportVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleSimpleVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.*;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncryptUtil;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class VehicleService {

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private VehicleManager vehicleManager;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private BracketDao bracketDao;
    @Autowired
    private DriverVehicleDao driverVehicleDao;
    @Autowired
    private VehicleDataTracerService vehicleDataTracerService;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private WaybillDao waybillDao;
    @Resource
    private MaintenanceDao maintenanceDao;

    /**
     * 分页查询车辆信息
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<VehicleVO>> queryByPage(VehicleQueryForm queryDTO, Long enterpriseId) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        queryDTO.setEnterpriseId(enterpriseId);
        queryDTO.setDeletedFlag(Boolean.FALSE);

        List<VehicleVO> vehicleVOList = vehicleDao.queryByPage(page, queryDTO);
        if (CollectionUtils.isEmpty(vehicleVOList)) {
            PageResult<VehicleVO> pageResult = SmartPageUtil.convert2PageResult(page, Lists.newArrayList());
            return ResponseDTO.ok(pageResult);
        }
        this.buildNextMaintenanceInfo(vehicleVOList);
        buildList(vehicleVOList, enterpriseId, Boolean.TRUE);
        FieldEncryptUtil.injectList(vehicleVOList);
        PageResult<VehicleVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, vehicleVOList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 设置下一次保养信息
     *
     * @param vehicleVOList
     */
    private void buildNextMaintenanceInfo(List<VehicleVO> vehicleVOList) {
        List<Long> vehicleIdList = vehicleVOList.stream().map(VehicleVO::getVehicleId).collect(Collectors.toList());
        List<Long> maintenanceIdList = maintenanceDao.selectByVehicleIds(vehicleIdList, Boolean.FALSE);
        if (CollectionUtils.isEmpty(maintenanceIdList)) {
            return;
        }
        List<MaintenanceEntity> maintenanceEntityList = maintenanceDao.selectBatchIds(maintenanceIdList);
        Map<Long, MaintenanceEntity> maintenanceMap = maintenanceEntityList.stream().collect(Collectors.toMap(MaintenanceEntity::getVehicleId, Function.identity()));
        vehicleVOList.stream().forEach(vehicleVO -> {
            MaintenanceEntity maintenanceEntity = maintenanceMap.get(vehicleVO.getVehicleId());
            if (ObjectUtil.isNotEmpty(maintenanceEntity)) {
                vehicleVO.setNextMaintenanceDate(maintenanceEntity.getNextMaintenanceDate());
                vehicleVO.setNextMaintenanceMileage(maintenanceEntity.getNextMaintenanceMileage());
            }
        });
    }

    /**
     * 设置列展示信息
     *
     * @param vehicleVOList
     */
    private void buildList(List<VehicleVO> vehicleVOList, Long enterpriseId, Boolean injectFlag) {
        // 车辆
        List<Long> vehicleIdList = vehicleVOList.stream().map(VehicleVO::getVehicleId).collect(Collectors.toList());
        List<DriverVehicleVO> driverVehicleVOS = driverVehicleDao.selectByVehicleIdList(vehicleIdList, Boolean.FALSE, Boolean.FALSE);
        Map<Long, List<DriverVehicleVO>> driverVehicleMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(driverVehicleVOS)) {
            driverVehicleMap = driverVehicleVOS.stream().collect(groupingBy(DriverVehicleVO::getVehicleId));
        }
        for (VehicleVO vehicleVO : vehicleVOList) {
            List<DriverVehicleVO> driverVehicleVOList = driverVehicleMap.getOrDefault(vehicleVO.getVehicleId(), Lists.newArrayList());
            if (injectFlag) {
                FieldEncryptUtil.injectList(driverVehicleVOList);
            }
            vehicleVO.setDriverVehicleVOList(driverVehicleVOList);
        }
    }

    /**
     * 获取所有有效车辆
     *
     * @return
     */
    public ResponseDTO<List<VehicleSimpleVO>> getAll(Long enterpriseId, Long driverId) {
        List<VehicleSimpleVO> vehicleList = vehicleDao.selectByDisableFlagAndAuditStatus(enterpriseId, Boolean.FALSE, null, Boolean.FALSE);
        if (null == driverId) {
            return ResponseDTO.ok(vehicleList);
        }
        List<DriverVehicleVO> driverVehicleList = driverVehicleDao.selectByDriverId(driverId, Boolean.FALSE, null, Boolean.FALSE);
        if (CollectionUtils.isEmpty(driverVehicleList)) {
            return ResponseDTO.ok(vehicleList);
        }
        List<Long> driverVehicleIdList = driverVehicleList.stream().map(DriverVehicleVO::getVehicleId).collect(Collectors.toList());
        List<VehicleSimpleVO> result = vehicleList.stream().filter(e -> driverVehicleIdList.contains(e.getVehicleId())).collect(Collectors.toList());
        result.addAll(vehicleList.stream().filter(e -> !driverVehicleIdList.contains(e.getVehicleId())).collect(Collectors.toList()));
        return ResponseDTO.ok(result);
    }


    /**
     * 查询车辆详情
     *
     * @param vehicleId
     * @return
     */
    public ResponseDTO<VehicleDetailVO> getDetail(Long vehicleId, Long enterpriseId) {
        VehicleEntity vehicleEntity = vehicleDao.selectById(vehicleId);
        if (null == vehicleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (vehicleEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        VehicleDetailVO detailVO = SmartBeanUtil.copy(vehicleEntity, VehicleDetailVO.class);
        detailVO.setVehicleEnergyTypeName(dictCacheService.selectValueNameByValueCode(detailVO.getVehicleEnergyType()));
        detailVO.setBracketNo(getBracketNo(vehicleEntity));
        detailVO.setInsuranceList(insuranceService.selectByModuleIdAndType(detailVO.getVehicleId(), InsuranceModuleTypeEnum.VEHICLE.getValue()));
        detailVO.setDriverVehicleList(selectDriverByVehicleId(vehicleId));
        if (detailVO.getManagerId() != null) {
            EmployeeEntity employeeEntity = employeeDao.selectById(detailVO.getManagerId());
            detailVO.setManagerName(employeeEntity != null ? employeeEntity.getActualName() : StringConst.EMPTY);
        }
        return ResponseDTO.ok(detailVO);
    }

    /**
     * 根据车辆ID查询司机列表
     *
     * @param vehicleId
     * @return
     */
    public List<DriverVehicleVO> selectDriverByVehicleId(Long vehicleId) {
        return driverVehicleDao.selectByVehicleId(vehicleId, null, Boolean.FALSE);
    }

    /**
     * 根据车辆信息查询挂车编号
     *
     * @param vehicleEntity
     * @return
     */
    private String getBracketNo(VehicleEntity vehicleEntity) {
        Long bracketId = vehicleEntity.getBracketId();
        if (null == bracketId) {
            return null;
        }
        BracketEntity bracketEntity = bracketDao.selectById(bracketId);
        if (null == bracketEntity || bracketEntity.getDeletedFlag()) {
            return null;
        }
        return bracketEntity.getBracketNo();
    }

    /**
     * 保存车辆
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(VehicleAddForm addForm, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        // 判断车牌号是否存在
        List<VehicleEntity> vehicleList = vehicleDao.selectByNameExcludeIds(enterpriseId, addForm.getVehicleNumber(), null, false);
        if (CollectionUtils.isNotEmpty(vehicleList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "车牌号已存在");
        }
        addForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        addForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        addForm.setOperatorType(dataTracerRequestForm.getOperatorType());
        addForm.setOperatorTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());
        Long vehicleId = vehicleManager.addHandle(addForm, enterpriseId);
        vehicleDataTracerService.saveLog(vehicleId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 保存车辆
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(VehicleUpdateForm updateForm, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        Long vehicleId = updateForm.getVehicleId();

        // 判断车牌号是否存在
        List<VehicleEntity> vehicleList = vehicleDao.selectByNameExcludeIds(enterpriseId, updateForm.getVehicleNumber(), Lists.newArrayList(vehicleId), false);
        if (CollectionUtils.isNotEmpty(vehicleList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "车牌号已存在");
        }

        VehicleEntity dbVehicleEntity = vehicleDao.selectById(vehicleId);
        if (null == dbVehicleEntity || dbVehicleEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 原始详情
        VehicleDetailVO beforeVehicleDetailVO = this.getDetail(vehicleId, enterpriseId).getData();
        // 更新
        updateForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        updateForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        vehicleManager.updateHandle(updateForm);
        // 更新后详情
        VehicleDetailVO afterVehicleDetailVO = this.getDetail(vehicleId, enterpriseId).getData();
        vehicleDataTracerService.updateLog(beforeVehicleDetailVO, afterVehicleDetailVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除车辆
     *
     * @param vehicleIdList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> vehicleIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        if (vehicleEntityList.size() != vehicleIdList.size()) {
            return ResponseDTO.userErrorParam("车辆不存在");
        }
        Map<Long, String> vehicleNumberMap = vehicleEntityList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, VehicleEntity::getVehicleNumber));
        List<VehicleWaybillCountDTO> vehicleWaybillCountList = waybillDao.selectCountByVehicleIdList(vehicleIdList, WaybillStatusEnum.CANCEL.getValue());
        List<VehicleWaybillCountDTO> validWaybillCountList = vehicleWaybillCountList.stream().filter(e -> e.getCount() > NumberConst.ZERO).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(validWaybillCountList)) {
            List<String> vehicleNumberList = validWaybillCountList.stream().map(e -> {
                return vehicleNumberMap.getOrDefault(e.getVehicleId(), StringConst.EMPTY);
            }).collect(Collectors.toList());
            return ResponseDTO.userErrorParam("【" + StringUtils.join(vehicleNumberList, StringConst.SEPARATOR) + "】存在有效运单，不能删除");
        }

        vehicleManager.batchDelete(vehicleIdList);
        vehicleDataTracerService.batchDeleteLog(vehicleIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量启用
     *
     * @param vehicleIdList
     * @return
     */
    public ResponseDTO<String> batchUpdateDisabled(List<Long> vehicleIdList, boolean disabledFlag, DataTracerRequestForm dataTracerRequestForm) {
        vehicleManager.batchUpdateDisabled(vehicleIdList, disabledFlag);
        vehicleDataTracerService.batchUpdateDisabledLog(vehicleIdList, disabledFlag, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 车辆审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(VehicleAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Integer> auditStatusList = Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getValue(),
                AuditStatusEnum.REJECT.getValue());
        if (!auditStatusList.contains(auditForm.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }
        VehicleEntity vehicleEntity = vehicleDao.selectById(auditForm.getVehicleId());
        if (null == vehicleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(vehicleEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车辆审核状态不正确，请刷新后重试");
        }
        VehicleEntity auditEntity = new VehicleEntity();
        auditEntity.setVehicleId(auditForm.getVehicleId());
        auditEntity.setAuditStatus(auditForm.getAuditStatus());
        vehicleDao.updateById(auditEntity);
        vehicleDataTracerService.batchAuditLog(Lists.newArrayList(auditForm.getVehicleId()), auditForm.getAuditStatus(), auditForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 车辆批量审核
     *
     * @param batchAuditForm
     * @return
     */
    public ResponseDTO<String> batchAudit(VehicleBatchAuditForm batchAuditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Integer> auditStatusList = Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getValue(),
                AuditStatusEnum.REJECT.getValue());
        if (!auditStatusList.contains(batchAuditForm.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }

        List<Long> vehicleIdList = batchAuditForm.getVehicleIdList();
        List<VehicleEntity> vehicleList = vehicleDao.selectVehicleList(vehicleIdList, AuditStatusEnum.WAIT_AUDIT.getValue(), Boolean.FALSE);
        if (vehicleList.size() != vehicleIdList.size()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车辆数据异常，请刷新后重试");
        }
        vehicleDao.batchUpdateAuditStatus(vehicleIdList, batchAuditForm.getAuditStatus(), batchAuditForm.getRemark());
        vehicleDataTracerService.batchAuditLog(vehicleIdList, batchAuditForm.getAuditStatus(), batchAuditForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public List<VehicleVO> getByIdList(Collection<Long> vehicleIdList) {
        return SmartBeanUtil.copyList(vehicleDao.selectBatchIds(vehicleIdList), VehicleVO.class);
    }


    /**
     * 批量修改负责人
     *
     * @param managerUpdateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchUpdateManager(VehicleManagerUpdateForm managerUpdateForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> vehicleIdList = managerUpdateForm.getVehicleIdList();
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        if (vehicleEntityList.size() != vehicleIdList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(managerUpdateForm.getManagerId());
        if (employeeEntity == null) {
            return ResponseDTO.userErrorParam("负责人不存在");
        }
        List<VehicleEntity> updateList = vehicleIdList.stream().map(fleetId -> {
            VehicleEntity updateEntity = new VehicleEntity();
            updateEntity.setVehicleId(fleetId);
            updateEntity.setManagerId(managerUpdateForm.getManagerId());
            return updateEntity;
        }).collect(Collectors.toList());
        vehicleManager.updateBatchById(updateList);
        vehicleDataTracerService.batchUpdateManagerLog(vehicleIdList, employeeEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 车牌号map
     *
     * @param vehicleIdList
     * @return
     */
    public Map<Long, String> getVehicleNumberMap(Collection<Long> vehicleIdList) {
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            return Maps.newHashMap();
        }
        List<VehicleEntity> vehicleList = vehicleDao.selectBatchIds(vehicleIdList);
        return vehicleList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, VehicleEntity::getVehicleNumber));
    }

    /**
     * 车辆导出
     *
     * @param vehicleQueryForm
     * @return
     */
    public List<VehicleExportVO> queryByExport(VehicleQueryForm vehicleQueryForm, Long enterpriseId) {
        vehicleQueryForm.setDeletedFlag(Boolean.FALSE);
        vehicleQueryForm.setEnterpriseId(enterpriseId);
        List<VehicleVO> vehicleVOList = vehicleDao.queryByPage(null, vehicleQueryForm);
        if (CollectionUtils.isEmpty(vehicleVOList)) {
            return Lists.newArrayList();
        }
        buildList(vehicleVOList, enterpriseId, Boolean.FALSE);
        List<VehicleExportVO> exportList = vehicleVOList.stream().map(e -> {
            VehicleExportVO exportVO = SmartBeanUtil.copy(e, VehicleExportVO.class);
            exportVO.setAuditStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getAuditStatus(), AuditStatusEnum.class));
            exportVO.setDisabledFlagDesc(e.getDisabledFlag() ? "禁用" : "启用");
            exportVO.setBusinessModeDesc(SmartBaseEnumUtil.getEnumDescByValue(e.getBusinessMode(), BusinessModeEnum.class));
            List<String> driverNameList = e.getDriverVehicleVOList().stream().map(DriverVehicleVO::getDriverName).collect(Collectors.toList());
            exportVO.setDriverNameList(SmartStringUtil.join(StringConst.SEPARATOR, driverNameList));
            exportVO.setVehicleTypeDesc(dictCacheService.selectValueNameByValueCode(e.getVehicleType()));
            return exportVO;
        }).collect(Collectors.toList());
        return exportList;
    }
    /**
     * 修改经营方式
     * @param updateForm
     * @param enterpriseId
     * @return
     */
    public ResponseDTO<String> businessModeUpdate(VehicleBusinessModeUpdateForm updateForm, Long enterpriseId) {
        VehicleEntity updateEntity = new VehicleEntity();
        updateEntity.setVehicleId(updateForm.getVehicleId());
        updateEntity.setBusinessMode(updateForm.getBusinessMode());
        vehicleDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }


    public ResponseDTO<List<VehicleSimpleVO>> getCarCostVehicleList(Long enterpriseId) {
        List<VehicleSimpleVO> vehicleList = vehicleDao.selectByBusinessModeAndDisableFlag(enterpriseId, BusinessModeEnum.INNER_MANAGEMENT.getValue(), Boolean.FALSE, Boolean.FALSE);
        return ResponseDTO.ok(vehicleList);
    }
}
