package net.lab1024.tms.fixedasset.module.business.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.report.domain.DepreciationStatisticQueryForm;
import net.lab1024.tms.fixedasset.module.business.report.domain.DepreciationStatisticVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = FixedAssetSwaggerTagConst.Business.REPORT)
public class ReportController {
    @Autowired
    private ReportService reportService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/report/depreciation/queryPage")
    public ResponseDTO<PageResult<DepreciationStatisticVO>> queryPage(@RequestBody @Valid DepreciationStatisticQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(reportService.queryPage(queryForm));
    }
}
