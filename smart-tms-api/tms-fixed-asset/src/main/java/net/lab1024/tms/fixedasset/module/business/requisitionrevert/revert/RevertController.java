package net.lab1024.tms.fixedasset.module.business.requisitionrevert.revert;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.REVERT)
@RestController
public class RevertController {

    @Autowired
    private RequisitionRevertService requisitionRevertService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/revert/queryPage")
    public ResponseDTO<PageResult<RequisitionRevertVO>> queryPage(@RequestBody @Valid RequisitionRevertQueryForm queryForm) {
        queryForm.setType(RequisitionRevertTypeEnum.REVERT.getValue());
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(requisitionRevertService.queryPage(queryForm));
    }

    @ApiOperation("新建资产退回 @author lidoudou")
    @PostMapping("/asset/revert/add")
    public ResponseDTO<String> add(@RequestBody @Valid RequisitionRevertAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return requisitionRevertService.addRevert(addForm, dataTracerRequestForm);
    }
}