package net.lab1024.tms.admin.module.system.login.third;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.login.domain.LoginEmployeeDetail;
import net.lab1024.tms.admin.module.system.login.service.LoginService;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 *
 * @author zhuoda
 */
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_LOGIN})
public class ThirdLoginController {

    @Autowired
    private LoginService loginService;

    @NoNeedLogin
    @GetMapping("/auth")
    @ApiOperation("登录 @author zhuoda")
    public ResponseDTO<LoginEmployeeDetail> login(@RequestParam(value = "authCode") String authCode) {
        return loginService.dingDingLogin(authCode, SmartRequestEnterpriseUtil.getRequestEnterprise());
    }
}
