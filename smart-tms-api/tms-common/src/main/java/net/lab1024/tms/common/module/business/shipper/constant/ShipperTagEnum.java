package net.lab1024.tms.common.module.business.shipper.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 货主标签
 *
 * @author yandy
 * @date 2220/10/24 14:27
 */
public enum ShipperTagEnum implements BaseEnum {

    /**
     * 0 潜在
     */
    POTENTIAL(0, "潜在"),

    /**
     * 1 意向
     */
    INTENTION(1, "意向"),

    /**
     * 2 洽谈
     */
    NEGOTIATION(2, "洽谈"),

    /**
     * 3 成交
     */
    DEAL(3, "成交"),

    /**
     * 4 流失
     */
    LOSS(4, "流失"),



    ;

    private Integer status;

    private String desc;

    ShipperTagEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return status;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
