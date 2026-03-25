package net.lab1024.tms.common.module.business.carcost.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 自有车科目 - 支付方式
 *
 * @author zhaoxinyang
 * @date 2023/10/21 17:18
 */
@Getter
@AllArgsConstructor
public enum CarCostCategoryPayModeEnum implements BaseEnum {

    CASH(1, "现金"),

    OIL_CARD(2, "油卡"),

    ETC_CARD(3, "ETC卡"),

    ;

    private final Integer value;

    private final String desc;

}
