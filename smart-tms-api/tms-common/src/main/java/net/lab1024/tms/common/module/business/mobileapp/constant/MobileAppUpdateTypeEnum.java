package net.lab1024.tms.common.module.business.mobileapp.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * app平台更新类型
 *
 * @author yandy
 * @date 2023/5/16 17:18
 */
public enum MobileAppUpdateTypeEnum implements BaseEnum {

    /**
     * 1 无需更新
     */
    NOT_NEED_UPDATE(1, "无需更新"),

    /**
     * 2 自由更新
     */
    FREEDOM_UPDATE(2, "自由更新"),

    /**
     * 3 强制更新
     */
    FORCE_UPDATE(3, "强制更新"),
    ;

    private Integer type;

    private String desc;

    MobileAppUpdateTypeEnum(Integer type, String desc) {
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
