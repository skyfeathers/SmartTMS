package net.lab1024.tms.common.module.business.pic.constants;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author zhuoda
 * @Date 2021/1/22
 */
public enum PicTypeEnum implements BaseEnum {

    ADMIN_APP(1, "司机端"),

    DRIVER_APP(2, "司机端"),

    SHIPPER_APP(3, "货主端"),

    ;

    private Integer location;

    private String desc;

    PicTypeEnum(Integer location, String desc) {
        this.location = location;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return location;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
