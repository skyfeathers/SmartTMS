package net.lab1024.tms.admin.module.business.carcost.basicinfo.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 自有车-基本信息 Form
 *
 * @author zhaoxinyang
 * @date 2023/10/23 16:19
 */
@Data
public class CarCostBasicInfoForm {

    @ApiModelProperty("运单ID")
    @NotNull(message = "运单ID不能为空")
    private Long waybillId;

    @ApiModelProperty("高速里程")
    @NotNull(message = "高速里程不能为空")
    @DecimalMin(value = "0", message = "高速里程不能小于0")
    private BigDecimal highSpeedMileage;

    @ApiModelProperty("低速里程")
    @NotNull(message = "低速里程不能为空")
    @DecimalMin(value = "0", message = "低速里程不能小于0")
    private BigDecimal lowSpeedMileage;

    @ApiModelProperty("GPS里程")
    @NotNull(message = "GPS里程不能为空")
    @DecimalMin(value = "0", message = "GPS里程不能小于0")
    private BigDecimal gpsMileage;
    /**
     * 无用 -目前通过加油统计总升数
     */
    @ApiModelProperty("用油量-升数")
//    @NotNull(message = "用油量不能为空")
//    @DecimalMin(value = "0", message = "用油量不能小于0")
    private BigDecimal oilConsumption;

    @ApiModelProperty("工资板块-基本工资")
    @NotNull(message = "基本工资不能为空")
    @DecimalMin(value = "0", message = "基本工资不能小于0")
    private BigDecimal basicSalary;

    @ApiModelProperty("工资板块-补助")
    @NotNull(message = "补助不能为空")
    @DecimalMin(value = "0", message = "补助不能小于0")
    private BigDecimal allowance;

    @ApiModelProperty("工资板块-提成工资")
    @NotNull(message = "提成工资不能为空")
    @DecimalMin(value = "0", message = "提成工资不能小于0")
    private BigDecimal commissionSalary;

    @ApiModelProperty("工资板块-出勤天数")
    @NotNull(message = "出勤天数不能为空")
    @Range(min = 0, max = 31, message = "出勤天数不能小于0且不能大于31")
    private Integer attendanceDays;

    @ApiModelProperty(value = "企业id", hidden = true)
    private Long enterpriseId;
}