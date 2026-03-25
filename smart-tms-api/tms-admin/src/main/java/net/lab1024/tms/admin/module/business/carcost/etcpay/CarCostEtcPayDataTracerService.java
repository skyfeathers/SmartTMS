package net.lab1024.tms.admin.module.business.carcost.etcpay;

import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
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
public class CarCostEtcPayDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新建Etc支出Log
     *
     * @param etcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void saveEtcPayLog(CarCostEtcPayEntity etcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanObjectParse("新增ETC支出:", etcPayEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(etcPayEntity.getVehicleId());
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
     * @param beforeEtcPayEntity
     * @param afterEtcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateEtcPayLog(CarCostEtcPayEntity beforeEtcPayEntity, CarCostEtcPayEntity afterEtcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = dataTracerFieldService.beanParse(beforeEtcPayEntity, afterEtcPayEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeEtcPayEntity.getVehicleId());
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

    /**
     * 审核Etc支出Log
     *
     * @param afterEtcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void auditEtcPayLog(CarCostEtcPayEntity afterEtcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        String content = this.getAuditContent(afterEtcPayEntity, dataTracerRequestForm);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(afterEtcPayEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.AUDIT);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setExtraData(new DataTracerExtraData(CarCostCashPayEntity.class, null, afterEtcPayEntity));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getAuditContent(CarCostEtcPayEntity afterEtcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        StringBuilder builder = new StringBuilder();
        builder.append("ETC支出审核信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核人】").append(DataTracerConstant.SPLIT).append(dataTracerRequestForm.getOperatorName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核结果】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(afterEtcPayEntity.getAuditStatus(), AuditStatusEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【审核备注】").append(DataTracerConstant.SPLIT).append(afterEtcPayEntity.getAuditRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 关联运单日志
     *
     * @param vehicleId
     * @param etcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void relateWaybillLog(Long vehicleId, CarCostEtcPayEntity etcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("自有车ETC支出关联运单，运单ID：" + etcPayEntity.getWaybillId());
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
     * @param etcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelRelateWaybillLog(CarCostEtcPayEntity etcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(etcPayEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("自有车ETC支出取消关联运单");
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 删除ETC支出日志
     *
     * @param etcPayEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void delEtcPayLog(CarCostEtcPayEntity etcPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(etcPayEntity.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
        dataTracerForm.setOperateContent("自有车ETC支出删除");
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }
}
