package net.lab1024.tms.admin.module.business.flow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentCreateForm;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentService;
import net.lab1024.tms.admin.module.business.flow.instance.FlowInstanceService;
import net.lab1024.tms.admin.module.business.flow.instance.FlowInstanceTaskService;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceAuditForm;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceBatchAuditForm;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceQueryForm;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceTaskVO;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 10:39
 */
@Api(tags = AdminSwaggerTagConst.Business.MANAGER_FLOW)
@RestController
public class FlowInstanceController {

    @Autowired
    private FlowInstanceService flowInstanceService;
    @Autowired
    private FlowInstanceTaskService flowInstanceTaskService;
    @Autowired
    private FlowCommentService flowCommentService;

    @ApiOperation("审批实例-查询待我审核的总数 by yandy")
    @GetMapping("/flow/instance/wait/audit/num")
    public ResponseDTO<Integer> waitAuditNum() {
        return flowInstanceService.waitAuditNum(SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation("审批实例-分页查询 by yandy")
    @PostMapping("/flow/instance/query")
    public ResponseDTO<PageResult<FlowInstanceVO>> flowInstanceQuery(@RequestBody @Valid FlowInstanceQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return flowInstanceService.flowInstanceQuery(queryForm, SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation("审批实例-任务处理流程信息 by yandy")
    @GetMapping("/flow/instance/task/list/{instanceId}")
    public ResponseDTO<List<FlowInstanceTaskVO>> instanceTaskList(@PathVariable Long instanceId) {
        return flowInstanceTaskService.instanceTaskList(instanceId);
    }

    @ApiOperation("审批实例-当前处理人id集合 by yandy")
    @GetMapping("/flow/instance/task/handler/{instanceId}")
    public ResponseDTO<List<Long>> instanceTaskCurrentHandler(@PathVariable Long instanceId) {
        return flowInstanceTaskService.instanceTaskCurrentHandler(instanceId);
    }

    @ApiOperation("审批实例-审核 by yandy")
    @PostMapping("/flow/instance/audit")
    public ResponseDTO<String> instanceTaskAudit(@RequestBody @Valid FlowInstanceAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return flowInstanceTaskService.instanceTaskAudit(auditForm, dataTracerRequestForm);
    }

    @ApiOperation("审批实例-审核 by yandy")
    @PostMapping("/flow/instance/batchAudit")
    public ResponseDTO<String> instanceTaskBatchAudit(@RequestBody @Valid FlowInstanceBatchAuditForm auditForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return flowInstanceTaskService.instanceTaskBatchAudit(auditForm, dataTracerRequestForm);
    }

    @ApiOperation("审批实例-撤销 by yandy")
    @GetMapping("/flow/instance/cancel/{instanceId}")
    public ResponseDTO<String> cancelFlowInstance(@PathVariable Long instanceId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return flowInstanceService.cancelFlowInstance(instanceId, dataTracerRequestForm);
    }

    @ApiOperation("审批实例-获取当前流程所在的任务节点 by yandy")
    @GetMapping("/flow/instance/current/task/{instanceId}")
    public ResponseDTO<FlowInstanceTaskVO> flowCurrentTask(@PathVariable Long instanceId) {
        return flowInstanceService.flowCurrentTask(instanceId);
    }

    @ApiOperation("审批实例-评论 by lihaifan")
    @PostMapping("/flow/instance/comment")
    public ResponseDTO<String> addFlowComment(@RequestBody @Valid FlowCommentCreateForm createForm) {
        createForm.setOperatorId(SmartRequestUtil.getRequestUserId());
        createForm.setOperatorName(SmartRequestUtil.getRequestUser().getUserName());

        createForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return flowCommentService.addFlowComment(createForm);
    }

}
