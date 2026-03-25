package net.lab1024.tms.admin.module.business.review.service;

import net.lab1024.tms.common.module.business.review.ReviewEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehicleDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReviewDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 保存审车记录 Log
     *
     * @param reviewEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void addLog(ReviewEntity reviewEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("添加审车记录", reviewEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(reviewEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.ADD_REVIEW);
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
     * 更新审车记录 Log
     *
     * @param beforeEntity
     * @param afterEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(ReviewEntity beforeEntity, ReviewEntity afterEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanParse(beforeEntity, afterEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.UPDATE_REVIEW);
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
     * 删除审车记录 Log
     *
     * @param reviewEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void deleteLog(ReviewEntity reviewEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("删除审车信息", reviewEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(reviewEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.DEL_REVIEW);
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