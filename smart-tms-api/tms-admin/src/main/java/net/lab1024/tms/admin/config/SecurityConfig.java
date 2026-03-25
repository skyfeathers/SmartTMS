package net.lab1024.tms.admin.config;

import net.lab1024.tms.admin.constant.AdminUrlPrefixConst;
import net.lab1024.tms.admin.module.system.login.service.LoginService;
import net.lab1024.tms.common.common.security.AbstractSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author yandy
 * @date 2021/8/3 17:50
 */
@Configuration
public class SecurityConfig extends AbstractSecurityConfig {
    /**
     * 获取TOKEN 解析类
     */
    @Autowired
    private LoginService loginService;

    @Override
    protected BiFunction<String, HttpServletRequest, UserDetails> userFunction() {
        return (token, request) -> loginService.getLoginUserDetail(token, request);
    }

    @Override
    protected String[] getAuthenticatedUrlPatterns() {
        return new String[]{AdminUrlPrefixConst.ADMIN_URL_PATTERN};
    }


}
