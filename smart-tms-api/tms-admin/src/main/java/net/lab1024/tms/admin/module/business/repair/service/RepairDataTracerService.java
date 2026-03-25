package net.lab1024.tms.admin.module.business.repair.service;

import net.lab1024.tms.admin.module.business.repair.domain.RepairContentForm;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import net.lab1024.tms.common.common.enumeration.BaseEnum;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.bracket.constant.BracketDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.repair.entity.RepairEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehicleDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;

    @Async
    public void addLog(RepairEntity repairEntity, List<RepairContentForm> contentFormList, DataTracerRequestForm dataTracerRequestForm) {
        Long moduleId = repairEntity.getModuleId();
        Integer moduleType = repairEntity.getModuleType();
        RepairModuleTypeEnum repairModuleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, RepairModuleTypeEnum.class);
        BaseEnum operateType = BracketDataTracerOperateTypeEnum.ADD_REPAIR;
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.BRACKET;
        if (repairModuleTypeEnum == RepairModuleTypeEnum.VEHICLE) {
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.VEHICLE;
            operateType = VehicleDataTracerOperateTypeEnum.ADD_REPAIR;
        }
        String content = this.getContent(repairEntity, contentFormList);
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(moduleId);
        dataTracerForm.setBusinessType(dataTracerBusinessTypeEnum);
        dataTracerForm.setOperateType(operateType);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    private String getContent(RepairEntity repairEntity, List<RepairContentForm> contentFormList) {
        StringBuilder repairInfo = new StringBuilder();
        contentFormList.forEach(e -> {
            repairInfo.append("【维修内容").append(DataTracerConstant.SPLIT).append(e.getRepairContent()).append(DataTracerConstant.BLANK)
                    .append("维修金额").append(DataTracerConstant.SPLIT).append(e.getRepairAmount()).append("】").append(DataTracerConstant.TAB);
        });

        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维修地点ID】").append(DataTracerConstant.SPLIT).append(repairEntity.getRepairPlantId()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维修人】").append(DataTracerConstant.SPLIT).append(repairEntity.getRepairPerson()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维修内容信息】").append(DataTracerConstant.SPLIT).append(repairInfo).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维修时间】").append(DataTracerConstant.SPLIT).append(repairEntity.getRepairDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(repairEntity.getRemark()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【附件】").append(DataTracerConstant.SPLIT).append(repairEntity.getAttachment()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 更新日志
     *
     * @param beforeRepairEntity
     * @param beforeContentFormList
     * @param afterRepairEntity
     * @param afterContentFormList
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(RepairEntity beforeRepairEntity,
                          List<RepairContentForm> beforeContentFormList,
                          RepairEntity afterRepairEntity,
                          List<RepairContentForm> afterContentFormList,
                          DataTracerRequestForm dataTracerRequestForm) {
        Long moduleId = beforeRepairEntity.getModuleId();
        Integer moduleType = beforeRepairEntity.getModuleType();
        RepairModuleTypeEnum repairModuleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, RepairModuleTypeEnum.class);

        BaseEnum operateType = BracketDataTracerOperateTypeEnum.UPDATE_REPAIR;
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.BRACKET;
        if (repairModuleTypeEnum == RepairModuleTypeEnum.VEHICLE) {
            operateType = VehicleDataTracerOperateTypeEnum.UPDATE_REPAIR;
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.VEHICLE;
        }

        String beforeContent = this.getContent(beforeRepairEntity, beforeContentFormList);
        String afterContent = this.getContent(afterRepairEntity, afterContentFormList);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(moduleId);
        dataTracerForm.setBusinessType(dataTracerBusinessTypeEnum);
        dataTracerForm.setOperateType(operateType);
        dataTracerForm.setDiff(new DataTracerDiffBO(beforeContent, afterContent));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    @Async
    public void deleteLog(RepairEntity repairEntity, DataTracerRequestForm dataTracerRequestForm) {
        this.deleteHandle(repairEntity, dataTracerRequestForm);
    }

    private void deleteHandle(RepairEntity repairEntity, DataTracerRequestForm dataTracerRequestForm) {
        Integer moduleType = repairEntity.getModuleType();
        Long moduleId = repairEntity.getModuleId();
        RepairModuleTypeEnum repairModuleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, RepairModuleTypeEnum.class);

        BaseEnum operateType = BracketDataTracerOperateTypeEnum.DEL_REPAIR;
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.BRACKET;
        if (repairModuleTypeEnum == RepairModuleTypeEnum.VEHICLE) {
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.VEHICLE;
            operateType = VehicleDataTracerOperateTypeEnum.DEL_REPAIR;
        }
        String content = dataTracerFieldService.beanObjectParse("删除内容", repairEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(moduleId);
        dataTracerForm.setBusinessType(dataTracerBusinessTypeEnum);
        dataTracerForm.setOperateType(operateType);
        dataTracerForm.setOperateContent(content);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


}
