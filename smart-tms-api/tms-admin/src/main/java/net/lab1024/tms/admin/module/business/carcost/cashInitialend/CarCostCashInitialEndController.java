package net.lab1024.tms.admin.module.business.carcost.cashInitialend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCashInitialEndVO;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.CAR_COST_INITIAL_END})
public class CarCostCashInitialEndController{

    @Resource
    private CarCostCashInitialEndService carCostCashInitialEndService;

    @ApiOperation(value = "自有车-现金期初期末-信息 @author zhaoxinyang")
    @GetMapping("/car/cost/cashInitialEnd/info/{waybillId}")
    public ResponseDTO<CarCostCashInitialEndVO> list(@PathVariable("waybillId") Long waybillId) {
        return carCostCashInitialEndService.info(waybillId);
    }

}
