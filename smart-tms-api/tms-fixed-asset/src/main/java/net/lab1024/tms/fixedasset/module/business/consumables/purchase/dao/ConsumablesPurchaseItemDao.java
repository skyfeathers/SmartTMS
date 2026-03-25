package net.lab1024.tms.fixedasset.module.business.consumables.purchase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity.ConsumablesPurchaseItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 易耗品采购
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:26
 */
@Mapper
@Component
public interface ConsumablesPurchaseItemDao extends BaseMapper<ConsumablesPurchaseItemEntity> {

    /**
     * 根据采购ID查询关联资产
     *
     * @param purchaseIdList
     * @return
     */
    List<ConsumablesPurchaseItemEntity> queryByPurchaseIdList(@Param("purchaseIdList") List<Long> purchaseIdList);
}
