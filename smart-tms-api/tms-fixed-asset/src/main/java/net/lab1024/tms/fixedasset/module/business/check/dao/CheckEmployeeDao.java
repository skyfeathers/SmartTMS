package net.lab1024.tms.fixedasset.module.business.check.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.check.domain.entity.CheckEmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 盘点人
 *
 * @author lidoudou
 * @date 2023/3/24 上午09:28
 */
@Mapper
@Component
public interface CheckEmployeeDao extends BaseMapper<CheckEmployeeEntity> {

    /**
     * 根据采购ID查询关联盘点人
     *
     * @param checkIdList
     * @return
     */
    List<CheckEmployeeEntity> queryByCheckIdList(@Param("checkIdList") List<Long> checkIdList);

    /**
     * 根据盘点ID查询关联员工
     *
     * @param checkId
     * @return
     */
    List<Long> queryByCheckId(@Param("checkId") Long checkId);
}
