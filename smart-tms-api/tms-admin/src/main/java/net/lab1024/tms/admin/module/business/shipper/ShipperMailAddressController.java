package net.lab1024.tms.admin.module.business.shipper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperContactDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperMailAddressDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperMailAddressAddForm;
import net.lab1024.tms.admin.module.business.shipper.domain.form.ShipperMailAddressUpdateForm;
import net.lab1024.tms.admin.module.business.shipper.service.ShipperMailAddressService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@Api(tags = {AdminSwaggerTagConst.Business.SHIPPER_MAIL_ADDRESS})
public class ShipperMailAddressController {

    @Autowired
    private ShipperMailAddressService shipperMailAddressService;

    @ApiOperation(value = "查询邮寄信息 @author yandy")
    @GetMapping("/shipper/mailAddress/query/{shipperId}")
    public ResponseDTO<List<ShipperMailAddressDTO>> query(@PathVariable Long shipperId) {
        return shipperMailAddressService.query(shipperId);
    }

    @RepeatSubmit
    @ApiOperation(value = "添加邮寄信息 @author yandy")
    @PostMapping("/shipper/mailAddress/save")
    public ResponseDTO<String> save(@RequestBody @Valid ShipperMailAddressAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperMailAddressService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "更新邮寄信息 @author yandy")
    @PostMapping("/shipper/mailAddress/update")
    public ResponseDTO<String> update(@RequestBody @Valid ShipperMailAddressUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperMailAddressService.update(updateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "删除邮寄信息 @author yandy")
    @GetMapping("/shipper/mailAddress/delete/{mailAddressId}")
    public ResponseDTO<String> delete(@PathVariable Long mailAddressId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperMailAddressService.delete(mailAddressId, dataTracerRequestForm);
    }
}
