package net.lab1024.tms.admin.module.business.maintenance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceAddForm;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceQueryForm;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceUpdateForm;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceVO;
import net.lab1024.tms.admin.module.business.maintenance.service.MaintenanceService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.MAINTENANCE})
public class MaintenanceController {

    @Resource
    private MaintenanceService maintenanceService;

    @ApiOperation(value = "分页查询保养信息 @author zhaoxinyang")
    @PostMapping("/maintenance/page/query")
    public ResponseDTO<PageResult<MaintenanceVO>> queryByPage(@RequestBody @Valid MaintenanceQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return maintenanceService.query(queryForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "新建保养记录 @author zhaoxinyang")
    @PostMapping("/maintenance/add")
    public ResponseDTO<String> add(@RequestBody @Valid MaintenanceAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return maintenanceService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "修改保养记录 @author zhaoxinyang")
    @PostMapping("/maintenance/update")
    public ResponseDTO<String> update(@RequestBody @Valid MaintenanceUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return maintenanceService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "删除保养记录 @author zhaoxinyang")
    @GetMapping("/maintenance/delete/{maintenanceId}")
    public ResponseDTO<String> delete(@PathVariable("maintenanceId") Long maintenanceId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return maintenanceService.delete(maintenanceId, dataTracerRequestForm);
    }

}