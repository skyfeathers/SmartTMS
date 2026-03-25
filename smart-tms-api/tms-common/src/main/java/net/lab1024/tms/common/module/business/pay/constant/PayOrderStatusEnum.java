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
public enum PayOrderStatusEnum implements BaseEnum {

    NO_APPLY(0, "未申请"),

    NO_PAY(10, "未支付"),

    PAID(20, "已支付"),

    PAID_FAIL(25, "支付失败"),

    CANCEL(50, "作废"),
    ;

    private Integer value;

    private String desc;
}
