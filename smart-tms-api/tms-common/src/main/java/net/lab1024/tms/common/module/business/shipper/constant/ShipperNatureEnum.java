package net.lab1024.tms.common.module.business.shipper.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 17:21
 */
public enum ShipperNatureEnum implements BaseEnum {

    ENTERPRISE(0, "企业"),
    PERSONAL(1, "个人");


    private Integer value;
    private String desc;

    ShipperNatureEnum(Integer value, String desc) {
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
