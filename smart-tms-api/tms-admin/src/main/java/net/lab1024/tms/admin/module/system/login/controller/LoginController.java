package net.lab1024.tms.admin.module.system.login.controller;

import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.login.domain.LoginEmployeeDetail;
import net.lab1024.tms.admin.module.system.login.domain.LoginEnterpriseVO;
import net.lab1024.tms.admin.module.system.login.domain.LoginForm;
import net.lab1024.tms.admin.module.system.login.service.LoginService;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.RequestHeaderConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.captcha.domain.CaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 登录
 *
 * @author zhuoda
 */
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_LOGIN})
public class LoginController {

    @Autowired
    private LoginService loginService;

    @NoNeedLogin
    @GetMapping("/login/getLoginEnterprise")
    @ApiOperation("获取登录结果信息  @author zhuoda")
    public ResponseDTO<LoginEnterpriseVO> getLoginEnterprise() {
        return loginService.getLoginEnterprise(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @NoNeedLogin
    @PostMapping("/login")
    @ApiOperation("登录 @author zhuoda")
    public ResponseDTO<LoginEmployeeDetail> login(@Valid @RequestBody LoginForm loginForm, HttpServletRequest request) {
        String ip = ServletUtil.getClientIP(request);
        String userAgent = ServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT);
        return loginService.login(loginForm, ip, userAgent, SmartRequestEnterpriseUtil.getRequestEnterprise());
    }

    @GetMapping("/login/getLoginInfo")
    @ApiOperation("获取登录结果信息  @author zhuoda")
    public ResponseDTO<LoginEmployeeDetail> getLoginInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof LoginEmployeeDetail)) {
            return ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID);
        }

        LoginEmployeeDetail loginEmployeeDetail = (LoginEmployeeDetail) authentication.getPrincipal();
        loginEmployeeDetail.setLoginPassword(null);
        return ResponseDTO.ok(loginEmployeeDetail);
    }

    @GetMapping("/login/refresh")
    @ApiOperation("刷新权限  @author zhuoda")
    public ResponseDTO<String> refresh() {
        loginService.removeLoginUserDetailCache(SmartRequestUtil.getRequestUserId());
        return ResponseDTO.ok();
    }

    @ApiOperation("退出登陆  @author zhuoda")
    @GetMapping("/login/logout")
    public ResponseDTO<String> logout(@RequestHeader(value = RequestHeaderConst.TOKEN, required = false) String token) {
        return loginService.logout(token, SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation("获取验证码  @author zhuoda")
    @GetMapping("/login/getCaptcha")
    @NoNeedLogin
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return loginService.getCaptcha();
    }

}
