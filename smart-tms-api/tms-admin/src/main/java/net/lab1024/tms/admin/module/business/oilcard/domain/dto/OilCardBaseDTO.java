package net.lab1024.tms.admin.module.business.oilcard.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lidoudou
 * @date 2022/6/30 上午10:52
 */
@Data
public class OilCardBaseDTO {

    @ApiModelProperty("油卡卡号")
    @NotBlank(message = "油卡卡号不能为空")
    private String oilCardNo;

    @ApiModelPropertyEnum(value = OilCardTypeEnum.class, desc = "油卡类型")
    @CheckEnum(value = OilCardTypeEnum.class, required = true, message = "油卡类型错误")
    @NotNull(message = "油卡类型不能为空")
    private Integer type;

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    @CheckEnum(value = OilCardFuelTypeEnum.class, required = true, message = "燃料类型错误")
    @NotNull(message = "燃料类型不能为空")
    private Integer fuelType;

    @ApiModelProperty("主卡ID")
    private Long masterOilCardId;

    @ApiModelProperty("品牌-数据字典(OIL-CARD-BRAND)")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String brand;

    @ApiModelProperty("用途-数据字典(OIL-CARD-PURPOSE)")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String purpose;

    @ApiModelProperty("领取人")
    private Long receiveUserId;

    @ApiModelProperty("持卡司机")
    private Long useDriverId;

    @ApiModelProperty("持卡车")
    private Long useVehicleId;

    @ApiModelProperty("期初余额")
    private BigDecimal beginBalance;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否定点加油")
    @NotNull(message = "是否定点加油不能为空")
    private Boolean fixedPointFlag;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty("计划充值金额")
    private BigDecimal preRechargeAmount;


    @ApiModelProperty(value = "所属公司")
    private Long enterpriseId;
}
