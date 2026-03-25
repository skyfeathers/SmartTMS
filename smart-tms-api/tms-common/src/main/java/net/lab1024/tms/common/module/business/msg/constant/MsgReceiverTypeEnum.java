package net.lab1024.tms.common.module.business.msg.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 消息 接受者类型枚举
 *
 * @author listen
 * @date 2022/07/22 20:20
 */
@Getter
@AllArgsConstructor
public enum MsgReceiverTypeEnum implements BaseEnum {

    ADMIN(1, "后管"),

    SHIPPER(2, "货主"),

    DRIVER(3, "司机"),

    ;

    private final Integer value;

    private final String desc;
}
