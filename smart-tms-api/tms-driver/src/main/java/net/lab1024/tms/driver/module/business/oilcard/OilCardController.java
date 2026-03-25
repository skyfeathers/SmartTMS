package net.lab1024.tms.driver.module.business.oilcard;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.driver.module.business.oilcard.domain.OilCardSimpleVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {DriverSwaggerTagConst.Business.OIL_CARD})
public class OilCardController {

    @Resource
    private OilCardService oilCardService;

    @ApiOperation(value = "根据车辆ID查询绑定油卡列表 @author lidoudou")
    @GetMapping("/oil/card/vehicle/query/list/{vehicleId}")
    public ResponseDTO<List<OilCardSimpleVO>> queryListByVehicleId(@PathVariable Long vehicleId) {
        return oilCardService.queryListByVehicleId(vehicleId);
    }

    @ApiOperation(value = "根据ID获取余额 @author lidoudou")
    @GetMapping("/oil/card/balance/{oilCardId}")
    public ResponseDTO<BigDecimal> getBalanceByOilCardId(@PathVariable Long oilCardId) {
        return oilCardService.getBalanceByOilCardId(oilCardId);
    }
}
