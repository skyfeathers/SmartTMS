package net.lab1024.tms.driver.interceptor;

import com.alibaba.fastjson.JSONObject;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.RequestHeaderConst;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 公用api 拦截器
 *
 * @author Administrator
 */
@Component
public class DriverAuthorityInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private List<String> ignoreUrlList;

    public Boolean contain(List<String> ignores, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(ignores)) {
            return false;
        }
        String uri = request.getRequestURI();
        for (String ignoreUrl : ignores) {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            boolean match = antPathMatcher.match(ignoreUrl, uri);
            if (match) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拦截服务器端响应处理ajax请求返回结果
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //OPTIONS请求直接return
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }
        boolean isHandler = handler instanceof HandlerMethod;
        if (!isHandler) {
            return true;
        }
        if (this.contain(ignoreUrlList, request)) {
            return true;
        }

        //不需要登录的注解
        NoNeedLogin noNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class);
        if (null != noNeedLogin) {
            return true;
        }
        String xHeaderToken = request.getHeader(RequestHeaderConst.TOKEN);
        String xRequestToken = request.getParameter(RequestHeaderConst.TOKEN);
        String xAccessToken = null != xHeaderToken ? xHeaderToken : xRequestToken;
        if (null == xAccessToken) {
            this.outputResult(response, UserErrorCode.LOGIN_STATE_INVALID);
            return false;
        }
        RequestUser requestUser = tokenService.getTokenRequestUser(xAccessToken, request);
        if (requestUser == null) {
            this.outputResult(response, UserErrorCode.LOGIN_STATE_INVALID);
            return false;
        }
        // 设置登录用户
        SmartRequestUtil.setRequestUser(requestUser);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SmartRequestUtil.remove();
    }

    /**
     * 错误输出
     *
     * @param response
     * @param userErrorCode
     * @throws IOException
     */
    private void outputResult(HttpServletResponse response, UserErrorCode userErrorCode) throws IOException {
        ResponseDTO<Object> wrap = ResponseDTO.error(userErrorCode);
        String msg = JSONObject.toJSONString(wrap);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(msg);
        response.flushBuffer();
    }
}
