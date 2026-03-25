package net.lab1024.tms.admin.module.business.reportform.employee;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.*;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.vo.*;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@RestController
@Api(tags = AdminSwaggerTagConst.Business.EMPLOYEE_BUSINESS)
public class EmployeeBusinessController {

    @Autowired
    private EmployeeSalesTargetService salesTargetService;

    @ApiOperation("员工业绩目标-查询 by lidoudou")
    @PostMapping("/employee/salesTarget/query")
    public ResponseDTO<List<EmployeeSalesTargetVO>> querySalesTarget(@RequestBody @Valid EmployeeSalesTargetQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(salesTargetService.query(queryForm, SmartRequestUtil.getRequestUser()));
    }

    @ApiOperation("员工业绩目标-更新 by lidoudou")
    @PostMapping("/employee/salesTarget/update")
    public ResponseDTO<String> updateSalesTarget(@RequestBody @Valid EmployeeSalesTargetUpdateForm updateForm) {
        RequestUser employee = SmartRequestUtil.getRequestUser();
        updateForm.setUpdateId(employee.getUserId());
        updateForm.setUpdateName(employee.getUserName());
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return salesTargetService.update(updateForm);
    }

    @ApiOperation("销售日报表 by zhaoxinyang")
    @PostMapping("/sale/day/statistic/query")
    public ResponseDTO<List<SaleDayStatisticVO>> querySaleDayList(@RequestBody @Valid SalesDayStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return salesTargetService.querySaleDayStatisticList(queryForm);
    }

    @ApiOperation("销售日报表-导出 by zhaoxinyang")
    @GetMapping("/sale/day/statistic/export")
    public void exportSaleDayList(SalesDayStatisticQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<SaleDayStatisticExportVO> exportVOList = salesTargetService.exportSaleDayStatisticList(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "销售日报表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportVOList);
        map.put(NormalExcelConstants.CLASS, SaleDayStatisticExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation("客服日报表 by zhaoxinyang")
    @PostMapping("/customerService/day/statistic/query")
    public ResponseDTO<List<CustomerServiceDayStatisticVO>> queryCustomerServiceDayList(@RequestBody @Valid CustomerServiceDayStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return salesTargetService.queryCustomerServiceDayList(queryForm);
    }

    @ApiOperation("客服日报表-导出 by zhaoxinyang")
    @GetMapping("/customerService/day/statistic/export")
    public void exportCustomerServiceDayList(CustomerServiceDayStatisticQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<CustomerServiceDayStatisticExportVO> exportVOList = salesTargetService.exportCustomerServiceDayList(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "客服日报表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportVOList);
        map.put(NormalExcelConstants.CLASS, CustomerServiceDayStatisticExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation("调度日报表 by zhaoxinyang")
    @PostMapping("/schedule/day/statistic/query")
    public ResponseDTO<List<ScheduleDayStatisticVO>> queryScheduleDayList(@RequestBody @Valid ScheduleDayStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return salesTargetService.queryScheduleDayList(queryForm);
    }

    @ApiOperation("调度日报表-导出 by zhaoxinyang")
    @GetMapping("/schedule/day/statistic/export")
    public void exportScheduleDayList(ScheduleDayStatisticQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ScheduleDayStatisticExportVO> exportVOList = salesTargetService.exportScheduleDayList(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "调度日报表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportVOList);
        map.put(NormalExcelConstants.CLASS, ScheduleDayStatisticExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}
