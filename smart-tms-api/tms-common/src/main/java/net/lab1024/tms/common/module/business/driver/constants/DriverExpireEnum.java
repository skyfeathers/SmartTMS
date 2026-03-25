package net.lab1024.tms.common.module.business.driver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 司机审核状态
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:31
 */
@AllArgsConstructor
@Getter
public enum DriverExpireEnum implements BaseEnum {

    /**
     * 0 未到期
     */
    NONE(0, "未到期"),

    /**
     * 1 已到期
     */
    EXPIRED(1, "已到期"),

    ;

    private final Integer value;

    private final String desc;
}
