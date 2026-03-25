package net.lab1024.tms.common.module.business.carcost.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 自有车-现金收入 ADD Form
 *
 * @author zhaoxinyang
 * @date 2023/10/24 08:58
 */
@Data
public class CarCostCashReceiveAddForm {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("收入金额")
    @NotNull(message = "收入金额不能为空")
    private BigDecimal amount;

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