package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:21 下午
 */
@Data
public class RecognizeBusinessLicenseBO {

    /**
     * 913301095U78M2HC4A
     */
    private String creditCode;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司类型 有限责任公司(自然人投资或控股)
     */
    private String companyType;

    /**
     * 注册地址 萧山区宁围街道泰宏巷40号联合中心北区
     */
    private String businessAddress;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 主营范围
     */
    private String businessScope;

    /**
     * 注册资本 壹佰万元整
     */
    private String registeredCapital;

    /**
     * 注册日期 2017年01月04日
     */
    private String RegistrationDate;

    /**
     * 有效期  2017年01月04日至长期
     */
    private String validPeriod;

    /**
     * 有效期 开始日期 20170104
     */
    private String validFromDate;

    /**
     * 有效期 截止日期 29991231
     */
    private String validToDate;

    private String companyForm;

}