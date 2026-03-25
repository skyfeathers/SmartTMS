package net.lab1024.tms.admin.module.business.repair.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.bracket.BracketDao;
import net.lab1024.tms.admin.module.business.material.repairplant.RepairPlantDao;
import net.lab1024.tms.admin.module.business.repair.dao.RepairContentDao;
import net.lab1024.tms.admin.module.business.repair.dao.RepairDao;
import net.lab1024.tms.admin.module.business.repair.domain.*;
import net.lab1024.tms.admin.module.business.repair.manager.RepairManager;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantEntity;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import net.lab1024.tms.common.module.business.repair.entity.RepairContentEntity;
import net.lab1024.tms.common.module.business.repair.entity.RepairEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepairService {

    @Autowired
    private RepairDao repairDao;

    @Resource
    private RepairContentDao repairContentDao;

    @Autowired
    private RepairDataTracerService repairDataTracerService;

    @Resource
    private RepairManager repairManager;

    @Resource
    private RepairPlantDao repairPlantDao;

    @Resource
    private VehicleDao vehicleDao;

    @Resource
    private BracketDao bracketDao;

    /**
     * 分页查询维修信息
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<RepairVO>> queryByPage(RepairQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RepairVO> repairList = repairDao.queryByPage(page, queryForm);
        this.buildContentInfo(repairList);
        PageResult<RepairVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, repairList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 查询维修厂家金额总计
     *
     * @param repairPlantId
     * @return
     */
    public ResponseDTO<BigDecimal> amountSum(Long repairPlantId) {
        List<Long> repairIdList = repairDao.selectByRepairPlantId(repairPlantId, false);
        if (CollectionUtils.isEmpty(repairIdList)) {
            return ResponseDTO.ok(BigDecimal.ZERO);
        }
        BigDecimal amountSum = repairContentDao.selectSumAmountByRepairIdList(repairIdList);
        return ResponseDTO.ok(amountSum);
    }

    /**
     * 设置维修内容信息
     *
     * @param repairList
     */
    private void buildContentInfo(List<RepairVO> repairList) {
        if (CollectionUtil.isEmpty(repairList)) {
            return ;
        }

        List<Long> repairIdList = repairList.stream().map(RepairVO::getRepairId).collect(Collectors.toList());
        List<RepairContentEntity> contentEntityList = repairContentDao.selectByRepairIds(repairIdList);
        Map<Long, List<RepairContentEntity>> contentMap =
                contentEntityList.stream().collect(Collectors.groupingBy(RepairContentEntity::getRepairId));
        for (RepairVO repairVO : repairList) {
            List<RepairContentEntity> contentList = contentMap.get(repairVO.getRepairId());
            if (CollectionUtil.isEmpty(contentList)) {
                repairVO.setSumAmount(BigDecimal.ZERO);
                continue;
            }

            List<RepairContentVO> contentVOList = SmartBeanUtil.copyList(contentList, RepairContentVO.class);
            repairVO.setContentVOList(contentVOList);
            repairVO.setSumAmount(contentVOList.stream()
                    .map(RepairContentVO::getRepairAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        }
    }

    /**
     * 保存维修信息
     *
     * @param addDTO
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(RepairAddForm addDTO, DataTracerRequestForm dataTracerRequestForm) {
        RepairPlantEntity plantEntity = repairPlantDao.selectById(addDTO.getRepairPlantId());
        if (ObjectUtil.isEmpty(plantEntity) || plantEntity.getDeletedFlag() || plantEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "维修厂不可用");
        }
        Long moduleId = addDTO.getModuleId();
        if (RepairModuleTypeEnum.VEHICLE.equalsValue(addDTO.getModuleType())) {
            VehicleEntity vehicleEntity = vehicleDao.selectById(moduleId);
            if (ObjectUtil.isEmpty(vehicleEntity) || vehicleEntity.getDeletedFlag() || vehicleEntity.getDisabledFlag()) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "车辆不可用");
            }
        } else {
            BracketEntity bracketEntity = bracketDao.selectById(moduleId);
            if (ObjectUtil.isEmpty(bracketEntity) || bracketEntity.getDeletedFlag()) {
                return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "挂车不可用");
            }
        }
        RepairEntity repairEntity = SmartBeanUtil.copy(addDTO, RepairEntity.class);
        repairEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        repairEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());

        List<RepairContentForm> contentFormList = addDTO.getContentFormList();
        repairManager.handleAdd(repairEntity, contentFormList);
        repairDataTracerService.addLog(repairEntity, contentFormList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 修改维修信息
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(RepairUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        RepairPlantEntity plantEntity = repairPlantDao.selectById(updateForm.getRepairPlantId());
        if (ObjectUtil.isEmpty(plantEntity) || plantEntity.getDeletedFlag() || plantEntity.getDisabledFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "维修厂不可用");
        }
        Long repairId = updateForm.getRepairId();
        RepairEntity beforeRepairEntity = repairDao.selectById(repairId);
        if (null == beforeRepairEntity || beforeRepairEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        List<RepairContentEntity> beforeContentEntityList = repairContentDao.selectByRepairIds(Lists.newArrayList(repairId));
        List<RepairContentForm> beforeContentFormList = SmartBeanUtil.copyList(beforeContentEntityList, RepairContentForm.class);
        RepairEntity afterRepairEntity = SmartBeanUtil.copy(updateForm, RepairEntity.class);
        afterRepairEntity.setUpdateUserId(dataTracerRequestForm.getOperatorId());
        afterRepairEntity.setUpdateUserName(dataTracerRequestForm.getOperatorName());
        List<RepairContentForm> afterContentFormList = updateForm.getContentFormList();
        repairManager.handleUpdate(afterRepairEntity, afterContentFormList);
        repairDataTracerService.updateLog(beforeRepairEntity, beforeContentFormList, afterRepairEntity, afterContentFormList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 删除维修信息
     *
     * @param repairId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> deleteRepair(Long repairId, DataTracerRequestForm dataTracerRequestForm) {
        RepairEntity repairEntity = repairDao.selectById(repairId);
        if (null == repairEntity || repairEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "维修信息不存在");
        }

        repairDao.updateDeletedFlag(repairId, Boolean.TRUE);
        repairDataTracerService.deleteLog(repairEntity, dataTracerRequestForm);
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
    public List<RepairVO> selectListByVehicleId(Long vehicleId, LocalDate startTime, LocalDate endTime) {
        RepairQueryForm repairQueryForm = new RepairQueryForm();
        repairQueryForm.setModuleId(vehicleId);
        repairQueryForm.setModuleType(RepairModuleTypeEnum.VEHICLE.getValue());
        repairQueryForm.setCreateStartTime(startTime);
        repairQueryForm.setCreateEndTime(endTime);
        repairQueryForm.setDeletedFlag(Boolean.FALSE);
        List<RepairVO> repairList = repairDao.queryByPage(null, repairQueryForm);
        this.buildContentInfo(repairList);
        return repairList;
    }

    /**
     * 根据车辆ID以及创建时间筛选
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    public List<RepairVO> selectListByVehicleIdList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        RepairQueryForm repairQueryForm = new RepairQueryForm();
        repairQueryForm.setModuleIdList(vehicleIdList);
        repairQueryForm.setModuleType(RepairModuleTypeEnum.VEHICLE.getValue());
        repairQueryForm.setCreateStartTime(startTime);
        repairQueryForm.setCreateEndTime(endTime);
        repairQueryForm.setDeletedFlag(Boolean.FALSE);
        repairQueryForm.setEnterpriseId(enterpriseId);
        List<RepairVO> repairList = repairDao.queryByPage(null, repairQueryForm);
        this.buildContentInfo(repairList);
        return repairList;
    }

    public Map<Long, BigDecimal> getCarCostRepairAmount(CarCostMonthStatisticQueryForm queryForm) {
        List<CarCostVehicleMonthAmountDTO> carCostVehicleMonthAmountDTOList = repairDao.sumByModuleIdListAndType(queryForm, RepairModuleTypeEnum.VEHICLE.getValue());
        return carCostVehicleMonthAmountDTOList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));
    }

}
