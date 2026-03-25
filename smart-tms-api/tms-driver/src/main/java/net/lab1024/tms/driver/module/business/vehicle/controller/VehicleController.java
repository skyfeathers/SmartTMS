package net.lab1024.tms.driver.module.business.vehicle.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverVehicleQueryForm;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverVehicleQueryVO;
import net.lab1024.tms.driver.module.business.vehicle.domain.form.VehicleAddForm;
import net.lab1024.tms.driver.module.business.vehicle.domain.form.VehicleUpdateForm;
import net.lab1024.tms.driver.module.business.vehicle.domain.vo.VehicleDetailVO;
import net.lab1024.tms.driver.module.business.vehicle.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = {DriverSwaggerTagConst.Business.VEHICLE})
public class VehicleController {

    @Resource
    private VehicleService vehicleService;

    @ApiOperation(value = "查询司机创建的车辆 @author zhaoxinyang")
    @PostMapping("/vehicle/page/query")
    public ResponseDTO<PageResult<DriverVehicleQueryVO>> getVehicleListByDriverId(@RequestBody @Valid DriverVehicleQueryForm queryForm) {
        RequestUser driver = SmartRequestUtil.getRequestUser();
        return ResponseDTO.ok(vehicleService.getVehicleListByDriverId(queryForm, driver));
    }

    @ApiOperation(value = "查询司机关联的车辆 @author zhaoxinyang")
    @GetMapping("/vehicle/relate/list")
    public ResponseDTO<List<DriverVehicleQueryVO>> relateVehicleList() {
        return vehicleService.relateVehicleList(SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation(value = "查询车辆详情 @author zhaoxinyang")
    @GetMapping("/vehicle/detail/{vehicleId}")
    public ResponseDTO<VehicleDetailVO> getDetail(@PathVariable("vehicleId") Long vehicleId) {
        return vehicleService.getDetail(vehicleId);
    }

    @RepeatSubmit
    @ApiOperation(value = "保存车辆 @author zhaoxinyang")
    @PostMapping("/vehicle/save")
    public ResponseDTO<String> save(@RequestBody @Valid VehicleAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑车辆 @author zhaoxinyang")
    @PostMapping("/vehicle/update")
    public ResponseDTO<String> update(@RequestBody @Valid VehicleUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return vehicleService.update(updateForm, dataTracerRequestForm);
    }

}
