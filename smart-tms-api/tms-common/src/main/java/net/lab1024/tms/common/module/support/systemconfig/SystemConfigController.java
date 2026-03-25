package net.lab1024.tms.common.module.support.systemconfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigAddForm;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigQueryForm;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigUpdateForm;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [  ]
 *
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
@Api(tags = {SwaggerTagConst.Support.SUPPORT_SYSTEM_CONFIG})
@RestController
public class SystemConfigController extends SupportBaseController {

    @Autowired
    private SystemConfigService systemConfigService;

    @ApiOperation("分页查询系统配置")
    @PostMapping("/config/query")
    public ResponseDTO<PageResult<SystemConfigVO>> querySystemConfigPage(@RequestBody @Valid SystemConfigQueryForm queryForm) {
        return systemConfigService.queryConfigPage(queryForm);
    }

    @ApiOperation("添加配置参数")
    @PostMapping("/config/add")
    public ResponseDTO<String> addSystemConfig(@RequestBody @Valid SystemConfigAddForm configAddForm) {
        return systemConfigService.add(configAddForm);
    }

    @ApiOperation("修改配置参数")
    @PostMapping("/config/update")
    public ResponseDTO<String> updateSystemConfig(@RequestBody @Valid SystemConfigUpdateForm updateForm) {
        return systemConfigService.updateSystemConfig(updateForm);
    }

    @ApiOperation("查询配置详情")
    @GetMapping("/config/queryByKey")
    @NoNeedLogin
    public ResponseDTO<SystemConfigVO> queryByKey(@RequestParam String configKey) {
        return ResponseDTO.ok(systemConfigService.getConfig(configKey));
    }

}
