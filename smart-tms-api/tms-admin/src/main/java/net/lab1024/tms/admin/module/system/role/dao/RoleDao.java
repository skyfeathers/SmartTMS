package net.lab1024.tms.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.system.role.domain.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {

    /**
     * 根据角色名称查询
     * @param enterpriseId
     * @param roleName
     * @return
     */
    RoleEntity getByRoleName(@Param("enterpriseId") Long enterpriseId,  @Param("roleName") String roleName);

    RoleEntity getByRoleCode(@Param("enterpriseId") Long enterpriseId,  @Param("roleCode") String roleCode);

    List<RoleEntity> selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

}
