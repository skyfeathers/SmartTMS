package net.lab1024.tms.fixedasset.module.business.repair.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.repair.constant.AssetRepairStatusEnum;
import net.lab1024.tms.fixedasset.module.business.repair.domain.form.RepairAddForm;
import net.lab1024.tms.fixedasset.module.business.repair.domain.form.RepairQueryForm;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairDetailVO;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairVO;
import net.lab1024.tms.fixedasset.module.business.repair.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 固定资产-维修登记 Controller
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@RestController
@Api(tags = FixedAssetSwaggerTagConst.Business.REPAIR)
public class RepairController {

    @Autowired
    private RepairService repairService;

    @ApiOperation("分页查询 @author 卓大")
    @PostMapping("/repair/queryPage")
    public ResponseDTO<PageResult<RepairVO>> queryPage(@RequestBody @Valid RepairQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(repairService.queryPage(queryForm));
    }

    @ApiOperation("添加 @author 卓大")
    @PostMapping("/repair/add")
    public ResponseDTO<String> add(@RequestBody @Valid RepairAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return repairService.add(addForm, dataTracerRequestForm);
    }


    @ApiOperation("详情 @author 卓大")
    @GetMapping("/repair/getDetail/{repairId}")
    public ResponseDTO<RepairDetailVO> getDetail(@PathVariable Long repairId) {
        return ResponseDTO.ok(repairService.getDetail(repairId));
    }

    // ----------------------- 审核通过、拒绝 -----------------------

    @ApiOperation("审核通过 @author 卓大")
    @PostMapping("/repair/pass")
    public ResponseDTO<String> pass(@RequestBody @Valid ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return repairService.batchAudit(idList, AssetRepairStatusEnum.REPAIRING, dataTracerRequestForm);
    }

    @ApiOperation("拒绝 @author 卓大")
    @PostMapping("/repair/reject")
    public ResponseDTO<String> reject(@RequestBody @Valid ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return repairService.batchAudit(idList, AssetRepairStatusEnum.REJECT, dataTracerRequestForm);
    }

    // ----------------------- 完成维修 -----------------------

    @ApiOperation("审核通过 @author 卓大")
    @PostMapping("/repair/finish")
    public ResponseDTO<String> finish(@RequestBody @Valid ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return repairService.batchFinish(idList, dataTracerRequestForm);
    }
}
