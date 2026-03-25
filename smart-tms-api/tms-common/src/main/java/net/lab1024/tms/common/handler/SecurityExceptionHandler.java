package net.lab1024.tms.common.handler;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * [ 全局异常拦截 ]
 *
 * @author 罗伊
 * @date 2020/8/25 11:57
 */
@Slf4j
@ControllerAdvice
@ConditionalOnClass(AccessDeniedException.class)
public class SecurityExceptionHandler {


    /**
     * 权限异常
     */
    @ResponseBody
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseDTO<?> permissionExceptionHandler(AccessDeniedException e) {
        return ResponseDTO.error(UserErrorCode.NO_PERMISSION);
    }

}
