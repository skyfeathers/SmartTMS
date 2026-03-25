package net.lab1024.tms.common.module.business.mobileapp.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * app平台类型枚举
 *
 * @author yandy
 * @date 2023/5/16 17:18
 */
public enum MobileAppPlatformTypeEnum implements BaseEnum {

    /**
     * 1 安卓
     */
    ANDROID(1, "ANDROID"),

    /**
     * 2 苹果
     */
    IOS(2, "苹果"),
    ;

    private Integer type;

    private String desc;

    MobileAppPlatformTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return type;
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
