package net.lab1024.tms.admin.module.business.material.customsbroker.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.util.SmartVerificationUtil;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 业务资料-报关行-新建
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class CustomsBrokerCreateForm {

    @ApiModelProperty("报关行简称")
    @NotBlank(message = "报关行简称不能为空")
    @Length(max = 200, message = "报关行简称最多200字符")
    private String customsBrokerShortName;

    @ApiModelProperty("报关行编号")
    @NotBlank(message = "报关行编号不能为空")
    @Length(max = 200, message = "报关行编号最多200字符")
    private String customsBrokerCode;

    @ApiModelProperty("报关行名称")
    @Length(max = 200, message = "报关行名称最多200字符")
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
    @Length(max = 500, message = "详细地址最多500字符")
    private String address;

    @ApiModelProperty("联系人")
    @Length(max = 100, message = "联系人最多100字符")
    private String contact;

    @ApiModelProperty("联系人电话")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号格式不正确")
    private String contactPhone;

    @ApiModelProperty("备注")
    @Length(max = 500, message = "备注最多500字符")
    private String remark;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}
