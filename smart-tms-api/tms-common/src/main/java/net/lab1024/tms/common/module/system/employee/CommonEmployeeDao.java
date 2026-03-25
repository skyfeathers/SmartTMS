package net.lab1024.tms.common.module.system.employee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeBO;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/10/28 11:12 上午
 */
@Mapper
@Component
public interface CommonEmployeeDao extends BaseMapper<EmployeeEntity> {


    /**
     * 获取某个部门的员工Id
     *
     * @param departmentId
     * @param disabledFlag
     * @return
     */
    List<Long> getEmployeeIdByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 获取某批部门的员工Id
     *
     * @param departmentIds
     * @return
     */
    List<Long> getEmployeeIdByDepartmentIdList(@Param("departmentIds") List<Long> departmentIds, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 根据删除状态查询
     *
     * @param deletedFlag
     * @return
     */
    List<EmployeeEntity> selectListByDeletedFlag(@Param("deletedFlag") Boolean deletedFlag);


    List<EmployeeBO> selectByIdList(@Param("employeeIdList") List<Long> employeeIdList);
}