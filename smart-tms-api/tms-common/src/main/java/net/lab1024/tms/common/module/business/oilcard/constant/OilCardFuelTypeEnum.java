package net.lab1024.tms.common.module.business.oilcard.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 油卡燃料类型
 *
 * @author zhaoxinyang
 * @date 2023/11/06 10:15
 */
@Getter
@AllArgsConstructor
public enum OilCardFuelTypeEnum implements BaseEnum {

    DIESEL_OIL_CARD(1, "专卡"),

    GASOLINE_CARD(2, "普卡"),
    ;

    private Integer value;

    private String desc;

}