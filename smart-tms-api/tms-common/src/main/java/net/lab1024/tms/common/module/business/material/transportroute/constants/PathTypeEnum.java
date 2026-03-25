package net.lab1024.tms.common.module.business.material.transportroute.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 路线类型
 *
 * @author lidoudou
 * @date 2022/7/12 上午11:59
 */
@AllArgsConstructor
@Getter
public enum PathTypeEnum implements BaseEnum {

    CONTAINER_LOCATION(1, "提箱地点"),

    PLACING_LOADING(2, "装货地点"),

    UNLOADING_LOCATION(3, "卸货地点"),

    RETURN_CONTAINER_LOCATION(4, "还箱地点"),
    ;

    private Integer value;

    private String desc;

}
