package net.lab1024.tms.admin.module.business.shipper;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperBasicDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperContactDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperInvoiceDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.*;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperDetailVO;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperExportVO;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperListVO;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperSimpleVO;
import net.lab1024.tms.admin.module.business.shipper.service.ShipperService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.domain.ValidateList;
import net.lab1024.tms.common.common.excel.ExcelStyle;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
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
 * [ 货主 ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:22
 */
@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.SHIPPER})
public class ShipperController {

    @Autowired
    private ShipperService shipperService;

    @ApiOperation(value = "分页查询货主 @author yandy")
    @PostMapping("/shipper/page/query")
    public ResponseDTO<PageResult<ShipperListVO>> queryByPage(@RequestBody @Valid ShipperQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询货主基础信息列表 @author yandy")
    @PostMapping("/shipper/list")
    public ResponseDTO<List<ShipperBasicDTO>> queryList() {
        return shipperService.queryBasicList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @RepeatSubmit
    @ApiOperation(value = "添加货主 @author yandy")
    @PostMapping("/shipper/save")
    public ResponseDTO<String> save(@RequestBody @Valid ShipperAddForm addDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        addDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperService.save(addDTO, dataTracerRequestForm);
    }

    @ApiOperation(value = "货主详情 @author yandy")
    @GetMapping("/shipper/detail/{shipperId}")
    public ResponseDTO<ShipperDetailVO> detail(@PathVariable Long shipperId) {
        return shipperService.getDetail(shipperId);
    }

    @ApiOperation(value = "货主基本信息详情 @author yandy")
    @GetMapping("/shipper/simpleDetail/{shipperId}")
    public ResponseDTO<ShipperSimpleVO> simpleDetail(@PathVariable Long shipperId) {
        return shipperService.simpleDetail(shipperId);
    }

    @RepeatSubmit
    @ApiOperation(value = "更新货主 @author yandy")
    @PostMapping("/shipper/update")
    public ResponseDTO<String> update(@RequestBody @Valid ShipperUpdateForm updateDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperService.update(updateDTO, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "删除货主 @author yandy")
    @PostMapping("/shipper/batch/delete")
    public ResponseDTO<String> batchDelete(@RequestBody @Valid ValidateList<Long> shipperIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperService.batchDelete(shipperIdList, dataTracerRequestForm);
    }

    @ApiOperation(value = "查询默认联系人 @author lidoudou")
    @GetMapping("/shipper/default/contact/{shipperId}")
    public ResponseDTO<ShipperContactDTO> selectContact(@PathVariable Long shipperId) {
        return shipperService.selectDefaultContact(shipperId);
    }

    @ApiOperation("批量审核 by lidoudou")
    @PostMapping("/shipper/batch/audit")
    public ResponseDTO<String> batchAudit(@RequestBody @Valid ShipperBatchAuditForm batchAuditDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperService.batchAudit(batchAuditDTO, dataTracerRequestForm);
    }
    // ==================== 更新货主的负责人 start =========================

    @ApiOperation(value = "更新业务负责人 @author lidoudou")
    @PostMapping("/shipper/manager/update")
    public ResponseDTO<String> updateManager(@RequestBody @Valid ShipperManagerUpdateForm updateDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperService.updateManager(updateDTO, dataTracerRequestForm);
    }

    @ApiOperation(value = "更新客服负责人 @author lidoudou")
    @PostMapping("/shipper/principal/update")
    public ResponseDTO<String> updatePrincipal(@RequestBody @Valid ShipperPrincipalUpdateForm updateDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperService.updatePrincipal(updateDTO, dataTracerRequestForm);
    }
    // ==================== 更新货主的负责人 end =========================

    @RepeatSubmit
    @ApiOperation(value = "更新校验状态 @author lidoudou")
    @PostMapping("/shipper/principal/batch/update/verify")
    public ResponseDTO<String> updateVerifyFlag(@RequestBody List<Long> shipperIdList, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperService.updateVerifyFlag(shipperIdList, dataTracerRequestForm);
    }


    @ApiOperation("查询是否企业是否有重名的货主 true存在 false不存在 by lidoudou")
    @PostMapping("/shipper/query/enterprise/name")
    public ResponseDTO<Boolean> queryByName(@RequestBody ShipperNameExistsForm existsForm) {
        existsForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return shipperService.queryByName(existsForm);
    }

    @ApiOperation(value = "货主导出 @author zhaikk")
    @GetMapping("/shipper/export")
    public void export(ShipperQueryForm queryForm, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        List<ShipperExportVO> list = shipperService.queryByExport(queryForm);
        ExportParams exportParams = new ExportParams();
        String title = "货主列表";
        exportParams.setTitle(title);
        exportParams.setSheetName(title);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setStyle(ExcelStyle.class);
        map.put(NormalExcelConstants.FILE_NAME, title);
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, ShipperExportVO.class);
        map.put(NormalExcelConstants.PARAMS, exportParams);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
