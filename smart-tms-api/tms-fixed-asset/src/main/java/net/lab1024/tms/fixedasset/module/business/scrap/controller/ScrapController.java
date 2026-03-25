package net.lab1024.tms.fixedasset.module.business.scrap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.scrap.constant.AssetScrapStatusEnum;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.form.ScrapAddForm;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.form.ScrapQueryForm;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapVO;
import net.lab1024.tms.fixedasset.module.business.scrap.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 固定资产-报废 Controller
 *
 * @Author 卓大
 * @Date 2023-03-23 15:01:51
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@RestController
@Api(tags = FixedAssetSwaggerTagConst.Business.CLEAR)
public class ScrapController {

    @Autowired
    private ScrapService scrapService;

    @ApiOperation("分页查询 @author 卓大")
    @PostMapping("/scrap/queryPage")
    public ResponseDTO<PageResult<ScrapVO>> queryPage(@RequestBody @Valid ScrapQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(scrapService.queryPage(queryForm));
    }

    @ApiOperation("添加 @author 卓大")
    @PostMapping("/scrap/add")
    public ResponseDTO<String> add(@RequestBody @Valid ScrapAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return scrapService.add(addForm, dataTracerRequestForm);
    }


    @ApiOperation("详情 @author 卓大")
    @GetMapping("/scrap/getDetail/{scrapId}")
    public ResponseDTO<ScrapVO> getDetail(@PathVariable Long scrapId) {
        return ResponseDTO.ok(scrapService.getDetail(scrapId));
    }

    // ----------------------- 审核通过、拒绝 -----------------------

    @ApiOperation("审核通过 @author 卓大")
    @PostMapping("/scrap/pass")
    public ResponseDTO<String> pass(@RequestBody @Valid ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return scrapService.batchAudit(idList, AssetScrapStatusEnum.PASS, dataTracerRequestForm);
    }

    @ApiOperation("拒绝 @author 卓大")
    @PostMapping("/scrap/reject")
    public ResponseDTO<String> reject(@RequestBody @Valid ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return scrapService.batchAudit(idList, AssetScrapStatusEnum.REJECT, dataTracerRequestForm);
    }

}
