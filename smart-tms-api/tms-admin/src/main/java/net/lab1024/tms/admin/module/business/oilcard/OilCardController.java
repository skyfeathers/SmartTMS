package net.lab1024.tms.admin.module.business.oilcard;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.*;
import net.lab1024.tms.admin.module.business.oilcard.domain.vo.*;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardBalanceRecordService;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardService;
import net.lab1024.tms.common.common.annoation.NoNeedLogin;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.OIL_CARD})
public class OilCardController {

    @Autowired
    private OilCardService oilCardService;

    @Autowired
    private OilCardBalanceRecordService balanceRecordService;

    @ApiOperation(value = "分页查询 @author lidoudou")
    @PostMapping("/oil/card/page/query")
    public ResponseDTO<PageResult<OilCardVO>> queryByPage(@RequestBody @Valid OilCardQueryForm queryForm) {
        return oilCardService.queryByPage(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation(value = "分页查询 @author lidoudou")
    @PostMapping("/oil/card/summary")
    public ResponseDTO<BigDecimal> cardSummary(@RequestBody @Valid OilCardQueryForm queryForm) {
        return oilCardService.cardSummary(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), SmartRequestUtil.getRequestUserId());
    }


    @ApiOperation(value = "查询不分页的列表 @author lidoudou")
    @PostMapping("/oil/card/query/list")
    public ResponseDTO<List<OilCardVO>> queryList(@RequestBody OilCardQueryForm queryForm) {
        return oilCardService.queryList(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation(value = "根据车辆ID查询绑定油卡列表 @author lidoudou")
    @GetMapping("/oil/card/vehicle/query/list/{vehicleId}")
    public ResponseDTO<List<OilCardSimpleVO>> queryListByVehicleId(@PathVariable Long vehicleId) {
        return oilCardService.queryListByVehicleId(vehicleId);
    }

    @ApiOperation(value = "详情 @author lidoudou")
    @GetMapping("/oil/card/detail/{oilCardId}")
    public ResponseDTO<OilCardVO> detail(@PathVariable Long oilCardId) {
        return ResponseDTO.ok(oilCardService.getDetail(oilCardId));
    }

    @RepeatSubmit
    @ApiOperation(value = "新建 @author lidoudou")
    @PostMapping("/oil/card/save")
    public ResponseDTO<String> add(@RequestBody @Valid OilCardAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        Long enterpriseId = SmartRequestEnterpriseUtil.getRequestEnterpriseId();
        addForm.setEnterpriseId(enterpriseId);
        return oilCardService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "编辑 @author lidoudou")
    @PostMapping("/oil/card/update")
    public ResponseDTO<String> update(@RequestBody @Valid OilCardUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        Long enterpriseId = SmartRequestEnterpriseUtil.getRequestEnterpriseId();
        updateForm.setEnterpriseId(enterpriseId);
        return oilCardService.update(updateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "批量删除 @author lidoudou")
    @PostMapping("/oil/card/update/batch/delete")
    public ResponseDTO<String> batchUpdateDeleteFlag(@RequestBody List<Long> oilCardIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return oilCardService.batchUpdateDeletedFlag(oilCardIdList, dataTracerRequestForm);
    }

    @ApiOperation(value = "充值 @author lidoudou")
    @PostMapping("/oil/card/recharge")
    public ResponseDTO<String> recharge(@RequestBody @Valid OilCardBalanceChangeForm rechargeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        rechargeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        rechargeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        return oilCardService.recharge(rechargeForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "手动消耗 author lidoudou")
    @PostMapping("/oil/card/expend")
    public ResponseDTO<String> expend(@RequestBody @Valid OilCardBalanceChangeForm rechargeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        rechargeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        rechargeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        return oilCardService.expend(rechargeForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "挂失圈回 油卡禁用 author lidoudou")
    @PostMapping("/oil/card/cancel")
    public ResponseDTO<String> cancel(@RequestBody @Valid OilCardBalanceChangeForm rechargeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        rechargeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        rechargeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        return oilCardService.cancel(rechargeForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "圈回 author lidoudou")
    @PostMapping("/oil/card/circumflex")
    public ResponseDTO<String> circumflex(@RequestBody @Valid OilCardBalanceChangeForm rechargeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        rechargeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        rechargeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        return oilCardService.circumflex(rechargeForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "分配副卡 author lidoudou")
    @PostMapping("/oil/slave/card/distribute")
    public ResponseDTO<String> distributeSlaveCard(@RequestBody @Valid OilCardBalanceChangeForm rechargeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        rechargeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        rechargeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        return oilCardService.distributeSlaveCard(rechargeForm, dataTracerRequestForm);
    }


    @ApiOperation(value = "查询交易记录 @author lidoudou")
    @PostMapping("/oil/card/balance/record/page/query")
    public ResponseDTO<PageResult<OilCardBalanceRecordVO>> queryBalanceRecordPage(@RequestBody @Valid OilCardBalanceRecordQueryForm queryForm) {
        return balanceRecordService.queryByPage(queryForm);
    }

    @Deprecated
    @ApiOperation(value = "副卡分配公司 author lidoudou")
    @PostMapping("/oil/slave/card/enterprise/update")
    public ResponseDTO<String> batchUpdateEnterprise(@RequestBody @Valid SlaveOilCardEnterpriseUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        updateForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        return oilCardService.batchUpdateEnterprise(updateForm, dataTracerRequestForm);
    }


    @ApiOperation(value = "导出主卡")
    @GetMapping("/oil/card/master/export")
    @NoNeedLogin
    public void exportMasterOilCard(OilCardQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<OilCardExportVO> list = oilCardService.queryByExport(queryForm, SmartRequestEnterpriseUtil.getRequestEnterpriseId(), SmartRequestUtil.getRequestUserId());
        ExportParams exportParams = new ExportParams();
        String title = "油卡-主卡";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, OilCardExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "导出")
    @GetMapping("/oil/card/balance/export")
    public void balanceRecordExport(OilCardBalanceRecordQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        ResponseDTO<List<OilCardBalanceRecordExportVO>> listResp = balanceRecordService.queryByExport(queryForm);
        if (!listResp.getOk()) {
            throw new RuntimeException(listResp.getMsg());
        }
        ExportParams exportParams = new ExportParams();
        String title = "油卡余额交易记录";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, listResp.getData());
        map.put(NormalExcelConstants.CLASS, OilCardBalanceRecordExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation("导入油卡副卡 by lidoudou")
    @PostMapping("/oil/card/slave/excel/import")
    public ResponseDTO<String> excelImport(SlaveOilCardImportForm importForm, MultipartFile uploadFile,HttpServletRequest request) throws Exception {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        Long enterpriseId = SmartRequestEnterpriseUtil.getRequestEnterpriseId();
        importForm.setEnterpriseId(enterpriseId);
        return oilCardService.excelImport(importForm, uploadFile, dataTracerRequestForm);
    }

    @ApiOperation("编辑计划充值金额 by lidoudou")
    @PostMapping("/oil/card/preRechargeAmount/update")
    public ResponseDTO<String> updatePreRechargeAmount(@RequestBody @Valid OilCardPreRechargeAmountUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return oilCardService.updatePreRechargeAmount(updateForm, dataTracerRequestForm);
    }

    @ApiOperation("编辑计划充值金额 by lidoudou")
    @PostMapping("/oil/card/preRechargeAmount/set")
    public ResponseDTO<String> setPreRechargeAmount(@RequestBody @Valid OilCardPreRechargeAmountUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return oilCardService.setPreRechargeAmount(updateForm, dataTracerRequestForm);
    }

    @ApiOperation("根据计划充值金额去充值 by lidoudou")
    @PostMapping("/oil/card/preRechargeAmount/recharge")
    public ResponseDTO<String> rechargeByPreRechargeAmount(@RequestBody @Valid OilCardBalanceChangeForm rechargeForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        rechargeForm.setOperatorId(dataTracerRequestForm.getOperatorId());
        rechargeForm.setOperatorName(dataTracerRequestForm.getOperatorName());
        return oilCardService.rechargeByPreRechargeAmount(rechargeForm, dataTracerRequestForm);
    }
}
