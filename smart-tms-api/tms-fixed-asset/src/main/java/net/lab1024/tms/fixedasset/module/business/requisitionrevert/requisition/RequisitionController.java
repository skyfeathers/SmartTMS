package net.lab1024.tms.fixedasset.module.business.requisitionrevert.requisition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionRevertTypeEnum;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertAddForm;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertQueryForm;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertVO;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.service.RequisitionRevertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.REQUISITION)
@RestController
public class RequisitionController {

    @Autowired
    private RequisitionRevertService requisitionService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/requisition/queryPage")
    public ResponseDTO<PageResult<RequisitionRevertVO>> queryPage(@RequestBody @Valid RequisitionRevertQueryForm queryForm) {
        queryForm.setType(RequisitionRevertTypeEnum.REQUISITION.getValue());
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(requisitionService.queryPage(queryForm));
    }

    @ApiOperation("新建资产领用 @author lidoudou")
    @PostMapping("/asset/requisition/add")
    public ResponseDTO<String> add(@RequestBody @Valid RequisitionRevertAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return requisitionService.addRequisition(addForm, dataTracerRequestForm);
    }

    @ApiOperation("确认资产领用 @author lidoudou")
    @GetMapping("/asset/requisition/complete/{requisitionRevertId}")
    public ResponseDTO<String> completeRequisition(@PathVariable Long requisitionRevertId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return requisitionService.completeRequisition(requisitionRevertId, dataTracerRequestForm);
    }

    @ApiOperation("驳回资产领用 @author lidoudou")
    @GetMapping("/asset/requisition/reject/{requisitionRevertId}")
    public ResponseDTO<String> rejectRequisition(@PathVariable Long requisitionRevertId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return requisitionService.rejectRequisition(requisitionRevertId, dataTracerRequestForm);
    }

    @ApiOperation("获取详情 @author lidoudou")
    @GetMapping("/asset/requisitionrevert/detail/{requisitionRevertId}")
    public ResponseDTO<RequisitionRevertVO> getDetail(@PathVariable Long requisitionRevertId) {
        return ResponseDTO.ok(requisitionService.getDetail(requisitionRevertId));
    }
}