package net.lab1024.tms.fixedasset.module.business.check.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 盘点类型
 *
 * @author lidoudou
 * @date 2023/3/24 上午10:38
 */
@Getter
@AllArgsConstructor
public enum CheckTypeEnum implements BaseEnum {

    QR_CODE(1, "扫码盘点"),

    NUMBER(2, "数量盘点"),

    ;

    private final Integer value;

    private final String desc;
}
