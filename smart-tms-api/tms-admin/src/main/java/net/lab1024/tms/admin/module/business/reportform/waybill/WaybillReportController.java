package net.lab1024.tms.admin.module.business.reportform.waybill;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.EmployeeSalesQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.OilCardRateQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.SalesReceiveAmountStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.WaybillVehicleCountQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.form.CustomerWaybillCountQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.form.ShipperProfitPageQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo.*;
import net.lab1024.tms.admin.module.business.waybill.domain.form.WaybillQueryForm;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillCostDetailExcelVO;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillProfitVO;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.PageResult;
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

/**
 * 运单报表统计
 *
 * @author lidoudou
 * @date 2022/9/23 下午5:50
 */
@Api(tags = AdminSwaggerTagConst.Business.WAYBILL_REPORT)
@RestController
public class WaybillReportController {

    @Autowired
    private WaybillReportService waybillReportService;

    @ApiOperation(value = "查询运单利润表 @author lidoudou")
    @PostMapping("/waybill/profit/query")
    public ResponseDTO<List<WaybillProfitVO>> queryWaybillProfit(@RequestBody @Valid WaybillQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<WaybillProfitVO> costCategoryAmountVOList = waybillReportService.queryList(queryForm, SmartRequestUtil.getRequestUserId());
        return ResponseDTO.ok(costCategoryAmountVOList);
    }


