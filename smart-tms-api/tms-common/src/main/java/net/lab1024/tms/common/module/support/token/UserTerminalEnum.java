
package net.lab1024.tms.common.module.support.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author yandy
 * @description:
 * @date 2022/9/13 10:23 上午
 */
@AllArgsConstructor
@Getter
public enum UserTerminalEnum implements BaseEnum {


    ADMIN(1, "后管","admin-token:"),

    DRIVER(2, "司机端","driver-token:"),

    SHIPPER(3, "货主端","shipper-token:"),

    ;


    private Integer value;
    private String desc;
    private String redisKey;

}