package net.lab1024.tms.admin.module.system.role.service;

import net.lab1024.tms.admin.module.system.role.dao.RoleDao;
import net.lab1024.tms.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.tms.admin.module.system.role.dao.RoleMenuDao;
import net.lab1024.tms.common.module.system.role.domain.entity.RoleEntity;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleAddForm;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleUpdateForm;
import net.lab1024.tms.admin.module.system.role.domain.vo.RoleVO;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色管理业务
 *
 * @author 胡克
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private SystemConfigService systemConfigService;
    /**
     * 新增添加角色
     *
     * @param roleAddForm
     * @return ResponseDTO
     */
    public ResponseDTO addRole(RoleAddForm roleAddForm) {
        RoleEntity existRoleEntity = roleDao.getByRoleName(roleAddForm.getEnterpriseId(), roleAddForm.getRoleName());
        if (null != existRoleEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "角色名称重复");
        }
        RoleEntity existRoleCodeEntity = roleDao.getByRoleCode(roleAddForm.getEnterpriseId(), roleAddForm.getRoleCode());
        if (null != existRoleCodeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "角色编码重复");
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleAddForm, RoleEntity.class);
        roleDao.insert(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据角色id 删除
     *
     * @param roleId
     * @return ResponseDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO deleteRole(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (roleEntity.getSystemFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "系统级角色无法删除");
        }
        roleDao.deleteById(roleId);
        roleMenuDao.deleteByRoleId(roleId);
        roleEmployeeDao.deleteByRoleId(roleId);
        return ResponseDTO.ok();
    }

    /**
     * 更新角色
     *
     * @param roleUpdateForm
     * @return ResponseDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRole(RoleUpdateForm roleUpdateForm) {
        RoleEntity dbRoleEntity = roleDao.selectById(roleUpdateForm.getRoleId());
        if (null == dbRoleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (dbRoleEntity.getSystemFlag()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "系统级角色无法修改");
        }
        RoleEntity existRoleEntity = roleDao.getByRoleName(roleUpdateForm.getEnterpriseId(), roleUpdateForm.getRoleName());
        if (null != existRoleEntity && !existRoleEntity.getRoleId().equals(roleUpdateForm.getRoleId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "角色名称重复");
        }

        RoleEntity existRoleCodeEntity = roleDao.getByRoleCode(roleUpdateForm.getEnterpriseId(), roleUpdateForm.getRoleCode());
        if (null != existRoleCodeEntity && !existRoleCodeEntity.getRoleId().equals(roleUpdateForm.getRoleId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "角色编码重复");
        }

        RoleEntity roleEntity = SmartBeanUtil.copy(roleUpdateForm, RoleEntity.class);
        roleDao.updateById(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据id获取角色数据
     *
     * @param roleId
     * @return ResponseDTO
     */
    public ResponseDTO<RoleVO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        RoleVO role = SmartBeanUtil.copy(roleEntity, RoleVO.class);
        return ResponseDTO.ok(role);
    }

    /**
     * 获取所有角色列表
     *
     * @return ResponseDTO
     */
    public ResponseDTO<List<RoleVO>> getAllRole(Long enterpriseId) {
        List<RoleEntity> roleEntityList = roleDao.selectByEnterpriseId(enterpriseId);
        List<RoleVO> roleList = SmartBeanUtil.copyList(roleEntityList, RoleVO.class);
        return ResponseDTO.ok(roleList);
    }

    /**
     * 查询系统内置角色id
     * @param enterpriseId
     * @param systemConfigKeyEnum
     * @return
     */
    public Long queryRoleId(Long enterpriseId, SystemConfigKeyEnum systemConfigKeyEnum) {
        String roleCode = systemConfigService.getConfigValue(systemConfigKeyEnum);
        if (StringUtils.isEmpty(roleCode)) {
            throw new BusinessException("系统缺失配置项"+ systemConfigKeyEnum.getValue());
        }
        RoleEntity roleEntity = roleDao.getByRoleCode(enterpriseId, roleCode);
        if (roleEntity == null) {
            throw new BusinessException("系统配置项【"+ systemConfigKeyEnum.getValue()+"】所对应的角色数据不存在");
        }
        return roleEntity.getRoleId();
    }

    public Long queryRoleId(Long enterpriseId, String roleCode) {
        RoleEntity roleEntity = roleDao.getByRoleCode(enterpriseId, roleCode);
        if (roleEntity == null) {
            throw new BusinessException("角色代码【"+roleCode+"'所对应的角色数据不存在");
        }
        return roleEntity.getRoleId();
    }
}
