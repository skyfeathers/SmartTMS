package net.lab1024.tms.fixedasset.module.business.allocation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationAddForm;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationQueryForm;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.ALLOCATION)
@RestController
public class AllocationController {

    @Autowired
    private AllocationService allocationService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/allocation/queryPage")
    public ResponseDTO<PageResult<AllocationVO>> queryPage(@RequestBody @Valid AllocationQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(allocationService.queryPage(queryForm));
    }

    @ApiOperation("新建资产调拨 @author lidoudou")
    @PostMapping("/asset/allocation/add")
    public ResponseDTO<String> add(@RequestBody @Valid AllocationAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return allocationService.addAllocation(addForm, dataTracerRequestForm);
    }

    @ApiOperation("通过资产调拨申请 @author lidoudou")
    @GetMapping("/asset/allocation/complete/{allocationId}")
    public ResponseDTO<String> complete(@PathVariable Long allocationId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return allocationService.complete(allocationId, dataTracerRequestForm);
    }

    @ApiOperation("驳回资产调拨申请 @author lidoudou")
    @GetMapping("/asset/allocation/reject/{allocationId}")
    public ResponseDTO<String> reject(@PathVariable Long allocationId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return allocationService.reject(allocationId, dataTracerRequestForm);
    }

    @ApiOperation("获取详情 @author lidoudou")
    @GetMapping("/asset/allocation/detail/{allocationId}")
    public ResponseDTO<AllocationVO> getDetail(@PathVariable Long allocationId) {
        AllocationVO allocationVO = allocationService.getDetail(allocationId);
        if (null == allocationVO) {
            return ResponseDTO.userErrorParam("调拨详情不存在");
        }
        return ResponseDTO.ok(allocationVO);
    }
}