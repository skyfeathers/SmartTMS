package net.lab1024.tms.admin.module.business.reportform.carcost;


import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostDayOrMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostDayStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.*;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 自有车费用报表
 *
 * @author lidoudou
 * @date 2023/4/8 上午8:58
 */
@RestController
@Api(tags = AdminSwaggerTagConst.Business.CAR_COST_REPORT)
public class CarCostReportController {

    @Autowired
    private CarCostReportNewService carCostReportNewService;


    @ApiOperation(value = "自有车车辆日统计查询 @author zhaoxinyang")
    @PostMapping("/car/cost/day/statistic/query")
    public ResponseDTO<List<CarCostDayReportVO>> queryDayStatisticsReport(@RequestBody @Valid CarCostDayStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostReportNewService.queryDayCarCost(queryForm);
    }

    @NoNeedLogin
    @ApiOperation(value = "自有车费用日/月费用统计 @author zhaoxinyang")
    @PostMapping("/car/cost/dayOrMonth/statistic")
    public ResponseDTO<List<CarCostDayOrMonthStatisticVO>> dayOrMonthStatistic(@RequestBody @Valid CarCostDayOrMonthStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostReportNewService.dayOrMonthStatistic(queryForm);
    }

    @NoNeedLogin
    @ApiOperation(value = "自有车车辆日统计查询 @author lidoudou")
    @GetMapping("/car/cost/day/statistics/export")
    public void exportDayStatisticsReport(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response, CarCostDayStatisticQueryForm queryForm) throws IOException {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        Workbook workbook = carCostReportNewService.exportDayStatisticsReport(queryForm);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + new String("日统计报表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
        workbook.write(response.getOutputStream());
    }

    @NoNeedLogin
    @ApiOperation(value = "自有车车辆费用流水 by zhaoxinyang")
    @GetMapping("/car/cost/flow/export")
    public void export(CarCostDayStatisticQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<CarCostFlowExportVO> list = carCostReportNewService.costFlowExport(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "车辆费用流水";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, CarCostFlowExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    @ApiOperation(value = "自有车车辆月报表统计查询 @author lidoudou")
    @PostMapping("/car/cost/month/statistic/query")
    public ResponseDTO<List<CarCostMonthStatisticVO>> queryMonthStatisticsReport(@RequestBody @Valid CarCostMonthStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostReportNewService.queryMonthStatisticsReport(queryForm);
    }

    @NoNeedLogin
    @ApiOperation(value = "自有车月报表统计导出 by lidoudou")
    @GetMapping("/car/cost/month/statistic/export")
    public void exportCarCostMonthStatistic(CarCostMonthStatisticQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        ResponseDTO<List<CarCostMonthStatisticVO>> responseDTO = carCostReportNewService.queryMonthStatisticsReport(queryForm);
        if (!responseDTO.getOk()) {
            throw new BusinessException(responseDTO.getMsg());
        }
        ExportParams exportParams = new ExportParams();
        String title = "车辆费用流水";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, responseDTO.getData());
        map.put(NormalExcelConstants.CLASS, CarCostMonthStatisticVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
