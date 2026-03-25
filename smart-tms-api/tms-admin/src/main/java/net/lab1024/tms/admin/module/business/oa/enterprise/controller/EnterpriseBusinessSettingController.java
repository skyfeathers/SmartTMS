package net.lab1024.tms.admin.module.business.oa.enterprise.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.form.BusinessSettingUpdateForm;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseBusinessSettingVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.service.EnterpriseBusinessSettingService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * OA企业模块
 *
 * @author lihaifan
 * @date 2022/6/22 16:49
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.OA_ENTERPRISE})
public class EnterpriseBusinessSettingController {

    @Autowired
    private EnterpriseBusinessSettingService businessSettingService;

    @ApiOperation("查询配置详情  by lidoudou")
    @GetMapping("/enterprise/setting/queryByKey/{enterpriseId}")
    public ResponseDTO<EnterpriseBusinessSettingVO> queryByKey(@PathVariable Long enterpriseId, @RequestParam String settingKey) {
        return businessSettingService.getSetting(enterpriseId, settingKey);
    }

    @ApiOperation("保存配置 by lidoudou")
    @PostMapping("/enterprise/setting/save")
    public ResponseDTO<String> save(@RequestBody @Valid BusinessSettingUpdateForm updateForm) {
        return businessSettingService.saveSetting(updateForm);
    }

}
