package net.lab1024.tms.driver.module.business.waybill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherForm;
import net.lab1024.tms.common.module.business.waybill.domain.vo.WaybillVoucherVO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.waybill.domain.form.WaybillQueryForm;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.WaybillCountVO;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.WaybillDetailVO;
import net.lab1024.tms.driver.module.business.waybill.domain.vo.WaybillVO;
import net.lab1024.tms.driver.module.business.waybill.service.WaybillService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhaoxinyang
 *
 * @description:
 * @date 2023/8/14 8:47
 */
@Slf4j
@RestController
@OperateLog
@Api(tags = {DriverSwaggerTagConst.Business.WAYBILL})
public class WaybillController {

    @Resource
    private WaybillService waybillService;
    
    @ApiOperation(value = "查询运单列表 @author zhaoxinyang")
    @PostMapping("/waybill/query")
    public ResponseDTO<PageResult<WaybillVO>> query(@RequestBody @Valid WaybillQueryForm queryForm) {
        queryForm.setDriverId(SmartRequestUtil.getRequestUserId());
        return waybillService.selectWaybillPage(queryForm);
    }
    
    @ApiOperation(value = "查询运单详情 @author zhaoxinyang")
    @GetMapping("/waybill/getDetail/{waybillId}")
    public ResponseDTO<WaybillDetailVO> detail(@PathVariable Long waybillId) {
        return waybillService.selectWaybillDetail(waybillId);
    }
    
    @ApiOperation(value = "上传凭证 @author zhaoxinyang")
    @PostMapping("/waybill/addVoucher")
    public ResponseDTO<String> addVoucher(@RequestBody @Valid WaybillVoucherForm voucherForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return waybillService.addVoucher(voucherForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "查询运单凭证 @author zhaoxinyang")
    @GetMapping("/waybill/voucher/getVoucherListByWaybillId/{waybillId}")
    public ResponseDTO<List<WaybillVoucherVO>> getVoucherListByWaybillId(@PathVariable Long waybillId) {
        return ResponseDTO.ok(waybillService.getVoucherList(waybillId));
    }

    @ApiOperation(value = "统计司机运单数量和运输完成的运单数量 @author zhaoxinyang")
    @GetMapping("/waybill/count")
    public ResponseDTO<WaybillCountVO> selectWaybillCount() {
        Long driverId = SmartRequestUtil.getRequestUserId();
        return waybillService.selectWaybillCount(driverId);
    }

}
