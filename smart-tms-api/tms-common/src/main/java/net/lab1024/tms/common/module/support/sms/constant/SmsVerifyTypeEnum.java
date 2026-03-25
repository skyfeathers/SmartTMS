package net.lab1024.tms.common.module.support.sms.constant;


import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 验证码类短信 枚举类
 *
 * @author listen
 * @date 2020/02/20 16:50
 */
public enum SmsVerifyTypeEnum implements BaseEnum {

    /**
     * 5 账号注册
     */
    REGISTER(5, "registerCode", "账号注册"),

    /**
     * 10 修改密码
     */
    UPDATE_PWD(10, "updatePwdCode", "修改密码"),

    /**
     * 15 身份验证
     */
    AUTHENTICATION(15, "authenticationCode", "身份验证"),

    /**
     * 20 验证码登录
     */
    VERIFY_CODE_LOGIN(20, "verifyCodeLogin", "验证码登录"),

    ;

    private Integer type;

    private String codeKey;

    private String desc;

    public static final String INFO = "5账号注册,10修改密码,15身份验证,20验证码登录";

    SmsVerifyTypeEnum(Integer type, String codeKey, String desc) {
        this.type = type;
        this.codeKey = codeKey;
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

    public String getCodeKey() {
        return codeKey;
    }
}