    @ApiOperation(value = "导出")
    @GetMapping("/waybill/profit/export")
    public void export(WaybillQueryForm exportForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        exportForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<WaybillCostDetailExcelVO> excelVOList = waybillReportService.query4Export(exportForm, SmartRequestUtil.getRequestUserId());
        ExportParams exportParams = new ExportParams();
        String title = "运单利润表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, excelVOList);
        map.put(NormalExcelConstants.CLASS, WaybillCostDetailExcelVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "查询货主毛利 @author lidoudou")
    @PostMapping("/waybill/shipper/profit/query")
    public ResponseDTO<PageResult<ShipperProfitVO>> queryShipperProfit(@RequestBody @Valid ShipperProfitPageQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        PageResult<ShipperProfitVO> costCategoryAmountVOList = waybillReportService.pageShipperProfit(queryForm);
        return ResponseDTO.ok(costCategoryAmountVOList);
    }

    @ApiOperation(value = "查询货主毛利 @author lidoudou")
    @PostMapping("/waybill/shipper/profit/summary")
    public ResponseDTO<ShipperProfitSummaryVO> queryShipperProfitSummary(@RequestBody @Valid ShipperProfitPageQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return waybillReportService.queryShipperProfitSummary(queryForm);
    }

    @NoNeedLogin
    @ApiOperation(value = "导出货主毛利 @author lidoudou")
    @GetMapping("/waybill/shipper/profit/export")
    public void exportWaybillShipperProfit(ShipperProfitPageQueryForm exportForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        exportForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ShipperProfitVO> excelVOList = waybillReportService.queryShipperProfitList(exportForm, null);
        ExportParams exportParams = new ExportParams();
        String title = "客户毛利表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, excelVOList);
        map.put(NormalExcelConstants.CLASS, ShipperProfitVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "查询客服收入成本 @author lidoudou")
    @PostMapping("/waybill/customerService/amount/query")
    public ResponseDTO<List<WaybillEmployeeAmountStatisticVO>> queryAmountByCustomerService(@RequestBody @Valid EmployeeSalesQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(waybillReportService.queryAmountByCustomerService(queryForm));
    }
    @ApiOperation(value = "导出客服收入成本 @author lidoudou")
    @GetMapping("/waybill/customerService/amount/export")
    public void exportAmountByCustomerService(EmployeeSalesQueryForm exportForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        exportForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<WaybillEmployeeAmountStatisticVO> excelVOList = waybillReportService.queryAmountByCustomerService(exportForm);
        ExportParams exportParams = new ExportParams();
        String title = "客服收入成本明细表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, excelVOList);
        map.put(NormalExcelConstants.CLASS, WaybillEmployeeAmountStatisticVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "查询业务收入成本 @author lidoudou")
    @PostMapping("/waybill/sale/amount/query")
    public ResponseDTO<List<WaybillEmployeeAmountStatisticVO>> queryAmountBySale(@RequestBody @Valid EmployeeSalesQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(waybillReportService.queryAmountBySale(queryForm));
    }

    @ApiOperation(value = "导出业务收入成本 @author lidoudou")
    @GetMapping("/waybill/sale/amount/export")
    public void exportAmountBySale(EmployeeSalesQueryForm exportForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        exportForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<WaybillEmployeeAmountStatisticVO> excelVOList = waybillReportService.queryAmountBySale(exportForm);
        ExportParams exportParams = new ExportParams();
        String title = "销售收入成本明细表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, excelVOList);
        map.put(NormalExcelConstants.CLASS, WaybillEmployeeAmountStatisticVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "查询外调车油卡比例表 @author lidoudou")
    @PostMapping("/waybill/oilCard/rate/query")
    public ResponseDTO<List<EmployeeOilCardRateVO>> queryOilCardRate(@RequestBody @Valid OilCardRateQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(waybillReportService.queryOilCard(queryForm));
    }

    @ApiOperation(value = "导出外调车油卡比例表 @author lidoudou")
    @GetMapping("/waybill/oilCard/rate/export")
    public void exportOilCardRate(OilCardRateQueryForm exportForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        exportForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<EmployeeOilCardRateVO> excelVOList = waybillReportService.queryOilCard(exportForm);
        ExportParams exportParams = new ExportParams();
        String title = "外调车油卡比例表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, excelVOList);
        map.put(NormalExcelConstants.CLASS, EmployeeOilCardRateVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "查询销售应收金额统计 @author lidoudou")
    @PostMapping("/waybill/sale/receiveAmount/query")
    public ResponseDTO<List<ReceiveAmountStatisticVO>> queryReceiveAmountBySale(@RequestBody @Valid SalesReceiveAmountStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return waybillReportService.queryReceiveAmountBySale(queryForm);
    }

    @ApiOperation(value = "导出应收金额统计 @author yongqi.wu")
    @GetMapping("/waybill/sale/receiveAmount/export")
    public void exportReceiveAmountBySale(SalesReceiveAmountStatisticQueryForm queryForm,HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ReceiveAmountStatisticVO> data = waybillReportService.queryReceiveAmountBySale(queryForm).getData();
        ExportParams exportParams = new ExportParams();
        String title = "应收金额统计";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, data);
        map.put(NormalExcelConstants.CLASS, ReceiveAmountStatisticVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    @ApiOperation(value = "车辆运单统计 @author lidoudou")
    @PostMapping("/waybill/vehicle/count/query")
    public ResponseDTO<PageResult<WaybillVehicleCountVO>> queryWaybillVehicleCount(@RequestBody @Valid WaybillVehicleCountQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return waybillReportService.queryWaybillVehicleCount(queryForm);
    }

    @ApiOperation(value = "导出车辆运单统计 @author lidoudou")
    @GetMapping("/waybill/vehicle/count/export")
    public void exportOilCardRate(WaybillVehicleCountQueryForm exportForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        exportForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<WaybillVehicleCountExportVO> excelVOList = waybillReportService.exportWaybillVehicleCount(exportForm);
        ExportParams exportParams = new ExportParams();
        String title = "车辆运单统计";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, excelVOList);
        map.put(NormalExcelConstants.CLASS, WaybillVehicleCountExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "客服单量统计列表 by lidoudou")
    @PostMapping("/waybill/count/list")
    public ResponseDTO<List<WaybillCountVO>> queryWaybillCount(@RequestBody @Valid CustomerWaybillCountQueryForm exportForm) {
        exportForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<WaybillCountVO> waybillCountVOList = waybillReportService.queryCustomerWaybillCountList(exportForm);
        return ResponseDTO.ok(waybillCountVOList);
    }

}
