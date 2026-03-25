package net.lab1024.tms.fixedasset.module.business.consumables.check.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckEmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 易耗品盘点人
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:34
 */
@Mapper
@Component
public interface ConsumablesCheckEmployeeDao extends BaseMapper<ConsumablesCheckEmployeeEntity> {

    /**
     * 根据采购ID查询关联盘点人
     *
     * @param checkIdList
     * @return
     */
    List<ConsumablesCheckEmployeeEntity> queryByCheckIdList(@Param("checkIdList") List<Long> checkIdList);

    /**
     * 根据盘点ID查询关联员工
     *
     * @param checkId
     * @return
     */
    List<Long> queryByCheckId(@Param("checkId") Long checkId);
}
