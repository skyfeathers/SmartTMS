package net.lab1024.tms.admin.module.business.review.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 审车信息新建
 *
 * @author zhaoxinyang
 * @date 2023/10/18 09:51
 */
@Data
public class ReviewAddForm {

    @NotNull(message = "车辆ID不能为空")
    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("审车人")
    @Length(max = 50, message = "审车人最多50个字符")
    private String reviewPerson;

    @ApiModelProperty("审车费用")
    @NotNull(message = "审车费用不能为空")
    @DecimalMin(value = "0", message = "审车费用不能小于0")
    @DecimalMax(value = "9999999.99", message = "审车费用不能大于9999999.99")
    private BigDecimal reviewAmount;

    @ApiModelProperty("审车地点")
    @Length(max = 100, message = "审车地点最多100个字符")
    private String reviewPlant;

    @ApiModelProperty("备注")
    @Length(max = 100, message = "备注最多100个字符")
    private String remark;

    @ApiModelProperty(value = "审车时间", example = "2023-01-01")
    private LocalDate reviewDate;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
