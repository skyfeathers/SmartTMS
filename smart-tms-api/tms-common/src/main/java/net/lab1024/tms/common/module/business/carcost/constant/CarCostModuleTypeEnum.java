package net.lab1024.tms.common.module.business.carcost.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 自有车-模块类型
 *
 * @author zhaoxinyang
 * @date 2023/11/01 16:40
 */
@Getter
@AllArgsConstructor
public enum CarCostModuleTypeEnum implements BaseEnum {

    CASH_RECEIVE(1, "现金申请"),

    OIL_CARD_RECEIVE(2, "油卡充值"),

    CASH_PAY(3, "现金支出"),

    OIL_PAY(4, "油费支出"),

    ETC_PAY(5, "ETC支出"),

    UREA_PAY(6, "尿素支出"),

    ;

    private final Integer value;

    private final String desc;
}