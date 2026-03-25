package net.lab1024.tms.admin.module.business.material.yard.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 业务资料-堆场管理-新建
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class YardCreateForm {

    @ApiModelProperty("堆场名称")
    @NotBlank(message = "堆场名称不能为空")
    @Length(max = 200, message = "堆场名称最多200字符")
    private String yardName;

    @ApiModelProperty("堆场编号")
    @NotBlank(message = "堆场编号不能为空")
    @Length(max = 200, message = "堆场编号最多200字符")
    private String yardCode;

    @ApiModelProperty("省份")
    @NotNull(message = "省份不能为空")
    private Integer province;

    @ApiModelProperty("省份名称")
    @NotBlank(message = "省份名称不能为空")
    private String provinceName;

    @ApiModelProperty("城市")
    @NotNull(message = "城市不能为空")
    private Integer city;

    @ApiModelProperty("城市名称")
    @NotBlank(message = "城市名称不能为空")
    private String cityName;

    @ApiModelProperty("区县")
    @NotNull(message = "区县不能为空")
    private Integer district;

    @ApiModelProperty("区县名称")
    @NotBlank(message = "区县名称不能为空")
    private String districtName;

    @ApiModelProperty("详细地址")
    @NotBlank(message = "详细地址不能为空")
    @Length(max = 500, message = "详细地址最多500字符")
    private String address;

    @ApiModelProperty("联系人")
    @NotBlank(message = "联系人不能为空")
    @Length(max = 100, message = "联系人最多100字符")
    private String contact;

    @ApiModelProperty("联系人电话")
    @NotBlank(message = "联系人电话不能为空")
    private String contactPhone;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}
