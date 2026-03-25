package net.lab1024.tms.admin.module.business.repair;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.repair.domain.RepairAddForm;
import net.lab1024.tms.admin.module.business.repair.domain.RepairQueryForm;
import net.lab1024.tms.admin.module.business.repair.domain.RepairUpdateForm;
import net.lab1024.tms.admin.module.business.repair.domain.RepairVO;
import net.lab1024.tms.admin.module.business.repair.service.RepairService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.REPAIR})
public class RepairController {

    @Autowired
    private RepairService repairService;

    @ApiOperation(value = "分页查询维修信息 @author lidoudou")
    @PostMapping("/repair/page/query")
    public ResponseDTO<PageResult<RepairVO>> queryByPage(@RequestBody @Valid RepairQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return repairService.queryByPage(queryForm);
    }

    @ApiOperation(value = "查询维修厂家金额总计 @author zhaoxinyang")
    @GetMapping("/repair/amount/sum/{repairPlantId}")
    public ResponseDTO<BigDecimal> amountSum(@PathVariable Long repairPlantId) {
        return repairService.amountSum(repairPlantId);
    }

    @RepeatSubmit
    @ApiOperation(value = "新建维修信息 @author lidoudou")
    @PostMapping("/repair/save")
    public ResponseDTO<String> add(@RequestBody @Valid RepairAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return repairService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑维修信息 @author lidoudou")
    @PostMapping("/repair/update")
    public ResponseDTO<String> update(@RequestBody @Valid RepairUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return repairService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "维修信息删除 @author lidoudou")
    @GetMapping("/repair/delete/{repairId}")
    public ResponseDTO<String> delete(@PathVariable Long repairId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return repairService.deleteRepair(repairId, dataTracerRequestForm);
    }
}
