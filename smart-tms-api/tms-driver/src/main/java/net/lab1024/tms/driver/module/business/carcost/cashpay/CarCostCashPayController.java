package net.lab1024.tms.driver.module.business.carcost.cashpay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashPayUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.carcost.cashpay.domain.CarCostCashPayVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {DriverSwaggerTagConst.Business.CAR_COST})
public class CarCostCashPayController {

    @Resource
    private CarCostCashPayService carCostCashPayService;

    @ApiOperation(value = "自有车-现金支出-列表 @author zhaoxinyang")
    @GetMapping("/car/cost/cashPay/list/{waybillId}")
    public ResponseDTO<List<CarCostCashPayVO>> list(@PathVariable("waybillId") Long waybillId) {
        return carCostCashPayService.list(waybillId);
    }

    @ApiOperation(value = "自有车-现金支出-详情 @author zhaoxinyang")
    @GetMapping("/car/cost/cashPay/detail/{cashPayId}")
    public ResponseDTO<CarCostTabulationDetailVO> detail(@PathVariable("cashPayId") Long cashPayId) {
        return carCostCashPayService.detail(cashPayId);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-现金支出-新建 @author zhaoxinyang")
    @PostMapping("/car/cost/cashPay/add")
    public ResponseDTO<String> add(@RequestBody @Valid CarCostCashPayAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostCashPayService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-现金支出-编辑 @author zhaoxinyang")
    @PostMapping("/car/cost/cashPay/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostCashPayUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostCashPayService.update(updateForm, dataTracerRequestForm);
    }

}
