package net.lab1024.tms.admin.module.business.material.cabinet.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 业务资料-柜型管理-新建
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class CabinetCreateForm {

    @ApiModelProperty("柜型名称")
    @NotBlank(message = "柜型名称不能为空")
    @Length(max = 200, message = "柜型名称最多200字符")
    private String cabinetName;

    @ApiModelProperty("柜型皮重")
    @NotNull(message = "柜型皮重不能为空")
    @DecimalMin(value = "1",message="柜型皮重不能小于1")
    @DecimalMax(value = "99999.99",message = "柜型皮重最大99999.99")
    private BigDecimal tare;

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
