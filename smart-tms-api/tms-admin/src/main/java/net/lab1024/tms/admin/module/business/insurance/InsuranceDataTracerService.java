package net.lab1024.tms.admin.module.business.insurance;

import net.lab1024.tms.common.common.enumeration.BaseEnum;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.bracket.constant.BracketDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.insurance.domain.InsuranceEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehicleDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author yandy
 */
@Service
public class InsuranceDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;


    @Async
    public void saveLog(InsuranceEntity insuranceEntity, DataTracerRequestForm dataTracerRequestForm) {
        this.addHandle(insuranceEntity, dataTracerRequestForm);
    }

    private void addHandle(InsuranceEntity insuranceEntity, DataTracerRequestForm dataTracerRequestForm) {
        Integer moduleType = insuranceEntity.getModuleType();
        Long moduleId = insuranceEntity.getModuleId();
        InsuranceModuleTypeEnum insuranceModuleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, InsuranceModuleTypeEnum.class);

        BaseEnum operateType = BracketDataTracerOperateTypeEnum.ADD_INSURANCE;
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.BRACKET;
        if (insuranceModuleTypeEnum == InsuranceModuleTypeEnum.VEHICLE) {
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.VEHICLE;
            operateType = VehicleDataTracerOperateTypeEnum.ADD_INSURANCE;
        }
        String content = dataTracerFieldService.beanObjectParse("新增内容", insuranceEntity);

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
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }

    /**
     * 更新日志
     *
     * @param beforeEntity
     * @param afterEntity
     * @param dataTracerRequestForm
     */

    @Async
    public void updateLog(InsuranceEntity beforeEntity, InsuranceEntity afterEntity, DataTracerRequestForm dataTracerRequestForm) {
        // 是否统一模块
        Boolean sameModule = beforeEntity.getModuleType().equals(afterEntity.getModuleType());
        // 同一模块，不同对象
        if (sameModule && !beforeEntity.getModuleId().equals(afterEntity.getModuleId())) {
            this.deleteHandle(beforeEntity, dataTracerRequestForm);
            this.addHandle(afterEntity, dataTracerRequestForm);
            return;
        }
        // 不同模块
        if (!sameModule) {
            this.deleteHandle(beforeEntity, dataTracerRequestForm);
            this.addHandle(afterEntity, dataTracerRequestForm);
            return;
        }
        //同模块，同对象
        Integer moduleType = beforeEntity.getModuleType();
        Long moduleId = beforeEntity.getModuleId();
        InsuranceModuleTypeEnum insuranceModuleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, InsuranceModuleTypeEnum.class);

        BaseEnum operateType = BracketDataTracerOperateTypeEnum.UPDATE_INSURANCE;
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.BRACKET;
        if (insuranceModuleTypeEnum == InsuranceModuleTypeEnum.VEHICLE) {
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.VEHICLE;
            operateType = VehicleDataTracerOperateTypeEnum.UPDATE_INSURANCE;
        }
        String content = dataTracerFieldService.beanParse(beforeEntity, afterEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(moduleId);
        dataTracerForm.setBusinessType(dataTracerBusinessTypeEnum);
        dataTracerForm.setOperateType(operateType);
        dataTracerForm.setOperateContent(beforeEntity.getPolicyNumber() + content);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }


    @Async
    public void batchDeleteLog(List<InsuranceEntity> insuranceEntityList, DataTracerRequestForm dataTracerRequestForm) {
        for (InsuranceEntity insuranceEntity : insuranceEntityList) {
            this.deleteHandle(insuranceEntity, dataTracerRequestForm);
        }
    }

    private void deleteHandle(InsuranceEntity insuranceEntity, DataTracerRequestForm dataTracerRequestForm) {
        Integer moduleType = insuranceEntity.getModuleType();
        Long moduleId = insuranceEntity.getModuleId();
        InsuranceModuleTypeEnum insuranceModuleTypeEnum = SmartBaseEnumUtil.getEnumByValue(moduleType, InsuranceModuleTypeEnum.class);

        BaseEnum operateType = BracketDataTracerOperateTypeEnum.DEL_INSURANCE;
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.BRACKET;
        if (insuranceModuleTypeEnum == InsuranceModuleTypeEnum.VEHICLE) {
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.VEHICLE;
            operateType = VehicleDataTracerOperateTypeEnum.DEL_INSURANCE;
        }
        String content = dataTracerFieldService.beanObjectParse("删除内容", insuranceEntity);

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
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }


}
