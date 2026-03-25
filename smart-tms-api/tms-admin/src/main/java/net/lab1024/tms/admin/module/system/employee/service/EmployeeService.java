package net.lab1024.tms.admin.module.system.employee.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseListVO;
import net.lab1024.tms.admin.module.business.oa.enterprise.service.EnterpriseBusinessSettingService;
import net.lab1024.tms.admin.module.business.oa.enterprise.service.EnterpriseService;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.department.service.DepartmentService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.domain.form.*;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.admin.module.system.employee.manager.EmployeeManager;
import net.lab1024.tms.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.tms.admin.module.system.role.domain.vo.RoleEmployeeVO;
import net.lab1024.tms.admin.module.system.role.service.RoleService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.constant.BusinessSettingEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.support.systemconfig.domain.SystemConfigVO;
import net.lab1024.tms.common.module.system.department.DepartmentCacheManager;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 员工管理
 *
 * @author 罗伊
 */
@Service
public class EmployeeService {

    private static final String DEFAULT_PASSWORD = "123456";

    private static final String PASSWORD_SALT_FORMAT = "smart_%s_admin_$^&*";

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeManager employeeManager;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private DepartmentCacheManager departmentCacheManager;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private EnterpriseBusinessSettingService enterpriseBusinessSettingService;


    public EmployeeEntity getById(Long employeeId) {
        return employeeDao.selectById(employeeId);
    }


