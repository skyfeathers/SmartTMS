package net.lab1024.tms.common.module.support.token;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 登录设备类型
 * @author zhuoda
 * @Date 2022-03-01
 */
public enum LoginDeviceEnum implements BaseEnum {

    PC(1, "电脑端"),

    ANDROID(2, "安卓"),

    APPLE(3, "苹果"),

    H5(4, "H5"),
    ;

    LoginDeviceEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private Integer value;
    private String desc;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
