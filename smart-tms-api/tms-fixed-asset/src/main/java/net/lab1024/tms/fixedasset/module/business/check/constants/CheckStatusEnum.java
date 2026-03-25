package net.lab1024.tms.fixedasset.module.business.check.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 资产盘点状态
 *
 * @author lidoudou
 * @date 2023/3/24 上午10:36
 */
@Getter
@AllArgsConstructor
public enum CheckStatusEnum implements BaseEnum {

    NOT_CHECK(5, "未盘点"),

    PAN_PING(10, "盘平"),

    PROFIT(15, "盘盈"),

    LOSS(20, "盘亏"),


    ;

    private final Integer value;

    private final String desc;
}
