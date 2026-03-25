package net.lab1024.tms.admin.module.business.quick.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.quick.constant.QuickCreateTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;

import javax.validation.constraints.NotNull;

/**
 * 快速创建司机、车辆、挂车
 *
 * @author lidoudou
 * @date 2022/10/17 下午3:10
 */
@Data
public class VehicleQuickCreateForm {

    @ApiModelPropertyEnum(desc = "创建类型", value = QuickCreateTypeEnum.class)
    @NotNull(message = "创建类型不能为空")
    private Integer createType;

    @ApiModelProperty("负责人")
    private Long managerId;

    @ApiModelPropertyEnum(desc = "经营方式", value = BusinessModeEnum.class)
    @NotNull(message = "经营方式不能为空")
    private Integer businessMode;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("司机电话")
    private String telephone;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("挂车车牌号")
    private String bracketNo;

    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty(hidden = true)
    private String createUserName;

    @ApiModelProperty(hidden = true)
    private Integer createUserType;

    @ApiModelProperty(hidden = true)
    private String createUserTypeDesc;

}
