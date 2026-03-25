package net.lab1024.tms.admin.module.business.fleet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBankDTO;
import net.lab1024.tms.admin.module.business.fleet.domain.form.*;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.*;
import net.lab1024.tms.admin.module.business.fleet.service.FleetService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.FLEET})
public class FleetController {

    @Autowired
    private FleetService fleetService;

    @ApiOperation(value = "分页查询车队 @author lidoudou")
    @PostMapping("/fleet/page/query")
    public ResponseDTO<PageResult<FleetVO>> queryByPage(@RequestBody @Valid FleetQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return fleetService.queryByPage(queryForm);
    }

    @ApiOperation(value = "查询车队列表 @author lidoudou")
    @GetMapping("/fleet/query/list")
    public ResponseDTO<List<FleetSimpleVO>> queryList() {
        return fleetService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "车队详情 @author lidoudou")
    @GetMapping("/fleet/detail/{fleetId}")
    public ResponseDTO<FleetDetailVO> detail(@PathVariable Long fleetId) {
        return fleetService.getDetail(fleetId);
    }

    @ApiOperation(value = "车队银行列表 @author yandy")
    @GetMapping("/fleet/bankList/{fleetId}")
    public ResponseDTO<List<FleetBankDTO>> bankList(@PathVariable Long fleetId) {
        return fleetService.bankList(fleetId);
    }

    @ApiOperation(value = "添加车队银行 @author lidoudou")
    @PostMapping("/fleet/bank/add")
    public ResponseDTO<String> bankAdd(@RequestBody @Valid FleetBankAddForm bankAddForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return fleetService.bankAdd(bankAddForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "新建车队 @author lidoudou")
    @PostMapping("/fleet/save")
    public ResponseDTO<String> add(@RequestBody @Valid FleetAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return fleetService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑车队 @author lidoudou")
    @PostMapping("/fleet/update")
    public ResponseDTO<String> update(@RequestBody @Valid FleetUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return fleetService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "车队删除 @author lidoudou")
    @PostMapping("/fleet/batch/delete")
    public ResponseDTO<String> delete(@RequestBody @Valid ValidateList<Long> fleetIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return fleetService.deleteFleet(fleetIdList, dataTracerRequestForm);
    }

    @ApiOperation(value = "分页查询司机 @author lidoudou")
    @PostMapping("/fleet/driver/page/query")
    public ResponseDTO<PageResult<FleetDriverVO>> queryDriverByPage(@RequestBody @Valid FleetItemQueryForm queryForm) {
        return fleetService.queryDriverByPage(queryForm);
    }

    @ApiOperation(value = "查询司机 @author lidoudou")
    @GetMapping("/fleet/driver/query/list/{fleetId}")
    public ResponseDTO<List<FleetDriverVO>> queryDriverList(@PathVariable("fleetId") Long fleetId) {
        return fleetService.queryDriverList(fleetId);
    }

    @ApiOperation(value = "分页查询车辆 @author lidoudou")
    @PostMapping("/fleet/vehicle/page/query")
    public ResponseDTO<PageResult<FleetVehicleVO>> queryVehicleByPage(@RequestBody @Valid FleetItemQueryForm queryForm) {
        return fleetService.queryVehicleByPage(queryForm);
    }

    @ApiOperation(value = "添加关联模块 @author lidoudou")
    @PostMapping("/fleet/item/add")
    public ResponseDTO<String> addItem(@RequestBody @Valid FleetItemAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return fleetService.addItem(addForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "删除关联信息 @author lidoudou")
    @GetMapping("/fleet/item/delete/{fleetItemId}")
    public ResponseDTO<String> deleteItemById(@PathVariable Long fleetItemId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return fleetService.deleteItem(fleetItemId, dataTracerRequestForm);
    }

    @ApiOperation("批量审核 by lihaifan")
    @PostMapping("/fleet/audit")
    public ResponseDTO<String> audit(@RequestBody @Valid FleetAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return fleetService.audit(auditForm, dataTracerRequestForm);
    }

    @ApiOperation("批量修改负责人 by lidoudou")
    @PostMapping("/fleet/manager/update")
    public ResponseDTO<String> batchUpdateManager(@RequestBody @Valid FleetManagerUpdateForm updateManagerForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return fleetService.batchUpdateManager(updateManagerForm, dataTracerRequestForm);
    }
}
