package net.lab1024.tms.driver.module.business.driver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverUpdateForm;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverVO;
import net.lab1024.tms.driver.module.business.driver.service.DriverService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@Api(tags = {DriverSwaggerTagConst.Business.DRIVER})
public class DriverController {

    @Resource
    private DriverService driverService;

    @ApiOperation(value = "司机信息 @author zhaoxinyang")
    @GetMapping("/driver/info")
    public ResponseDTO<DriverVO> info() {
        return driverService.getInfo(SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation(value = "司机信息 @author zhaoxinyang")
    @GetMapping("/driver/balance")
    public ResponseDTO<BigDecimal> getBalance() {
        return driverService.getBalance(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), SmartRequestUtil.getRequestUserId());
    }

    @RepeatSubmit
    @ApiOperation(value = "获取司机余额 @author lidoudou")
    @PostMapping("/driver/update")
    public ResponseDTO<String> update(@RequestBody @Valid DriverUpdateForm updateForm, HttpServletRequest request) {
        DataTracerRequestForm dataTracerRequestForm = new DataTracerRequestForm(request);
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return driverService.update(updateForm, dataTracerRequestForm);
    }

}
