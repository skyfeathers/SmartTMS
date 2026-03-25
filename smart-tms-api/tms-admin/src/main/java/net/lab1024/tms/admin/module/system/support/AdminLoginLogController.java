package net.lab1024.tms.admin.module.system.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.loginlog.LoginLogService;
import net.lab1024.tms.common.module.support.loginlog.domain.LoginLogQueryForm;
import net.lab1024.tms.common.module.support.loginlog.domain.LoginLogVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录日志
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/07/22 19:46:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Api(tags = AdminSwaggerTagConst.System.LOGIN_LOG)
public class AdminLoginLogController extends AdminBaseController {

    @Resource
    private LoginLogService loginLogService;

    @ApiOperation("分页查询 @author 卓大")
    @PostMapping("/loginLog/page/query")
    @PreAuthorize("@saAuth.checkPermission('support:loginLog:query')")
    public ResponseDTO<PageResult<LoginLogVO>> queryByPage(@RequestBody LoginLogQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return loginLogService.queryByPage(queryForm);
    }


}
