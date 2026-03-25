package net.lab1024.tms.fixedasset.module.business.purchase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.fixedasset.module.business.purchase.domain.PurchaseItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资产采购关联表
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:26
 */
@Mapper
@Component
public interface PurchaseItemDao extends BaseMapper<PurchaseItemEntity> {

    /**
     * 根据采购ID查询关联资产
     *
     * @param purchaseIdList
     * @return
     */
    List<PurchaseItemEntity> queryByPurchaseIdList(@Param("purchaseIdList") List<Long> purchaseIdList);
}
