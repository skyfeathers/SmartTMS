package net.lab1024.tms.common.module.business.order.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 订单状态
 *
 * @author lidoudou
 * @date 2022/7/13 上午11:03
 */
@AllArgsConstructor
@Getter
public enum OrderStatusEnum implements BaseEnum {

    TRANSPORT(50, "运输中"),

    COMPLETE(60, "已完成"),

    CANCEL(70, "已取消"),
    ;

    private Integer value;

    private String desc;

}
