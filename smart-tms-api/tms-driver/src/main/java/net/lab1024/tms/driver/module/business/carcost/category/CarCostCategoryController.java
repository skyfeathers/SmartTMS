package net.lab1024.tms.driver.module.business.carcost.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCategoryVO;
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
@Api(tags = {DriverSwaggerTagConst.Business.CAR_COST})
public class CarCostCategoryController {

    @Resource
    private CarCostCategoryService carCostCategoryService;

    @ApiOperation(value = "费用分类-根据costType查询 @author yandy")
    @GetMapping("/car/cost/category/query/{costType}")
    public ResponseDTO<List<CarCostCategoryVO>> costSubjectList(@PathVariable("costType") Integer costType) {
        return carCostCategoryService.queryByCostType(costType);
    }
}