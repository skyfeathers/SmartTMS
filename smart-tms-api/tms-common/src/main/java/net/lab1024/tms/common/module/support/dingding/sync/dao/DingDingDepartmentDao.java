package net.lab1024.tms.common.module.support.dingding.sync.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingDepartmentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 钉钉部门配置
 *
 * @author lidoudou
 * @date 2023/4/22 下午1:57
 */
@Mapper
@Component
public interface DingDingDepartmentDao extends BaseMapper<DingDingDepartmentEntity> {

    /**
     * 根据TMS部门ID查询
     *
     * @param departmentId
     * @return
     */
    DingDingDepartmentEntity getByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据钉钉部门ID查询
     *
     * @param deptId
     * @return
     */
    DingDingDepartmentEntity getByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据公司ID查询
     *
     * @param enterpriseId
     * @return
     */
    List<DingDingDepartmentEntity> selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}