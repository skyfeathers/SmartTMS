package net.lab1024.tms.admin.module.business.material.businesstype.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.material.businesstype.constant.TripTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 业务资料-业务类型-新建
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class BusinessTypeCreateForm {

    @ApiModelProperty("业务类型名称")
    @NotBlank(message = "业务类型名称不能为空")
    @Length(max = 200, message = "业务类型名称最多200字符")
    private String businessTypeName;

    @ApiModelProperty("业务代码")
    @NotBlank(message = "业务代码不能为空")
    @Length(max = 200, message = "业务代码最多200字符")
    private String businessTypeCode;

    @ApiModelPropertyEnum(value = TripTypeEnum.class, desc = "运输类型")
    @NotNull(message = "业务类型不能为空")
    @CheckEnum(value = TripTypeEnum.class, message = "业务类型不正确")
    private Integer tripType;

    @ApiModelProperty("默认状态")
    @NotNull(message = "默认状态不能为空")
    private Boolean defaultFlag;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}
