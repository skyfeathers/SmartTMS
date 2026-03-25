package net.lab1024.tms.driver.module.system.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.system.login.domain.DriverLoginVO;
import net.lab1024.tms.driver.module.system.login.domain.DriverSmsLoginForm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = DriverSwaggerTagConst.Business.LOGIN)
@RestController
public class DriverLoginController {

    @Resource
    private DriverLoginService driverLoginService;

    @NoNeedLogin
    @ApiOperation("司机端短信验证码登录 @author zhaoxinyang")
    @PostMapping("/login/sms")
    public ResponseDTO<DriverLoginVO> smsVerifyLogin(@RequestBody @Valid DriverSmsLoginForm loginForm) {
        loginForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return driverLoginService.smsVerifyLogin(loginForm);
    }

    @ApiOperation(value = "查询司机基础信息 @author zhaoxinyang")
    @GetMapping("/login/getLoginInfo")
    public ResponseDTO<DriverLoginVO> getLoginInfo() {
        Long driverId = SmartRequestUtil.getRequestUserId();
        return driverLoginService.getLoginInfo(driverId);
    }
    
}
