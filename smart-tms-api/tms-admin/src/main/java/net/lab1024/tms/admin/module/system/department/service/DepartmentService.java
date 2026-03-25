package net.lab1024.tms.admin.module.system.department.service;

import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.department.domain.form.DepartmentAddForm;
import net.lab1024.tms.admin.module.system.department.domain.form.DepartmentUpdateForm;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.system.department.DepartmentCacheManager;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentTreeVO;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author zhuoda
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentCacheManager departmentCacheManager;

    // ---------------------------- 增加、修改、删除 ----------------------------

    /**
     * 新增添加部门
     *
     * @param departmentAddForm
     * @return AjaxResult
     */

    public ResponseDTO<String> addDepartment(DepartmentAddForm departmentAddForm) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentAddForm, DepartmentEntity.class);
        departmentDao.insert(departmentEntity);
        return ResponseDTO.ok();
    }


    /**
     * 更新部门信息
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateDepartment(DepartmentUpdateForm updateDTO) {
        if (updateDTO.getParentId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "父级部门id不能为空");
        }
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getDepartmentId());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(updateDTO, DepartmentEntity.class);
        departmentEntity.setSort(updateDTO.getSort());
        departmentDao.updateById(departmentEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据id删除部门
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<String> deleteDepartment(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (null == departmentEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 是否有子级部门
        int subDepartmentNum = departmentDao.countSubDepartment(departmentId);
        if (subDepartmentNum > 0) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先删除子级部门");
        }

        // 是否有未删除员工
        int employeeNum = employeeDao.countByDepartmentId(departmentId, Boolean.FALSE);
        if (employeeNum > 0) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先删除部门员工");
        }
        departmentDao.deleteById(departmentId);
        return ResponseDTO.ok();
    }


    // ---------------------------- 查询 ----------------------------

    /**
     * 获取部门树形结构
     *
     * @return
     */
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree(Long enterpriseId) {
        List<DepartmentTreeVO> treeVOList = departmentCacheManager.getDepartmentTree(enterpriseId);
        return ResponseDTO.ok(treeVOList);
    }


    /**
     * 自身以及所有下级的部门id列表
     *
     * @param departmentId
     * @return
     */
    public List<Long> selfAndChildrenIdList(Long departmentId) {
        return departmentCacheManager.getDepartmentSelfAndChildren(departmentId);
    }


    /**
     * 获取所有部门
     *
     * @return
     */
    public List<DepartmentVO> listAll(Long enterpriseId) {
        return departmentCacheManager.getDepartmentList(enterpriseId);
    }


    /**
     * 获取部门
     *
     * @param departmentId
     */
    public DepartmentVO getDepartmentById(Long departmentId) {
        return departmentCacheManager.queryById(departmentId);
    }

    /**
     * 获取部门路径：/公司/研发部/产品组
     *
     * @param departmentId
     */
    public String getDepartmentPath(Long departmentId) {
        return departmentCacheManager.getDepartmentPath(departmentId);
    }
}
