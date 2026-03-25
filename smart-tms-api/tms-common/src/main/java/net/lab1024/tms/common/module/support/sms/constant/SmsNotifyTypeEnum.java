package net.lab1024.tms.common.module.support.sms.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 通知类短信 枚举类
 *
 * @author listen
 * @date 2020/02/20 16:50
 */
public enum SmsNotifyTypeEnum implements BaseEnum {


    ;

    private Integer type;

    private String codeKey;

    private String desc;

    SmsNotifyTypeEnum(Integer type, String codeKey, String desc) {
        this.type = type;
        this.codeKey = codeKey;
        this.desc = desc;
    }

    public String getCodeKey() {
        return codeKey;
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
