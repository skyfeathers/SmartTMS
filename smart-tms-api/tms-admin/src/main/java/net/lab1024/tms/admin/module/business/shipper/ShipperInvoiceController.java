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
import net.lab1024.tms.admin.module.business.shipper.service.ShipperInvoiceService;
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
 * [ 货主-开票信息 ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:22
 */
@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.SHIPPER_INVOICE})
public class ShipperInvoiceController {

    @Autowired
    private ShipperInvoiceService shipperInvoiceService;


    @ApiOperation(value = "查询货主的发票信息 @author lidoudou")
    @GetMapping("/shipper/invoice/{shipperId}")
    public ResponseDTO<List<ShipperInvoiceDTO>> selectInvoice(@PathVariable Long shipperId) {
        return shipperInvoiceService.selectInvoiceByShipperId(shipperId);
    }


    @RepeatSubmit
    @ApiOperation(value = "添加货主开票信息 @author yandy")
    @PostMapping("/shipper/invoice/save")
    public ResponseDTO<String> saveInvoice(@RequestBody @Valid ShipperInvoiceAddForm addDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperInvoiceService.saveInvoice(addDTO, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "添加货主开票信息 @author yandy")
    @PostMapping("/shipper/invoice/update")
    public ResponseDTO<String> updateInvoice(@RequestBody @Valid ShipperInvoiceUpdateForm addDTO, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperInvoiceService.updateInvoice(addDTO, dataTracerRequestForm);
    }

    @ApiOperation(value = "删除货主的发票信息 @author lidoudou")
    @GetMapping("/shipper/invoice/delete/{invoiceId}")
    public ResponseDTO<String> deleteInvoice(@PathVariable Long invoiceId) {
        return shipperInvoiceService.deleteInvoice(invoiceId);
    }

}
