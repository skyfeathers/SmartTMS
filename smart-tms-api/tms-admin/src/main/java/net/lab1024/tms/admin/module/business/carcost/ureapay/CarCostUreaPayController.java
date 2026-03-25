package net.lab1024.tms.admin.module.business.carcost.ureapay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostUreaPayAddForm;
import net.lab1024.tms.admin.module.business.carcost.ureapay.domain.CarCostUreaPayAuditForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostUreaPayUpdateForm;
import net.lab1024.tms.admin.module.business.carcost.ureapay.domain.CarCostUreaPayVO;
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
public class CarCostUreaPayController {

    @Resource
    private CarCostUreaPayService carCostUreaPayService;

    @ApiOperation(value = "自有车-尿素支出-列表 @author zhaoxinyang")
    @GetMapping("/car/cost/ureaPay/list/{waybillId}")
    public ResponseDTO<List<CarCostUreaPayVO>> list(@PathVariable("waybillId") Long waybillId) {
        return carCostUreaPayService.list(waybillId);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-尿素支出-新建 @author zhaoxinyang")
    @PostMapping("/car/cost/ureaPay/add")
    public ResponseDTO<String> add(@RequestBody @Valid CarCostUreaPayAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostUreaPayService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-尿素支出-编辑 @author zhaoxinyang")
    @PostMapping("/car/cost/ureaPay/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostUreaPayUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostUreaPayService.update(updateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-尿素支出-审核 @author zhaoxinyang")
    @PostMapping("/car/cost/ureaPay/audit")
    public ResponseDTO<String> audit(@RequestBody @Valid CarCostUreaPayAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostUreaPayService.audit(auditForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-尿素支出-删除 @author zhaoxinyang")
    @GetMapping("/car/cost/ureaPay/del/{ureaPayId}")
    public ResponseDTO<String> del(@PathVariable Long ureaPayId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostUreaPayService.del(ureaPayId, dataTracerRequestForm);
    }

}
