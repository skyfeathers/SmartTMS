package net.lab1024.tms.admin.module.business.pay;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.pay.domain.form.*;
import net.lab1024.tms.admin.module.business.pay.domain.vo.*;
import net.lab1024.tms.admin.module.business.pay.service.PayOrderService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 5:40 下午
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.PAY})
public class PayOrderController {

    @Autowired
    private PayOrderService payOrderService;


    @RepeatSubmit
    @PostMapping("/payOrder/create")
    @ApiOperation(value = "创建付款单 @author yandy")
    public ResponseDTO<String> payOrderCreate(@RequestBody @Valid PayOrderCreateForm payOrderCreateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        payOrderCreateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return payOrderService.payOrderCreate(payOrderCreateForm, dataTracerRequestForm);
    }

    @GetMapping("/payOrder/detail/{payOrderId}")
    @ApiOperation(value = "查询付款单详情 @author yandy")
    public ResponseDTO<PayOrderDetailVO> detail(@PathVariable Long payOrderId) {
        return payOrderService.detail(payOrderId);
    }

    @PostMapping("/payOrder/query")
    @ApiOperation(value = "查询付款单列表 @author zhuoda")
    public ResponseDTO<PageResult<PayOrderVO>> query(@RequestBody @Valid PayOrderQueryForm payOrderQueryForm) {
        payOrderQueryForm.setRequestUserId(SmartRequestUtil.getRequestUserId());
        payOrderQueryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return payOrderService.query(payOrderQueryForm);
    }

    @PostMapping("/payOrder/query/amount/statistics")
    @ApiOperation(value = "统计付款单应付、已付合计 @author lidoudou")
    public ResponseDTO<PayOrderAmountStatisticVO> amountStatistics(@RequestBody @Valid PayOrderQueryForm payOrderQueryForm) {
        payOrderQueryForm.setRequestUserId(SmartRequestUtil.getRequestUserId());
        payOrderQueryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return payOrderService.queryAmountStatistics(payOrderQueryForm);
    }

    @GetMapping("/payOrder/queryByWaybill/{waybillId}")
    @ApiOperation(value = "根据运单查询付款单列表 @author yandy")
    public ResponseDTO<List<PayOrderVO>> queryByWaybill(@PathVariable Long waybillId) {
        return payOrderService.queryByWaybill(waybillId, SmartRequestUtil.getRequestUserId());
    }

    @PostMapping("/payOrder/oilCard/recharge")
    @ApiOperation(value = "油卡充值 @author lidoudou")
    public ResponseDTO<String> oilCardRecharge(@RequestBody @Valid OilCardRechargeForm rechargeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return payOrderService.oilCardRecharge(rechargeForm, dataTracerRequestForm);
    }


    @PostMapping("/payOrder/pay")
    @ApiOperation(value = "付款 @author yandy")
    public ResponseDTO<String> pay(@RequestBody @Valid PayOrderPaymentForm payOrderPaymentForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return payOrderService.pay(payOrderPaymentForm, dataTracerRequestForm);
    }

    @PostMapping("/payOrder/batch/pay")
    @ApiOperation(value = "批量付款 @author yandy")
    public ResponseDTO<String> batchPay(@RequestBody @Valid PayOrderBatchPaymentForm batchPaymentForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return payOrderService.batchPay(batchPaymentForm, dataTracerRequestForm);
    }

    @PostMapping("/payOrder/verification")
    @ApiOperation(value = "核销 @author yandy")
    public ResponseDTO<String> verification(@RequestBody @Valid PayOrderVerificationForm payOrderVerificationForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return payOrderService.verification(payOrderVerificationForm, dataTracerRequestForm);
    }

    @PostMapping("/payOrder/batch/verification")
    @ApiOperation(value = "核销 @author yandy")
    public ResponseDTO<String> batchVerification(@RequestBody @Valid PayOrderBatchVerificationForm batchVerificationForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return payOrderService.batchVerification(batchVerificationForm, dataTracerRequestForm);
    }

    @PostMapping("/payOrder/cancel")
    @ApiOperation(value = "作废 @author lidoudou")
    public ResponseDTO<String> cancel(@RequestBody @Valid PayOrderCancelForm payOrderCancelForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return payOrderService.cancel(payOrderCancelForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "导出")
    @GetMapping("/payOrder/export")
    public void export(PayOrderQueryForm payOrderQueryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        payOrderQueryForm.setRequestUserId(SmartRequestUtil.getRequestUserId());
        payOrderQueryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<PayOrderExportVO> list = payOrderService.queryByExport(payOrderQueryForm);
        ExportParams exportParams = new ExportParams();
        String title = "应付账款明细表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, PayOrderExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    @ApiOperation(value = "应付账款运单导出")
    @GetMapping("/payOrder/waybill/export")
    public void exportByWaybill(PayOrderQueryForm payOrderQueryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        payOrderQueryForm.setRequestUserId(SmartRequestUtil.getRequestUserId());
        payOrderQueryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<PayOrderWaybillExportVO> exportList = payOrderService.exportByWaybill(payOrderQueryForm);
        ExportParams exportParams = new ExportParams();
        String title = "应付账款运单明细表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, exportList);
        map.put(NormalExcelConstants.CLASS, PayOrderWaybillExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}