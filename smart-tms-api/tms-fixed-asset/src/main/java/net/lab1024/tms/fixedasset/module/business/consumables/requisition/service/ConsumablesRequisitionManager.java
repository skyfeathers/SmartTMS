package net.lab1024.tms.fixedasset.module.business.consumables.requisition.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.dao.ConsumablesRequisitionDao;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity.ConsumablesRequisitionEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity.ConsumablesRequisitionItemEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionItemAddForm;
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
import java.util.stream.Collectors;

/**
 * 领用-退回manager
 *
 * @author lidoudou
 * @date 2023/3/21 上午10:04
 */
@Service
public class ConsumablesRequisitionManager extends ServiceImpl<ConsumablesRequisitionDao, ConsumablesRequisitionEntity> {

    @Autowired
    private ConsumablesRequisitionDao requisitionDao;
    @Autowired
    private ConsumablesRequisitionItemManager requisitionItemManager;
    @Autowired
    private ConsumablesStockManager consumablesStockManager;
    @Autowired
    private ConsumablesStockRecordManager consumablesStockRecordManager;

    /**
     * 保存
     *
     * @param requisitionEntity
     * @param consumablesStockList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveRequisition(ConsumablesRequisitionEntity requisitionEntity, List<ConsumablesRequisitionItemAddForm> consumablesStockList) {
        requisitionDao.insert(requisitionEntity);

        List<ConsumablesRequisitionItemEntity> itemList = consumablesStockList.stream().map(item -> {
            ConsumablesRequisitionItemEntity consumablesRequisitionItemEntity = new ConsumablesRequisitionItemEntity();
            consumablesRequisitionItemEntity.setConsumablesId(item.getConsumablesId());
            consumablesRequisitionItemEntity.setRequisitionId(requisitionEntity.getRequisitionId());
            consumablesRequisitionItemEntity.setCount(item.getCount());
            return consumablesRequisitionItemEntity;
        }).collect(Collectors.toList());
        requisitionItemManager.saveBatch(itemList);

        // 领用后减少易耗品数量
        Map<Long, Integer> requisitionCountMap = consumablesStockList.stream().collect(Collectors.toMap(ConsumablesRequisitionItemAddForm::getConsumablesId, ConsumablesRequisitionItemAddForm::getCount));
        List<Long> consumablesIdList = consumablesStockList.stream().map(ConsumablesRequisitionItemAddForm::getConsumablesId).collect(Collectors.toList());
        List<ConsumablesStockEntity> stockList = consumablesStockManager.getBaseMapper().selectByConsumablesIdListAndLocationId(consumablesIdList, requisitionEntity.getLocationId());

        List<ConsumablesStockRecordEntity> stockRecordEntityList = Lists.newArrayList();
        List<ConsumablesStockEntity> updateList = stockList.stream().map(dbStockEntity -> {
            ConsumablesStockEntity consumablesStockEntity = new ConsumablesStockEntity();
            consumablesStockEntity.setId(dbStockEntity.getId());
            Integer changeCount = requisitionCountMap.getOrDefault(dbStockEntity.getConsumablesId(), NumberConst.ZERO);
            Integer afterCount = dbStockEntity.getStockCount() - changeCount;
            consumablesStockEntity.setStockCount(afterCount);

            stockRecordEntityList.add(buildRecordEntity(requisitionEntity, dbStockEntity, changeCount, afterCount));
            return consumablesStockEntity;
        }).collect(Collectors.toList());
        consumablesStockManager.updateBatchById(updateList);
        consumablesStockRecordManager.saveBatch(stockRecordEntityList);
    }

    private ConsumablesStockRecordEntity buildRecordEntity(ConsumablesRequisitionEntity requisitionEntity, ConsumablesStockEntity dbStockEntity, Integer changeCount, Integer afterCount) {
        ConsumablesStockRecordEntity recordEntity = new ConsumablesStockRecordEntity();

        recordEntity.setRecordType(ConsumablesStockRecordTypeEnum.REQUISITION.getValue());
        recordEntity.setLocationId(requisitionEntity.getLocationId());
        recordEntity.setOrderId(requisitionEntity.getRequisitionId());
        recordEntity.setOrderNo(requisitionEntity.getRequisitionNo());
        recordEntity.setCreateUserId(requisitionEntity.getCreateUserId());
        recordEntity.setCreateUserName(requisitionEntity.getCreateUserName());

        recordEntity.setConsumablesId(dbStockEntity.getConsumablesId());
        recordEntity.setBeforeCount(dbStockEntity.getStockCount());
        recordEntity.setChangeCount(changeCount);
        recordEntity.setAfterCount(afterCount);
        recordEntity.setBeforeAveragePrice(dbStockEntity.getAveragePrice());
        recordEntity.setPrice(BigDecimal.ZERO);
        recordEntity.setAfterAveragePrice(dbStockEntity.getAveragePrice());
        return recordEntity;
    }

}
