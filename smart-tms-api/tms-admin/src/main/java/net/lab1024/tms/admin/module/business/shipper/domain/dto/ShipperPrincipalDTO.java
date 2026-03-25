package net.lab1024.tms.admin.module.business.shipper.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;

/***
 * 货主负责人DTO
 *
 * @author lidoudou
 * @date 2022/6/24 上午10:49
 */
@Data
public class ShipperPrincipalDTO {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelPropertyEnum(value = PrincipalTypeEnum.class, desc = "类型：")
    private Integer type;
}
