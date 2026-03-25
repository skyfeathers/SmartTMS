package net.lab1024.tms.common.module.business.receive.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 开票状态
 *
 * @author lidoudou
 * @date 2022/8/5 上午9:50
 */
@AllArgsConstructor
@Getter
public enum InvoiceStatusEnum implements BaseEnum {

    /**
     * 1 待开票
     */
    WAIT_INVOICE(1, "待开票"),

    /**
     * 2 已开票
     */
    INVOICE(2, "已开票"),

    /**
     * 部分开票
     */
    PORTION_INVOICE(3,"部分开票"),

    /**
     * 作废
     */
    CANCEL(4, "作废"),

    ;

    private final Integer value;

    private final String desc;
}
