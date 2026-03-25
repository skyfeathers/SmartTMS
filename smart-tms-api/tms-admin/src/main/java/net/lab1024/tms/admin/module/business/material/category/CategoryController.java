package net.lab1024.tms.admin.module.business.material.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.material.category.CategoryService;
import net.lab1024.tms.common.module.business.material.category.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类目 路由
 *
 * @author Turbolisten
 * @date 2021/1/21 9:10
 */
@Api(tags = AdminSwaggerTagConst.Business.INFORMATION_CATEGORY)
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("添加类目 by listen")
    @PostMapping("/information/category/add")
    public ResponseDTO<Long> add(@RequestBody @Valid CategoryAddDTO addDTO) {
        return categoryService.add(addDTO);
    }

    @ApiOperation("更新类目 by listen")
    @PostMapping("/information/category/update")
    public ResponseDTO<String> update(@RequestBody @Valid CategoryUpdateDTO updateDTO) {
        return categoryService.update(updateDTO);
    }

    @ApiOperation("查询类目详情 by listen")
    @GetMapping("/information/category/{categoryId}")
    public ResponseDTO<CategoryVO> queryDetail(@PathVariable Long categoryId) {
        return categoryService.queryDetail(categoryId);
    }

    @ApiOperation("查询类目层级树 by listen")
    @PostMapping("/information/category/tree")
    public ResponseDTO<List<CategoryTreeVO>> queryTree(@RequestBody @Valid CategoryTreeQueryDTO queryDTO) {
        return categoryService.queryTree(queryDTO);
    }

    @ApiOperation("删除类目 by listen")
    @GetMapping("/information/category/del/{categoryId}")
    public ResponseDTO<String> delete(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}