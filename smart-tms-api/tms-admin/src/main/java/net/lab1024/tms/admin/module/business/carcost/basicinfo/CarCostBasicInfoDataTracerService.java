package net.lab1024.tms.admin.module.business.carcost.basicinfo;

import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostBasicInfoEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarCostBasicInfoDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    //--------------------------------基本信息----------------------------------

    /**
     * 基本信息保存日志
     *
     * @param basicInfoEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void saveBasicInfoLog(CarCostBasicInfoEntity basicInfoEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("新建自有车基本信息:", basicInfoEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(basicInfoEntity.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 基本信息更新日志
     *
     * @param beforeEntity
     * @param afterEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateBasicInfoLog(CarCostBasicInfoEntity beforeEntity, CarCostBasicInfoEntity afterEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanParse(beforeEntity, afterEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeEntity.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 更改确认状态日志
     *
     * @param basicInfoEntity
     * @param confirmFlag
     * @param dataTracerRequestForm
     */
    @Async
    public void updateConfirmLog(CarCostBasicInfoEntity basicInfoEntity, Boolean confirmFlag, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(basicInfoEntity.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("更改基本信息确认状态为：" + confirmFlag.toString());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 删除基本信息日志
     *
     * @param basicInfoEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void deleteLog(CarCostBasicInfoEntity basicInfoEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(basicInfoEntity.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
        dataTracerForm.setOperateContent("删除费用基本信息");
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }
}
