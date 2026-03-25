package net.lab1024.tms.admin.module.business.driver;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.driver.domain.form.*;
import net.lab1024.tms.admin.module.business.driver.domain.vo.*;
import net.lab1024.tms.admin.module.business.driver.service.DriverService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.DRIVER})
public class DriverController {

    @Autowired
    private DriverService driverService;

    @ApiOperation(value = "分页查询司机 @author lidoudou")
    @PostMapping("/driver/page/query")
    public ResponseDTO<PageResult<DriverVO>> queryByPage(@RequestBody @Valid DriverQueryForm queryForm) {
        return driverService.queryByPage(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "司机详情 @author lidoudou")
    @GetMapping("/driver/detail/{driverId}")
    public ResponseDTO<DriverDetailVO> detail(@PathVariable Long driverId) {
        return ResponseDTO.ok(driverService.getDetail(driverId, SmartRequestEnterpriseUtil.getRequestEnterpriseId()));
    }

    @ApiOperation(value = "司机银行列表 @author lidoudou")
    @GetMapping("/driver/bankList/{driverId}")
    public ResponseDTO<List<DriverBankVO>> bankList(@PathVariable Long driverId) {
        return driverService.bankList(driverId);
    }


    @ApiOperation(value = "添加司机银行 @author lidoudou")
    @PostMapping("/driver/bank/add")
    public ResponseDTO<String> bankAdd(@RequestBody @Valid DriverBankAddForm bankAddForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.bankAdd(bankAddForm, dataTracerRequestForm);
    }


    @RepeatSubmit
    @ApiOperation(value = "新建司机 @author lidoudou")
    @PostMapping("/driver/save")
    public ResponseDTO<String> add(@RequestBody @Valid DriverAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.save(addForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑经营模式 @author lidoudou")
    @PostMapping("/driver/businessMode/update")
    public ResponseDTO<String> businessModeUpdate(@RequestBody @Valid DriverBusinessModeUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.businessModeUpdate(updateForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑司机 @author lidoudou")
    @PostMapping("/driver/update")
    public ResponseDTO<String> update(@RequestBody @Valid DriverUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.update(updateForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @ApiOperation(value = "司机删除 @author lidoudou")
    @PostMapping("/driver/batch/delete")
    public ResponseDTO<String> batchDelete(@RequestBody @Valid ValidateList<Long> driverIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.batchDeleteDriver(driverIdList, dataTracerRequestForm);
    }

    @ApiOperation("审核 by lidoudou")
    @PostMapping("/driver/audit")
    public ResponseDTO<String> audit(@RequestBody @Valid DriverAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.audit(auditForm, dataTracerRequestForm);
    }

    @ApiOperation("批量审核 by lidoudou")
    @PostMapping("/driver/batch/audit")
    public ResponseDTO<String> batchAudit(@RequestBody @Valid DriverBatchAuditForm batchAuditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.batchAudit(batchAuditForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "更新状态 @author lidoudou")
    @GetMapping("/driver/updateDisableFlag/{driverId}")
    public ResponseDTO<String> updateDisableFlag(@PathVariable Long driverId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.updateStatus(driverId, dataTracerRequestForm);
    }

    @ApiOperation(value = "批量更新状态 @author lihaifan")
    @PostMapping("/driver/batchUpdateDisableFlag")
    public ResponseDTO<String> batchUpdateDisableFlag(@RequestBody @Valid DriverBatchDisableForm batchDisableForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.batchUpdateStatus(batchDisableForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "司机下拉框查询 @author lihaifan")
    @GetMapping("/driver/select")
    public ResponseDTO<List<DriverSelectVO>> queryDriverSelect(@RequestParam(value = "vehicleId", required = false) Long vehicleId) {
        return driverService.queryDriverSelect(vehicleId, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation("批量修改负责人 by lidoudou")
    @PostMapping("/driver/manager/update")
    public ResponseDTO<String> batchUpdateManager(@RequestBody @Valid DriverManagerUpdateForm updateManagerForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverService.batchUpdateManager(updateManagerForm, dataTracerRequestForm);
    }

    @ApiOperation("查询是否有重名的司机 by lidoudou")
    @GetMapping("/driver/query/name")
    public ResponseDTO<List<DriverSimpleVO>> queryByName(@RequestParam String driverName) {
        return driverService.queryByName(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), driverName);
    }


    @ApiOperation(value = "司机导出 by lidoudou")
    @GetMapping("/driver/export")
    public void export(DriverQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<DriverExportVO> list = driverService.queryByExport(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        ExportParams exportParams = new ExportParams();
        String title = "司机列表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, DriverExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
