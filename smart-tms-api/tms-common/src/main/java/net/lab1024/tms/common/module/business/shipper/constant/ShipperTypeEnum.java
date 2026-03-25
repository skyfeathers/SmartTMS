package net.lab1024.tms.common.module.business.shipper.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 17:21
 */
public enum ShipperTypeEnum implements BaseEnum {

    CUSTOMER(0, "客户"),
    SUPPLIER(1, "工厂")
    ;

    private Integer value;
    private String desc;

    ShipperTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
