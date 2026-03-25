package net.lab1024.tms.driver.module.business.bracket;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.bracket.domain.form.BracketAddForm;
import net.lab1024.tms.driver.module.business.bracket.domain.form.BracketQueryForm;
import net.lab1024.tms.driver.module.business.bracket.domain.form.BracketUpdateForm;
import net.lab1024.tms.driver.module.business.bracket.domain.vo.BracketDetailVO;
import net.lab1024.tms.driver.module.business.bracket.domain.vo.BracketListVO;
import net.lab1024.tms.driver.module.business.bracket.domain.vo.BracketSimpleVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = {DriverSwaggerTagConst.Business.BRACKET})
public class BracketController {

    @Resource
    private BracketService bracketService;

    @ApiOperation(value = "分页查询 @author zhaoxinyang")
    @PostMapping("/bracket/page/query")
    public ResponseDTO<PageResult<BracketListVO>> queryByPage(@RequestBody @Valid BracketQueryForm queryForm) {
        RequestUser driver = SmartRequestUtil.getRequestUser();
        return bracketService.queryByPage(queryForm, driver);
    }

    @ApiOperation(value = "详情 @author zhaoxinyang")
    @GetMapping("/bracket/detail/{bracketId}")
    public ResponseDTO<BracketDetailVO> detail(@PathVariable("bracketId") Long bracketId) {
        return bracketService.getDetail(bracketId);
    }

    @RepeatSubmit
    @ApiOperation(value = "新建挂车 @author zhaoxinyang")
    @PostMapping("/bracket/save")
    public ResponseDTO<String> add(@RequestBody @Valid BracketAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return bracketService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑 @author zhaoxinyang")
    @PostMapping("/bracket/update")
    public ResponseDTO<String> update(@RequestBody @Valid BracketUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return bracketService.update(updateForm, dataTracerRequestForm);
    }

}
