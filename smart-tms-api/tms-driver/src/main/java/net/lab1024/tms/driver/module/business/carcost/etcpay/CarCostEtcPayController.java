package net.lab1024.tms.driver.module.business.carcost.etcpay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostEtcPayAddForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostEtcPayUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.carcost.etcpay.domain.CarCostEtcPayVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {DriverSwaggerTagConst.Business.CAR_COST})
public class CarCostEtcPayController {

    @Resource
    private CarCostEtcPayService carCostEtcPayService;

    @ApiOperation(value = "自有车-ETC支出-列表 @author zhaoxinyang")
    @GetMapping("/car/cost/etcPay/list/{waybillId}")
    public ResponseDTO<List<CarCostEtcPayVO>> list(@PathVariable("waybillId") Long waybillId) {
        return carCostEtcPayService.list(waybillId);
    }

    @ApiOperation(value = "自有车-ETC支出-详情 @author zhaoxinyang")
    @GetMapping("/car/cost/etcPay/detail/{etcPayId}")
    public ResponseDTO<CarCostTabulationDetailVO> detail(@PathVariable("etcPayId") Long etcPayId) {
        return carCostEtcPayService.detail(etcPayId);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-ETC支出-新建 @author zhaoxinyang")
    @PostMapping("/car/cost/etcPay/add")
    public ResponseDTO<String> add(@RequestBody @Valid CarCostEtcPayAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostEtcPayService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-ETC支出-编辑 @author zhaoxinyang")
    @PostMapping("/car/cost/etcPay/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostEtcPayUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostEtcPayService.update(updateForm, dataTracerRequestForm);
    }

}
