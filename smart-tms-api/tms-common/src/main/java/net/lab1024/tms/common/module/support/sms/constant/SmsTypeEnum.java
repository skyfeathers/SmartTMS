package net.lab1024.tms.common.module.support.sms.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 短信类型 枚举类
 *
 * @author listen
 * @date 2020/02/20 16:50
 */
public enum SmsTypeEnum implements BaseEnum {

    /**
     * 1 验证码
     */
    VERIFICATION_CODE(1, "验证码"),

    /**
     * 2 通知短信
     */
    NOTIFY(2, "通知短信"),

    ;

    private Integer type;

    private String desc;

    SmsTypeEnum(Integer type, String desc) {
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
