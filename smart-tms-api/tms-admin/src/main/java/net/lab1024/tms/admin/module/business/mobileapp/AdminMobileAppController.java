package net.lab1024.tms.admin.module.business.mobileapp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppAddForm;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppQueryForm;
import net.lab1024.tms.common.module.business.mobileapp.domain.form.MobileAppUpdateForm;
import net.lab1024.tms.common.module.business.mobileapp.domain.vo.MobileAppVO;
import net.lab1024.tms.common.module.business.mobileapp.service.MobileAppService;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = AdminSwaggerTagConst.Business.APP_VERSION)
public class AdminMobileAppController {

    @Resource
    private MobileAppService mobileAppService;

    @ApiOperation("分页查询 @author zhaoxinyang")
    @PostMapping("/mobileApp/queryPage")
    public ResponseDTO<PageResult<MobileAppVO>> queryPage(@RequestBody @Valid MobileAppQueryForm queryForm) {
        return mobileAppService.queryPage(queryForm);
    }

    @ApiOperation("添加 @author zhaoxinyang")
    @PostMapping("/mobileApp/add")
    public ResponseDTO<String> add(@RequestBody @Valid MobileAppAddForm addForm) {
        return mobileAppService.add(addForm);
    }

    @ApiOperation("更新 @author zhaoxinyang")
    @PostMapping("/mobileApp/update")
    public ResponseDTO<String> update(@RequestBody @Valid MobileAppUpdateForm updateForm) {
        return mobileAppService.update(updateForm);
    }

    @ApiOperation("单个删除 @author zhaoxinyang")
    @GetMapping("/mobileApp/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        return mobileAppService.delete(id);
    }

    @ApiOperation("批量删除 @author zhaoxinyang")
    @PostMapping("/mobileApp/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody List<Long> idList) {
        return mobileAppService.batchDelete(idList);
    }

}
