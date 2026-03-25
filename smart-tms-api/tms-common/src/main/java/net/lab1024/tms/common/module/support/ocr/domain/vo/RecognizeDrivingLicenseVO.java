package net.lab1024.tms.common.module.support.ocr.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeDrivingLicenseVO {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("驾驶证号")
    private String licenseNumber;

    @ApiModelProperty("国籍")
    private String nationality;

    @ApiModelProperty("住址")
    private String address;

    @ApiModelProperty("出生日期")
    private LocalDate birthDate;

    @ApiModelProperty("初次领证日期")
    private LocalDate initialIssueDate;

    @ApiModelProperty("发证机关")
    private String issueAuthority;

    @ApiModelProperty("准驾车型")
    private Integer approvedType;

    @ApiModelProperty("有效期 开始日期")
    private LocalDate validFromDate;

    @ApiModelProperty("有效期 截止日期")
    private LocalDate validToDate;

    @ApiModelProperty("有效期")
    private String validPeriod;
}