package net.lab1024.tms.fixedasset.module.business.borrowback.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 类型
 *
 * @author lidoudou
 * @date 2023/3/21 下午2:45
 */
@Getter
@AllArgsConstructor
public enum BorrowBackTypeEnum implements BaseEnum {

    BORROW(1, "借用"),

    BACK(2, "归还"),
    ;

    private final Integer value;

    private final String desc;
}
