package net.lab1024.tms.driver.module.business.carcost.oilcardinitialend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostOilCardInitialEndVO;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {DriverSwaggerTagConst.Business.CAR_COST_INITIAL_END})
public class CarCostOilCardInitialEndController {

    @Resource
    private CarCostOilCardInitialEndService carCostOilCardInitialEndService;

    @ApiOperation(value = "自有车-油卡期初期末-信息 @author zhaoxinyang")
    @GetMapping("/car/cost/oilCardInitialEnd/info/{waybillId}")
    public ResponseDTO<List<CarCostOilCardInitialEndVO>> list(@PathVariable("waybillId") Long waybillId) {
        return carCostOilCardInitialEndService.info(waybillId);
    }

}
