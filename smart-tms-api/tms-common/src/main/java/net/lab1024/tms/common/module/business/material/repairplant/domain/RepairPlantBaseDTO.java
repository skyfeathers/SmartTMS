package net.lab1024.tms.common.module.business.material.repairplant.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 维修厂 基础属性
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:17
 */
@Data
public class RepairPlantBaseDTO {

    @ApiModelProperty("维修厂家名称")
    @NotBlank(message = "维修厂家名称不能为空")
    @Length(max = 50, message = "维修厂家名称不能为空最多50字")
    private String repairPlantName;

    @ApiModelProperty("联系人")
    @Length(max = 20, message = "联系人最多20字")
    private String contactName;

    @ApiModelProperty("联系方式")
    @Length(max = 20, message = "联系方式最多20字")
    private String contactPhone;

    @ApiModelProperty("省份code")
    private Integer provinceCode;

    @ApiModelProperty("城市code")
    private Integer cityCode;

    @ApiModelProperty("地区code")
    private Integer areaCode;

    @ApiModelProperty("地区名称")
    @Length(max = 80, message = "维修厂家名称不能为空最多50字")
    private String addressArea;

    @ApiModelProperty("详细地址")
    @Length(max = 200, message = "维修厂家名称不能为空最多50字")
    private String addressDetail;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty("备注")
    @Length(max = 200, message = "备注最多200字")
    private String remark;
}
