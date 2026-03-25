package net.lab1024.tms.common.module.business.receive.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 核算状态
 *
 * @author lidoudou
 * @date 2022/8/5 上午9:40
 */
@AllArgsConstructor
@Getter
public enum CheckStatusEnum implements BaseEnum {

    /**
     * 1 待核算
     */
    WAIT_CHECK(1, "待核算"),

    /**
     * 2 已核算
     */
    CHECK(2, "已核算"),

    /**
     * 作废
     */
    CANCEL(3, "作废"),

    ;

    private final Integer value;

    private final String desc;
}
