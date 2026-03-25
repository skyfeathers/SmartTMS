package net.lab1024.tms.common.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhuoda
 * @Date 2021-09-27
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    SYSTEM_ERROR(10001, "系统似乎出现了点小问题"),

    ;

    private final int code;

    private final String msg;

    private final String level;

    SystemErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_SYSTEM;
    }

}

