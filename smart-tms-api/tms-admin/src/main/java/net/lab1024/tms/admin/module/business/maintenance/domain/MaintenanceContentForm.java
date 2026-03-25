package net.lab1024.tms.admin.module.business.maintenance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 新增保养内容
 *
 * @author zhaoxinyang
 * @date 2023/11/14 10:42
 */
@Data
public class MaintenanceContentForm {

    @ApiModelProperty("保养内容")
    @NotBlank(message = "保养内容不能为空")
    @Length(max = 100, message = "保养内容最多100个字符")
    private String maintenanceContent;

    @ApiModelProperty("保养费用")
    @NotNull(message = "保养费用不能为空")
    @DecimalMin(value = "0", message = "保养费用不能小于0")
    @DecimalMax(value = "9999999.99", message = "保养费用不能大于9999999.99")
    private BigDecimal maintenanceAmount;
    
}
