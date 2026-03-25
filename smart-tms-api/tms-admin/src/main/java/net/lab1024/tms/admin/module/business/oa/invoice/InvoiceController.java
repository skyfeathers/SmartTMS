package net.lab1024.tms.admin.module.business.oa.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceCreateForm;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceQueryForm;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceUpdateForm;
import net.lab1024.tms.admin.module.business.oa.invoice.domain.InvoiceVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * OA发票信息
 *
 * @author lihaifan
 * @date 2022/6/23 17:31
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.OA_INVOICE})
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @ApiOperation(value = "分页查询发票信息 @author lihaifan")
    @PostMapping("/oa/invoice/page/query")
    public ResponseDTO<PageResult<InvoiceVO>> queryByPage(@RequestBody @Valid InvoiceQueryForm queryDTO) {
        return invoiceService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询发票信息详情 @author lihaifan")
    @GetMapping("/oa/invoice/get/{invoiceId}")
    public ResponseDTO<InvoiceVO> getDetail(@PathVariable Long invoiceId) {
        return invoiceService.getDetail(invoiceId);
    }

    @ApiOperation(value = "新建发票信息 @author lihaifan")
    @PostMapping("/oa/invoice/create")
    public ResponseDTO<String> createInvoice(@RequestBody @Valid InvoiceCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return invoiceService.createInvoice(createVO);
    }

    @ApiOperation(value = "编辑发票信息 @author lihaifan")
    @PostMapping("/oa/invoice/update")
    public ResponseDTO<String> updateInvoice(@RequestBody @Valid InvoiceUpdateForm updateVO) {
        return invoiceService.updateInvoice(updateVO);
    }

    @ApiOperation(value = "删除发票信息 @author lihaifan")
    @GetMapping("/invoice/delete/{invoiceId}")
    public ResponseDTO<String> deleteInvoice(@PathVariable Long invoiceId) {
        return invoiceService.deleteInvoice(invoiceId);
    }

    @ApiOperation(value = "查询列表 @author lidoudou")
    @GetMapping("/oa/invoice/query/list/{enterpriseId}")
    public ResponseDTO<List<InvoiceVO>> queryList(@PathVariable Long enterpriseId) {
        return invoiceService.queryList(enterpriseId);
    }


}
