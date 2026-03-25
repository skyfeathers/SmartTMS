package net.lab1024.tms.admin.module.business.material.customsbroker.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * 业务资料-报关行
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class CustomsBrokerVO {

    @ApiModelProperty("报关行ID")
    private Long customsBrokerId;

    @ApiModelProperty("报关行简称")
    private String customsBrokerShortName;

    @ApiModelProperty("报关行编号")
    private String customsBrokerCode;

    @ApiModelProperty("报关行名称")
    private String customsBrokerName;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("城市")
    private Integer city;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区县")
    private Integer district;

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("联系人电话")
    private String contactPhone;

    @ApiModelProperty("备注")
    @Length(max = 500, message = "备注最多500字符")
    private String remark;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
