package net.lab1024.tms.common.module.business.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 订单类型
 *
 * @author lidoudou
 * @date 2022/8/24 下午2:26
 */
@AllArgsConstructor
@Getter
public enum OrderTypeEnum implements BaseEnum {

    ORDINARY(1, "普通订单"),

    NETWORK_FREIGHT(2, "网络货运"),

    AN_LIAN(3, "安联"),
    ;

    private Integer value;

    private String desc;

}
