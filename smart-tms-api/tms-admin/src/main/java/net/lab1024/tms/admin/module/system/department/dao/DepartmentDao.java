package net.lab1024.tms.admin.module.system.department.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * t_department dao接口
 *
 * @author zhuoda
 */
@Component
@Mapper
public interface DepartmentDao extends BaseMapper<DepartmentEntity> {

    /**
     * 根据部门id，查询此部门直接子部门的数量
     *
     * @param departmentId
     * @return int 子部门的数量
     */
    Integer countSubDepartment(@Param("departmentId") Long departmentId);

    /**
     * 获取全部部门列表
     *
     * @return
     */
    List<DepartmentVO> listAll();

    /**
     * 根据名称以及父级ID查询
     *
     * @param parentId
     * @param name
     * @return
     */
    DepartmentEntity selectByParentIdAndName(@Param("parentId") Long parentId, @Param("name") String name);

    Integer selectMaxSort();

    DepartmentEntity selectByParentId(@Param("enterpriseId") Long enterpriseId, @Param("parentId") Long parentId);
}
