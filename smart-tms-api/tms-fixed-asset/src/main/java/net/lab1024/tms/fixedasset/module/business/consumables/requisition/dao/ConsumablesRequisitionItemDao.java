package net.lab1024.tms.fixedasset.module.business.consumables.requisition.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity.ConsumablesRequisitionItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 领用明细dao
 *
 * @author lidoudou
 * @date 2023/4/14 上午9:47
 */
@Mapper
@Component
public interface ConsumablesRequisitionItemDao extends BaseMapper<ConsumablesRequisitionItemEntity> {

    /**
     * 根据ID查询关联资产
     *
     * @param requisitionIdList
     * @return
     */
    List<ConsumablesRequisitionItemEntity> queryByRequisitionIdList(@Param("requisitionIdList") List<Long> requisitionIdList);
}
