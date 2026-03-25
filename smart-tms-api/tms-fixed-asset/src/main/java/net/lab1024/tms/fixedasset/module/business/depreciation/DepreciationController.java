package net.lab1024.tms.fixedasset.module.business.depreciation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.form.DepreciationAddForm;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.form.DepreciationQueryForm;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo.DepreciationItemVO;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo.DepreciationVO;
import net.lab1024.tms.fixedasset.module.business.depreciation.service.DepreciationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 资产折旧
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:12
 */
@RestController
@Api(tags = FixedAssetSwaggerTagConst.Business.DEPRECIATION)
public class DepreciationController {
    @Autowired
    private DepreciationService depreciationService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/depreciation/queryPage")
    public ResponseDTO<PageResult<DepreciationVO>> queryPage(@RequestBody @Valid DepreciationQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return depreciationService.queryPage(queryForm);
    }

    @ApiOperation("保存 @author lidoudou")
    @PostMapping("/depreciation/add")
    public ResponseDTO<String> save(@RequestBody @Valid DepreciationAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return depreciationService.save(addForm, dataTracerRequestForm);
    }

    @ApiOperation("作废 @author lidoudou")
    @GetMapping("/depreciation/cancel/{depreciationId}")
    public ResponseDTO<String> cancel(@PathVariable Long depreciationId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return depreciationService.cancel(depreciationId, dataTracerRequestForm);
    }

    @ApiOperation("详情 @author lidoudou")
    @GetMapping("/depreciation/detail/{depreciationId}")
    public ResponseDTO<DepreciationVO> detail(@PathVariable Long depreciationId) {
        return depreciationService.getDetail(depreciationId);
    }

    @ApiOperation("查询资产并计算折旧信息 @author lidoudou")
    @PostMapping("/depreciation/asset/query")
    public ResponseDTO<List<DepreciationItemVO>> queryAsset(@RequestBody @Valid DepreciationAddForm addForm) {
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(depreciationService.queryAsset(addForm));
    }
}
