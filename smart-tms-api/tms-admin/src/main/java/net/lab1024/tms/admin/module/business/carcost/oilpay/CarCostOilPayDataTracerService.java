package net.lab1024.tms.admin.module.business.carcost.oilpay;

import net.lab1024.tms.admin.module.business.carcost.oilpay.domain.CarCostOilPayVO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
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
public class CarCostOilPayDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新建油费支出Log
     *
     * @param oilPayVO
     * @param dataTracerRequestForm
     */
    @Async
    public void saveOilPayLog(CarCostOilPayVO oilPayVO, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("新增加油记录:", oilPayVO);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilPayVO.getVehicleId());
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
     * @param beforeEntity
     * @param afterEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateOilPayLog(CarCostOilPayVO beforeEntity, CarCostOilPayVO afterEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanParse(beforeEntity, afterEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeEntity.getVehicleId());
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

    /**
     * 审核油费支出Log
     *
     * @param oilPayVO
     * @param dataTracerRequestForm
     */
    @Async
    public void auditOilPayLog(CarCostOilPayVO oilPayVO, DataTracerRequestForm dataTracerRequestForm) {
        String content = this.getAuditContent(oilPayVO, dataTracerRequestForm);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilPayVO.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.AUDIT);
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

    private String getAuditContent(CarCostOilPayVO oilPayVO, DataTracerRequestForm dataTracerRequestForm) {
        StringBuilder builder = new StringBuilder();
        builder.append("油费支出审核信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核人】").append(DataTracerConstant.SPLIT).append(dataTracerRequestForm.getOperatorName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核结果】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(oilPayVO.getAuditStatus(), AuditStatusEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核备注】").append(DataTracerConstant.SPLIT).append(oilPayVO.getAuditRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 关联运单
     *
     * @param vehicleId
     * @param oilPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void relateWaybillLog(Long vehicleId, CarCostOilPayEntity oilPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("自有车油卡支出关联运单，运单ID：" + oilPayEntity.getWaybillId());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 取消关联运单日志
     *
     * @param oilPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelRelateWaybillLog(CarCostOilPayEntity oilPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilPayEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("自有车油卡支出取消关联运单");
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 删除日志
     *
     * @param oilPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void delOilPayLog(CarCostOilPayEntity oilPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilPayEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
        dataTracerForm.setOperateContent("自有车油卡支出删除");
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }
}
