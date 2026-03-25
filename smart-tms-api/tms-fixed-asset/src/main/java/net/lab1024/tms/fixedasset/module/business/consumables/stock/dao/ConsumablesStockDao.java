package net.lab1024.tms.fixedasset.module.business.consumables.stock.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesStockEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesStockStatisticVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 库存
 *
 * @author lidoudou
 * @date 2023/4/12 上午11:25
 */
@Mapper
@Component
public interface ConsumablesStockDao extends BaseMapper<ConsumablesStockEntity> {

    /**
     * 根据易耗品ID查询
     *
     * @param consumablesIdList
     * @return
     */
    List<ConsumablesStockEntity> selectByConsumablesIdList(@Param("consumablesIdList") List<Long> consumablesIdList);

    /**
     * 根据易耗品ID统计库存
     *
     * @param consumablesIdList
     * @return
     */
    List<ConsumablesStockStatisticVO> sumByConsumablesIdListAndLocationId(@Param("consumablesIdList") List<Long> consumablesIdList, @Param("locationId") Long locationId);

    /**
     * 根据ID、所在位置查询
     *
     * @param consumablesIdList
     * @return
     */
    List<ConsumablesStockEntity> selectByConsumablesIdListAndLocationId(@Param("consumablesIdList") List<Long> consumablesIdList, @Param("locationId") Long locationId);
}
