package net.lab1024.tms.admin.module.business.etc;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.etc.domain.form.*;
import net.lab1024.tms.admin.module.business.etc.domain.vo.EtcBalanceRecordVO;
import net.lab1024.tms.admin.module.business.etc.domain.vo.EtcVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.ETC})
public class EtcController {

    @Autowired
    private EtcService etcService;

    @Autowired
    private EtcBalanceRecordService etcBalanceRecordService;

    @ApiOperation(value = "分页查询 @author lidoudou")
    @PostMapping("/etc/page/query")
    public ResponseDTO<PageResult<EtcVO>> queryByPage(@RequestBody @Valid EtcQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return etcService.queryByPage(queryForm);
    }

    @ApiOperation(value = "详情 @author lidoudou")
    @GetMapping("/etc/detail/{etcId}")
    public ResponseDTO<EtcVO> detail(@PathVariable Long etcId) {
        return ResponseDTO.ok(etcService.getDetail(etcId));
    }

    @RepeatSubmit
    @ApiOperation(value = "新建 @author lidoudou")
    @PostMapping("/etc/save")
    public ResponseDTO<String> add(@RequestBody @Valid EtcAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return etcService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑 @author lidoudou")
    @PostMapping("/etc/update")
    public ResponseDTO<String> update(@RequestBody @Valid EtcUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return etcService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "批量删除 @author lidoudou")
    @PostMapping("/etc/update/batch/delete")
    public ResponseDTO<String> batchUpdateDeleteFlag(@RequestBody List<Long> etcIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return etcService.batchUpdateDeletedFlag(etcIdList, dataTracerRequestForm);
    }

    @ApiOperation("EXCEL导入 by yandy")
    @PostMapping("/etc/excel/import")
    public ResponseDTO<String> excelImport(MultipartFile uploadFile, HttpServletRequest request) throws Exception {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return etcService.excelImport(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), uploadFile, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "充值 @author lidoudou")
    @PostMapping("/etc/recharge")
    public ResponseDTO<String> recharge(@RequestBody @Valid EtcRechargeForm rechargeForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        Long requestUserId = requestUser.getUserId();
        return etcBalanceRecordService.recharge(rechargeForm, requestUserId);
    }

    @ApiOperation(value = "查询 @author lidoudou")
    @PostMapping("/etc/balance/record/page/query")
    public ResponseDTO<PageResult<EtcBalanceRecordVO>> queryBalanceRecordPage(@RequestBody @Valid EtcBalanceRecordQueryForm queryForm) {
        return etcBalanceRecordService.queryByPage(queryForm);
    }
}
