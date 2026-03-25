package net.lab1024.tms.fixedasset.module.business.borrowback.back;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yandy
 * @description:
 * @date 2023/3/15 5:33 下午
 */
@Api(tags = FixedAssetSwaggerTagConst.Business.BACK)
@RestController
public class BackController {

    @Autowired
    private BorrowBackService borrowBackService;

    @ApiOperation("分页查询 @author lidoudou")
    @PostMapping("/asset/back/queryPage")
    public ResponseDTO<PageResult<BorrowBackVO>> queryPage(@RequestBody @Valid BorrowBackQueryForm queryForm) {
        queryForm.setType(BorrowBackTypeEnum.BACK.getValue());
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return ResponseDTO.ok(borrowBackService.queryPage(queryForm));
    }

    @ApiOperation("新建资产退回 @author lidoudou")
    @PostMapping("/asset/back/add")
    public ResponseDTO<String> add(@RequestBody @Valid BorrowBackAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return borrowBackService.addBack(addForm, dataTracerRequestForm);
    }
}