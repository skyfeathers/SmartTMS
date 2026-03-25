package net.lab1024.tms.common.module.system.department;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lidoudou
 * @date 2023/3/17 下午4:15
 */
@Mapper
@Component
public interface CommonDepartmentDao extends BaseMapper<DepartmentEntity> {
    /**
     * 获取全部部门列表
     *
     * @return
     */
    List<DepartmentVO> listByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    DepartmentVO queryById(@Param("departmentId")Long departmentId);
}