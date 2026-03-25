package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperGradeEnum;

import javax.validation.constraints.NotNull;

/**
 * [ 货主等级变更 ]
 *
 * @author yandanyang
 * @date 2020/10/24 10:20
 */
@Data
public class ShipperGradeChangeForm {

    @ApiModelProperty("货主id")
    @NotNull(message = "货主id不能为空")
    private Long shipperId;

    @ApiModelPropertyEnum(value = ShipperGradeEnum.class, desc = "等级")
    @CheckEnum(value = ShipperGradeEnum.class, message = "等级类型错误", required = true)
    private Integer grade;
}
