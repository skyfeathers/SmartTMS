package net.lab1024.tms.driver.module.business.carcost.basicinfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@OperateLog
@RestController
@Api(tags = {DriverSwaggerTagConst.Business.CAR_COST_BASIC_INFO})
public class CarCostBasicInfoController {

    @Resource
    private CarCostBasicInfoService carCostBasicInfoService;

    @ApiOperation(value = "自有车-基本信息-查询费用确认状态 @author zhaoxinyang")
    @GetMapping("/car/cost/basicInfo/confirmFlag/{waybillId}")
    public ResponseDTO<Boolean> selectConfirmFlagByWaybillId(@PathVariable("waybillId") Long waybillId) {
        return ResponseDTO.ok(carCostBasicInfoService.selectConfirmFlagByWaybillId(waybillId));
    }

}