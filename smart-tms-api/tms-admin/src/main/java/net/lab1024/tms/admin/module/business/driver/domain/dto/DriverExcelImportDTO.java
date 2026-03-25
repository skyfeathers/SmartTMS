package net.lab1024.tms.admin.module.business.driver.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 司机导入对象
 *
 * @author lidoudou
 * @date 2022/10/31 上午9:11
 */
@Data
public class DriverExcelImportDTO {

    @Excel(name = "速记码")
    private String shorthandCode;

    /**
     * 经营方式
     */
    @Excel(name = "经营方式")
    private String businessMode;

    @Excel(name = "司机姓名")
    private String driverName;

    @Excel(name = "司机电话")
    private String telephone;

    /**
     * 所属公司
     */
    @Excel(name = "所属公司")
    private String enterpriseName;

    /**
     * 负责人
     */
    @Excel(name = "负责人")
    private String managerName;

    @Excel(name = "身份证号码")
    private String drivingLicense;

    @Excel(name = "身份证地址")
    private String homeAddress;

    @Excel(name = "身份证有效期开始日期")
    private String idCardEffectiveStartDate;

    @Excel(name = "身份证有效期结束日期")
    private String idCardEffectiveEndDate;

    @Excel(name = "驾驶证号")
    private String drivingLicenseNo;

    /**
     * 准驾车型
     */
    @Excel(name = "准驾车型")
    private String vehicleClass;

    @Excel(name = "发证机关")
    private String issuingOrganizations;

    /**
     * 首次驾照签发日期
     */
    @Excel(name = "首次驾照签发日期")
    private String licenseFirstGetDate;

    /**
     * 驾驶证有效期自
     */
    @Excel(name = "驾驶证有效开始日期")
    private String validPeriodFrom;
    /**
     * 驾驶证有效期至
     */
    @Excel(name = "驾驶证有效结束日期")
    private String validPeriodTo;

    @Excel(name = "从业资格证号")
    private String qualificationCertificate;
    /**
     * 从业资格证有效期开始日期
     */
    @Excel(name = "从业资格证有效期开始日期")
    private String qualificationCertificateStartDate;

    /**
     * 从业资格证有效期结束日期
     */
    @Excel(name = "从业资格证有效期结束日期")
    private String qualificationCertificateEndDate;

    /**
     * 银行信息
     */

    @Excel(name = "开户名")
    private String accountName;

    @Excel(name = "银行账号")
    private String bankAccount;

    @Excel(name = "银行名称")
    private String bankName;

    @Excel(name = "支行名称")
    private String bankBranchName;

    /**
     * 是否默认
     */
    @Excel(name = "是否默认")
    private String defaultFlag;
}