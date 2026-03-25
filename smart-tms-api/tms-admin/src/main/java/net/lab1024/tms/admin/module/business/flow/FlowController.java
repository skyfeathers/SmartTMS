package net.lab1024.tms.admin.module.business.flow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.flow.define.FlowService;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowQueryForm;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowTaskConfigForm;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowVO;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskListenerVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = AdminSwaggerTagConst.Business.MANAGER_FLOW)
@RestController
public class FlowController {

    @Resource
    private FlowService flowService;

    @ApiOperation("审批流程-分页 by zhaoxinyang")
    @GetMapping("/flow/list")
    public ResponseDTO<List<FlowVO>> flowQuery() {
        return flowService.flowQuery();
    }

    @ApiOperation("审批流程-详情 by zhaoxinyang")
    @GetMapping("/flow/detail/{flowId}/{enterpriseId}")
    public ResponseDTO<FlowVO> detail(@PathVariable Long flowId, @PathVariable Long enterpriseId) {
        return flowService.detail(flowId, enterpriseId);
    }

    @ApiOperation("审批流程-监视器列表 by zhaoxinyang")
    @GetMapping("/flow/listener/list")
    public ResponseDTO<List<FlowTaskListenerVO>> listenerList() {
        return flowService.listenerList();
    }

    @RepeatSubmit
    @ApiOperation("审批流程-编辑 by zhaoxinyang")
    @PostMapping("/flow/task/config")
    public ResponseDTO<String> flowTaskConfig(@RequestBody @Valid FlowTaskConfigForm configForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return flowService.flowTaskConfig(configForm, dataTracerRequestForm);
    }


}
