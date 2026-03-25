package net.lab1024.tms.admin.module.business.vehicle.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleDetailVO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.VehicleDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class VehicleDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;
    @Autowired
    private DictCacheService dictCacheService;

    /**
     * 新增日志
     * @param vehicleId
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(Long vehicleId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(vehicleId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 更新日志
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(VehicleDetailVO beforeData, VehicleDetailVO afterData, DataTracerRequestForm dataTracerRequestForm){
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getVehicleId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData,afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getVehicleContent(beforeData), this.getVehicleContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(VehicleDetailVO.class,beforeData,afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }



    /**
     * 获取车辆信息
     * @param detailVO
     * @return
     */
    private String getVehicleContent(VehicleDetailVO detailVO){
        //基本信息
        String baseContent = this.getBaseContent(detailVO);
        //行驶证信息
        String drivingLicenseContent = this.getDrivingLicenseContent(detailVO);
        //道路运输证
        String roadTransportCertificateContent = this.getRoadTransportCertificateContent(detailVO);
        //挂靠企业
        String relyEnterpriseContent = this.getRelyEnterpriseContent(detailVO);
        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(drivingLicenseContent)
                .append(roadTransportCertificateContent)
                .append(relyEnterpriseContent);
        return builder.toString();
    }

    private String getBaseContent(VehicleDetailVO detailVO) {
        String driver = "";
        if(CollectionUtils.isNotEmpty(detailVO.getDriverVehicleList())){
            driver = detailVO.getDriverVehicleList().stream().map(e->e.getDriverName()+"-" + e.getTelephone()).collect(Collectors.joining(","));
        }
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【速记码】").append(DataTracerConstant.SPLIT).append(detailVO.getShorthand()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【经营方式】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getBusinessMode(), BusinessModeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【绑定司机】").append(DataTracerConstant.SPLIT).append(driver).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆审验到期时间】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleAuditExpireDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【绑定挂车】").append(DataTracerConstant.SPLIT).append(detailVO.getBracketNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【负责人】").append(DataTracerConstant.SPLIT).append(detailVO.getManagerName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【品牌型号】").append(DataTracerConstant.SPLIT).append(detailVO.getModel()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发动机号】").append(DataTracerConstant.SPLIT).append(detailVO.getEngineNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【机动车登记证书编号】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleRegistrationCertificateNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(detailVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getDrivingLicenseContent(VehicleDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("行驶证信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证正本-附件】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车牌号】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车牌颜色】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getVehiclePlateColorCode(), VehiclePlateColorEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆类型】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCodeSplit(detailVO.getVehicleType())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【车辆识别代码（车架号）】").append(DataTracerConstant.SPLIT).append(detailVO.getVin()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属人】").append(DataTracerConstant.SPLIT).append(detailVO.getOwner()).append(DataTracerConstant.LINE)
//                .append(DataTracerConstant.TAB).append("【所属人性质】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getOwnerType(), OwnerTypeEnum.class)).append(DataTracerConstant.LINE)
//                .append(DataTracerConstant.TAB).append("【车辆行驶证档案编号】").append(DataTracerConstant.SPLIT).append(detailVO.getLicenseNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证机关】").append(DataTracerConstant.SPLIT).append(detailVO.getIssuingOrganizations()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【注册日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRegisterDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证日期】").append(DataTracerConstant.SPLIT).append(detailVO.getIssueDate()).append(DataTracerConstant.LINE)
//                .append(DataTracerConstant.TAB).append("【到期日期】").append(DataTracerConstant.SPLIT).append(detailVO.getExpireDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【行驶证副本附件】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseEctypeAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【总质量】").append(DataTracerConstant.SPLIT).append(detailVO.getGrossMass()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【核定载质量（载重）】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleTonnage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【核定载质量（载重）】").append(DataTracerConstant.SPLIT).append(detailVO.getVehicleTonnage()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【整备质量】").append(DataTracerConstant.SPLIT).append(detailVO.getCurbWeight()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【牵引总质量）】").append(DataTracerConstant.SPLIT).append(detailVO.getTractionWeight()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【外廓尺寸】").append(DataTracerConstant.SPLIT).append(detailVO.getGabarite()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【能源类型】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCodeSplit(detailVO.getVehicleEnergyType())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用性质】").append(DataTracerConstant.SPLIT).append(detailVO.getNature()).append(DataTracerConstant.LINE);

        return builder.toString().replaceAll("null", "");
    }

    private String getRoadTransportCertificateContent(VehicleDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("道路运输证:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输证-附件】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输证号】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输经营许可证号】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportBusinessLicenseNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道运证有效期开始日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateStartDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道运证有效期结束日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRoadTransportCertificateExpireDate()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getRelyEnterpriseContent(VehicleDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("挂靠企业:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【挂靠企业协议附件】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【挂靠协议到期时间】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseExpireDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【挂靠企业营业执照】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseBusinessLicenseAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【挂靠企业名称】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【统一社会信用代码】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseUnifiedSocialCreditCode()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输经营许可证】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseRoadTransportBusinessLicenseAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【道路运输经营许可证号】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseRoadTransportBusinessLicenseNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseRoadTransportBusinessLicenseIssueDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【结束日期】").append(DataTracerConstant.SPLIT).append(detailVO.getRelyEnterpriseRoadTransportBusinessLicenseExpireDate()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 批量删除日志
     * @param vehicleIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchDeleteLog(List<Long> vehicleIdList, DataTracerRequestForm dataTracerRequestForm) {
        if(CollectionUtils.isEmpty(vehicleIdList)){
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long vehicleId : vehicleIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(vehicleId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 启用禁用标识
     * @param vehicleIdList
     * @param disabledFlag
     * @param dataTracerRequestForm
     */
    public void batchUpdateDisabledLog(List<Long> vehicleIdList, boolean disabledFlag, DataTracerRequestForm dataTracerRequestForm) {
        if(CollectionUtils.isEmpty(vehicleIdList)){
            return;
        }
        VehicleDataTracerOperateTypeEnum dataTracerOperateTypeEnum = VehicleDataTracerOperateTypeEnum.ENABLE;
        if(disabledFlag){
            dataTracerOperateTypeEnum = VehicleDataTracerOperateTypeEnum.DISABLE;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();

        for (Long vehicleId : vehicleIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(vehicleId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
            dataTracerForm.setOperateType(dataTracerOperateTypeEnum);
            dataTracerForm.setOperateContent(dataTracerOperateTypeEnum.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 批量审核日志
     * @param vehicleIdList
     * @param auditStatus
     * @param auditRemark
     * @param dataTracerRequestForm
     */
    public void batchAuditLog(List<Long> vehicleIdList, Integer auditStatus, String auditRemark, DataTracerRequestForm dataTracerRequestForm) {
        if(CollectionUtils.isEmpty(vehicleIdList)){
            return;
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus,AuditStatusEnum.class);
        String content = "修改审核状态为：" + auditStatusEnum.getDesc() + "；备注为：" + (StringUtils.isEmpty(auditRemark) ? "" : auditRemark);
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long vehicleId : vehicleIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(vehicleId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
            dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.AUDIT);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 批量修改负责人日志
     *
     * @param vehicleIdList
     * @param employeeEntity
     * @param dataTracerRequestForm
     */
    public void batchUpdateManagerLog(List<Long> vehicleIdList, EmployeeEntity employeeEntity, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            return;
        }
        String content = "修改负责人为:" + employeeEntity.getActualName();
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long bracket : vehicleIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(bracket);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
            dataTracerForm.setOperateType(VehicleDataTracerOperateTypeEnum.UPDATE_MANAGER);
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 记录导入新建日志
     *
     * @param vehicleEntityList
     * @param dataTracerRequestForm
     */
    @Async
    public void saveByImportLog(List<VehicleEntity> vehicleEntityList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(vehicleEntityList)) {
            return;
        }
        String content = "根据短驳订单导入新建车辆";
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (VehicleEntity vehicleEntity : vehicleEntityList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(vehicleEntity.getVehicleId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.VEHICLE);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setExtraData(new DataTracerExtraData(VehicleEntity.class, null, vehicleEntity));
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }
}