package net.lab1024.tms.common.module.business.shipper.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 跟进方式
 *
 * @author lidoudou
 * @date 2022/7/9 上午10:11
 */
public enum TrackWayEnum implements BaseEnum {

    WX(1, "微信"),

    PHONE(2, "电话"),

    FACE_TO_FACE(3, "面谈"),
    ;

    private Integer value;
    private String desc;

    TrackWayEnum(Integer value, String desc) {
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
