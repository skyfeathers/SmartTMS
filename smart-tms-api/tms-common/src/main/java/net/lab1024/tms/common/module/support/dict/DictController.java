package net.lab1024.tms.common.module.support.dict;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.controller.SupportBaseController;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.constant.SwaggerTagConst;
import net.lab1024.tms.common.module.support.dict.domain.form.*;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictKeyVO;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/5/26 12:18 上午
 */
@Api(tags = {SwaggerTagConst.Support.SUPPORT_DICT})
@RestController
public class DictController extends SupportBaseController {

    @Autowired
    private DictService dictService;
    @Autowired
    private DictCacheService dictCacheService;


    @ApiOperation("查询全部字典key - @author 卓大")
    @GetMapping("/dict/key/queryAll")
    public ResponseDTO<List<DictKeyVO>> queryAll() {
        return ResponseDTO.ok(dictService.queryAllKey());
    }

    @ApiOperation("分页查询数据字典KEY - @author yandy")
    @PostMapping("/dict/key/query")
    public ResponseDTO<PageResult<DictKeyVO>> keyQuery(@Valid @RequestBody DictKeyQueryForm queryForm) {
        return dictService.keyQuery(queryForm);
    }

    @ApiOperation("分页查询数据字典value - @author yandy")
    @PostMapping("/dict/value/query")
    public ResponseDTO<PageResult<DictValueVO>> valueQuery(@Valid @RequestBody DictValueQueryForm queryForm) {
        return dictService.valueQuery(queryForm);
    }

    @ApiOperation("数据字典KEY-添加- @author yandy")
    @PostMapping("/dict/key/add")
    public ResponseDTO<String> keyAdd(@Valid @RequestBody DictKeyAddForm keyAddForm) {
        return dictService.keyAdd(keyAddForm);
    }

    @ApiOperation("数据字典Value-添加- @author yandy")
    @PostMapping("/dict/value/add")
    public ResponseDTO<String> valueAdd(@Valid @RequestBody DictValueAddForm valueAddForm) {
        return dictService.valueAdd(valueAddForm);
    }

    @ApiOperation("数据字典KEY-更新- @author yandy")
    @PostMapping("/dict/key/edit")
    public ResponseDTO<String> keyEdit(@Valid @RequestBody DictKeyUpdateForm keyUpdateForm) {
        return dictService.keyEdit(keyUpdateForm);
    }

    @ApiOperation("数据字典Value-更新- @author yandy")
    @PostMapping("/dict/value/edit")
    public ResponseDTO<String> valueEdit(@Valid @RequestBody DictValueUpdateForm valueUpdateForm) {
        return dictService.valueEdit(valueUpdateForm);
    }

    @ApiOperation("数据字典KEY-删除- @author yandy")
    @PostMapping("/dict/key/delete")
    public ResponseDTO<String> keyDelete(@RequestBody List<Long> keyIdList) {
        return dictService.keyDelete(keyIdList);
    }

    @ApiOperation("数据字典Value-删除- @author yandy")
    @PostMapping("/dict/value/delete")
    public ResponseDTO<String> valueDelete(@RequestBody List<Long> valueIdList) {
        return dictService.valueDelete(valueIdList);
    }

    @ApiOperation("数据字典缓存-刷新- @author yandy")
    @GetMapping("/dict/cache/refresh")
    public ResponseDTO<String> cacheRefresh() {
        return dictCacheService.cacheRefresh();
    }

    @ApiOperation("数据字典-值列表- @author yandy")
    @GetMapping("/dict/value/list/{keyCode}")
    public ResponseDTO<List<DictValueVO>> valueList(@PathVariable String keyCode) {
        List<DictValueVO> dictValueVOList = dictCacheService.selectByKeyCode(keyCode);
        return ResponseDTO.ok(dictValueVOList);
    }
}
