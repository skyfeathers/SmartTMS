package net.lab1024.tms.admin.module.business.quick.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

@Getter
@AllArgsConstructor
public enum QuickCreateTypeEnum implements BaseEnum {

    DRIVER(10, "司机"),

    VEHICLE(20, "车辆"),

    BRACKET(30, "挂车"),

    ALL(40, "全部"),

    ;

    private final Integer value;

    private final String desc;

}
