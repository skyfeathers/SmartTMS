package net.lab1024.tms.fixedasset.module.business.consumables.purchase.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.dao.ConsumablesPurchaseDao;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity.ConsumablesPurchaseEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity.ConsumablesPurchaseItemEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form.ConsumablesPurchaseItemAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesStockEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.service.ConsumablesStockManager;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.ConsumablesStockRecordManager;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.ConsumablesStockRecordTypeEnum;
import net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.entity.ConsumablesStockRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 易耗品采购
 *
 * @author lidoudou
 * @date 2023/4/13 上午8:58
 */
@Service
public class ConsumablesPurchaseManager extends ServiceImpl<ConsumablesPurchaseDao, ConsumablesPurchaseEntity> {

    @Autowired
    private ConsumablesPurchaseItemManager consumablesPurchaseItemManager;
    @Autowired
    private ConsumablesStockManager consumablesStockManager;
    @Autowired
    private ConsumablesStockRecordManager consumablesStockRecordManager;

    /**
     * 保存
     *
     * @param consumablesPurchaseEntity
     * @param itemList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(ConsumablesPurchaseEntity consumablesPurchaseEntity, List<ConsumablesPurchaseItemAddForm> itemList) {
        getBaseMapper().insert(consumablesPurchaseEntity);

        Long locationId = consumablesPurchaseEntity.getLocationId();
        Long purchaseId = consumablesPurchaseEntity.getPurchaseId();
        List<ConsumablesPurchaseItemEntity> itemEntityList = itemList.stream().map(e -> {
            ConsumablesPurchaseItemEntity itemEntity = SmartBeanUtil.copy(e, ConsumablesPurchaseItemEntity.class);
            itemEntity.setPurchaseId(purchaseId);
            return itemEntity;
        }).collect(Collectors.toList());
        consumablesPurchaseItemManager.saveBatch(itemEntityList);

        List<Long> consumablesIdList = itemList.stream().map(ConsumablesPurchaseItemAddForm::getConsumablesId).collect(Collectors.toList());
        List<ConsumablesStockEntity> stockList = consumablesStockManager.getBaseMapper().selectByConsumablesIdListAndLocationId(consumablesIdList, locationId);
        Map<Long, ConsumablesStockEntity> consumablesStockMap = stockList.stream().collect(Collectors.toMap(ConsumablesStockEntity::getConsumablesId, Function.identity()));


        Map<Long, Integer> purchaseCountMap = itemList.stream().collect(Collectors.toMap(ConsumablesPurchaseItemAddForm::getConsumablesId, ConsumablesPurchaseItemAddForm::getCount));
        Map<Long, BigDecimal> purchasePriceMap = itemList.stream().collect(Collectors.toMap(ConsumablesPurchaseItemAddForm::getConsumablesId, ConsumablesPurchaseItemAddForm::getPrice));

        List<ConsumablesStockRecordEntity> stockRecordList = Lists.newArrayList();
        List<ConsumablesStockEntity> updateList = itemList.stream().map(purchaseItem -> {
            Long consumablesId = purchaseItem.getConsumablesId();

            ConsumablesStockEntity dbConsumablesStockEntity = consumablesStockMap.get(consumablesId);
            Long id = null;
            Integer dbStockCount = NumberConst.ZERO;
            BigDecimal dbAveragePrice = BigDecimal.ZERO;
            if (null != dbConsumablesStockEntity) {
                dbStockCount = dbConsumablesStockEntity.getStockCount();
                dbAveragePrice = dbConsumablesStockEntity.getAveragePrice();
                id = dbConsumablesStockEntity.getId();
            }

            ConsumablesStockEntity updateEntity = new ConsumablesStockEntity();
            updateEntity.setId(id);
            updateEntity.setConsumablesId(consumablesId);
            updateEntity.setLocationId(locationId);
            // 设置库存数量、平均价格
            Integer purchaseCount = purchaseCountMap.getOrDefault(consumablesId, NumberConst.ZERO);
            BigDecimal purchasePrice = purchasePriceMap.getOrDefault(consumablesId, BigDecimal.ZERO);
            Integer totalCount = purchaseCount + dbStockCount;
            updateEntity.setStockCount(totalCount);
            // 设置平均价格
            BigDecimal totalStockPrice = SmartBigDecimalUtil.Amount.multiply(dbAveragePrice, BigDecimal.valueOf(dbStockCount));
            BigDecimal totalPurchasePrice = SmartBigDecimalUtil.Amount.multiply(purchasePrice, BigDecimal.valueOf(purchaseCount));
            BigDecimal averagePrice = SmartBigDecimalUtil.Amount.divide(totalStockPrice.add(totalPurchasePrice), BigDecimal.valueOf(totalCount));
            updateEntity.setAveragePrice(averagePrice);

            stockRecordList.add(buildRecordEntity(consumablesPurchaseEntity, dbStockCount, dbAveragePrice, purchaseItem, updateEntity));
            return updateEntity;
        }).collect(Collectors.toList());
        consumablesStockManager.saveOrUpdateBatch(updateList);
        consumablesStockRecordManager.saveBatch(stockRecordList);
    }

    private ConsumablesStockRecordEntity buildRecordEntity(ConsumablesPurchaseEntity purchaseEntity, Integer dbStockCount, BigDecimal dbAveragePrice, ConsumablesPurchaseItemAddForm purchaseItem, ConsumablesStockEntity updateEntity) {
        ConsumablesStockRecordEntity recordEntity = new ConsumablesStockRecordEntity();

        recordEntity.setRecordType(ConsumablesStockRecordTypeEnum.PURCHASE.getValue());
        recordEntity.setOrderId(purchaseEntity.getPurchaseId());
        recordEntity.setOrderNo(purchaseEntity.getPurchaseNo());
        recordEntity.setLocationId(purchaseEntity.getLocationId());
        recordEntity.setCreateUserId(purchaseEntity.getCreateUserId());
        recordEntity.setCreateUserName(purchaseEntity.getCreateUserName());

        recordEntity.setConsumablesId(purchaseItem.getConsumablesId());
        recordEntity.setBeforeCount(dbStockCount);
        recordEntity.setChangeCount(purchaseItem.getCount());
        recordEntity.setAfterCount(updateEntity.getStockCount());
        recordEntity.setBeforeAveragePrice(dbAveragePrice);
        recordEntity.setPrice(purchaseItem.getPrice());
        recordEntity.setAfterAveragePrice(updateEntity.getAveragePrice());
        return recordEntity;
    }
}
