package net.lab1024.tms.admin.module.business.carcost.ureapay;

import net.lab1024.tms.admin.module.business.carcost.ureapay.domain.CarCostUreaPayVO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostUreaPayEntity;
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
public class CarCostUreaPayDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新建油费支出Log
     *
     * @param ureaPayVO
     * @param dataTracerRequestForm
     */
    @Async
    public void saveUreaPayLog(CarCostUreaPayVO ureaPayVO, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("新增尿素支出:", ureaPayVO);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(ureaPayVO.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostUreaPayVO.class, null, ureaPayVO));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 更新油费支出Log
     *
     * @param beforeUreaPayVO
     * @param afterUreaPayVO
     * @param dataTracerRequestForm
     */
    @Async
    public void updateUreaPayLog(CarCostUreaPayVO beforeUreaPayVO, CarCostUreaPayVO afterUreaPayVO, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanParse(beforeUreaPayVO, afterUreaPayVO);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeUreaPayVO.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostUreaPayVO.class, beforeUreaPayVO, afterUreaPayVO));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 审核油费支出Log
     *
     * @param ureaPayVO
     * @param dataTracerRequestForm
     */
    @Async
    public void auditUreaPayLog(CarCostUreaPayVO ureaPayVO, DataTracerRequestForm dataTracerRequestForm) {
        String content = this.getAuditContent(ureaPayVO, dataTracerRequestForm);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(ureaPayVO.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.AUDIT);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostUreaPayVO.class, null, ureaPayVO));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getAuditContent(CarCostUreaPayVO ureaPayVO, DataTracerRequestForm dataTracerRequestForm) {
        StringBuilder builder = new StringBuilder();
        builder.append("尿素支出审核信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核人】").append(DataTracerConstant.SPLIT).append(dataTracerRequestForm.getOperatorName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核结果】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(ureaPayVO.getAuditStatus(), AuditStatusEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核备注】").append(DataTracerConstant.SPLIT).append(ureaPayVO.getAuditRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 关联运单日志
     *
     * @param vehicleId
     * @param ureaPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void relateWaybillLog(Long vehicleId, CarCostUreaPayEntity ureaPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("自有车尿素支出关联运单，运单ID：" + ureaPayEntity.getWaybillId());
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
     * @param ureaPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelRelateWaybillLog(CarCostUreaPayEntity ureaPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(ureaPayEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("自有车尿素支出取消关联运单");
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 删除记录日志
     *
     * @param ureaPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void delUreaPayLog(CarCostUreaPayEntity ureaPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(ureaPayEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
        dataTracerForm.setOperateContent("自有车尿素支出删除");
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }
}
