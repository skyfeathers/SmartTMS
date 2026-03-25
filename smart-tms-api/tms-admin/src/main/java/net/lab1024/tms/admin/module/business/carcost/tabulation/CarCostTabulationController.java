package net.lab1024.tms.admin.module.business.carcost.tabulation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.carcost.tabulation.domain.*;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationSimpleVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
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
@Api(tags = {AdminSwaggerTagConst.Business.CAR_COST_TABULATION})
public class CarCostTabulationController {

    @Resource
    private CarCostTabulationService carCostTabulationService;

    @ApiOperation(value = "自有车-费用列表-简单列表 @author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/simple/list")
    public ResponseDTO<List<CarCostTabulationSimpleVO>> simpleList() {
        return carCostTabulationService.simpleList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "自有车-费用列表-分页查询 @author zhaoxinyang")
    @PostMapping("/car/cost/tabulation/page")
    public ResponseDTO<PageResult<CarCostTabulationVO>> page(@RequestBody @Valid CarCostTabulationQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return carCostTabulationService.page(queryForm);
    }

    @ApiOperation(value = "自有车-费用列表 @author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/list/{waybillId}")
    public ResponseDTO<List<CarCostTabulationVO>> queryList(@PathVariable Long waybillId) {
        return carCostTabulationService.queryList(waybillId);
    }

    @ApiOperation(value = "自有车-费用列表-详情 @author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/detail/{moduleId}/{moduleType}")
    public ResponseDTO<CarCostTabulationDetailVO> detail(@PathVariable Long moduleId, @PathVariable Integer moduleType) {
        return carCostTabulationService.detail(moduleId, moduleType);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-费用列表-审核 @author zhaoxinyang")
    @PostMapping("/car/cost/tabulation/audit")
    public ResponseDTO<String> audit(@RequestBody @Valid CarCostTabulationAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostTabulationService.audit(auditForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-费用列表-批量审核 @author zhaoxinyang")
    @PostMapping("/car/cost/tabulation/batch/audit")
    public ResponseDTO<String> batchAudit(@RequestBody @Valid CarCostTabulationBatchAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostTabulationService.batchAudit(auditForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-费用列表-关联运单 @author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/relate/waybill/{tabulationId}/{waybillId}")
    public ResponseDTO<String> relateWaybill(@PathVariable Long tabulationId, @PathVariable Long waybillId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostTabulationService.relateWaybill(tabulationId, waybillId, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-费用列表-批量关联运单 @author zhaoxinyang")
    @PostMapping("/car/cost/tabulation/batch/relate/waybill")
    public ResponseDTO<String> batchRelateWaybill(@RequestBody @Valid CarCostTabulationBatchRelateForm relateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostTabulationService.batchRelateWaybill(relateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-费用列表-取消关联运单 author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/cancel/relate/waybill/{tabulationId}")
    public ResponseDTO<String> cancelRelateWaybill(@PathVariable Long tabulationId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostTabulationService.cancelRelateWaybill(tabulationId, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-费用列表-批量取消关联运单 @author zhaoxinyang")
    @PostMapping("/car/cost/tabulation/batch/cancel/relate/waybill")
    public ResponseDTO<String> batchCancelRelateWaybill(@RequestBody @Valid CarCostTabulationBatchCancelRelateForm relateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostTabulationService.batchCancelRelateWaybill(relateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "自有车-费用列表-删除 author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/del/{tabulationId}")
    public ResponseDTO<String> del(@PathVariable Long tabulationId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return carCostTabulationService.del(tabulationId, dataTracerRequestForm);
    }

}
