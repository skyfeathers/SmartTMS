package net.lab1024.tms.admin.module.business.receive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.receive.domain.form.*;
import net.lab1024.tms.admin.module.business.receive.domain.vo.ReceiveOrderInvoiceListVO;
import net.lab1024.tms.admin.module.business.receive.service.ReceiveOrderInvoiceService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.RECEIVE_ORDER})
public class ReceiveOrderInvoiceController {

    @Autowired
    private ReceiveOrderInvoiceService receiveOrderInvoiceService;

    @ApiOperation(value = "分页查询开票列表 @author lidoudou")
    @PostMapping("/receive/order/invoice/page/query")
    public ResponseDTO<PageResult<ReceiveOrderInvoiceListVO>> queryInvoiceByPage(@RequestBody @Valid ReceiveInvoiceQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderInvoiceService.queryInvoiceByPage(queryForm);
    }

    @ApiOperation(value = "财务开票 @author lidoudou")
    @PostMapping("/receive/order/invoice/apply")
    public ResponseDTO<String> applyInvoice(@RequestBody @Valid ApplyInvoiceForm applyInvoiceForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        applyInvoiceForm.setInvoiceUserId(requestUser.getUserId());
        applyInvoiceForm.setInvoiceUserName(requestUser.getUserName());

        return receiveOrderInvoiceService.invoice(applyInvoiceForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "财务批量开票 @author lidoudou")
    @PostMapping("/receive/order/invoice/batch/apply")
    public ResponseDTO<String> batchApplyInvoice(@RequestBody @Valid BatchApplyInvoiceForm batchApplyInvoiceForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        batchApplyInvoiceForm.setInvoiceUserId(requestUser.getUserId());
        batchApplyInvoiceForm.setInvoiceUserName(requestUser.getUserName());
        return receiveOrderInvoiceService.batchInvoice(batchApplyInvoiceForm, dataTracerRequestForm);
    }


    @ApiOperation(value = "上传对账单 @author lidoudou")
    @PostMapping("/receive/order/invoice/bill/upload")
    public ResponseDTO<String> uploadBill(@RequestBody @Valid InvoiceUploadBillForm uploadBillForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        uploadBillForm.setInvoiceUserId(requestUser.getUserId());
        uploadBillForm.setInvoiceUserName(requestUser.getUserName());
        return receiveOrderInvoiceService.uploadBill(uploadBillForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "作废开票申请 @author lidoudou")
    @PostMapping("/receive/order/invoice/cancel")
    public ResponseDTO<String> cancelInvoice(@RequestBody @Valid ReceiveOrderInvoiceCancelForm cancelForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        cancelForm.setOperateUserId(SmartRequestUtil.getRequestUserId());
        return receiveOrderInvoiceService.cancelInvoice(cancelForm, dataTracerRequestForm);
    }


    @ApiOperation(value = "更新发票 @author zhaikk")
    @PostMapping("/receive/order/invoice/update")
    public ResponseDTO<String> updateInvoice(@RequestBody @Valid UpdateInvoiceForm updateInvoiceForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return receiveOrderInvoiceService.updateInvoice(updateInvoiceForm,dataTracerRequestForm);
    }
}
