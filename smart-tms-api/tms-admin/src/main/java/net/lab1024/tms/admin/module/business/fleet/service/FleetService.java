package net.lab1024.tms.admin.module.business.fleet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetBankDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBankDTO;
import net.lab1024.tms.admin.module.business.fleet.domain.form.*;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.*;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.constants.FleetItemTypeEnum;
import net.lab1024.tms.common.module.business.fleet.domain.FleetBankEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetItemEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncryptUtil;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 车队管理
 *
 * @author lidoudou
 * @date 2022/6/27 下午3:26
 */
@Service
public class FleetService {

    @Autowired
    private FleetDao fleetDao;

    @Autowired
    private FleetManager fleetManager;

    @Autowired
    private FleetBankDao bankDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private FleetItemManager fleetItemManager;

    @Autowired
    private FleetDataTracerService fleetDataTracerService;

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DictCacheService dictCacheService;
    /**
     * 分页查询车队信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<FleetVO>> queryByPage(FleetQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FleetVO> fleetList = fleetDao.queryByPage(page, queryForm);
        FieldEncryptUtil.injectList(fleetList);
        PageResult<FleetVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, fleetList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 查询车队列表
     *
     * @return
     */
    public ResponseDTO<List<FleetSimpleVO>> queryList(Long enterpriseId) {
        FleetQueryForm queryForm = new FleetQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setEnterpriseId(enterpriseId);
        List<FleetVO> fleetVOList = fleetDao.queryByPage(null, queryForm);
        List<FleetSimpleVO> fleetList = SmartBeanUtil.copyList(fleetVOList, FleetSimpleVO.class);
        return ResponseDTO.ok(fleetList);
    }

    /**
     * 查询车队详情
     *
     * @param fleetId
     * @return
     */
    public ResponseDTO<FleetDetailVO> getDetail(Long fleetId) {
        FleetDetailVO detailVO = fleetDao.getDetail(fleetId, Boolean.FALSE);
        if (null == detailVO) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        List<FleetBankEntity> bankList = bankDao.selectByFleetId(fleetId);
        detailVO.setBankList(SmartBeanUtil.copyList(bankList, FleetBankDTO.class));
        if (detailVO.getManagerId() != null) {
            EmployeeEntity employeeEntity = employeeDao.selectById(detailVO.getManagerId());
            detailVO.setManagerName(employeeEntity != null ? employeeEntity.getActualName() : StringConst.EMPTY);
        }
        return ResponseDTO.ok(detailVO);
    }

    /**
     * 保存车队
     *
     * @param addDTO
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(FleetAddForm addDTO, DataTracerRequestForm dataTracerRequestForm) {
        // 判断车队名称是否存在
        List<FleetEntity> fleetList = fleetDao.selectByNameExcludeIds(addDTO.getEnterpriseId(), addDTO.getFleetName(), null, false);
        if (CollectionUtils.isNotEmpty(fleetList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "车队名称已存在");
        }
        Long fleetId = fleetManager.addHandle(addDTO, dataTracerRequestForm.getOperatorId(), dataTracerRequestForm.getOperatorName());
        fleetDataTracerService.saveLog(fleetId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 保存车队
     *
     * @param updateDTO
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(FleetUpdateForm updateDTO, DataTracerRequestForm dataTracerRequestForm) {
        Long fleetId = updateDTO.getFleetId();
        // 判断车队名称是否存在
        List<FleetEntity> fleetList = fleetDao.selectByNameExcludeIds(updateDTO.getEnterpriseId(), updateDTO.getFleetName(), Arrays.asList(fleetId), false);
        if (CollectionUtils.isNotEmpty(fleetList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "车队名称已存在");
        }
        FleetEntity dbFleetEntity = fleetDao.selectById(fleetId);
        if (null == dbFleetEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        FleetDetailVO beforeData = this.getDetail(fleetId).getData();
        fleetManager.updateHandle(updateDTO, dataTracerRequestForm.getOperatorId(), dataTracerRequestForm.getOperatorName());
        FleetDetailVO afterData = this.getDetail(fleetId).getData();
        fleetDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 删除车队
     *
     * @param fleetIdList
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> deleteFleet(List<Long> fleetIdList, DataTracerRequestForm dataTracerRequestForm) {
        fleetManager.deleteFleet(fleetIdList, dataTracerRequestForm.getOperatorId());
        fleetDataTracerService.batchDeleteLog(fleetIdList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 根据车队查询司机信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<FleetDriverVO>> queryDriverByPage(FleetItemQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setItemType(FleetItemTypeEnum.DRIVER.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FleetDriverVO> fleetList = fleetItemManager.getBaseMapper().queryDriverByPage(page, queryForm);
        PageResult<FleetDriverVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, fleetList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 根据车队查询司机信息
     *
     * @param fleetId
     * @return
     */
    public ResponseDTO<List<FleetDriverVO>> queryDriverList(Long fleetId) {
        FleetItemQueryForm queryForm = new FleetItemQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setFleetId(fleetId);
        queryForm.setItemType(FleetItemTypeEnum.DRIVER.getValue());
        List<FleetDriverVO> driverList = fleetItemManager.getBaseMapper().queryDriverByPage(null, queryForm);
        return ResponseDTO.ok(driverList);
    }

