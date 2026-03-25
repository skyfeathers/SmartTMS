package net.lab1024.tms.fixedasset.module.business.consumables.purchase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form.ConsumablesPurchaseAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form.ConsumablesPurchaseQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo.ConsumablesPurchaseDetailVO;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo.ConsumablesPurchaseVO;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.service.ConsumablesPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 低值易耗采购
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:00
 */
@RestController
@Api(tags = {FixedAssetSwaggerTagConst.Business.CONSUMABLES_PURCHASE})
public class ConsumablesPurchaseController {

    @Autowired
    private ConsumablesPurchaseService consumablesPurchaseService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/consumables/purchase/queryPage")
    public ResponseDTO<PageResult<ConsumablesPurchaseVO>> queryPage(@RequestBody @Valid ConsumablesPurchaseQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(consumablesPurchaseService.queryPage(queryForm));
    }

    @ApiOperation("新建 @author lidoudou")
    @PostMapping("/consumables/purchase/add")
    public ResponseDTO<String> add(@RequestBody @Valid ConsumablesPurchaseAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return consumablesPurchaseService.add(addForm, dataTracerRequestForm);
    }

    @ApiOperation("获取详情 @author lidoudou")
    @GetMapping("/consumables/purchase/detail/{purchaseId}")
    public ResponseDTO<ConsumablesPurchaseDetailVO> getDetail(@PathVariable Long purchaseId) {
        return consumablesPurchaseService.getDetail(purchaseId);
    }
}
