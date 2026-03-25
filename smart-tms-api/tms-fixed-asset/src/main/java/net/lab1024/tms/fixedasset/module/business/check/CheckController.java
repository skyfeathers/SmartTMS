package net.lab1024.tms.fixedasset.module.business.check;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckAddForm;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckCompleteForm;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckForm;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckQueryForm;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckDetailVO;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.CHECK)
@RestController
public class CheckController {

    @Autowired
    private CheckService checkService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/check/queryPage")
    public ResponseDTO<PageResult<CheckVO>> queryPage(@RequestBody @Valid CheckQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(checkService.queryPage(queryForm));
    }

    @ApiOperation("添加资产盘点 @author lidoudou")
    @PostMapping("/asset/check/add")
    public ResponseDTO<String> add(@RequestBody @Valid CheckAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return checkService.add(addForm, dataTracerRequestForm);
    }

    @ApiOperation("获取盘点详情 @author lidoudou")
    @GetMapping("/asset/check/detail/{checkId}")
    public ResponseDTO<CheckDetailVO> getDetail(@PathVariable Long checkId) {
        CheckDetailVO checkDetailVO = checkService.getDetail(checkId);
        return ResponseDTO.ok(checkDetailVO);
    }

    @ApiOperation("开始盘点 @author lidoudou")
    @PostMapping("/asset/check/update")
    public ResponseDTO<String> check(@RequestBody @Valid CheckForm checkForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return checkService.check(checkForm, dataTracerRequestForm);
    }

    @ApiOperation("完成盘点 @author lidoudou")
    @PostMapping("/asset/check/complete")
    public ResponseDTO<String> check(@RequestBody @Valid CheckCompleteForm checkCompleteForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return checkService.completeCheck(checkCompleteForm, dataTracerRequestForm);
    }
}