package net.lab1024.tms.driver.module.business.oilcard.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;

/**
 * 油卡简单信息 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/30 13:50
 */
@Data
public class OilCardSimpleVO {

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("油卡卡号")
    private String oilCardNo;

    @ApiModelProperty("品牌")
    @JsonSerialize(using = DictValueVoSerializer.class)
    private String brand;

    @ApiModelProperty("当前余额")
    private BigDecimal balance;

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    private Integer fuelType;
}
