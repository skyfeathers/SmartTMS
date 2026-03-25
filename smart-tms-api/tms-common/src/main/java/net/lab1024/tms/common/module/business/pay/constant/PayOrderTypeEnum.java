package net.lab1024.tms.common.module.business.pay.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author yandy
 * @Date 2022-08-13
 */
@Getter
@AllArgsConstructor
public enum PayOrderTypeEnum implements BaseEnum {

    CASH(0, "现金支付付款单"),

    OIL_CARD(10, "油卡支付付款单"),
    ;

    private Integer value;

    private String desc;
}
