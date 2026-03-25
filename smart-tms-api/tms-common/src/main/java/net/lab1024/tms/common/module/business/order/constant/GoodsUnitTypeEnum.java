package net.lab1024.tms.common.module.business.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author yandy
 * @description:
 * @date 2022/8/25 10:24 上午
 */
@AllArgsConstructor
@Getter
public enum GoodsUnitTypeEnum implements BaseEnum {

    UNIT_CAR(10, "车"),

    UNIT_BOX(20, "箱"),

    UNIT_ITEM(30, "件"),

    UNIT_TON(40, "吨"),
    ;

    private Integer value;

    private String desc;

}