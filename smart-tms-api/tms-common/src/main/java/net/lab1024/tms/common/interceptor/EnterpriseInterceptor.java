package net.lab1024.tms.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.RequestEnterprise;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.CommonEnterpriseService;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
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
public class EnterpriseInterceptor implements HandlerInterceptor {

    @Autowired
    private CommonEnterpriseService commonEnterpriseService;

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

        EnterpriseEntity enterpriseEntity = commonEnterpriseService.selectByDomainName(request);
        if (null == enterpriseEntity) {
            this.outputResult(response, UserErrorCode.LOGIN_DOMAIN_INVALID);
            return false;
        }
        // 设置登录用户
        RequestEnterprise requestEnterprise = new RequestEnterprise();
        requestEnterprise.setEnterpriseId(enterpriseEntity.getEnterpriseId());
        requestEnterprise.setEnterpriseName(enterpriseEntity.getEnterpriseName());
        requestEnterprise.setPlatformFlag(enterpriseEntity.getPlatformFlag());
        SmartRequestEnterpriseUtil.setRequestEnterprise(requestEnterprise);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SmartRequestEnterpriseUtil.remove();
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
