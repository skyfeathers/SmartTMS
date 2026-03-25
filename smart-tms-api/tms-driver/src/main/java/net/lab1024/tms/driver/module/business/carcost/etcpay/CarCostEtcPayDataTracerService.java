package net.lab1024.tms.driver.module.business.carcost.etcpay;

import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarCostEtcPayDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新建Etc支出Log
     *
     * @param vehicleId
     * @param etcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void saveEtcPayLog(Long vehicleId, CarCostEtcPayEntity etcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("新增ETC支出:", etcPayEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostOilPayEntity.class, null, etcPayEntity));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 更新Etc支出Log
     *
     * @param vehicleId
     * @param beforeEtcPayEntity
     * @param afterEtcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateEtcPayLog(Long vehicleId, CarCostEtcPayEntity beforeEtcPayEntity, CarCostEtcPayEntity afterEtcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanParse(beforeEtcPayEntity, afterEtcPayEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostOilPayEntity.class, beforeEtcPayEntity, afterEtcPayEntity));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

}
