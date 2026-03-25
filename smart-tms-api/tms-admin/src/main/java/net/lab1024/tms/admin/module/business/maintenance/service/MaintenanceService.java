package net.lab1024.tms.admin.module.business.maintenance.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.maintenance.dao.MaintenanceContentDao;
import net.lab1024.tms.admin.module.business.maintenance.dao.MaintenanceDao;
import net.lab1024.tms.admin.module.business.maintenance.domain.*;
import net.lab1024.tms.admin.module.business.maintenance.manager.MaintenanceManager;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceContentEntity;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceEntity;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    @Resource
    private VehicleDao vehicleDao;

    @Resource
    private MaintenanceDao maintenanceDao;

    @Resource
    private MaintenanceContentDao maintenanceContentDao;

    @Resource
    private MaintenanceManager maintenanceManager;

    @Resource
    private MaintenanceDataTracerService maintenanceDataTracerService;

    /**
     * 车辆保养分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<MaintenanceVO>> query(MaintenanceQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MaintenanceVO> maintenanceVOList = maintenanceDao.queryByPage(page, queryForm);
        this.buildContentVO(maintenanceVOList);
        PageResult<MaintenanceVO> pageResult = SmartPageUtil.convert2PageResult(page, maintenanceVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 填充保养内容
     *
     * @param maintenanceVOList
     */
    private void buildContentVO(List<MaintenanceVO> maintenanceVOList) {
        if (CollectionUtil.isEmpty(maintenanceVOList)) {
            return;
        }
        List<Long> maintenanceIdList = maintenanceVOList.stream().map(MaintenanceVO::getMaintenanceId).collect(Collectors.toList());
        List<MaintenanceContentEntity> contentEntityList = maintenanceContentDao.selectByMaintenanceIds(maintenanceIdList);
        Map<Long, List<MaintenanceContentEntity>> contentMap = contentEntityList.stream().collect(Collectors.groupingBy(MaintenanceContentEntity::getMaintenanceId));
        for (MaintenanceVO maintenanceVO : maintenanceVOList) {
            List<MaintenanceContentEntity> contentList = contentMap.get(maintenanceVO.getMaintenanceId());
            if (CollectionUtil.isEmpty(contentList)) {
                continue;
            }
            List<MaintenanceContentVO> contentVOList = SmartBeanUtil.copyList(contentList, MaintenanceContentVO.class);
            maintenanceVO.setContentVOList(contentVOList);
        }
    }

    /**
     * 新建保养记录
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(MaintenanceAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long vehicleId = addForm.getVehicleId();
        VehicleEntity vehicleEntity = vehicleDao.selectById(vehicleId);
        if (ObjectUtil.isEmpty(vehicleEntity) || vehicleEntity.getDeletedFlag() || vehicleEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车辆不可用");
        }
        MaintenanceEntity maintenanceEntity = SmartBeanUtil.copy(addForm, MaintenanceEntity.class);
        maintenanceEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        maintenanceEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());

        List<MaintenanceContentForm> contentFormList = addForm.getContentFormList();
        maintenanceManager.addHandle(maintenanceEntity, contentFormList);
        maintenanceDataTracerService.addLog(maintenanceEntity, contentFormList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(MaintenanceUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long maintenanceId = updateForm.getMaintenanceId();
        MaintenanceEntity beforeEntity = maintenanceDao.selectById(maintenanceId);
        if (ObjectUtil.isEmpty(beforeEntity) || beforeEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "保养信息不存在");
        }

        List<MaintenanceContentForm> afterContentFormList = updateForm.getContentFormList();
        MaintenanceEntity afterEntity = SmartBeanUtil.copy(updateForm, MaintenanceEntity.class);
        afterEntity.setUpdateUserId(dataTracerRequestForm.getOperatorId());
        afterEntity.setUpdateUserName(dataTracerRequestForm.getOperatorName());

        List<MaintenanceContentEntity> beforeContentEntityList = maintenanceContentDao.selectByMaintenanceIds(Lists.newArrayList(maintenanceId));
        maintenanceManager.updateHandel(afterEntity, afterContentFormList);

        List<MaintenanceContentForm> beforeContentFormList = SmartBeanUtil.copyList(beforeContentEntityList, MaintenanceContentForm.class);
        maintenanceDataTracerService.updateLog(beforeEntity, beforeContentFormList, afterEntity, afterContentFormList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除车辆保养记录
     *
     * @param maintenanceId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> delete(Long maintenanceId, DataTracerRequestForm dataTracerRequestForm) {
        MaintenanceEntity maintenanceEntity = maintenanceDao.selectById(maintenanceId);
        if (ObjectUtil.isEmpty(maintenanceEntity) || maintenanceEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "保养信息不存在");
        }

        maintenanceDao.updateDeletedFlag(maintenanceId, Boolean.TRUE);
        maintenanceDataTracerService.deleteLog(maintenanceEntity, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 根据车辆ID以及创建时间筛选
     *
     * @param vehicleId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<MaintenanceVO> selectListByVehicleId(Long vehicleId, LocalDate startTime, LocalDate endTime) {
        MaintenanceQueryForm maintenanceQueryForm = new MaintenanceQueryForm();
        maintenanceQueryForm.setVehicleId(vehicleId);
        maintenanceQueryForm.setStartDate(startTime);
        maintenanceQueryForm.setEndDate(endTime);
        maintenanceQueryForm.setDeletedFlag(Boolean.FALSE);
        List<MaintenanceVO> maintenanceVOList = maintenanceDao.queryByPage(null, maintenanceQueryForm);
        this.buildContentVO(maintenanceVOList);
        return maintenanceVOList;
    }

    /**
     * 根据车辆ID以及创建时间筛选
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    public List<MaintenanceVO> selectListByVehicleIdList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        MaintenanceQueryForm maintenanceQueryForm = new MaintenanceQueryForm();
        maintenanceQueryForm.setVehicleIdList(vehicleIdList);
        maintenanceQueryForm.setStartDate(startTime);
        maintenanceQueryForm.setEndDate(endTime);
        maintenanceQueryForm.setDeletedFlag(Boolean.FALSE);
        maintenanceQueryForm.setEnterpriseId(enterpriseId);
        List<MaintenanceVO> maintenanceVOList = maintenanceDao.queryByPage(null, maintenanceQueryForm);
        this.buildContentVO(maintenanceVOList);
        return maintenanceVOList;
    }

    public Map<Long, BigDecimal> getCarCostMaintenanceAmount(CarCostMonthStatisticQueryForm queryForm) {
        List<CarCostVehicleMonthAmountDTO> carCostVehicleMonthAmountDTOList = maintenanceDao.sumByModuleIdListAndType(queryForm, RepairModuleTypeEnum.VEHICLE.getValue());
        return carCostVehicleMonthAmountDTOList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));
    }
}