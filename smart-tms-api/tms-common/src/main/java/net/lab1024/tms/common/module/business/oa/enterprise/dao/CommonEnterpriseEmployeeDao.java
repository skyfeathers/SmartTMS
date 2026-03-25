package net.lab1024.tms.common.module.business.oa.enterprise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * OA企业模块
 *
 * @author lihaifan
 * @date 2022/6/22 16:49
 */
@Mapper
@Component
public interface CommonEnterpriseEmployeeDao extends BaseMapper<EnterpriseEmployeeEntity> {

    /**
     * 查询员工关联的企业
     *
     * @param employeeId
     * @return
     */
    List<Long> selectEnterpriseIdByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 查询企业下的所有员工id
     *
     * @param enterpriseIdList
     * @return
     */
    List<Long> selectEmployeeIdByEnterpriseIdList(@Param("enterpriseIdList") Collection<Long> enterpriseIdList);
}
