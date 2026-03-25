package net.lab1024.tms.common.module.support.table;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.common.module.support.table.domain.TableColumnUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhuoda
 * @date 2022-08-20 17:34
 */

@RestController
@Api(tags = {SwaggerTagConst.Support.SUPPORT_TABLE_COLUMN})
public class TableColumnController extends SupportBaseController {

    @Autowired
    private TableColumnService tableColumnService;

    @ApiOperation("修改表格列 @author zhuoda")
    @PostMapping("/tableColumn/update")
    @RepeatSubmit
    public ResponseDTO<String> updateTableColumn(@RequestBody @Valid TableColumnUpdateForm updateForm) {
        return tableColumnService.updateTableColumns(SmartRequestUtil.getRequestUserId(), updateForm);
    }

    @ApiOperation("恢复默认（删除） @author zhuoda")
    @GetMapping("/tableColumn/delete/{tableId}")
    @RepeatSubmit
    public ResponseDTO<String> deleteTableColumn(@PathVariable Integer tableId) {
        return tableColumnService.deleteTableColumn(SmartRequestUtil.getRequestUserId(), tableId);
    }

    @ApiOperation("查询表格列 @author zhuoda")
    @GetMapping("/tableColumn/getColumns/{tableId}")
    public ResponseDTO<String> getColumns(@PathVariable Integer tableId) {
        return ResponseDTO.ok(tableColumnService.getTableColumns(SmartRequestUtil.getRequestUserId(), tableId));
    }
}
