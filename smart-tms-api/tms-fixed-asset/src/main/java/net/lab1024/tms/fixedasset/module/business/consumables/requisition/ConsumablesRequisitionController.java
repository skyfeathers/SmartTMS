package net.lab1024.tms.fixedasset.module.business.consumables.requisition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.vo.ConsumablesRequisitionVO;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.service.ConsumablesRequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.CONSUMABLES_REQUISITION)
@RestController
public class ConsumablesRequisitionController {

    @Autowired
    private ConsumablesRequisitionService requisitionService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/consumables/requisition/queryPage")
    public ResponseDTO<PageResult<ConsumablesRequisitionVO>> queryPage(@RequestBody @Valid ConsumablesRequisitionQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(requisitionService.queryPage(queryForm));
    }

    @ApiOperation("新建资产领用 @author lidoudou")
    @PostMapping("/consumables/requisition/add")
    public ResponseDTO<String> add(@RequestBody @Valid ConsumablesRequisitionAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return requisitionService.addRequisition(addForm, dataTracerRequestForm);
    }

    @ApiOperation("获取详情 @author lidoudou")
    @GetMapping("/consumables/requisition/detail/{requisitionId}")
    public ResponseDTO<ConsumablesRequisitionVO> getDetail(@PathVariable Long requisitionId) {
        return ResponseDTO.ok(requisitionService.getDetail(requisitionId));
    }
}