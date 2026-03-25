package net.lab1024.tms.fixedasset.module.business.borrowback.borrow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.fixedasset.constant.FixedAssetSwaggerTagConst;
import net.lab1024.tms.fixedasset.module.business.borrowback.BorrowBackService;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowBackTypeEnum;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackAddForm;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackQueryForm;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.BORROW)
@RestController
public class BorrowController {

    @Autowired
    private BorrowBackService borrowBackService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/borrow/queryPage")
    public ResponseDTO<PageResult<BorrowBackVO>> queryPage(@RequestBody @Valid BorrowBackQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        queryForm.setType(BorrowBackTypeEnum.BORROW.getValue());
        return ResponseDTO.ok(borrowBackService.queryPage(queryForm));
    }

    @ApiOperation("新建资产领用 @author lidoudou")
    @PostMapping("/asset/borrow/add")
    public ResponseDTO<String> add(@RequestBody @Valid BorrowBackAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return borrowBackService.addBorrow(addForm, dataTracerRequestForm);
    }

    @ApiOperation("确认资产借用 @author lidoudou")
    @GetMapping("/asset/borrow/complete/{borrowBackId}")
    public ResponseDTO<String> completeBorrow(@PathVariable Long borrowBackId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return borrowBackService.completeBorrow(borrowBackId, dataTracerRequestForm);
    }

    @ApiOperation("驳回资产借用申请 @author lidoudou")
    @GetMapping("/asset/borrow/reject/{borrowBackId}")
    public ResponseDTO<String> rejectBorrow(@PathVariable Long borrowBackId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return borrowBackService.rejectBorrow(borrowBackId, dataTracerRequestForm);
    }

    @ApiOperation("查询详情 @author lidoudou")
    @GetMapping("/asset/borrowback/detail/{borrowBackId}")
    public ResponseDTO<BorrowBackVO> getDetail(@PathVariable Long borrowBackId) {
        return ResponseDTO.ok(borrowBackService.getDetail(borrowBackId));
    }
}