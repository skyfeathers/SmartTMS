package net.lab1024.tms.admin.module.business.mobileapp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppUpdateQueryDTO;
import net.lab1024.tms.common.module.business.mobileapp.domain.vo.MobileAppUpdateVO;
import net.lab1024.tms.common.module.business.mobileapp.service.MobileAppCommonService;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.APP_VERSION})
public class AppVersionController {

    @Resource
    private MobileAppCommonService mobileAppCommonService;

    @NoNeedLogin
    @ApiOperation("获取用户协议 by zhaoxinyang")
    @GetMapping("/mobileApp/user/agreement")
    public ResponseDTO<String> getUserAgreement() {
        return mobileAppCommonService.getUserAgreement();
    }

    @NoNeedLogin
    @ApiOperation("获取用户协议 by zhaoxinyang")
    @GetMapping("/mobileApp/private/agreement")
    public ResponseDTO<String> getPrivateAgreement() {
        return mobileAppCommonService.getPrivateAgreement();
    }

    @NoNeedLogin
    @ApiOperation("查询最新版本,是否需要更新 by zhaoxinyang")
    @PostMapping("/mobileApp/newest/query")
    public ResponseDTO<MobileAppUpdateVO> appUpdate(@RequestBody @Valid MobileAppUpdateQueryDTO queryDTO) {
        return mobileAppCommonService.queryVersion(queryDTO);
    }

    @NoNeedLogin
    @ApiOperation("查询下载地址 by yandy")
    @GetMapping("/mobileApp/download/{platformType}")
    public ResponseDTO<String> appDownload(@PathVariable Integer platformType) {
        return mobileAppCommonService.appDownload(platformType);
    }

}
