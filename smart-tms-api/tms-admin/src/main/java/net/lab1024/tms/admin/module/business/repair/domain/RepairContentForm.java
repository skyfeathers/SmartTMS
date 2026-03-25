package net.lab1024.tms.admin.module.business.repair.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/***
 * 新增维修内容
 *
 * @author lidoudou
 * @date 2022/6/27 上午10:20
 */
@Data
public class RepairContentForm {

    @ApiModelProperty("维修内容")
    @NotBlank(message = "维修内容不能为空")
    @Length(max = 100, message = "维修内容最多100个字符")
    private String repairContent;

    @ApiModelProperty("维修费用")
    @NotNull(message = "维修费用不能为空")
    @DecimalMin(value = "0", message = "维修费用不能小于0")
    @DecimalMax(value = "9999999.99", message = "维修费用不能大于9999999.99")
    private BigDecimal repairAmount;

}