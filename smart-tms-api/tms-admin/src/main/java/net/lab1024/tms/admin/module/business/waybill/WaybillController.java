package net.lab1024.tms.admin.module.business.waybill;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.waybill.domain.form.*;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.*;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillPathService;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherForm;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherUpdateForm;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.WAYBILL})
public class WaybillController {

    @Autowired
    private WaybillService waybillService;
    @Autowired
    private WaybillPathService waybillPathService;

    @ApiOperation(value = "创建运单 @author zhuoda")
    @PostMapping("/waybill/schedule")
    public ResponseDTO<String> createWaybill(@RequestBody @Valid WayBillCreateForm wayBillCreateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.createWaybill(wayBillCreateForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), dataTracerRequestForm);
    }

    @ApiOperation(value = "更新运单 @author zhuoda")
    @PostMapping("/waybill/update")
    public ResponseDTO<String> updateWaybill(@RequestBody @Valid WaybillUpdateForm waybillUpdateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.updateWaybill(waybillUpdateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "更新运单运输路线 @author yandy")
    @PostMapping("/waybill/path/update")
    @RepeatSubmit
    public ResponseDTO<String> waybillPathUpdate(@RequestBody @Valid WaybillPathUpdateForm waybillPathUpdateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillPathService.waybillPathUpdate(waybillPathUpdateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "统计费用金额 @author lidoudou")
    @PostMapping("/waybill/statisticAmount")
    public ResponseDTO<WaybillStatisticAmountVO> statisticAmount(@RequestBody @Valid WaybillQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return waybillService.statisticAmount(queryForm);
    }

    @ApiOperation(value = "统计费用金额 @author lidoudou")
    @GetMapping("/waybill/appStatisticAmount")
    public ResponseDTO<WaybillStatisticAmountAppVO> appStatisticAmount() {
        return waybillService.appStatisticAmount(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "查询运单列表 @author lidoudou")
    @PostMapping("/waybill/query")
    public ResponseDTO<PageResult<WaybillVO>> query(@RequestBody @Valid WaybillQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return waybillService.query(queryForm);
    }

    @ApiOperation(value = "根据查询条件统计运单数量 @author lidoudou")
    @PostMapping("/waybill/count")
    public ResponseDTO<Integer> countWaybill(@RequestBody @Valid WaybillQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return waybillService.count(queryForm);
    }

    @ApiOperation(value = "根据订单id查询运单列表 @author zhuoda")
    @GetMapping("/waybill/queryByOrderId/{orderId}")
    public ResponseDTO<List<WaybillVO>> queryByOrderId(@PathVariable Long orderId) {
        return waybillService.queryByOrderId(orderId);
    }

    @ApiOperation(value = "查询运单详情 @author zhuoda")
    @GetMapping("/waybill/getDetail/{waybillId}")
    public ResponseDTO<WaybillDetailVO> detail(@PathVariable Long waybillId) {
        return waybillService.detail(waybillId);
    }

    @ApiOperation(value = "运单费用维护 @author yandy")
    @PostMapping("/waybill/cost")
    public ResponseDTO<String> cost(@RequestBody @Valid WaybillCostForm waybillCostForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.cost(waybillCostForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "现金费用结算查询 @author yandy")
    @GetMapping("/waybill/cashCost/settle/query/{waybillId}")
    public ResponseDTO<WaybillSettleVO> cashCostSettleQuery(@PathVariable Long waybillId) {
        return waybillService.cashCostSettleQuery(waybillId);
    }

    @ApiOperation(value = "油卡费用结算查询 @author yandy")
    @GetMapping("/waybill/oilCardCost/settle/query/{waybillId}")
    public ResponseDTO<WaybillSettleVO> oilCardCostSettleQuery(@PathVariable Long waybillId) {
        return waybillService.oilCardCostSettleQuery(waybillId);
    }

    @ApiOperation(value = "运单-箱号和铅封号-更新 @author zhaoxinyang")
    @PostMapping("/waybill/leadSealAndContainerNumber/update")
    public ResponseDTO<String> updateLeadSealAndContainerNumber(@RequestBody @Valid WaybillLeadSealAndContainerNumberUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.updateLeadSealAndContainerNumber(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "作废 @author zhuoda")
    @GetMapping("/waybill/cancel/{waybillId}")
    public ResponseDTO<String> cancel(@PathVariable Long waybillId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.cancel(waybillId, dataTracerRequestForm);
    }

    @ApiOperation(value = "查询运单凭证 @author zhuoda")
    @GetMapping("/waybill/voucher/getVoucherListByWaybillId/{waybillId}")
    public ResponseDTO<List<WaybillVoucherVO>> getVoucherListByWaybillId(@PathVariable Long waybillId) {
        return ResponseDTO.ok(waybillService.getVoucherList(waybillId));
    }

    @ApiOperation(value = "上传凭证 @author zhuoda")
    @PostMapping("/waybill/addVoucher")
    public ResponseDTO<String> addVoucher(@RequestBody @Valid WaybillVoucherForm voucherForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.addVoucher(voucherForm, dataTracerRequestForm);
    }


    @ApiOperation(value = "更新凭证 @author zhuoda")
    @PostMapping("/waybill/updateVoucher")
    @RepeatSubmit
    public ResponseDTO<String> updateVoucher(@RequestBody @Valid WaybillVoucherUpdateForm voucherUpdateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.updateVoucher(voucherUpdateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "删除凭证 @author zhuoda")
    @GetMapping("/waybill/deleteVoucher/{waybillVoucherId}")
    @RepeatSubmit
    public ResponseDTO<String> deleteVoucher(@PathVariable Long waybillVoucherId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.deleteVoucher(waybillVoucherId, dataTracerRequestForm);
    }

    @ApiOperation(value = "分段运输删除司机信息 @author zhuoda")
    @GetMapping("/waybill/splitTransport/delete/{splitTransportId}")
    public ResponseDTO<String> splitTransportDelete(@PathVariable Long splitTransportId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.splitTransportDelete(splitTransportId, dataTracerRequestForm);
    }

    @ApiOperation(value = "分段运输分配司机信息 @author zhuoda")
    @PostMapping("/waybill/splitTransport/dispatch")
    public ResponseDTO<String> splitTransportDispatch(@RequestBody @Valid WaybillSplitTransportDispatchForm dispatchForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.splitTransportDispatch(dispatchForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "查询分段运输信息 @author zhuoda")
    @GetMapping("/waybill/getSplitTransport/{waybillId}")
    public ResponseDTO<List<WaybillSplitTransportVO>> getSplitTransport(@PathVariable Long waybillId) {
        return ResponseDTO.ok(waybillService.getSplitTransport(waybillId));
    }


    @ApiOperation(value = "更新为运输完成 @author lidoudou")
    @PostMapping("/waybill/complete")
    public ResponseDTO<String> complete(@RequestBody @Valid WaybillCompleteForm completeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.complete(completeForm, dataTracerRequestForm);
    }


    @ApiOperation(value = "上传回单 @author lidoudou")
    @PostMapping("/waybill/receipt/upload")
    public ResponseDTO<String> uploadReceiptAttachment(@RequestBody @Valid WaybillReceiptUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.uploadReceiptAttachment(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "上传派车单 @author lidoudou")
    @PostMapping("/waybill/truckOrder/upload")
    public ResponseDTO<String> uploadTruckOrderAttachment(@RequestBody @Valid WaybillTruckOrderUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.uploadTrunkOrderAttachment(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "运单列表导出 by lidoudou")
    @GetMapping("/waybill/export")
    public void export(WaybillQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<WaybillExportVO> list = waybillService.queryByExport(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "运单列表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, WaybillExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

}
