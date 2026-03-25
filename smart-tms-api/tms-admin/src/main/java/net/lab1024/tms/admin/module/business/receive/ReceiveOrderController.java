package net.lab1024.tms.admin.module.business.receive;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.receive.domain.form.*;
import net.lab1024.tms.admin.module.business.receive.domain.vo.*;
import net.lab1024.tms.admin.module.business.receive.service.ReceiveOrderService;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.RECEIVE_ORDER})
public class ReceiveOrderController {

    @Autowired
    private ReceiveOrderService receiveOrderService;

    @ApiOperation("获取提交对账的货主展示信息")
    @PostMapping("/receive/order/submit/info")
    public ResponseDTO<ReceiveOrderSubmitVO> getCheckInfo(@RequestBody List<Long> waybillIdList) {
        return receiveOrderService.getShipperCheckInfo(waybillIdList, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "提交应收核算 @author lidoudou")
    @PostMapping("/receive/order/submit")
    public ResponseDTO<String> submit(@RequestBody @Valid ReceiveOrderSubmitForm submitForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        submitForm.setCreateUserId(dataTracerRequestForm.getOperatorId());
        submitForm.setCreateUserName(dataTracerRequestForm.getOperatorName());

        submitForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.submit(submitForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "提交应收核算并申请开票 @author lidoudou")
    @PostMapping("/receive/order/submit/invoice")
    public ResponseDTO<String> submitInvoice(@RequestBody @Valid ReceiveOrderForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addForm.setCreateUserId(dataTracerRequestForm.getOperatorId());
        addForm.setCreateUserName(dataTracerRequestForm.getOperatorName());

        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.submitInvoice(addForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "分页查询 @author lidoudou")
    @PostMapping("/receive/order/page/query")
    public ResponseDTO<PageResult<ReceiveOrderVO>> queryByPage(@RequestBody @Valid ReceiveQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.queryByPage(queryForm);
    }

    @ApiOperation(value = "应收金额统计 @author zhaoxinyang")
    @PostMapping("/receive/amount/statistic")
    public ResponseDTO<ReceiveAmountStatisticsVO> amountStatistics(@RequestBody @Valid ReceiveQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.amountStatistics(queryForm);
    }

    @ApiOperation(value = "分页查询 @author lidoudou")
    @PostMapping("/receive/order/shipper/page/query")
    public ResponseDTO<PageResult<ReceiveOrderVO>> queryByShipperPage(@RequestBody @Valid ShipperReceiveQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.queryPageByShipper(queryForm);
    }

    @GetMapping("/receive/order/{receiveOrderId}")
    @ApiOperation(value = "应收款详情 @author lidoudou")
    public ResponseDTO<ReceiveOrderDetailVO> getDetail(@PathVariable Long receiveOrderId) {
        return receiveOrderService.getDetail(receiveOrderId);
    }

    @ApiOperation(value = "确认核算 @author lidoudou")
    @PostMapping("/receive/order/check")
    public ResponseDTO<String> confirmCheck(@RequestBody @Valid ReceiveCheckForm checkForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        checkForm.setCheckUserId(SmartRequestUtil.getRequestUserId());
        checkForm.setCheckUserName(SmartRequestUtil.getRequestUser().getUserName());
        return receiveOrderService.confirmCheck(checkForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "核销 @author lidoudou")
    @PostMapping("/receive/order/verification")
    public ResponseDTO<String> verification(@RequestBody @Valid ReceiveVerificationForm verificationForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        verificationForm.setCreateUserId(requestUser.getUserId());
        verificationForm.setCreateUserName(requestUser.getUserName());

        verificationForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.verification(verificationForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "批量核销 @author lidoudou")
    @PostMapping("/receive/order/batch/verification")
    public ResponseDTO<String> batchVerification(@RequestBody @Valid ReceiveBatchVerificationForm batchVerificationForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        batchVerificationForm.setCreateUserId(requestUser.getUserId());
        batchVerificationForm.setCreateUserName(requestUser.getUserName());
        batchVerificationForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.batchVerification(batchVerificationForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "获取批量核销的合计金额信息 @author lidoudou")
    @PostMapping("/receive/order/batch/verification/amount")
    public ResponseDTO<ReceiveOrderVerificationAmountVO> batchVerificationAmount(@RequestBody @Valid ValidateList<Long> receiveOrderIdList) {
        return receiveOrderService.batchVerificationAmount(receiveOrderIdList);
    }

    @ApiOperation(value = "作废对账单 @author lidoudou")
    @PostMapping("/receive/order/cancel")
    public ResponseDTO<String> cancel(@RequestBody @Valid ReceiveOrderCancelForm cancelForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        cancelForm.setOperateUserId(SmartRequestUtil.getRequestUserId());
        return receiveOrderService.cancel(cancelForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "作废未核销 @author lidoudou")
    @PostMapping("/receive/order/verification/cancel")
    public ResponseDTO<String> cancelVerification(@RequestBody @Valid ReceiveOrderCancelForm cancelForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        cancelForm.setOperateUserId(SmartRequestUtil.getRequestUserId());
        return receiveOrderService.cancelVerification(cancelForm, dataTracerRequestForm);
    }

    @PostMapping("/receive/order/wait/query")
    @ApiOperation(value = "首页-查询待收款的订单 @author lidoudou")
    public ResponseDTO<PageResult<ReceiveOrderAmountVO>> queryWaitReceiveByPage(@RequestBody @Valid WaitReceiveQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return receiveOrderService.queryWaitReceiveByPage(queryForm);
    }

    @GetMapping("/receive/order/queryByWaybill/{waybillId}")
    @ApiOperation(value = "根据运单查询付款单列表 @author lidoudou")
    public ResponseDTO<List<ReceiveOrderVO>> queryByWaybill(@PathVariable Long waybillId) {
        return receiveOrderService.queryByWaybill(waybillId);
    }

    @ApiOperation(value = "应收核算 @author lidoudou")
    @GetMapping("/receive/order/check/export")
    public void exportCheckList(ReceiveQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ReceiveOrderVO> list = receiveOrderService.query4Export(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "应收核算";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ReceiveOrderVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "应收核销 @author lidoudou")
    @GetMapping("/receive/order/export")
    public void export(ReceiveQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ReceiveOrderVO> list = receiveOrderService.query4Export(queryForm);
        List<ReceiveOrderExportVO> exportList = SmartBeanUtil.copyList(list, ReceiveOrderExportVO.class);
        ExportParams exportParams = new ExportParams();
        String title = "应收核销";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportList);
        map.put(NormalExcelConstants.CLASS, ReceiveOrderExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "应收帐款 @author lidoudou")
    @GetMapping("/receive/order/wait/export")
    public void customerExport(WaitReceiveQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ReceiveOrderAmountReportExportVO> list = receiveOrderService.exportWaitReceive(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "应收帐款";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ReceiveOrderAmountReportExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    @ApiOperation(value = "应收核销导出 @author lidoudou")
    @GetMapping("/receive/order/verification/export")
    public void exportVerificationList(ReceiveQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ReceiveOrderReportExportVO> list = receiveOrderService.queryVerification4Export(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "应收帐款";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ReceiveOrderReportExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    @ApiOperation(value = "应收核销明细导出 @author zhaikk")
    @GetMapping("/receive/order/verification/item/export")
    public void exportVerificationItemList(ReceiveQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ReceiveOrderReportExportItemVO> list = receiveOrderService.queryVerificationItemExport(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "应收核销明细";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ReceiveOrderReportExportItemVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "应收-财务开票-导出 @author lidoudou")
    @GetMapping("/receive/order/make/invoice/export/{receiveOrderId}")
    public void makeInvoiceExport(@PathVariable Long receiveOrderId, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<ReceiveOrderMakeInvoiceExcelVO> list = receiveOrderService.makeInvoiceExport(receiveOrderId);
        List<ReceiveOrderMakeInvoiceExcelVO> exportList = SmartBeanUtil.copyList(list, ReceiveOrderMakeInvoiceExcelVO.class);
        ExportParams exportParams = new ExportParams();
        String title = "财务开票";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportList);
        map.put(NormalExcelConstants.CLASS, ReceiveOrderMakeInvoiceExcelVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "应收-对账明细-导出 @author lidoudou")
    @GetMapping("/receive/order/waybill/export/{receiveOrderId}")
    public void waybillExport(@PathVariable Long receiveOrderId, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<ReceiveOrderWaybillExcelVO> list = receiveOrderService.waybillExport(receiveOrderId);
        List<ReceiveOrderWaybillExcelVO> exportList = SmartBeanUtil.copyList(list, ReceiveOrderWaybillExcelVO.class);
        ExportParams exportParams = new ExportParams();
        String title = "对账明细";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportList);
        map.put(NormalExcelConstants.CLASS, ReceiveOrderWaybillExcelVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}