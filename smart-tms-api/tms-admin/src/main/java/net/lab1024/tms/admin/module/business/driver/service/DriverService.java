package net.lab1024.tms.admin.module.business.driver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.driver.dao.DriverBankDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverTaxRegisterDao;
import net.lab1024.tms.admin.module.business.driver.domain.dto.DriverWaybillCountDTO;
import net.lab1024.tms.admin.module.business.driver.domain.form.*;
import net.lab1024.tms.admin.module.business.driver.domain.vo.*;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleDao;
import net.lab1024.tms.admin.module.business.drivervehicle.domain.DriverVehicleVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverTaxRegisterEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncryptUtil;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/***
 * 司机模块
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:42
 */
@Service
public class DriverService {

    @Autowired
    private DriverDao driverDao;
    @Autowired
    private DriverManager driverManager;
    @Autowired
    private DriverBankDao driverBankDao;
    @Autowired
    private DriverVehicleDao driverVehicleDao;
    @Autowired
    private DriverDataTracerService driverDataTracerService;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DriverTaxRegisterDao driverTaxRegisterDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private WaybillDao waybillDao;

    /**
     * 分页查询保险信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DriverVO>> queryByPage(DriverQueryForm queryForm, Long enterpriseId) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        queryForm.setEnterpriseId(enterpriseId);
        queryForm.setDeletedFlag(Boolean.FALSE);

        List<DriverVO> driverVOList = driverDao.queryByPage(page, queryForm);
        if (CollectionUtils.isEmpty(driverVOList)) {
            PageResult<DriverVO> pageResult = SmartPageUtil.convert2PageResult(page, Lists.newArrayList());
            return ResponseDTO.ok(pageResult);
        }
        buildListVO(driverVOList, enterpriseId, Boolean.TRUE);
        FieldEncryptUtil.injectList(driverVOList);
        PageResult<DriverVO> pageResult = SmartPageUtil.convert2PageResult(page, driverVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 设置司机列表字段
     *
     * @param driverVOList
     */
    private void buildListVO(List<DriverVO> driverVOList, Long enterpriseId, Boolean injectFlag) {
        //绑定车辆信息
        List<Long> driverIdList = driverVOList.stream().map(DriverVO::getDriverId).collect(Collectors.toList());
        List<DriverVehicleVO> driverVehicleVOS = driverVehicleDao.selectByDriverIdList(driverIdList, Boolean.FALSE, Boolean.FALSE);
        Map<Long, List<DriverVehicleVO>> driverVehicleMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(driverVehicleVOS)) {
            driverVehicleMap = driverVehicleVOS.stream().collect(groupingBy(DriverVehicleVO::getDriverId));
        }
        //设置信息
        for (DriverVO driverVO : driverVOList) {
            List<DriverVehicleVO> driverVehicleVOList = driverVehicleMap.getOrDefault(driverVO.getDriverId(), Lists.newArrayList());
            if (injectFlag) {
                FieldEncryptUtil.injectList(driverVehicleVOList);
            }
            driverVO.setDriverVehicleVOList(driverVehicleVOList);
        }
    }


    /**
     * 查询司机详情
     *
     * @param driverId
     * @return
     */
    public DriverDetailVO getDetail(Long driverId, Long enterpriseId) {
        DriverDetailVO detailVO = driverDao.getDetail(driverId, Boolean.FALSE);
        if (null == detailVO) {
            return null;
        }
        // 银行卡
        List<DriverBankVO> bankList = SmartBeanUtil.copyList(driverBankDao.selectByDriverId(driverId), DriverBankVO.class);
        detailVO.setBankList(bankList);
        // 车辆
        List<DriverVehicleVO> driverVehicleVOS = driverVehicleDao.selectByDriverId(driverId, Boolean.FALSE, null, Boolean.FALSE);
        detailVO.setDriverVehicleList(driverVehicleVOS);
        if (detailVO.getManagerId() != null) {
            EmployeeEntity employeeEntity = employeeDao.selectById(detailVO.getManagerId());
            detailVO.setManagerName(employeeEntity != null ? employeeEntity.getActualName() : StringConst.EMPTY);
        }
        DriverTaxRegisterEntity driverTaxRegisterEntity = driverTaxRegisterDao.selectByDriverId(driverId);
        if (null != driverTaxRegisterEntity) {
            detailVO.setTaxRegister(SmartBeanUtil.copy(driverTaxRegisterEntity, DriverTaxRegisterForm.class));
        }
        return detailVO;
    }

    /**
     * 保存司机
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(DriverAddForm addForm, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        // 手机号
        List<DriverEntity> driverList = driverDao.selectByPhoneExcludeIds(enterpriseId, addForm.getTelephone(), null, Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(driverList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "手机号已存在");
        }

        Long driverId = driverManager.addHandle(addForm, enterpriseId, dataTracerRequestForm.getOperatorId(), dataTracerRequestForm.getOperatorName());
        driverDataTracerService.saveLog(driverId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 保存司机
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(DriverUpdateForm updateForm, Long enterpriseId, DataTracerRequestForm dataTracerRequestForm) {
        Long driverId = updateForm.getDriverId();
        DriverEntity dbDriverEntity = driverDao.selectById(driverId);
        if (null == dbDriverEntity || dbDriverEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 手机号
        List<DriverEntity> driverList = driverDao.selectByPhoneExcludeIds(enterpriseId, updateForm.getTelephone(), Lists.newArrayList(updateForm.getDriverId()), Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(driverList)) {
            return ResponseDTO.userErrorParam("手机号已存在");
        }
        //变动前
        DriverDetailVO beforeData = this.getDetail(driverId, enterpriseId);
        driverManager.updateHandle(updateForm, enterpriseId, dataTracerRequestForm.getOperatorId(), dataTracerRequestForm.getOperatorName());
        //变动后
        DriverDetailVO afterData = this.getDetail(driverId, enterpriseId);
        driverDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }



    /**
     * 删除司机
     *
     * @param driverIdList
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchDeleteDriver(List<Long> driverIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DriverEntity> driverEntityList = driverDao.selectBatchIds(driverIdList);
        if (driverEntityList.size() != driverIdList.size()) {
            return ResponseDTO.userErrorParam("司机不存在");
        }
        Map<Long, String> driverNameMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getDriverName));
        List<DriverWaybillCountDTO> driverWaybillCountList = waybillDao.selectCountByDriverIdList(driverIdList, WaybillStatusEnum.CANCEL.getValue());
        List<DriverWaybillCountDTO> validWaybillCountList = driverWaybillCountList.stream().filter(e -> e.getCount() > NumberConst.ZERO).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(validWaybillCountList)) {
            List<String> driverNumberList = validWaybillCountList.stream().map(e -> {
                return driverNameMap.getOrDefault(e.getDriverId(), StringConst.EMPTY);
            }).collect(Collectors.toList());
            return ResponseDTO.userErrorParam("【" + StringUtils.join(driverNumberList, StringConst.SEPARATOR) + "】存在有效运单，不能删除");
        }
        driverManager.deleteDriver(driverIdList, dataTracerRequestForm.getOperatorId());
        driverDataTracerService.batchDeleteLog(driverIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 司机审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> audit(DriverAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Integer> auditStatusList = Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getValue(),
                AuditStatusEnum.REJECT.getValue());
        if (!auditStatusList.contains(auditForm.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }
        DriverEntity driverEntity = driverDao.selectById(auditForm.getDriverId());
        if (null == driverEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (!AuditStatusEnum.WAIT_AUDIT.equalsValue(driverEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "司机审核状态不正确，请刷新后重试");
        }
        DriverEntity auditEntity = new DriverEntity();
        auditEntity.setDriverId(auditForm.getDriverId());
        auditEntity.setAuditStatus(auditForm.getAuditStatus());
        auditEntity.setAuditRemark(auditForm.getRemark());
        auditEntity.setUpdateUserId(dataTracerRequestForm.getOperatorId());
        driverDao.updateById(auditEntity);
        driverDataTracerService.batchAuditLog(Lists.newArrayList(auditForm.getDriverId()), auditForm.getAuditStatus(), auditForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 司机批量审核
     *
     * @param batchAuditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchAudit(DriverBatchAuditForm batchAuditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Integer> auditStatusList = Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getValue(),
                AuditStatusEnum.REJECT.getValue());
        if (!auditStatusList.contains(batchAuditForm.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }

        List<Long> driverIdList = batchAuditForm.getDriverIdList();
        List<DriverEntity> driverList = driverDao.selectList(driverIdList, AuditStatusEnum.WAIT_AUDIT.getValue(), null, Boolean.FALSE);
        if (driverList.size() != driverIdList.size()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "司机数据异常，请刷新后重试");
        }
        List<DriverEntity> driverEntityList = Lists.newArrayList();
        for (Long driverId : driverIdList) {
            DriverEntity auditEntity = new DriverEntity();
            auditEntity.setDriverId(driverId);
            auditEntity.setAuditStatus(batchAuditForm.getAuditStatus());
            auditEntity.setAuditRemark(batchAuditForm.getRemark());
            auditEntity.setUpdateUserId(dataTracerRequestForm.getOperatorId());
            driverEntityList.add(auditEntity);
        }
        driverManager.updateBatchById(driverEntityList);
        driverDataTracerService.batchAuditLog(driverIdList, batchAuditForm.getAuditStatus(), batchAuditForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新状态
     *
     * @param driverId
     * @param dataTracerRequestForm
     */
    public ResponseDTO<String> updateStatus(Long driverId, DataTracerRequestForm dataTracerRequestForm) {
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (null == driverEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DriverStatusEnum driverStatusEnum = DriverStatusEnum.ACTIVE;
        if (DriverStatusEnum.ACTIVE.equalsValue(driverEntity.getStatus())) {
            driverStatusEnum = DriverStatusEnum.DISABLED;
        }
        DriverEntity updateEntity = new DriverEntity();
        updateEntity.setDriverId(driverId);
        updateEntity.setStatus(driverStatusEnum.getValue());
        driverDao.updateById(updateEntity);

        driverDataTracerService.batchUpdateStatusLog(Lists.newArrayList(driverId), driverStatusEnum.getValue(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量更新状态
     *
     * @param batchDisableForm
     * @return
     */
    public ResponseDTO<String> batchUpdateStatus(DriverBatchDisableForm batchDisableForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> driverIdList = batchDisableForm.getDriverIdList();
        List<DriverEntity> driverEntities = driverDao.selectBatchIds(driverIdList);
        driverEntities = driverEntities.stream().filter(e -> !e.getDeletedFlag()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(driverEntities) || driverEntities.size() != driverIdList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        List<DriverEntity> driverEntityList = Lists.newArrayList();
        for (Long driverId : driverIdList) {
            DriverEntity updateEntity = new DriverEntity();
            updateEntity.setDriverId(driverId);
            updateEntity.setStatus(batchDisableForm.getStatus());
            driverEntityList.add(updateEntity);
        }
        driverManager.updateBatchById(driverEntityList);
        driverDataTracerService.batchUpdateStatusLog(driverIdList, batchDisableForm.getStatus(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 司机下拉框查询
     *
     * @return
     */
    public ResponseDTO<List<DriverSelectVO>> queryDriverSelect(Long vehicleId, Long enterpriseId) {
        Integer auditStatus = null;
        List<DriverSelectVO> driverSelectVOS = driverDao.queryDriverSelect(enterpriseId, auditStatus, DriverStatusEnum.ACTIVE.getValue(), Boolean.FALSE);
        if (null == vehicleId) {
            return ResponseDTO.ok(driverSelectVOS);
        }

        // 获取车辆关联的司机，默认排在前面
        List<DriverVehicleVO> relatedDriverList = driverVehicleDao.selectByVehicleId(vehicleId, DriverStatusEnum.ACTIVE.getValue(), Boolean.FALSE);
        List<Long> relatedDriverIdList = relatedDriverList.stream().map(DriverVehicleVO::getDriverId).collect(Collectors.toList());
        List<DriverSelectVO> sortList = driverSelectVOS.stream().filter(e -> relatedDriverIdList.contains(e.getDriverId())).collect(Collectors.toList());
        sortList.addAll(driverSelectVOS.stream().filter(e -> !relatedDriverIdList.contains(e.getDriverId())).collect(Collectors.toList()));
        return ResponseDTO.ok(sortList);
    }


    /**
     * 批量修改负责人
     *
     * @param managerUpdateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchUpdateManager(DriverManagerUpdateForm managerUpdateForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> driverIdList = managerUpdateForm.getDriverIdList();
        List<DriverEntity> driverEntityList = driverDao.selectBatchIds(driverIdList);
        if (driverEntityList.size() != driverIdList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(managerUpdateForm.getManagerId());
        if (employeeEntity == null) {
            return ResponseDTO.userErrorParam("负责人不存在");
        }
        List<DriverEntity> updateList = driverIdList.stream().map(fleetId -> {
            DriverEntity updateEntity = new DriverEntity();
            updateEntity.setDriverId(fleetId);
            updateEntity.setManagerId(managerUpdateForm.getManagerId());
            return updateEntity;
        }).collect(Collectors.toList());
        driverManager.updateBatchById(updateList);
        driverDataTracerService.batchUpdateManagerLog(driverIdList, employeeEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 银行列表
     *
     * @param driverId
     * @return
     */
    public ResponseDTO<List<DriverBankVO>> bankList(Long driverId) {
        List<DriverBankEntity> bankEntityList = driverBankDao.selectByDriverId(driverId);
        if (CollectionUtils.isEmpty(bankEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<DriverBankVO> bankList = SmartBeanUtil.copyList(bankEntityList, DriverBankVO.class);
        return ResponseDTO.ok(bankList);
    }

    /**
     * 添加司机银行信息
     *
     * @param bankAddForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> bankAdd(DriverBankAddForm bankAddForm, DataTracerRequestForm dataTracerRequestForm) {
        DriverEntity driverEntity = driverDao.selectById(bankAddForm.getDriverId());
        if (null == driverEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        String bankName = dictCacheService.selectValueNameByValueCode(bankAddForm.getBankType());
        DriverBankEntity driverBankEntity = SmartBeanUtil.copy(bankAddForm, DriverBankEntity.class);
        driverBankEntity.setBankName(bankName);
        driverBankEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        driverBankEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        driverBankEntity.setCreateUserType(dataTracerRequestForm.getOperatorType());
        driverBankEntity.setCreateUserTypeDesc(dataTracerRequestForm.getOperatorTypeDesc());
        driverManager.addBank(driverBankEntity);
        driverDataTracerService.bankAddLog(driverBankEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public Map<Long, String> getDriverNameMap(Collection<Long> driverIdList) {
        if (CollectionUtils.isEmpty(driverIdList)) {
            return Maps.newHashMap();
        }
        List<DriverEntity> driverList = driverDao.selectBatchIds(driverIdList);
        return driverList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getDriverName));
    }

    /**
     * 查询同名的司机
     *
     * @param driverName
     * @return
     */
    public ResponseDTO<List<DriverSimpleVO>> queryByName(Long enterpriseId, String driverName) {
        List<DriverEntity> driverList = driverDao.selectByName(enterpriseId, driverName, Boolean.FALSE);
        List<DriverSimpleVO> driverSimpleList = SmartBeanUtil.copyList(driverList, DriverSimpleVO.class);
        Set<Long> enterpriseIdList = driverSimpleList.stream().filter(e -> e.getEnterpriseId() != null).map(DriverSimpleVO::getEnterpriseId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(enterpriseIdList)) {
            List<EnterpriseEntity> enterpriseEntityList = enterpriseDao.selectBatchIds(enterpriseIdList);
            Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));
            driverSimpleList.forEach(item -> {
                item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
            });
        }
        return ResponseDTO.ok(driverSimpleList);
    }

    /**
     * 司机导出
     *
     * @param driverQueryForm
     * @return
     */
    public List<DriverExportVO> queryByExport(DriverQueryForm driverQueryForm, Long enterpriseId) {
        driverQueryForm.setDeletedFlag(Boolean.FALSE);
        driverQueryForm.setEnterpriseId(enterpriseId);
        List<DriverVO> driverVOList = driverDao.queryByPage(null, driverQueryForm);
        if (CollectionUtils.isEmpty(driverVOList)) {
            return Lists.newArrayList();
        }

        buildListVO(driverVOList, enterpriseId, Boolean.FALSE);
        List<DriverExportVO> exportList = driverVOList.stream().map(driverVO -> {
            DriverExportVO exportVO = SmartBeanUtil.copy(driverVO, DriverExportVO.class);
            exportVO.setVehicleNumberList(driverVO.getDriverVehicleVOList().stream().map(DriverVehicleVO::getVehicleNumber).collect(Collectors.joining(StringConst.SEPARATOR)));
            exportVO.setAuditStatusDesc(SmartBaseEnumUtil.getEnumDescByValue(driverVO.getAuditStatus(), AuditStatusEnum.class));
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
    public ResponseDTO<String> businessModeUpdate(DriverBusinessModeUpdateForm updateForm, Long enterpriseId) {
        Long driverId = updateForm.getDriverId();
        DriverEntity updateEntity = new DriverEntity();
        updateEntity.setDriverId(driverId);
        updateEntity.setBusinessMode(updateForm.getBusinessMode());
        driverDao.updateById(updateEntity);
        return ResponseDTO.ok();
    }




}
