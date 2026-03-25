package net.lab1024.tms.common.module.business.fleet.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 类型
 *
 * @author lidoudou
 * @date 2022/6/28 下午2:29
 */
@AllArgsConstructor
@Getter
public enum FleetItemTypeEnum implements BaseEnum {

    /**
     * 1 司机
     */
    DRIVER(1, "司机"),

    /**
     * 2 车辆
     */
    VEHICLE(2, "车辆"),

    ;

    private final Integer value;

    private final String desc;
}
