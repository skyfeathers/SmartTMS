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
import net.lab1024.tms.admin.module.business.shipper.service.ShipperContactService;
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
@Api(tags = {AdminSwaggerTagConst.Business.SHIPPER_CONTACT})
public class ShipperContactController {

    @Autowired
    private ShipperContactService shipperContactService;

    @ApiOperation(value = "查询联系人 @author yandy")
    @GetMapping("/shipper/contact/query/{shipperId}")
    public ResponseDTO<List<ShipperContactDTO>> query(@PathVariable Long shipperId) {
        return shipperContactService.query(shipperId);
    }

    @RepeatSubmit
    @ApiOperation(value = "添加联系人 @author yandy")
    @PostMapping("/shipper/contact/save")
    public ResponseDTO<String> save(@RequestBody @Valid ShipperContactAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperContactService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "更新联系人 @author yandy")
    @PostMapping("/shipper/contact/update")
    public ResponseDTO<String> update(@RequestBody @Valid ShipperContactUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperContactService.update(updateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "删除联系人 @author yandy")
    @GetMapping("/shipper/contact/delete/{contactId}")
    public ResponseDTO<String> delete(@PathVariable Long contactId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperContactService.delete(contactId, dataTracerRequestForm);
    }

}
