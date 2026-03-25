package net.lab1024.tms.common.module.business.material.cost.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Getter
@AllArgsConstructor
public enum CostItemCategoryEnum implements BaseEnum {

    FREIGHT_COST(1, "运费"),

    EXCEPTION_COST(10, "异常费用"),

    OIL_CARD_COST(15, "油卡"),

    OTHER(100, "其他"),
    ;

    private Integer value;

    private String desc;

}
