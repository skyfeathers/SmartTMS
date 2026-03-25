package net.lab1024.tms.fixedasset.module.business.asset.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetAddForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetQueryForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetUpdateForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 固定资产 Controller
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@RestController
@Api(tags = FixedAssetSwaggerTagConst.Business.ASSET_BASE)
public class AssetController {

    @Autowired
    private AssetService assetService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/queryPage")
    public ResponseDTO<PageResult<AssetVO>> queryPage(@RequestBody @Valid AssetQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(assetService.queryPage(queryForm));
    }

    @ApiOperation("添加 @author lidoudou")
    @PostMapping("/asset/add")
    public ResponseDTO<String> add(@RequestBody @Valid AssetAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return assetService.add(addForm, dataTracerRequestForm);
    }

    @ApiOperation("更新 @author lidoudou")
    @PostMapping("/asset/update")
    public ResponseDTO<String> update(@RequestBody @Valid AssetUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return assetService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation("批量删除 @author lidoudou")
    @PostMapping("/asset/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return assetService.batchDelete(idList, dataTracerRequestForm);
    }

    @ApiOperation("单个删除 @author lidoudou")
    @GetMapping("/asset/delete/{assetId}")
    public ResponseDTO<String> batchDelete(@PathVariable Long assetId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return assetService.delete(assetId, dataTracerRequestForm);
    }


    @ApiOperation("获取详情 @author lidoudou")
    @GetMapping("/asset/detail/{assetId}")
    public ResponseDTO<AssetVO> getDetail(@PathVariable Long assetId) {
        return assetService.assetDetail(assetId);
    }
}
