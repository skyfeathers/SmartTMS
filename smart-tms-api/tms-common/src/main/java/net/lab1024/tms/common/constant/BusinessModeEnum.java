package net.lab1024.tms.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 经营方式
 *
 * @author lidoudou
 * @date 2022/6/25 下午5:13
 */
@AllArgsConstructor
@Getter
public enum BusinessModeEnum implements BaseEnum {
    /**
     * 1 内管车
     */
    INNER_MANAGEMENT(1, "内管车"),

    /**
     * 2 挂靠车
     */
    RELY(2, "挂靠车"),

    /**
     * 3 外派车
     */
    ASSIGNMENT(3, "外派车"),

    ;

    private final Integer value;

    private final String desc;
}
