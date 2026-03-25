package net.lab1024.tms.common.common.enumeration;

/**
 * 用户类型
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/10/19 21:46:24
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
public enum UserTypeEnum implements BaseEnum {

    ADMIN(1, "员工"),

    DRIVER(2, "司机"),

    SHIPPER(3, "货主");


    private Integer type;

    private String desc;

    UserTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return type;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
