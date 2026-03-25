package net.lab1024.tms.admin.module.business.material.goods;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsCreateForm;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsQueryForm;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsUpdateForm;
import net.lab1024.tms.admin.module.business.material.goods.domain.GoodsVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 业务资料-货物管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:31
 */
@Slf4j
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.MATERIAL_GOODS})
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "分页查询货物 @author yandy")
    @PostMapping("/goods/page/query")
    public ResponseDTO<PageResult<GoodsVO>> queryByPage(@RequestBody @Valid GoodsQueryForm queryDTO) {
        queryDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return goodsService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "查询货物详情 @author yandy")
    @GetMapping("/goods/get/{goodsId}")
    public ResponseDTO<GoodsVO> getDetail(@PathVariable Long goodsId) {
        return goodsService.getDetail(goodsId);
    }

    @ApiOperation(value = "新建货物 @author yandy")
    @PostMapping("/goods/create")
    public ResponseDTO<String> createGoods(@RequestBody @Valid GoodsCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return goodsService.createGoods(createVO, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "编辑货物 @author yandy")
    @PostMapping("/goods/update")
    public ResponseDTO<String> updateGoods(@RequestBody @Valid GoodsUpdateForm updateVO) {
        return goodsService.updateGoods(updateVO);
    }

    @ApiOperation(value = "删除货物 @author yandy")
    @GetMapping("/goods/delete/{goodsId}")
    public ResponseDTO<String> deleteGoods(@PathVariable Long goodsId) {
        return goodsService.deleteGoods(goodsId);
    }

    @ApiOperation(value = "查询货物列表 @author yandy")
    @GetMapping("/goods/list")
    public ResponseDTO<List<GoodsVO>> queryList() {
        return goodsService.queryList(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }
}
