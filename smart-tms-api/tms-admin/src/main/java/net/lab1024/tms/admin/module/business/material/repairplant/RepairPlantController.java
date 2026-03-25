package net.lab1024.tms.admin.module.business.material.repairplant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantAddForm;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantAdminVO;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantQueryForm;
import net.lab1024.tms.admin.module.business.material.repairplant.domain.RepairPlantUpdateForm;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantSimpleVO;
import net.lab1024.tms.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 业务资料-业务类型
 *
 * @author listen
 * @date 2022/07/13 22:20
 */
@Api(tags = AdminSwaggerTagConst.Business.INFORMATION_REPAIR_PLANT)
@RestController
public class RepairPlantController {

    @Autowired
    private RepairPlantService repairPlantService;

    @RepeatSubmit
    @ApiOperation("维修厂家-新增 @listen")
    @PostMapping("/information/repairPlant/add")
    public ResponseDTO<String> add(@RequestBody @Valid RepairPlantAddForm addForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        addForm.setCreateUserName(requestUser.getUserName());
        addForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return repairPlantService.add(addForm);
    }

    @RepeatSubmit
    @ApiOperation("维修厂家-更新 @listen")
    @PostMapping("/information/repairPlant/update")
    public ResponseDTO<String> update(@RequestBody @Valid RepairPlantUpdateForm updateForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        updateForm.setUpdateUserName(requestUser.getUserName());
        updateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return repairPlantService.update(updateForm);
    }

    @ApiOperation("维修厂家-分页查询 @listen")
    @PostMapping("/information/repairPlant/query")
    public ResponseDTO<PageResult<RepairPlantAdminVO>> query(@RequestBody @Valid RepairPlantQueryForm queryForm) {
        queryForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return repairPlantService.query(queryForm);
    }

    @ApiOperation("维修厂家-查询全部 @listen")
    @GetMapping("/information/repairPlant/query/all")
    public ResponseDTO<List<RepairPlantSimpleVO>> queryAll() {
        return ResponseDTO.ok(repairPlantService.queryAll(SmartRequestEnterpriseUtil.getRequestEnterpriseId()));
    }

    @RepeatSubmit
    @ApiOperation("维修厂家-删除 @listen")
    @GetMapping("/information/repairPlant/del/{repairPlantId}")
    public ResponseDTO<String> del(@PathVariable Integer repairPlantId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return repairPlantService.del(repairPlantId, requestUser.getUserName());
    }

}