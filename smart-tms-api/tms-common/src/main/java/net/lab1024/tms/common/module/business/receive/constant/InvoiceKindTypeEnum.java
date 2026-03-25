package net.lab1024.tms.common.module.business.receive.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 发票种类
 *
 * @author lidoudou
 * @date 2022/8/2 下午2:05
 */
@AllArgsConstructor
@Getter
public enum InvoiceKindTypeEnum implements BaseEnum {

    /**
     * 1 普通发票
     */
    COMMON_INVOICE(1, "普通发票"),

    /**
     * 专项发票
     */
    SPECIAL_INVOICE(2, "专项发票"),

    /**
     * 增值税发票
     */
    VAT_INVOICE(3, "增值税发票"),

    ;

    private final Integer value;

    private final String desc;
}
