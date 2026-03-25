package net.lab1024.tms.fixedasset.module.business.consumables.stockrecord;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.form.ConsumablesStockRecordQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.vo.ConsumablesStockRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 库存记录
 *
 * @author lidoudou
 * @date 2023/4/18 上午9:07
 */
@RestController
@Api(tags = {FixedAssetSwaggerTagConst.Business.CONSUMABLES_STOCK_RECORD})
public class ConsumablesStockRecordController {

    @Autowired
    private ConsumablesStockRecordService consumablesStockRecordService;


    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/consumables/stock/record/queryPage")
    public ResponseDTO<PageResult<ConsumablesStockRecordVO>> queryPage(@RequestBody @Valid ConsumablesStockRecordQueryForm queryForm) {
        return ResponseDTO.ok(consumablesStockRecordService.queryPage(queryForm));
    }

}
