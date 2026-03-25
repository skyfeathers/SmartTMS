package net.lab1024.tms.common.module.business.receive.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 发票类型
 *
 * @author lidoudou
 * @date 2022/8/2 上午11:59
 */
@AllArgsConstructor
@Getter
public enum InvoiceTypeEnum implements BaseEnum {

    /**
     * 1 纸质发票
     */
    PAPER_INVOICE(1, "纸质发票"),

    /**
     * 电子发票
     */
    ELECTRONIC_INVOICE(2, "电子发票"),

    ;

    private final Integer value;

    private final String desc;
}
