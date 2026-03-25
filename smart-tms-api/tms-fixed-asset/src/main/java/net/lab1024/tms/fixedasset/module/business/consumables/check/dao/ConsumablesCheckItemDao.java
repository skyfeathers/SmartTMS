package net.lab1024.tms.fixedasset.module.business.consumables.check.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 盘点易耗品详细
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:34
 */
@Mapper
@Component
public interface ConsumablesCheckItemDao extends BaseMapper<ConsumablesCheckItemEntity> {

    /**
     * 根据采购ID查询关联易耗品
     *
     * @param checkId
     * @return
     */
    List<ConsumablesCheckItemEntity> queryByCheckId(@Param("checkId") Long checkId);

    /**
     * 根据盘点ID查询关联易耗品
     *
     * @param checkIdList
     * @return
     */
    List<ConsumablesCheckItemEntity> queryByCheckIdList(@Param("checkIdList") List<Long> checkIdList);
}
