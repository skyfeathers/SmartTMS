package net.lab1024.tms.admin.module.business.carcost.oilpay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilPayAddForm;
import net.lab1024.tms.admin.module.business.carcost.oilpay.domain.CarCostOilPayAuditForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostOilPayUpdateForm;
import net.lab1024.tms.admin.module.business.carcost.oilpay.domain.CarCostOilPayVO;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.CAR_COST_PAY})
public class CarCostOilPayController {

    @Resource
    private CarCostOilPayService carCostOilPayService;

    @ApiOperation(value = "自有车-油费支出-列表 @author zhaoxinyang")
    @GetMapping("/car/cost/oilPay/list/{waybillId}")
    public ResponseDTO<List<CarCostOilPayVO>> list(@PathVariable("waybillId") Long waybillId) {
        return carCostOilPayService.list(waybillId);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-油费支出-新建 @author zhaoxinyang")
    @PostMapping("/car/cost/oilPay/add")
    public ResponseDTO<String> add(@RequestBody @Valid CarCostOilPayAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostOilPayService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-油费支出-编辑 @author zhaoxinyang")
    @PostMapping("/car/cost/oilPay/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostOilPayUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostOilPayService.update(updateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-油费支出-审核 @author zhaoxinyang")
    @PostMapping("/car/cost/oilPay/audit")
    public ResponseDTO<String> audit(@RequestBody @Valid CarCostOilPayAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostOilPayService.audit(auditForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-油费支出-删除 @author zhaoxinyang")
    @GetMapping("/car/cost/oilPay/del/{oilPayId}")
    public ResponseDTO<String> del(@PathVariable Long oilPayId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostOilPayService.del(oilPayId, dataTracerRequestForm);
    }

}
