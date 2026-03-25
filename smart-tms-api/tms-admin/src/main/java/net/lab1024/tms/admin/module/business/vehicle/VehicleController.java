package net.lab1024.tms.admin.module.business.vehicle;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverBusinessModeUpdateForm;
import net.lab1024.tms.admin.module.business.vehicle.domain.form.*;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleDetailVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleExportVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleSimpleVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleVO;
import net.lab1024.tms.admin.module.business.vehicle.service.VehicleService;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
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

/**
 * 车辆
 *
 * @author lidoudou
 * @date 2022/11/21 下午2:51
 */
@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.VEHICLE})
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @NoNeedLogin
    @ApiOperation(value = "查询所有有效车辆 @author zhuoda")
    @GetMapping("/vehicle/getAll")
    public ResponseDTO<List<VehicleSimpleVO>> getAll(@RequestParam(value = "driverId", required = false) Long driverId) {
        return vehicleService.getAll(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), driverId);
    }

    @ApiOperation(value = "查询自有车费用所用的车辆列表 @author zhuoda")
    @GetMapping("/vehicle/carCostVehicleList")
    public ResponseDTO<List<VehicleSimpleVO>> getCarCostVehicleList() {
        return vehicleService.getCarCostVehicleList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }


    @ApiOperation(value = "分页查询车辆 @author lidoudou")
    @PostMapping("/vehicle/page/query")
    public ResponseDTO<PageResult<VehicleVO>> queryByPage(@RequestBody @Valid VehicleQueryForm queryDTO) {
        return vehicleService.queryByPage(queryDTO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "车辆详情 @author lidoudou")
    @GetMapping("/vehicle/detail/{vehicleId}")
    public ResponseDTO<VehicleDetailVO> detail(@PathVariable Long vehicleId) {
        return vehicleService.getDetail(vehicleId, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @RepeatSubmit
    @ApiOperation(value = "新建车辆 @author lidoudou")
    @PostMapping("/vehicle/save")
    public ResponseDTO<String> add(@RequestBody @Valid VehicleAddForm addDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.save(addDTO, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑经营模式 @author lidoudou")
    @PostMapping("/vehicle/businessMode/update")
    public ResponseDTO<String> businessModeUpdate(@RequestBody @Valid VehicleBusinessModeUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.businessModeUpdate(updateForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑车辆 @author lidoudou")
    @PostMapping("/vehicle/update")
    public ResponseDTO<String> update(@RequestBody @Valid VehicleUpdateForm updateDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.update(updateDTO, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @ApiOperation(value = "车辆删除 @author lidoudou")
    @PostMapping("/vehicle/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody @Valid ValidateList<Long> vehicleIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.batchDelete(vehicleIdList, dataTracerRequestForm);
    }

    @ApiOperation(value = "车辆启用 @author lidoudou")
    @PostMapping("/vehicle/batchEnabled")
    public ResponseDTO<String> batchEnabled(@RequestBody @Valid ValidateList<Long> vehicleIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.batchUpdateDisabled(vehicleIdList, Boolean.TRUE, dataTracerRequestForm);
    }

    @ApiOperation(value = "车辆禁用 @author lidoudou")
    @PostMapping("/vehicle/batchDisabled")
    public ResponseDTO<String> batchDisabled(@RequestBody @Valid ValidateList<Long> vehicleIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.batchUpdateDisabled(vehicleIdList, Boolean.FALSE, dataTracerRequestForm);
    }

    @ApiOperation("批量审核 by lidoudou")
    @PostMapping("/vehicle/batch/audit")
    public ResponseDTO<String> batchAudit(@RequestBody @Valid VehicleBatchAuditForm batchAuditDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.batchAudit(batchAuditDTO, dataTracerRequestForm);
    }

    @ApiOperation("批量修改负责人 by lidoudou")
    @PostMapping("/vehicle/manager/update")
    public ResponseDTO<String> batchUpdateManager(@RequestBody @Valid VehicleManagerUpdateForm updateManagerForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.batchUpdateManager(updateManagerForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "车辆导出 by lidoudou")
    @GetMapping("/vehicle/export")
    public void export(VehicleQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<VehicleExportVO> list = vehicleService.queryByExport(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        ExportParams exportParams = new ExportParams();
        String title = "车辆列表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, VehicleExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
