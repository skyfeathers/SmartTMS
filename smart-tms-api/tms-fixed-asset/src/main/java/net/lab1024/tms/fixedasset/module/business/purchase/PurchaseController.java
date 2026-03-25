package net.lab1024.tms.fixedasset.module.business.purchase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.purchase.domain.PurchaseAddForm;
import net.lab1024.tms.fixedasset.module.business.purchase.domain.PurchaseQueryForm;
import net.lab1024.tms.fixedasset.module.business.purchase.domain.PurchaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 资产采购
 *
 * @author lidoudou
 * @date 2023/3/18 下午4:49
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.PURCHASE)
@RestController
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/purchase/queryPage")
    public ResponseDTO<PageResult<PurchaseVO>> queryPage(@RequestBody @Valid PurchaseQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(purchaseService.queryPage(queryForm));
    }

    @ApiOperation("添加资产采购 @author lidoudou")
    @PostMapping("/asset/purchase/add")
    public ResponseDTO<String> add(@RequestBody @Valid PurchaseAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return purchaseService.add(addForm, dataTracerRequestForm);
    }

    @ApiOperation("查询详情 @author lidoudou")
    @GetMapping("/asset/purchase/detail/{purchaseId}")
    public ResponseDTO<PurchaseVO> add(@PathVariable Long purchaseId) {
        return null;
    }
}