package net.lab1024.tms.common.module.business.driver.constants;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/5/19 10:29
 */
public enum DriverBalanceTradeTypeEnum implements BaseEnum {

    CASH_RECEIVE(5, "现金收入"),

    CASH_PAY(10, "现金支出"),

    ETC_PAY(15, "ETC支出"),

    OIL_CARD_PAY(20, "油卡支出"),

    UREA_PAY(25, "尿素支出"),


    ;

    private Integer type;

    private String desc;

    DriverBalanceTradeTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return type;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
