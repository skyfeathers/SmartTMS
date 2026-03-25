package net.lab1024.tms.common.module.business.driver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 司机状态
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:20
 */
@AllArgsConstructor
@Getter
public enum DriverStatusEnum implements BaseEnum {


    /**
     * 1 激活
     */
    ACTIVE(1, "启用"),

    /**
     * 2 禁用
     */
    DISABLED(2, "禁用");

    private final Integer value;

    private final String desc;
}
