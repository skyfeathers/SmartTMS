package net.lab1024.tms.driver.module.business.carcost.oilcardreceive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilCardReceiveAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilCardReceiveUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.carcost.oilcardreceive.domain.CarCostOilCardReceiveVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {DriverSwaggerTagConst.Business.CAR_COST})
public class CarCostOilCardReceiveController {

    @Resource
    private CarCostOilCardReceiveService carCostOilCardReceiveService;

    @ApiOperation(value = "自有车-油卡收入-列表 @author zhaoxinyang")
    @GetMapping("/car/cost/oilCardReceive/list/{waybillId}")
    public ResponseDTO<List<CarCostOilCardReceiveVO>> list(@PathVariable("waybillId") Long waybillId) {
        return carCostOilCardReceiveService.list(waybillId);
    }

    @ApiOperation(value = "自有车-油卡收入-详情 @author zhaoxinyang")
    @GetMapping("/car/cost/oilCardReceive/detail/{oilCardReceiveId}")
    public ResponseDTO<CarCostTabulationDetailVO> detail(@PathVariable("oilCardReceiveId") Long oilCardReceiveId) {
        return carCostOilCardReceiveService.detail(oilCardReceiveId);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-油卡收入-新建 @author zhaoxinyang")
    @PostMapping("/car/cost/oilCardReceive/add")
    public ResponseDTO<String> add(@RequestBody @Valid CarCostOilCardReceiveAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostOilCardReceiveService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-油卡收入-编辑 @author zhaoxinyang")
    @PostMapping("/car/cost/oilCardReceive/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostOilCardReceiveUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostOilCardReceiveService.update(updateForm, dataTracerRequestForm);
    }

}
