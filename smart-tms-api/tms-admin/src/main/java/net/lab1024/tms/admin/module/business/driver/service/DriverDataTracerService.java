package net.lab1024.tms.admin.module.business.driver.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.driver.domain.vo.DriverBankVO;
import net.lab1024.tms.admin.module.business.driver.domain.vo.DriverDetailVO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.business.driver.constants.VehicleClassEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
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
public class DriverDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新增日志
     *
     * @param driverId
     */
    @Async
    public void saveLog(Long driverId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(driverId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(DriverDetailVO beforeData, DriverDetailVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getDriverId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDriverContent(beforeData), this.getDriverContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(DriverDetailVO.class, beforeData, afterData));
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
    private String getDriverContent(DriverDetailVO detailVO) {
        //基本信息
        String baseContent = this.getBaseContent(detailVO);
        //司机身份证信息
        String idCardContent = this.getIdCardContent(detailVO);
        //驾驶证信息
        String drivingLicenseContent = this.getDrivingLicenseContent(detailVO);
        //从业资格证信息
        String qualificationCertificateContent = this.getQualificationCertificateContent(detailVO);
        //银行卡
        String bankContent = this.getBankContent(detailVO.getBankList());

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(idCardContent)
                .append(drivingLicenseContent)
                .append(qualificationCertificateContent)
                .append(bankContent);
        return builder.toString();
    }

    private String getBaseContent(DriverDetailVO detailVO) {
        String vehicle = "";
        if (CollectionUtils.isNotEmpty(detailVO.getDriverVehicleList())) {
            vehicle = detailVO.getDriverVehicleList().stream().map(e -> e.getVehicleNumber()).collect(Collectors.joining(","));
        }
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【司机电话】").append(DataTracerConstant.SPLIT).append(detailVO.getTelephone()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【业务类型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getBusinessMode(), BusinessModeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【关联企业】").append(DataTracerConstant.SPLIT).append(detailVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【速记码】").append(DataTracerConstant.SPLIT).append(detailVO.getShorthandCode()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【关联车辆】").append(DataTracerConstant.SPLIT).append(vehicle).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【负责人】").append(DataTracerConstant.SPLIT).append(detailVO.getManagerName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(detailVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getIdCardContent(DriverDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("行驶证信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（人像面）】").append(DataTracerConstant.SPLIT).append(detailVO.getIdCardBackPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证（国徽面）】").append(DataTracerConstant.SPLIT).append(detailVO.getIdCardFrontPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【司机姓名】").append(DataTracerConstant.SPLIT).append(detailVO.getDriverName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证号】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicense()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证地址】").append(DataTracerConstant.SPLIT).append(detailVO.getHomeAddress()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证有效期开始时间】").append(DataTracerConstant.SPLIT).append(detailVO.getIdCardEffectiveStartDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【身份证有效期结束时间】").append(DataTracerConstant.SPLIT).append(detailVO.getIdCardEffectiveEndDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【是否长期有效】").append(DataTracerConstant.SPLIT).append(null != detailVO.getIdCardEndlessFlag() && detailVO.getIdCardEndlessFlag() ? "是" : "否").append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getDrivingLicenseContent(DriverDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("驾驶证:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证正本图】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseFrontPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证副本图】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseBackPic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证号】").append(DataTracerConstant.SPLIT).append(detailVO.getDrivingLicenseNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【准驾车型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(detailVO.getVehicleClass(), VehicleClassEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【发证机关】").append(DataTracerConstant.SPLIT).append(detailVO.getIssuingOrganizations()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【首次驾照签发日期】").append(DataTracerConstant.SPLIT).append(detailVO.getLicenseFirstGetDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证有效开始日期】").append(DataTracerConstant.SPLIT).append(detailVO.getValidPeriodFrom()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【驾驶证有效结束日期】").append(DataTracerConstant.SPLIT).append(detailVO.getValidPeriodTo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【是否长期有效】").append(DataTracerConstant.SPLIT).append(null != detailVO.getDrivingLicenseEndlessFlag() && detailVO.getDrivingLicenseEndlessFlag() ? "是" : "否").append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    private String getQualificationCertificateContent(DriverDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("从业资格证:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证图】").append(DataTracerConstant.SPLIT).append(detailVO.getQualificationCertificatePic()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证号】").append(DataTracerConstant.SPLIT).append(detailVO.getQualificationCertificate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证有效期开始日期】").append(DataTracerConstant.SPLIT).append(detailVO.getQualificationCertificateStartDate()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【从业资格证有效期结束日期】").append(DataTracerConstant.SPLIT).append(detailVO.getQualificationCertificateEndDate()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 货主付款方式信息
     *
     * @param bankList
     * @return
     */
    private String getBankContent(List<DriverBankVO> bankList) {
        StringBuilder builder = new StringBuilder();
        builder.append("银行卡信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(bankList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (DriverBankVO driverBankVO : bankList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【开户名】").append(DataTracerConstant.SPLIT).append(driverBankVO.getAccountName()).append(DataTracerConstant.BLANK)
                    .append("【银行账号】").append(DataTracerConstant.SPLIT).append(driverBankVO.getBankAccount()).append(DataTracerConstant.BLANK)
                    .append("【银行名称】").append(DataTracerConstant.SPLIT).append(driverBankVO.getBankName()).append(DataTracerConstant.BLANK)
                    .append("【支行名称】").append(DataTracerConstant.SPLIT).append(driverBankVO.getBankBranchName()).append(DataTracerConstant.BLANK)
                    .append("【附件信息】").append(DataTracerConstant.SPLIT).append(driverBankVO.getBankCardFrontAttachment()).append(DataTracerConstant.BLANK)
                    .append("【是否默认】").append(DataTracerConstant.SPLIT).append(driverBankVO.getDefaultFlag() ? "是" : "否").append(DataTracerConstant.BLANK)
                    .append("【创建人】").append(DataTracerConstant.SPLIT).append(driverBankVO.getCreateUserName()).append(DataTracerConstant.BLANK)
                    .append("【创建人类型】").append(DataTracerConstant.SPLIT).append(driverBankVO.getCreateUserTypeDesc()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 批量删除日志
     *
     * @param driverIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchDeleteLog(List<Long> driverIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(driverIdList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long driverId : driverIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(driverId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
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
     *
     * @param driverIdList
     * @param status
     * @param dataTracerRequestForm
     */
    public void batchUpdateStatusLog(List<Long> driverIdList, Integer status, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(driverIdList)) {
            return;
        }
        DriverStatusEnum driverStatusEnum = SmartBaseEnumUtil.getEnumByValue(status, DriverStatusEnum.class);

        DriverDataTracerOperateTypeEnum dataTracerOperateTypeEnum = DriverDataTracerOperateTypeEnum.ACTIVE;
        if (DriverStatusEnum.DISABLED == driverStatusEnum) {
            dataTracerOperateTypeEnum = DriverDataTracerOperateTypeEnum.DISABLE;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();

        for (Long driverId : driverIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(driverId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
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
     *
     * @param driverIdList
     * @param auditStatus
     * @param auditRemark
     * @param dataTracerRequestForm
     */
    public void batchAuditLog(List<Long> driverIdList, Integer auditStatus, String auditRemark, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(driverIdList)) {
            return;
        }
        AuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(auditStatus, AuditStatusEnum.class);
        String content = "修改审核状态为：" + auditStatusEnum.getDesc() + "；备注为：" + (StringUtils.isEmpty(auditRemark) ? "" : auditRemark);
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long driverId : driverIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(driverId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
            dataTracerForm.setOperateType(DriverDataTracerOperateTypeEnum.AUDIT);
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
     * 添加银行日志
     * @param driverBankEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void bankAddLog(DriverBankEntity driverBankEntity, DataTracerRequestForm dataTracerRequestForm) {
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
     * 批量修改负责人日志
     *
     * @param driverIdList
     * @param employeeEntity
     * @param dataTracerRequestForm
     */
    public void batchUpdateManagerLog(List<Long> driverIdList, EmployeeEntity employeeEntity, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(driverIdList)) {
            return;
        }
        String content = "修改负责人为:" + employeeEntity.getActualName();
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long bracket : driverIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(bracket);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
            dataTracerForm.setOperateType(DriverDataTracerOperateTypeEnum.UPDATE_MANAGER);
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
     * @param driverEntityList
     * @param dataTracerRequestForm
     */
    @Async
    public void saveByImportLog(List<DriverEntity> driverEntityList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(driverEntityList)) {
            return;
        }
        String content = "根据短驳订单导入新建司机";
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (DriverEntity driverEntity : driverEntityList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(driverEntity.getDriverId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.DRIVER);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerForm.setOperateContent(content);
            dataTracerForm.setExtraData(new DataTracerExtraData(DriverEntity.class, null, driverEntity));
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }
}