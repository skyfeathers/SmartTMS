package net.lab1024.tms.admin.module.business.shipper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPaymentWayDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.form.*;
import net.lab1024.tms.admin.module.business.shipper.service.ShipperPaymentWayService;
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
 *
 * @author yandanyang
 * @date 2020/7/30 14:22
 */
@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.SHIPPER_PAYMENT})
public class ShipperPaymentWayController {

    @Autowired
    private ShipperPaymentWayService shipperPaymentWayService;

    @ApiOperation(value = "查询 @author yandy")
    @GetMapping("/shipper/paymentWay/query/{shipperId}")
    public ResponseDTO<List<ShipperPaymentWayDTO>> query(@PathVariable Long shipperId) {
        return shipperPaymentWayService.query(shipperId);
    }

    @RepeatSubmit
    @ApiOperation(value = "添加 @author yandy")
    @PostMapping("/shipper/paymentWay/save")
    public ResponseDTO<String> save(@RequestBody @Valid ShipperPaymentWayAddForm addForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperPaymentWayService.save(addForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "更新 @author yandy")
    @PostMapping("/shipper/paymentWay/update")
    public ResponseDTO<String> update(@RequestBody @Valid ShipperPaymentWayUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperPaymentWayService.update(updateForm, dataTracerRequestForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "删除 @author yandy")
    @GetMapping("/shipper/paymentWay/delete/{paymentWayId}")
    public ResponseDTO<String> delete(@PathVariable Long paymentWayId, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return shipperPaymentWayService.delete(paymentWayId, dataTracerRequestForm);
    }
}
