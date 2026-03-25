package net.lab1024.tms.fixedasset.module.business.consumables.stock;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesUpdateForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesDetailVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.service.ConsumablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 易耗品库存
 *
 * @author lidoudou
 * @date 2023/4/12 上午10:04
 */
@RestController
@Api(tags = {FixedAssetSwaggerTagConst.Business.CONSUMABLES_STOCK})
public class ConsumablesController {

    @Autowired
    private ConsumablesService consumablesService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/consumables/stock/queryPage")
    public ResponseDTO<PageResult<ConsumablesVO>> queryPage(@RequestBody @Valid ConsumablesQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(consumablesService.queryPage(queryForm));
    }

    @ApiOperation("添加 @author lidoudou")
    @PostMapping("/consumables/stock/add")
    public ResponseDTO<String> add(@RequestBody @Valid ConsumablesAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return consumablesService.add(addForm, dataTracerRequestForm);
    }

    @ApiOperation("更新 @author lidoudou")
    @PostMapping("/consumables/stock/update")
    public ResponseDTO<String> update(@RequestBody @Valid ConsumablesUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return consumablesService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation("详情 @author lidoudou")
    @GetMapping("/consumables/stock/detail/{consumablesId}")
    public ResponseDTO<ConsumablesDetailVO> get(@PathVariable Long consumablesId) {
        return consumablesService.getDetail(consumablesId);
    }
}
