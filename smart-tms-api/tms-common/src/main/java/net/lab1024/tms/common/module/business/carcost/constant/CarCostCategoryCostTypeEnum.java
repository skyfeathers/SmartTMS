package net.lab1024.tms.common.module.business.carcost.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 自有车-费用类型
 *
 * @author zhaoxinyang
 * @date 2023/10/24 17:22
 */
@Getter
@AllArgsConstructor
public enum CarCostCategoryCostTypeEnum implements BaseEnum {

    CASH_COST(10, "现金"),

    OIL_COST(20, "油费"),

    ETC_COST(30, "ETC"),

    UREA_COST(40, "尿素"),

    ;

    private final Integer value;

    private final String desc;
}