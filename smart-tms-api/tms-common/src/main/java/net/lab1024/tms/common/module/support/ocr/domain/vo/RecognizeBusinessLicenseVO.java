package net.lab1024.tms.common.module.support.ocr.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:21 下午
 */
@Data
public class RecognizeBusinessLicenseVO {

    @ApiModelProperty("营业证照编号")
    private String creditCode;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("公司类型")
    private String companyType;

    @ApiModelProperty("注册地址")
    private String businessAddress;

    @ApiModelProperty("法人")
    private String legalPerson;

    @ApiModelProperty("主营范围")
    private String businessScope;


    @ApiModelProperty("注册资本")
    private String registeredCapital;

    @ApiModelProperty("注册日期")
    private LocalDate RegistrationDate;

    /**
     * 有效期  2017年01月04日至长期
     */
    @ApiModelProperty("有效期")
    private String validPeriod;


    @ApiModelProperty("有效期 开始日期")
    private LocalDate validFromDate;

    @ApiModelProperty("有效期 截止日期")
    private LocalDate validToDate;

    private String companyForm;

}