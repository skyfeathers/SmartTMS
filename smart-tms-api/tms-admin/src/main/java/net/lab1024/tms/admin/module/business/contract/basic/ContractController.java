package net.lab1024.tms.admin.module.business.contract.basic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.contract.basic.domain.ContractQueryForm;
import net.lab1024.tms.admin.module.business.contract.basic.domain.ContractUpdateStatusForm;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.business.contract.domain.ContractCreateForm;
import net.lab1024.tms.common.module.business.contract.domain.ContractOnlineRecordVO;
import net.lab1024.tms.common.module.business.contract.domain.ContractUpdateForm;
import net.lab1024.tms.common.module.business.contract.domain.ContractVO;
import net.lab1024.tms.common.module.business.esign.domain.flow.ESignExecuteUrlVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 合同管理
 *
 * @author lihaifan
 * @date 2022/7/15 11:09
 */
@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.CONTRACT})
public class ContractController {

    @Autowired
    private ContractService contractService;

    @ApiOperation(value = "分页查询合同模块 @author lihaifan")
    @PostMapping("/contract/page/query")
    public ResponseDTO<PageResult<ContractVO>> queryByPage(@RequestBody @Valid ContractQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return contractService.queryByPage(queryForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "新建合同 @author lihaifan")
    @PostMapping("/contract/create")
    public ResponseDTO<String> createContract(@RequestBody @Valid ContractCreateForm createForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createForm.setOperatorId(requestUser.getUserId());
        createForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return contractService.createContract(createForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "新建合同 @author lihaifan")
    @PostMapping("/contract/update")
    public ResponseDTO<String> updateContract(@RequestBody @Valid ContractUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        updateForm.setOperatorId(requestUser.getUserId());
        return contractService.updateContract(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "批量更新状态 @author lihaifan")
    @PostMapping("/contract/batchUpdateStatus")
    public ResponseDTO<String> batchUpdateStatus(@RequestBody @Valid ContractUpdateStatusForm batchUpdateStatusForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        batchUpdateStatusForm.setOperatorId(requestUser.getUserId());
        return contractService.batchUpdateStatus(batchUpdateStatusForm);
    }

    @ApiOperation(value = "获取线上合同预览url @author shanyi")
    @GetMapping("/contract/getExecuteUrl/{contractId}/{signedFlag}")
    public ResponseDTO<ESignExecuteUrlVO> getExecuteUrl(@PathVariable("contractId") Long contractId, @PathVariable("signedFlag") Boolean signedFlag) {
        return contractService.getExecuteUrl(contractId, signedFlag);
    }

    @ApiOperation(value = "下载合同 @author shanyi")
    @GetMapping("/contract/downloadContract/{contractId}")
    public ResponseDTO<String> downloadContract(@PathVariable("contractId") Long contractId) {
        return contractService.downloadContract(contractId);
    }

    @ApiOperation(value = "查询线上合同操作记录 @author shanyi")
    @GetMapping("/contract/getOnlineRecords/{contractId}")
    public ResponseDTO<List<ContractOnlineRecordVO>> getOnlineRecords(@PathVariable("contractId") Long contractId) {
        return contractService.getOnlineRecords(contractId);
    }

    @ApiOperation(value = "合同撤销 @author shanyi")
    @GetMapping("/contract/revoke/{contractId}")
    public ResponseDTO<String> revoke(@PathVariable("contractId") Long contractId) {
        return contractService.revoke(contractId);
    }
}
