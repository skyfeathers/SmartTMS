package net.lab1024.tms.admin.module.business.carcost.basicinfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoForm;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoQueryForm;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoVO;
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
@Api(tags = {AdminSwaggerTagConst.Business.CAR_COST_BASIC_INFO})
public class CarCostBasicInfoController {

    @Resource
    private CarCostBasicInfoService carCostBasicInfoService;

    @ApiOperation(value = "自有车-基本信息-分页 @author zhaoxinyang")
    @PostMapping("/car/cost/basicInfo/page")
    public ResponseDTO<PageResult<CarCostBasicInfoVO>> page(@RequestBody @Valid CarCostBasicInfoQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostBasicInfoService.page(queryForm);
    }

    @ApiOperation(value = "自有车-基本信息-详情 @author zhaoxinyang")
    @GetMapping("/car/cost/basicInfo/detail/{waybillId}")
    public ResponseDTO<CarCostBasicInfoVO> detail(@PathVariable("waybillId") Long waybillId) {
        return carCostBasicInfoService.detail(waybillId);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-基本信息-编辑 @author zhaoxinyang")
    @PostMapping("/car/cost/basicInfo/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostBasicInfoForm carCostBasicInfoForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        carCostBasicInfoForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostBasicInfoService.update(carCostBasicInfoForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-基本信息-确认状态更新 @author zhaoxinyang")
    @GetMapping("/car/cost/basicInfo/confirm/update/{basicInfoId}/{confirmFlag}")
    public ResponseDTO<String> updateConfirm(@PathVariable("basicInfoId") Long basicInfoId, @PathVariable("confirmFlag") Boolean confirmFlag, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostBasicInfoService.updateConfirm(basicInfoId, confirmFlag, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-基本信息-删除 @author zhaoxinyang")
    @GetMapping("/car/cost/basicInfo/del/{basicInfoId}")
    public ResponseDTO<String> del(@PathVariable("basicInfoId") Long basicInfoId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostBasicInfoService.del(basicInfoId, dataTracerRequestForm);
    }

}