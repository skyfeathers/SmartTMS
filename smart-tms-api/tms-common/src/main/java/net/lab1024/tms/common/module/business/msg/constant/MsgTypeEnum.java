package net.lab1024.tms.common.module.business.msg.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 消息 类型枚举
 *
 * @author listen
 * @date 2022/07/22 20:20
 */
@Getter
@AllArgsConstructor
public enum MsgTypeEnum implements BaseEnum {

    AUDIT(100, "审批"),

    SHIPPER(200, "货主"),

    DRIVER(300, "司机"),

    BRACKET(400, "挂车"),

    CAR(500, "车辆"),

    ORDER(600, "订单"),

    CONTRACT(700, "合同"),

    RECEIVE_ORDER(800, "应收账款"),

    ;

    private final Integer value;

    private final String desc;
}