    /**
     * 查询员工列表
     *
     * @param employeeQueryForm
     * @return
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(EmployeeQueryForm employeeQueryForm) {
        employeeQueryForm.setDeletedFlag(false);
        Page pageParam = SmartPageUtil.convert2PageQuery(employeeQueryForm);
        if (null != employeeQueryForm.getDepartmentId()) {
            employeeQueryForm.setDepartmentIdList(departmentService.selfAndChildrenIdList(employeeQueryForm.getDepartmentId()));
            employeeQueryForm.setDepartmentId(null);
        }
        List<EmployeeVO> employeeList = employeeDao.queryEmployee(pageParam, employeeQueryForm);
        if (CollectionUtils.isEmpty(employeeList)) {
            PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(pageParam, employeeList);
            return ResponseDTO.ok(PageResult);
        }
        List<Long> employeeIdList = employeeList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        List<Long> departmentIdList = employeeList.stream().map(EmployeeVO::getDepartmentId).collect(Collectors.toList());
        // 查询员工角色
        List<RoleEmployeeVO> roleEmployeeEntityList = roleEmployeeDao.selectRoleByEmployeeIdList(employeeIdList);
        Map<Long, List<Long>> employeeRoleIdListMap = roleEmployeeEntityList.stream().collect(groupingBy(RoleEmployeeVO::getEmployeeId, Collectors.mapping(RoleEmployeeVO::getRoleId, Collectors.toList())));
        Map<Long, List<String>> employeeRoleNameListMap = roleEmployeeEntityList.stream().collect(groupingBy(RoleEmployeeVO::getEmployeeId, Collectors.mapping(RoleEmployeeVO::getRoleName, Collectors.toList())));
        // 查询员工部门
        Map<Long, String> departmentNameMap = departmentDao.selectBatchIds(departmentIdList).stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));
        employeeList.forEach(e -> {
            e.setRoleIdList(employeeRoleIdListMap.getOrDefault(e.getEmployeeId(), Lists.newArrayList()));
            e.setRoleNameList(employeeRoleNameListMap.getOrDefault(e.getEmployeeId(), Lists.newArrayList()));
            e.setDepartmentName(departmentNameMap.getOrDefault(e.getDepartmentId(), ""));
        });
        PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(pageParam, employeeList);
        return ResponseDTO.ok(PageResult);
    }

    /**
     * 新增员工
     *
     * @param employeeAddForm
     * @return
     */
    public synchronized ResponseDTO<String> addEmployee(EmployeeAddForm employeeAddForm) {
        Long enterpriseId = employeeAddForm.getEnterpriseId();
        // 校验名称是否重复
        EmployeeEntity employeeEntity = employeeDao.getByLoginName(enterpriseId, employeeAddForm.getLoginName(), false);
        if (null != employeeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名重复");
        }
        // 校验姓名是否重复
        employeeEntity = employeeDao.getByActualName(enterpriseId, employeeAddForm.getActualName(), false);
        if (null != employeeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "姓名重复");
        }
        // 校验电话是否存在
        employeeEntity = employeeDao.getByPhone(enterpriseId, employeeAddForm.getPhone(), false);
        if (null != employeeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "手机号已存在");
        }
        // 部门是否存在
        Long departmentId = employeeAddForm.getDepartmentId();
        DepartmentEntity department = departmentDao.selectById(departmentId);
        if (department == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "部门不存在");
        }
        if (!department.getEnterpriseId().equals(enterpriseId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "部门所属企业和当前域名匹配企业信息异常");
        }
        EmployeeEntity entity = SmartBeanUtil.copy(employeeAddForm, EmployeeEntity.class);
        // 设置密码 默认密码
        String newPwd = CommonConst.Password.DEFAULT;
        // 设置密码 默认密码
        entity.setLoginPwd(getEncryptPwd(newPwd));
        // 保存数据
        employeeManager.saveEmployee(entity, employeeAddForm.getRoleIdList());

        return ResponseDTO.ok(newPwd);
    }

    public static String createDefaultPwd() {
        Random random = new Random();
        StringBuffer pwd = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            if (i <= 2) {
                //从zi的24个字符串中一次循环取一个，random.nextInt(24)代表随机取一个24以内的数，如zi.charAt(0)表示字符串的第一个索引A。
                pwd.append(CommonConst.Password.DEFAULT_LETTER.charAt(random.nextInt(24)));
            } else if (i > 2 && i < 6) {
                //取10以内的任意一个数。
                pwd.append(random.nextInt(10));
            } else {
                //从zi的24个字符串中一次循环取一个，random.nextInt(24)代表随机取一个24以内的数，如zi.charAt(0)表示字符串的第一个索引A。
                pwd.append(CommonConst.Password.DEFAULT_UPPERCASE_LETTER.charAt(random.nextInt(24)));
            }
        }
        return pwd.toString();
    }

    /**
     * 更新员工
     *
     * @param employeeUpdateForm
     * @return
     */
    public synchronized ResponseDTO<String> updateEmployee(EmployeeUpdateForm employeeUpdateForm) {
        Long enterpriseId = employeeUpdateForm.getEnterpriseId();
        Long employeeId = employeeUpdateForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 部门是否存在
        Long departmentId = employeeUpdateForm.getDepartmentId();
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "部门不存在");
        }

        EmployeeEntity existEntity = employeeDao.getByLoginName(enterpriseId, employeeUpdateForm.getLoginName(), false);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名重复");
        }

        existEntity = employeeDao.getByPhone(enterpriseId, employeeUpdateForm.getPhone(), false);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "手机号已存在");
        }

        existEntity = employeeDao.getByActualName(enterpriseId, employeeUpdateForm.getActualName(), false);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "姓名重复");
        }

        // 不更新密码
        EmployeeEntity entity = SmartBeanUtil.copy(employeeUpdateForm, EmployeeEntity.class);
        entity.setLoginPwd(null);

        // 更新数据
        employeeManager.updateEmployee(entity, employeeUpdateForm.getRoleIdList());

        return ResponseDTO.ok();
    }

    /**
     * 更新禁用/启用状态
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> updateDisableFlag(Long employeeId) {
        if (null == employeeId) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        employeeDao.updateDisableFlag(employeeId, !employeeEntity.getDisabledFlag());

        return ResponseDTO.ok();
    }

    /**
     * 批量删除员工
     *
     * @param employeeIdList 员工ID列表
     * @return
     */
    public ResponseDTO<String> batchUpdateDeleteFlag(List<Long> employeeIdList) {
        if (CollectionUtils.isEmpty(employeeIdList)) {
            return ResponseDTO.ok();
        }
        List<EmployeeEntity> employeeEntityList = employeeManager.listByIds(employeeIdList);
        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok();
        }
        // 更新删除
        List<EmployeeEntity> deleteList = employeeIdList.stream().map(e -> {
            EmployeeEntity updateEmployee = new EmployeeEntity();
            updateEmployee.setEmployeeId(e);
            updateEmployee.setDeletedFlag(true);
            return updateEmployee;
        }).collect(Collectors.toList());
        employeeManager.updateBatchById(deleteList);
        return ResponseDTO.ok();
    }


    /**
     * 批量更新部门
     *
     * @param batchUpdateDepartmentForm
     * @return
     */
    public ResponseDTO<String> batchUpdateDepartment(EmployeeBatchUpdateDepartmentForm batchUpdateDepartmentForm) {
        List<Long> employeeIdList = batchUpdateDepartmentForm.getEmployeeIdList();
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(employeeIdList);
        if (employeeIdList.size() != employeeEntityList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 更新
        List<EmployeeEntity> updateList = employeeIdList.stream().map(e -> {
            EmployeeEntity updateEmployee = new EmployeeEntity();
            updateEmployee.setEmployeeId(e);
            updateEmployee.setDepartmentId(batchUpdateDepartmentForm.getDepartmentId());
            return updateEmployee;
        }).collect(Collectors.toList());
        employeeManager.updateBatchById(updateList);

        return ResponseDTO.ok();
    }


    /**
     * 更新密码
     *
     * @param updatePasswordForm
     * @return
     */
    public ResponseDTO<String> updatePassword(EmployeeUpdatePasswordForm updatePasswordForm) {
        Long employeeId = updatePasswordForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (employeeEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 校验原始密码
        String encryptPwd = getEncryptPwd(updatePasswordForm.getOldPassword());
        if (!Objects.equals(encryptPwd, employeeEntity.getLoginPwd())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "原密码有误，请重新输入");
        }

        // 新旧密码相同
        String newPassword = updatePasswordForm.getNewPassword();
        if (Objects.equals(updatePasswordForm.getOldPassword(), newPassword)) {
            return ResponseDTO.ok();
        }

        // 更新密码
        EmployeeEntity updateEntity = new EmployeeEntity();
        updateEntity.setEmployeeId(employeeId);
        updateEntity.setLoginPwd(getEncryptPwd(newPassword));
        updateEntity.setLastEditPwdDate(LocalDate.now());
        employeeDao.updateById(updateEntity);

        return ResponseDTO.ok();
    }

    /**
     * 获取某个部门的员工信息
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByDepartmentId(Long enterpriseId, Long departmentId, Boolean disabledFlag) {
        List<EmployeeEntity> employeeEntityList = employeeDao.selectByDepartmentId(departmentId, disabledFlag);
        if (disabledFlag != null) {
            employeeEntityList = employeeEntityList.stream().filter(e -> e.getDisabledFlag().equals(disabledFlag)).collect(Collectors.toList());
        }
        // 获取部门
        List<DepartmentVO> departmentList = departmentCacheManager.getDepartmentList(enterpriseId);
        Optional<DepartmentVO> departmentVO = departmentList.stream().filter(e -> e.getDepartmentId().equals(departmentId)).findFirst();
        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok(Collections.emptyList());
        }
        List<EmployeeVO> voList = employeeEntityList.stream().map(e -> {
            EmployeeVO employeeVO = SmartBeanUtil.copy(e, EmployeeVO.class);
            if (departmentVO.isPresent()) {
                employeeVO.setDepartmentName(departmentVO.get().getName());
            }
            return employeeVO;
        }).collect(Collectors.toList());
        return ResponseDTO.ok(voList);
    }


    /**
     * 重置密码
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> resetPassword(Integer employeeId) {
        employeeDao.updatePassword(employeeId, getEncryptPwd(DEFAULT_PASSWORD));
        return ResponseDTO.ok(DEFAULT_PASSWORD);
    }


    /**
     * 获取 加密后 的密码
     *
     * @param password
     * @return
     */
    public static String getEncryptPwd(String password) {
        return DigestUtils.md5Hex(String.format(PASSWORD_SALT_FORMAT, password));
    }

    public static void main(String[] args) {
        System.out.printf(getEncryptPwd("123456"));
    }

    /**
     * 查询全部员工
     *
     * @param disabledFlag
     * @param roleCode
     * @return
     */
    public ResponseDTO<List<EmployeeVO>> queryAllEmployee(Long enterpriseId, Long employeeId, Boolean disabledFlag, String roleCode) {
        Long roleId = null;
        if (StringUtils.isNotBlank(roleCode)) {
            roleId = roleService.queryRoleId(enterpriseId, roleCode);
        }
        EmployeeQueryForm employeeQueryForm = new EmployeeQueryForm();
        employeeQueryForm.setDisabledFlag(disabledFlag);

        employeeQueryForm.setDeletedFlag(Boolean.FALSE);
        employeeQueryForm.setLeaveFlag(Boolean.FALSE);
        employeeQueryForm.setRoleId(roleId);
        employeeQueryForm.setEnterpriseId(enterpriseId);
        List<EmployeeVO> employeeList = employeeDao.queryEmployee(employeeQueryForm);
        return ResponseDTO.ok(employeeList);
    }

    /**
     * 根据登录名获取员工
     *
     * @param enterpriseId
     * @return
     */
    public EmployeeEntity getByLoginName(Long enterpriseId, String loginName) {
        return employeeDao.getByLoginName(enterpriseId, loginName, null);
    }



    /**
     * 查询员工列表
     *
     * @param filterEmployeeIdList   可选
     * @param systemConfigKeyEnum 系统设置key
     * @return
     */
    public List<EmployeeVO> queryEmployeeList(Long enterpriseId, List<Long> filterEmployeeIdList, SystemConfigKeyEnum systemConfigKeyEnum) {
        Long roleId = roleService.queryRoleId(enterpriseId, systemConfigKeyEnum);
        // 最终可见员工map
        List<EmployeeVO> employeeList = roleEmployeeDao.selectEmployeeByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isNotEmpty(filterEmployeeIdList)) {
            employeeList = employeeList.stream().filter(e->filterEmployeeIdList.contains(e.getEmployeeId())).collect(Collectors.toList());
        }
        return employeeList;
    }

    /**
     * 根据系统配置获取查询的报表员工
     *
     * @param departmentId
     * @param configKey
     * @return
     */
    public List<EmployeeVO> queryDayStatisticEmployee(Long departmentId, SystemConfigKeyEnum configKey) {
        SystemConfigVO configVO = systemConfigService.getConfig(configKey);
        String configValue = configVO.getConfigValue();
        List<Long> departmentIdList;
        try {
            departmentIdList = Arrays.stream(configValue.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException("系统配置的所选部门ID错误");
        }

        if (departmentId != null) {
            if (!departmentIdList.contains(departmentId)) {
                throw new BusinessException("此部门不支持查询");
            }
            departmentIdList = Lists.newArrayList(departmentId);
        }

        List<EmployeeVO> employeeVOList = employeeDao.selectByDepartmentIds(departmentIdList, Boolean.FALSE, Boolean.FALSE);
        return employeeVOList;
    }

}
