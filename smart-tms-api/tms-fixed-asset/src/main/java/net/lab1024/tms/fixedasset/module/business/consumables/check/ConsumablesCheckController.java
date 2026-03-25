package net.lab1024.tms.fixedasset.module.business.consumables.check;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckCompleteForm;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckForm;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesCheckVO;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesConsumablesCheckDetailVO;
import net.lab1024.tms.fixedasset.module.business.consumables.check.service.ConsumablesCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.CONSUMABLES_CHECK)
@RestController
public class ConsumablesCheckController {

    @Autowired
    private ConsumablesCheckService consumablesCheckService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/consumables/check/queryPage")
    public ResponseDTO<PageResult<ConsumablesCheckVO>> queryPage(@RequestBody @Valid ConsumablesCheckQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(consumablesCheckService.queryPage(queryForm));
    }

    @ApiOperation("添加易耗品盘点 @author lidoudou")
    @PostMapping("/consumables/check/add")
    public ResponseDTO<String> add(@RequestBody @Valid ConsumablesCheckAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return consumablesCheckService.add(addForm, dataTracerRequestForm);
    }

    @ApiOperation("获取盘点详情 @author lidoudou")
    @GetMapping("/consumables/check/detail/{checkId}")
    public ResponseDTO<ConsumablesConsumablesCheckDetailVO> getDetail(@PathVariable Long checkId) {
        ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO = consumablesCheckService.getDetail(checkId);
        return ResponseDTO.ok(consumablesCheckDetailVO);
    }

    @ApiOperation("开始盘点 @author lidoudou")
    @PostMapping("/consumables/check/update")
    public ResponseDTO<String> check(@RequestBody @Valid ConsumablesCheckForm consumablesCheckForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return consumablesCheckService.check(consumablesCheckForm, dataTracerRequestForm);
    }

    @ApiOperation("完成盘点 @author lidoudou")
    @PostMapping("/consumables/check/complete")
    public ResponseDTO<String> check(@RequestBody @Valid ConsumablesCheckCompleteForm consumablesCheckCompleteForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return consumablesCheckService.completeCheck(consumablesCheckCompleteForm, dataTracerRequestForm);
    }
}