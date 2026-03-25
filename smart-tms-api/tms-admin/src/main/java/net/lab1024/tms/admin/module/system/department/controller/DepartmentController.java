package net.lab1024.tms.admin.module.system.department.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.department.domain.form.DepartmentAddForm;
import net.lab1024.tms.admin.module.system.department.domain.form.DepartmentUpdateForm;
import net.lab1024.tms.admin.module.system.department.service.DepartmentService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import net.lab1024.tms.common.module.system.department.domain.DepartmentTreeVO;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理
 *
 * @author zhuoda
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_DEPARTMENT})
public class DepartmentController extends AdminBaseController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询部门树形列表 @author zhuoda")
    @GetMapping("/department/treeList")
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        return departmentService.departmentTree(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation(value = "添加部门 @author zhuoda")
    @PostMapping("/department/add")
    @PreAuthorize("@saAuth.checkPermission('system:department:add')")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentAddForm createDTO) {
        createDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return departmentService.addDepartment(createDTO);
    }

    @ApiOperation(value = "更新部门 @author zhuoda")
    @PostMapping("/department/update")
    @PreAuthorize("@saAuth.checkPermission('system:department:update')")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateForm updateDTO) {
        return departmentService.updateDepartment(updateDTO);
    }

    @ApiOperation(value = "删除部门 @author zhuoda")
    @GetMapping("/department/delete/{departmentId}")
    @PreAuthorize("@saAuth.checkPermission('system:department:delete')")
    public ResponseDTO<String> deleteDepartment(@PathVariable Long departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    @ApiOperation(value = "查询部门列表 @author zhuoda")
    @GetMapping("/department/listAll")
    public ResponseDTO<List<DepartmentVO>> listAll() {
        return ResponseDTO.ok(departmentService.listAll(SmartRequestEnterpriseUtil.getRequestEnterpriseId()));
    }

    @ApiOperation(value = "查询部门列表 @author zhuoda")
    @GetMapping("/department/listByEnterprise/{enterpriseId}")
    public ResponseDTO<List<DepartmentVO>> listByEnterprise(@PathVariable Long enterpriseId) {
        return ResponseDTO.ok(departmentService.listAll(enterpriseId));
    }

}
