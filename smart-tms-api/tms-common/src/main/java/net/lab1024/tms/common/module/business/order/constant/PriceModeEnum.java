package net.lab1024.tms.common.module.business.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 计价方式
 *
 * @author lidoudou
 * @date 2022/7/12 上午10:56
 */
@AllArgsConstructor
@Getter
public enum PriceModeEnum implements BaseEnum {

    CHARTERED_BUS(1, "包车"),

    TON(2, "吨"),

    SQUARE(3, "方"),

    PIECE(4, "件");

    private Integer value;

    private String desc;

}
