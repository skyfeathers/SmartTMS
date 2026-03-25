package net.lab1024.tms.admin.module.business.maintenance.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceContentForm;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehicleDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MaintenanceDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新建保养记录Log
     *
     * @param maintenanceEntity
     * @param contentFormList
     * @param dataTracerRequestForm
     */
    @Async
    public void addLog(MaintenanceEntity maintenanceEntity, List<MaintenanceContentForm> contentFormList, DataTracerRequestForm dataTracerRequestForm) {
        String content = this.getContent(maintenanceEntity, contentFormList);
        
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(maintenanceEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.ADD_MAINTENANCE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 保养内容
     * 
     * @param maintenanceEntity
     * @param contentFormList
     * @return
     */
    private String getContent(MaintenanceEntity maintenanceEntity, List<MaintenanceContentForm> contentFormList) {
        StringBuilder repairInfo = new StringBuilder();
        contentFormList.forEach(e -> {
            repairInfo.append("【保养内容").append(DataTracerConstant.SPLIT).append(e.getMaintenanceContent()).append(DataTracerConstant.BLANK)
                    .append("保养金额").append(DataTracerConstant.SPLIT).append(e.getMaintenanceAmount()).append("】").append(DataTracerConstant.TAB);
        });

        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【保养地点】").append(DataTracerConstant.SPLIT).append(maintenanceEntity.getMaintenancePlant()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【保养人】").append(DataTracerConstant.SPLIT).append(maintenanceEntity.getMaintenancePerson()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【保养内容信息】").append(DataTracerConstant.SPLIT).append(repairInfo).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【保养时间】").append(DataTracerConstant.SPLIT).append(maintenanceEntity.getMaintenanceDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(maintenanceEntity.getRemark()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【保养里程】").append(DataTracerConstant.SPLIT).append(maintenanceEntity.getMileage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【下次保养时间】").append(DataTracerConstant.SPLIT).append(maintenanceEntity.getNextMaintenanceDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【下次保养里程】").append(DataTracerConstant.SPLIT).append(maintenanceEntity.getNextMaintenanceMileage()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 更新保养记录Log
     *
     * @param beforeEntity
     * @param beforeContentFormList
     * @param afterEntity
     * @param afterContentFormList
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(MaintenanceEntity beforeEntity,
                          List<MaintenanceContentForm> beforeContentFormList,
                          MaintenanceEntity afterEntity,
                          List<MaintenanceContentForm> afterContentFormList,
                          DataTracerRequestForm dataTracerRequestForm) {
        String beforeContent = this.getContent(beforeEntity, beforeContentFormList);
        String afterContent = this.getContent(afterEntity, afterContentFormList);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.UPDATE_MAINTENANCE);
        dataTracerForm.setDiff(new DataTracerDiffBO(beforeContent, afterContent));
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 删除保养记录
     *
     * @param maintenanceEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void deleteLog(MaintenanceEntity maintenanceEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = this.getContent(maintenanceEntity, Lists.newArrayList());

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(maintenanceEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.DEL_MAINTENANCE);
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