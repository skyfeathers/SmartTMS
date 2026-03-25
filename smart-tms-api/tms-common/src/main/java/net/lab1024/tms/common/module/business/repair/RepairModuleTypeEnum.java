package net.lab1024.tms.common.module.business.repair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 维修模块类型
 *
 * @author lidoudou
 * @date 2022/6/29 下午2:48
 */
@AllArgsConstructor
@Getter
public enum RepairModuleTypeEnum implements BaseEnum {

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
