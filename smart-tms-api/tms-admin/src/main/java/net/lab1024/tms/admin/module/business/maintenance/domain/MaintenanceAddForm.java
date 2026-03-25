package net.lab1024.tms.admin.module.business.maintenance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 保养信息新建
 *
 * @author zhaoxinyang
 * @date 2023/10/18 14:08
 */
@Data
public class MaintenanceAddForm {

    @NotNull(message = "车辆ID不能为空")
    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("保养人")
    @Length(max = 50, message = "保养人最多50个字符")
    private String maintenancePerson;

    @ApiModelProperty("保养地点")
    @Length(max = 100, message = "保养地点最多100个字符")
    private String maintenancePlant;

    @ApiModelProperty("备注")
    @Length(max = 100, message = "备注最多100个字符")
    private String remark;

    @NotNull(message = "保养里程不能为空")
    @ApiModelProperty("保养里程")
    @DecimalMin(value = "0", message = "保养里程不能小于0")
    private BigDecimal mileage;

    @ApiModelProperty(value = "保养时间", example = "2023-01-01")
    private LocalDate maintenanceDate;

    @ApiModelProperty(value = "下次保养时间", example = "2023-01-01")
    private LocalDate nextMaintenanceDate;

    @ApiModelProperty("下次保养里程")
    private BigDecimal nextMaintenanceMileage;

    @Valid
    @Size(min = 1, message = "保养内容和保养金额数据不能为空")
    @NotNull(message = "保养内容和保养金额不能为空")
    @ApiModelProperty("保养内容")
    private List<MaintenanceContentForm> contentFormList;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}