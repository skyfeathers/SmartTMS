package net.lab1024.tms.common.module.business.insurance.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 保险类型
 *
 * @author lidoudou
 * @date 2022/6/21 下午3:44
 */
@AllArgsConstructor
@Getter
public enum InsuranceModuleTypeEnum implements BaseEnum {

    /**
     * 1 车辆
     */
    VEHICLE(1, "车辆"),

    /**
     * 2 挂车
     */
    BRACKET(2, "挂车"),

    ;

    private final Integer value;

    private final String desc;
}
