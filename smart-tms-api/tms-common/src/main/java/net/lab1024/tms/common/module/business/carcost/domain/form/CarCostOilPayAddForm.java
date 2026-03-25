package net.lab1024.tms.common.module.business.carcost.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 油卡支出 Add Form
 *
 * @author zhaoxinyang
 * @date 2023/10/25 14:08
 */
@Data
public class CarCostOilPayAddForm {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    @CheckEnum(value = OilCardFuelTypeEnum.class, message = "燃料类型错误")
    private Integer fuelType;

    @ApiModelProperty("分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @ApiModelProperty("支出金额")
    @NotNull(message = "支出金额不能为空")
    @DecimalMin(value = "0.01", message = "支出金额不能小于0.01")
    private BigDecimal amount;

    @ApiModelProperty("升数")
    @DecimalMin(value = "0", message = "升数不能小于0")
    private BigDecimal oilConsumption;

    @ApiModelProperty("附件")
    @Length(max = 500, message = "附件最多500个字符")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    @Length(max = 100, message = "备注最多100个字符")
    private String remark;

    @ApiModelProperty(value = "企业id", hidden = true)
    private Long enterpriseId;
}
