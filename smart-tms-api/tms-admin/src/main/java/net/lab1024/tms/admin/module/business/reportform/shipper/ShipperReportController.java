package net.lab1024.tms.admin.module.business.reportform.shipper;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperAddQueryForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperDayStatisticForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperOrderQueryForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo.*;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


/**
 * 货主报表统计
 *
 * @author lidoudou
 * @date 2022/9/20 下午5:16
 */
@Api(tags = AdminSwaggerTagConst.Business.SHIPPER_REPORT)
@RestController
public class ShipperReportController {

    @Autowired
    private ShipperReportService shipperReportService;

    @ApiOperation(value = "查询客户每月订单量统计 @author lidoudou")
    @PostMapping("/shipper/order/month/statistic")
    public ResponseDTO<PageResult<ShipperOrderVO>> queryOrderByMonth(@RequestBody @Valid ShipperOrderQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperReportService.queryOrderByMonth(queryForm);
    }

    @ApiOperation(value = "查询客户每月订单量统计-合计 @author lidoudou")
    @PostMapping("/shipper/order/month/statistic/total")
    public ResponseDTO<List<ShipperCountMonthVO>> queryOrderByMonthTotal(@RequestBody @Valid ShipperOrderQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperReportService.queryOrderByMonthTotal(queryForm, SmartRequestUtil.getRequestUserId());
    }


    @ApiOperation(value = "查询客户每月运单量统计 @author lidoudou")
    @PostMapping("/shipper/waybill/month/statistic")
    public ResponseDTO<PageResult<ShipperOrderVO>> queryWaybillByMonth(@RequestBody @Valid ShipperOrderQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperReportService.queryWaybillByMonth(queryForm);
    }

    @ApiOperation(value = "查询客户每月运单量统计-合计 @author lidoudou")
    @PostMapping("/shipper/waybill/month/statistic/total")
    public ResponseDTO<List<ShipperCountMonthVO>> queryWaybillByMonthTotal(@RequestBody @Valid ShipperOrderQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperReportService.queryWaybillByMonthTotal(queryForm);
    }

    @ApiOperation(value = "客户每月订单量统计导出")
    @GetMapping("/shipper/order/month/statistic/export")
    public void orderByMonthExport(ShipperOrderQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        ResponseDTO<List<ShipperOrderExcelVO>> listResp = shipperReportService.orderCountExport(queryForm);
        if (!listResp.getOk()) {
            throw new RuntimeException(listResp.getMsg());
        }
        ExportParams exportParams = new ExportParams();
        String title = queryForm.getYear() + "年客户每月订单量统计";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, listResp.getData());
        map.put(NormalExcelConstants.CLASS, ShipperOrderExcelVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "客户每月运单量统计导出")
    @GetMapping("/shipper/waybill/month/statistic/export")
    public void waybillByMonthExport(ShipperOrderQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        ResponseDTO<List<ShipperOrderExcelVO>> listResp = shipperReportService.waybillCountExport(queryForm);
        if (!listResp.getOk()) {
            throw new RuntimeException(listResp.getMsg());
        }
        ExportParams exportParams = new ExportParams();
        String title = queryForm.getYear() + "年客户每月运单量统计";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, listResp.getData());
        map.put(NormalExcelConstants.CLASS, ShipperOrderExcelVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "查询客户每月新增统计 @author lidoudou")
    @PostMapping("/shipper/new/month/statistic")
    public ResponseDTO<List<ShipperCountMonthVO>> queryNewCountByMonth(@RequestBody @Valid ShipperAddQueryForm addQueryForm) {
        addQueryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperReportService.queryNewCountByMonth(addQueryForm);
    }

    @ApiOperation("货主日报表 by zhaoxinyang")
    @PostMapping("/shipper/day/statistic/query")
    public ResponseDTO<PageResult<ShipperDayStatisticVO>> queryShipperDayList(@RequestBody @Valid ShipperDayStatisticForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperReportService.queryShipperDayList(queryForm);
    }

    @ApiOperation("货主日报表-导出 by zhaoxinyang")
    @GetMapping("/shipper/day/statistic/export")
    public void exportShipperDayList(ShipperDayStatisticForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ShipperDayStatisticExportVO> exportVOList = shipperReportService.exportShipperDayList(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "货主日报表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportVOList);
        map.put(NormalExcelConstants.CLASS, ShipperDayStatisticExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}