    /**
     * 根据车队查询车辆信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<FleetVehicleVO>> queryVehicleByPage(FleetItemQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setItemType(FleetItemTypeEnum.VEHICLE.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FleetVehicleVO> fleetList = fleetItemManager.getBaseMapper().queryVehicleByPage(page, queryForm);
        PageResult<FleetVehicleVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, fleetList);
        return ResponseDTO.ok(pageResultDTO);
    }

    public ResponseDTO<String> addItem(FleetItemAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long fleetId = addForm.getFleetId();
        Integer itemType = addForm.getItemType();
        List<Long> itemIdList = addForm.getItemIdList();
        FleetEntity fleetEntity = fleetDao.selectById(fleetId);
        if (null == fleetEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (fleetEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ResponseDTO<String> validateResp = this.validate(itemType, itemIdList);
        if (!validateResp.getOk()) {
            return validateResp;
        }
        List<Long> existItemIdList = fleetItemManager.getBaseMapper().selectByFleetIdAndType(fleetId, itemType);
        // 过滤出不存在的ID
        List<Long> addItemIdList = itemIdList.stream().filter(e -> !existItemIdList.contains(e)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(addItemIdList)) {
            return ResponseDTO.ok();
        }

        Long requestUserId = dataTracerRequestForm.getOperatorId();
        String requestUserName = dataTracerRequestForm.getOperatorName();
        List<FleetItemEntity> addList = addItemIdList.stream().map(itemId -> {
            FleetItemEntity fleetItemEntity = new FleetItemEntity();
            fleetItemEntity.setFleetId(fleetId);
            fleetItemEntity.setItemType(itemType);
            fleetItemEntity.setItemId(itemId);
            fleetItemEntity.setCreateUserId(requestUserId);
            fleetItemEntity.setCreateUserName(requestUserName);
            return fleetItemEntity;
        }).collect(Collectors.toList());
        fleetItemManager.saveBatch(addList);
        fleetDataTracerService.addItemLog(addForm, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 验证数据正确性
     *
     * @param itemType
     * @param itemIdList
     * @return
     */
    private ResponseDTO<String> validate(Integer itemType, List<Long> itemIdList) {
        if (FleetItemTypeEnum.DRIVER.equalsValue(itemType)) {
            List<DriverEntity> driverList = driverDao.selectList(itemIdList, null, null, Boolean.FALSE);
            if (itemIdList.size() != driverList.size()) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "司机信息错误，请刷新后重试～");
            }
            return ResponseDTO.ok();
        } else {
            List<VehicleEntity> vehicleList = vehicleDao.selectVehicleList(itemIdList, null, Boolean.FALSE);
            if (itemIdList.size() != vehicleList.size()) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车辆信息错误，请刷新后重试～");
            }
            return ResponseDTO.ok();
        }
    }

    /**
     * 根据ID删除关联项目
     *
     * @param fleetItemId
     * @return
     */
    public ResponseDTO<String> deleteItem(Long fleetItemId, DataTracerRequestForm dataTracerRequestForm) {
        FleetItemEntity fleetItemEntity = fleetItemManager.getBaseMapper().selectById(fleetItemId);
        if (null == fleetItemEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        fleetItemManager.getBaseMapper().deleteById(fleetItemId);
        fleetDataTracerService.deleteItemLog(fleetItemEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 车队审核
     *
     * @param auditForm
     * @return
     */
    public ResponseDTO<String> audit(FleetAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Integer> auditStatusList = Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getValue(),
                AuditStatusEnum.REJECT.getValue());
        if (!auditStatusList.contains(auditForm.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审核状态错误");
        }
        List<Long> fleetIdList = auditForm.getFleetIdList();
        List<FleetEntity> fleetEntities = fleetDao.selectBatchIds(fleetIdList);
        List<FleetEntity> needAuditEntities = fleetEntities.stream().filter(e -> !e.getDeletedFlag() && AuditStatusEnum.WAIT_AUDIT.equalsValue(e.getAuditStatus())).collect(Collectors.toList());
        if (needAuditEntities.size() != fleetIdList.size()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车队数据异常，请刷新后重试");
        }
        fleetDao.batchUpdateAuditStatus(fleetIdList, auditForm.getAuditStatus(), auditForm.getRemark());
        fleetDataTracerService.batchAuditLog(fleetIdList, auditForm.getAuditStatus(), auditForm.getRemark(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 批量修改负责人
     *
     * @param managerUpdateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> batchUpdateManager(FleetManagerUpdateForm managerUpdateForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> fleetIdList = managerUpdateForm.getFleetIdList();
        List<FleetEntity> fleetEntityList = fleetDao.selectBatchIds(fleetIdList);
        if (fleetEntityList.size() != fleetIdList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(managerUpdateForm.getManagerId());
        if (employeeEntity == null) {
            return ResponseDTO.userErrorParam("负责人不存在");
        }
        List<FleetEntity> updateList = fleetIdList.stream().map(fleetId -> {
            FleetEntity updateEntity = new FleetEntity();
            updateEntity.setFleetId(fleetId);
            updateEntity.setManagerId(managerUpdateForm.getManagerId());
            return updateEntity;
        }).collect(Collectors.toList());
        fleetManager.updateBatchById(updateList);
        fleetDataTracerService.batchUpdateManagerLog(fleetIdList, employeeEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 车队银行列表
     *
     * @param fleetId
     * @return
     */
    public ResponseDTO<List<FleetBankDTO>> bankList(Long fleetId) {
        List<FleetBankEntity> bankList = bankDao.selectByFleetId(fleetId);
        if (CollectionUtils.isEmpty(bankList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<FleetBankDTO> fleetBankDTOList = SmartBeanUtil.copyList(bankList, FleetBankDTO.class);

        return ResponseDTO.ok(fleetBankDTOList);
    }

    /**
     * 添加银行
     * @param bankAddForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> bankAdd(FleetBankAddForm bankAddForm, DataTracerRequestForm dataTracerRequestForm) {
        FleetEntity fleetEntity = fleetDao.selectById(bankAddForm.getFleetId());
        if (null == fleetEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        String bankName = dictCacheService.selectValueNameByValueCode(bankAddForm.getBankType());
        FleetBankEntity fleetBankEntity = SmartBeanUtil.copy(bankAddForm, FleetBankEntity.class);
        fleetBankEntity.setBankName(bankName);
        fleetManager.bankAdd(fleetBankEntity);
        fleetDataTracerService.bankAddLog(fleetBankEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }
}
