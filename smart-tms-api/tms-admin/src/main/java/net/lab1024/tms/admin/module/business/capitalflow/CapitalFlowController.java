package net.lab1024.tms.admin.module.business.capitalflow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.capitalflow.domain.CapitalFlowQueryForm;
import net.lab1024.tms.admin.module.business.capitalflow.domain.CapitalFlowStatisticVO;
import net.lab1024.tms.admin.module.business.capitalflow.domain.PayOrderCapitalFlowVO;
import net.lab1024.tms.admin.module.business.capitalflow.domain.ReceiveCapitalFlowVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 资金流水
 *
 * @author lidoudou
 * @date 2022/8/20 上午11:41
 */
@Slf4j
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.CAPITAL_FLOW})
public class CapitalFlowController {

    @Autowired
    private CapitalFlowService capitalFlowService;

    @ApiOperation(value = "分页查询应收的资金流水 @author lidoudou")
    @PostMapping("/capital/flow/receive/page/query")
    public ResponseDTO<PageResult<ReceiveCapitalFlowVO>> queryReceiveByPage(@RequestBody @Valid CapitalFlowQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return capitalFlowService.queryReceiveByPage(queryForm);
    }

    @ApiOperation(value = "分页查询应付的资金流水 @author lidoudou")
    @PostMapping("/capital/flow/pay/page/query")
    public ResponseDTO<PageResult<PayOrderCapitalFlowVO>> queryPayByPage(@RequestBody @Valid CapitalFlowQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return capitalFlowService.queryPayByPage(queryForm);
    }

    @ApiOperation(value = "统计资金流水 @author lidoudou")
    @PostMapping("/capital/flow/statistic")
    public ResponseDTO<CapitalFlowStatisticVO> statistic(@RequestBody @Valid CapitalFlowQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return capitalFlowService.statistic(queryForm);
    }
}
