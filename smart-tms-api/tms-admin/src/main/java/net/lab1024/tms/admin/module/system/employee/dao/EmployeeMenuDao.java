package net.lab1024.tms.admin.module.system.employee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.system.employee.domain.entity.EmployeeMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author yandy
 */
@Mapper
@Component
public interface EmployeeMenuDao extends BaseMapper<EmployeeMenuEntity> {

    /**
     * 根据员工ID删除菜单权限
     * @param employeeId
     */
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据员工ID查询选择的菜单权限
     * @param employeeId
     * @return
     */
    List<Long> queryMenuIdByEmployeeId(@Param("employeeId") Long employeeId);

}
