package net.lab1024.tms.driver.module.business.carcost.tabulation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.driver.constant.DriverSwaggerTagConst;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.driver.module.business.carcost.tabulation.domain.CarCostTabulationQueryForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationSimpleVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {DriverSwaggerTagConst.Business.CAR_COST})
public class CarCostTabulationController {

    @Resource
    private CarCostTabulationService carCostTabulationService;

    @ApiOperation(value = "自有车-费用列表-简单列表 @author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/simple/list")
    public ResponseDTO<List<CarCostTabulationSimpleVO>> simpleList() {
        Long driverId = SmartRequestUtil.getRequestUserId();
        return carCostTabulationService.simpleList(driverId);
    }

    @ApiOperation(value = "自有车-费用列表-分页查询 @author zhaoxinyang")
    @PostMapping("/car/cost/tabulation/page")
    public ResponseDTO<PageResult<CarCostTabulationVO>> list(@RequestBody @Valid CarCostTabulationQueryForm queryForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        queryForm.setDriverId(requestUser.getUserId());
        queryForm.setCreateUserType(requestUser.getUserType().getValue());
        return carCostTabulationService.list(queryForm);
    }

    @ApiOperation(value = "自有车-费用列表-详情 @author zhaoxinyang")
    @GetMapping("/car/cost/tabulation/detail/{moduleId}/{moduleType}")
    public ResponseDTO<CarCostTabulationDetailVO> detail(@PathVariable("moduleId") Long moduleId, @PathVariable("moduleType") Integer moduleType) {
        return carCostTabulationService.detail(moduleId, moduleType);
    }

}
