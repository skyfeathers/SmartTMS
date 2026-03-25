package net.lab1024.tms.driver.module.business.carcost.oilpay;

import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.driver.module.business.carcost.oilpay.domain.CarCostOilPayVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarCostOilPayDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新建油费支出Log
     *
     * @param vehicleId
     * @param oilPayVO
     * @param dataTracerRequestForm
     */
    @Async
    public void saveOilPayLog(Long vehicleId, CarCostOilPayVO oilPayVO, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("新增加油记录:", oilPayVO);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostOilPayVO.class, null, oilPayVO));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 更新油费支出Log
     *
     * @param vehicleId
     * @param beforeEntity
     * @param afterEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateOilPayLog(Long vehicleId, CarCostOilPayVO beforeEntity, CarCostOilPayVO afterEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanParse(beforeEntity, afterEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostOilPayVO.class, beforeEntity, afterEntity));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

}
