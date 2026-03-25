package net.lab1024.tms.driver.module.business.driver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverBankBaseForm;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverBankUpdateForm;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverBankDetailVO;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverBankVO;
import net.lab1024.tms.driver.module.business.driver.service.DriverBankService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = {DriverSwaggerTagConst.Business.DRIVER})
public class DriverBankController {

    @Resource
    private DriverBankService driverBankService;

    @ApiOperation(value = "查询司机银行列表 @author zhaoxinyang")
    @GetMapping("/driver/bankList")
    public ResponseDTO<List<DriverBankVO>> selectDriverBankList() {
        Long driverId = SmartRequestUtil.getRequestUserId();
        return driverBankService.selectDriverBankList(driverId);
    }

    @ApiOperation(value = "添加司机银行 @author zhaoxinyang")
    @PostMapping("/driver/bank/add")
    public ResponseDTO<String> saveDriverBank(@RequestBody @Valid DriverBankBaseForm bankBaseForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return driverBankService.saveDriverBank(bankBaseForm, requestUser, dataTracerRequestForm);
    }

    @ApiOperation(value = "修改司机银行 @author zhaoxinyang")
    @PostMapping("/driver/bank/update")
    public ResponseDTO<String> updateDriverBank(@RequestBody @Valid DriverBankUpdateForm bankUpdateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        return driverBankService.updateDriverBank(bankUpdateForm, dataTracerRequestForm);
    }

    @ApiOperation(value = "获取司机银行详情 @author zhaoxinyang")
    @GetMapping("/driver/bank/detail/{bankId}")
    public ResponseDTO<DriverBankDetailVO> getBankDetail(@PathVariable("bankId") Long BankId) {
        return driverBankService.getBankDetail(BankId);
    }

}
