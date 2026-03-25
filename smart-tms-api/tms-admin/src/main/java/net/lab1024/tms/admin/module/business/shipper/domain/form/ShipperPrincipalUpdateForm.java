package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/***
 * 更新客服负责人
 * @author lidoudou
 * @date 2022/6/24 上午11:41
 */
@Data
public class ShipperPrincipalUpdateForm {

    @ApiModelProperty("货主ID")
    @NotNull(message = "货主不能为空")
    private List<Long> shipperIdList;

    @ApiModelProperty("负责人ID")
    @NotNull(message = "负责人不能为空")
    private List<Long> employeeIdList;

    @ApiModelPropertyEnum(value = PrincipalTypeEnum.class, desc = "类型")
    @NotNull(message = "类型不能为空")
    private Integer principalType;
}
