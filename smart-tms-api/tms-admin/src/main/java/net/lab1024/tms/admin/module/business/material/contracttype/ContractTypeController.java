package net.lab1024.tms.admin.module.business.material.contracttype;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.contracttype.domain.*;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.Business.COST_ITEM})
public class ContractTypeController {

    @Autowired
    private ContractTypeService contractTypeService;

    @PostMapping("/contractType/query")
    @ApiOperation(value = "查询全部 @author yandy")
    public ResponseDTO<PageResult<ContractTypeVO>> query(@RequestBody @Valid ContractTypeQueryForm queryDTO) {
        return contractTypeService.query(queryDTO);
    }

    @GetMapping("/contractType/queryAll")
    @ApiOperation(value = "查询全部 @author yandy")
    public ResponseDTO<List<ContractListVO>> queryAll() {
        return contractTypeService.queryAll();
    }

    @GetMapping("/contractType/delete/{contractTypeId}")
    @ApiOperation(value = "删除 @author yandy")
    public ResponseDTO<String> delete(@PathVariable Long contractTypeId) {
        return contractTypeService.delete(contractTypeId);
    }

    @PostMapping("/contractType/save")
    @ApiOperation(value = "保存 @author yandy")
    public ResponseDTO<String> save(@RequestBody @Valid ContractTypeForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        form.setCreateUserId(requestUser.getUserId());
        form.setCreateUserName(requestUser.getUserName());
        return contractTypeService.save(form);
    }

    @PostMapping("/contractType/update")
    @ApiOperation(value = "更新 @author yandy")
    public ResponseDTO<String> update(@RequestBody @Valid ContractTypeUpdateForm form) {
        return contractTypeService.update(form);
    }

}
