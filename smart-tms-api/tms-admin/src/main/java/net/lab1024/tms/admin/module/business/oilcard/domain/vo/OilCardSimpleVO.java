package net.lab1024.tms.admin.module.business.oilcard.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;

import java.math.BigDecimal;

/**
 * 油卡vo
 *
 * @author lidoudou
 * @date 2022/6/29 下午5:51
 */
@Data
public class OilCardSimpleVO {

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("油卡卡号")
    private String oilCardNo;

    @ApiModelPropertyEnum(value = OilCardTypeEnum.class, desc = "油卡类型")
    private Integer type;

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    private Integer fuelType;

    @ApiModelProperty("主卡ID")
    private Long masterOilCardId;

    @ApiModelProperty("主卡卡号")
    private String masterOilCardNo;

    @ApiModelProperty("品牌-数据字典(OIL-CARD-BRAND)")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String brand;

    @ApiModelProperty("用途-数据字典(OIL-CARD-PURPOSE)")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String purpose;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("当前余额")
    private BigDecimal balance;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;
}
