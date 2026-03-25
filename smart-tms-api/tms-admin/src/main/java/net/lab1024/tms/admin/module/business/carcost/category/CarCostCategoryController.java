package net.lab1024.tms.admin.module.business.carcost.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.carcost.category.domain.CarCostCategoryAddForm;
import net.lab1024.tms.admin.module.business.carcost.category.domain.CarCostCategoryQueryForm;
import net.lab1024.tms.admin.module.business.carcost.category.domain.CarCostCategoryUpdateForm;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCategoryVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.CAR_COST_CATEGORY})
public class CarCostCategoryController {

    @Resource
    private CarCostCategoryService carCostCategoryService;

    @ApiOperation(value = "费用分类-查询全部 @author lidoudou")
    @GetMapping("/car/cost/category/query/all")
    public ResponseDTO<List<CarCostCategoryVO>> queryAll() {
        return carCostCategoryService.queryAll();
    }

    @ApiOperation(value = "费用分类-分页查询 @author yandy")
    @PostMapping("/car/cost/category/query/page")
    public ResponseDTO<PageResult<CarCostCategoryVO>> queryPage(@RequestBody @Valid CarCostCategoryQueryForm queryForm) {
        return carCostCategoryService.queryPage(queryForm);
    }

    @ApiOperation(value = "费用分类-根据costType查询 @author yandy")
    @GetMapping("/car/cost/category/query/{costType}")
    public ResponseDTO<List<CarCostCategoryVO>> costSubjectList(@PathVariable("costType") Integer costType) {
        return carCostCategoryService.queryByCostType(costType);
    }

    @RepeatSubmit
    @ApiOperation(value = "费用分类-保存 @author yandy")
    @PostMapping("/car/cost/category/save")
    public ResponseDTO<String> save(@RequestBody @Valid CarCostCategoryAddForm addForm) {
        return carCostCategoryService.save(addForm);
    }

    @RepeatSubmit
    @ApiOperation(value = "费用分类-更新 @author yandy")
    @PostMapping("/car/cost/category/update")
    public ResponseDTO<String> update(@RequestBody @Valid CarCostCategoryUpdateForm updateForm) {
        return carCostCategoryService.update(updateForm);
    }

    @ApiOperation(value = "费用分类-删除 @author yandy")
    @GetMapping("/car/cost/category/delete/{categoryId}")
    public ResponseDTO<String> delete(@PathVariable("categoryId") Long categoryId) {
        return carCostCategoryService.delete(categoryId);
    }

}