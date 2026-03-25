package net.lab1024.tms.admin.module.business.oa.cost.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.oa.cost.constant.CostApplyStatusEnum;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyAddForm;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyQueryForm;
import net.lab1024.tms.admin.module.business.oa.cost.domain.vo.CostApplyDetailVO;
import net.lab1024.tms.admin.module.business.oa.cost.domain.vo.CostApplyVO;
import net.lab1024.tms.admin.module.business.oa.cost.service.CostApplyService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:43
 */
@RestController
@Api(tags = AdminSwaggerTagConst.Business.OA_COST)
public class CostApplyController {

    @Autowired
    private CostApplyService costApplyService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/oa/cost/apply/queryPage")
    public ResponseDTO<PageResult<CostApplyVO>> queryPage(@RequestBody @Valid CostApplyQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(costApplyService.queryPage(queryForm));
    }

    @ApiOperation("添加 @author lidoudou")
    @PostMapping("/oa/cost/apply/add")
    public ResponseDTO<String> add(@RequestBody @Valid CostApplyAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return costApplyService.add(addForm, dataTracerRequestForm);
    }


    @ApiOperation("详情 @author lidoudou")
    @GetMapping("/oa/cost/apply/getDetail/{applyId}")
    public ResponseDTO<CostApplyDetailVO> getDetail(@PathVariable Long applyId) {
        return ResponseDTO.ok(costApplyService.getDetail(applyId));
    }

    // ----------------------- 审核通过、拒绝 -----------------------

    @ApiOperation("审核通过 @author lidoudou")
    @PostMapping("/oa/cost/apply/pass")
    public ResponseDTO<String> pass(@RequestBody @Valid ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return costApplyService.batchAudit(idList, CostApplyStatusEnum.PASS, dataTracerRequestForm);
    }

    @ApiOperation("拒绝 @author lidoudou")
    @PostMapping("/oa/cost/apply/reject")
    public ResponseDTO<String> reject(@RequestBody @Valid ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return costApplyService.batchAudit(idList, CostApplyStatusEnum.REJECT, dataTracerRequestForm);
    }
}
