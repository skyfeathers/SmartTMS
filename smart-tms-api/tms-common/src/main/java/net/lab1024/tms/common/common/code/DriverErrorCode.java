package net.lab1024.tms.common.common.code;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DriverErrorCode implements ErrorCode {

    VERIFY_CODE_ERROR(40001, "短信验证码错误"),

    PHONE_NOT_REGISTER(40002, "司机手机号不存在"),

    STATUS_ERROR(40003, "您的账号状态异常，请联系客服")
    ;

    private final int code;

    private final String msg;

    private final String level;

    DriverErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_SYSTEM;
    }

}