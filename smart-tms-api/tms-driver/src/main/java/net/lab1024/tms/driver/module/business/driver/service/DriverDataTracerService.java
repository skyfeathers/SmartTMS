package net.lab1024.tms.driver.module.business.driver.service;

import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.driver.constants.DriverDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.driver.constants.VehicleClassEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DriverDataTracerService {

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 司机银行更新日志
     *
     * @param driverBankEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void saveDriverBankLog(DriverBankEntity driverBankEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(driverBankEntity.getDriverId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
        dataTracerForm.setOperateType(DriverDataTracerOperateTypeEnum.ADD_BANK);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanObjectParse(DriverDataTracerOperateTypeEnum.ADD_BANK.getDesc(), driverBankEntity));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 司机银行更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateDriverBankLog(DriverBankEntity beforeData, DriverBankEntity afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getBankId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
        dataTracerForm.setOperateType(DriverDataTracerOperateTypeEnum.UPDATE_BANK);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDriverBankContext(beforeData), this.getDriverBankContext(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(DriverBankEntity.class, beforeData, afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 获取司机银行内容
     *
     * @param driverBankEntity
     * @return
     */
    private String getDriverBankContext(DriverBankEntity driverBankEntity) {
        StringBuilder context = new StringBuilder();
        context.append("司机银行信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【银行类型】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getBankType()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【银行名称】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getBankName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【支行名称】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getBankBranchName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【开户名】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getAccountName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【银行账号】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getBankAccount()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【附件】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getBankCardFrontAttachment()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【默认状态】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getDefaultFlag()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【是否为纳税人信息】").append(DataTracerConstant.SPLIT).append(driverBankEntity.getTaxpayerFlag()).append(DataTracerConstant.LINE);
        return context.toString().replaceAll("null", "");
    }

    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(DriverVO beforeData, DriverVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getDriverId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDriverContent(beforeData), this.getDriverContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(DriverVO.class, beforeData, afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 获取车辆信息
     *
     * @param detailVO
     * @return
     */
    private String getDriverContent(DriverVO detailVO) {
        //司机身份信息
        String idCardContent = this.getIdCardContent(detailVO);
        //驾驶证信息
        String drivingLicenseContent = this.getDrivingLicenseContent(detailVO);
        //从业资格证信息
        String qualificationCertificateContent = this.getQualificationCertificateContent(detailVO);

        StringBuilder builder = new StringBuilder();
        builder.append(idCardContent)
                .append(drivingLicenseContent)
                .append(qualificationCertificateContent);
        return builder.toString();
    }

    private String getIdCardContent(DriverVO driverVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("身份信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【司机电话】").append(DataTracerConstant.SPLIT).append(driverVO.getTelephone()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（人像面）】").append(DataTracerConstant.SPLIT).append(driverVO.getIdCardBackPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（国徽面）】").append(DataTracerConstant.SPLIT).append(driverVO.getIdCardFrontPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【司机姓名】").append(DataTracerConstant.SPLIT).append(driverVO.getDriverName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证号】").append(DataTracerConstant.SPLIT).append(driverVO.getDrivingLicense()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证地址】").append(DataTracerConstant.SPLIT).append(driverVO.getHomeAddress()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证有效期开始时间】").append(DataTracerConstant.SPLIT).append(driverVO.getIdCardEffectiveStartDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证有效期结束时间】").append(DataTracerConstant.SPLIT).append(driverVO.getIdCardEffectiveEndDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【是否长期有效】").append(DataTracerConstant.SPLIT).append(null != driverVO.getIdCardEndlessFlag() && driverVO.getIdCardEndlessFlag() ? "是" : "否").append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getDrivingLicenseContent(DriverVO driverVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("驾驶证:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证正本图】").append(DataTracerConstant.SPLIT).append(driverVO.getDrivingLicenseFrontPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证副本图】").append(DataTracerConstant.SPLIT).append(driverVO.getDrivingLicenseBackPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证号】").append(DataTracerConstant.SPLIT).append(driverVO.getDrivingLicenseNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【准驾车型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(driverVO.getVehicleClass(), VehicleClassEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证机关】").append(DataTracerConstant.SPLIT).append(driverVO.getIssuingOrganizations()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【首次驾照签发日期】").append(DataTracerConstant.SPLIT).append(driverVO.getLicenseFirstGetDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证有效开始日期】").append(DataTracerConstant.SPLIT).append(driverVO.getValidPeriodFrom()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证有效结束日期】").append(DataTracerConstant.SPLIT).append(driverVO.getValidPeriodTo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【是否长期有效】").append(DataTracerConstant.SPLIT).append(null != driverVO.getDrivingLicenseEndlessFlag() && driverVO.getDrivingLicenseEndlessFlag() ? "是" : "否").append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getQualificationCertificateContent(DriverVO driverVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("从业资格证:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证图】").append(DataTracerConstant.SPLIT).append(driverVO.getQualificationCertificatePic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证号】").append(DataTracerConstant.SPLIT).append(driverVO.getQualificationCertificate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证有效期开始日期】").append(DataTracerConstant.SPLIT).append(driverVO.getQualificationCertificateStartDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证有效期结束日期】").append(DataTracerConstant.SPLIT).append(driverVO.getQualificationCertificateEndDate()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

}
