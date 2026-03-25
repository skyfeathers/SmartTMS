package net.lab1024.tms.admin.module.business.material.cost;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemForm;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemQueryForm;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemUpdateForm;
import net.lab1024.tms.admin.module.business.material.cost.domain.CostItemVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.COST_ITEM})
public class CostItemController {

    @Autowired
    private CostItemService costItemService;

    @PostMapping("/costItem/query")
    @ApiOperation(value = "查询全部 @author yandy")
    public ResponseDTO<PageResult<CostItemVO>> query(@RequestBody @Valid CostItemQueryForm queryDTO) {
        return costItemService.query(queryDTO);
    }

    @GetMapping("/costItem/delete/{costItemId}")
    @ApiOperation(value = "删除 @author zhuoda")
    public ResponseDTO<String> delete(@PathVariable Long costItemId) {
        return costItemService.delete(costItemId);
    }

    @PostMapping("/costItem/save")
    @ApiOperation(value = "保存 @author zhuoda")
    public ResponseDTO<String> save(@RequestBody @Valid CostItemForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        form.setCreateUserId(requestUser.getUserId());
        form.setCreateUserName(requestUser.getUserName());
        return costItemService.save(form);
    }

    @PostMapping("/costItem/update")
    @ApiOperation(value = "更新 @author zhuoda")
    public ResponseDTO<String> update(@RequestBody @Valid CostItemUpdateForm form) {
        return costItemService.update(form);
    }

}
