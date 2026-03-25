package net.lab1024.tms.admin.module.business.carcost.cashreceive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashReceiveAddForm;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.domain.CarCostCashReceiveAuditForm;
import net.lab1024.tms.common.module.business.carcost.domain.form.CarCostCashReceiveUpdateForm;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.domain.CarCostCashReceiveVO;
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
@Api(tags = {AdminSwaggerTagConst.Business.CAR_COST_RECEIVE})
public class CarCostCashReceiveController {

    @Resource
    private CarCostCashReceiveService carCostCashReceiveService;

    @ApiOperation(value = "自有车-现金收入-列表 @author zhaoxinyang")
    @GetMapping("/car/cost/cashReceive/list/{waybillId}")
    public ResponseDTO<List<CarCostCashReceiveVO>> list(@PathVariable("waybillId") Long waybillId) {
        return carCostCashReceiveService.list(waybillId);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-现金收入-新建 @author zhaoxinyang")
    @PostMapping("/car/cost/cashReceive/add")
    public ResponseDTO<String> add(@RequestBody @Valid CarCostCashReceiveAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostCashReceiveService.add(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-现金收入-编辑 @author zhaoxinyang")
    @PostMapping("/car/cost/cashReceive/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostCashReceiveUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostCashReceiveService.update(updateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-现金收入-审核 @author zhaoxinyang")
    @PostMapping("/car/cost/cashReceive/audit")
    public ResponseDTO<String> audit(@RequestBody @Valid CarCostCashReceiveAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostCashReceiveService.audit(auditForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-现金收入-删除 @author zhaoxinyang")
    @GetMapping("/car/cost/cashReceive/del/{cashReceiveId}")
    public ResponseDTO<String> del(@PathVariable Long cashReceiveId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostCashReceiveService.del(cashReceiveId, dataTracerRequestForm);
    }

}
