package net.lab1024.tms.fixedasset.module.business.consumables.check.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.module.business.consumables.check.constants.ConsumablesCheckStatusEnum;
import net.lab1024.tms.fixedasset.module.business.consumables.check.dao.ConsumablesCheckDao;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckEmployeeEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity.ConsumablesCheckItemEntity;
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
 * 易耗品盘点
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:35
 */
@Service
public class ConsumablesCheckManager extends ServiceImpl<ConsumablesCheckDao, ConsumablesCheckEntity> {

    @Autowired
    private ConsumablesCheckDao consumablesCheckDao;
    @Autowired
    private ConsumablesCheckItemManager consumablesCheckItemManager;
    @Autowired
    private ConsumablesCheckEmployeeManager consumablesCheckEmployeeManager;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private ConsumablesStockManager consumablesStockManager;
    @Autowired
    private ConsumablesStockRecordManager consumablesStockRecordManager;

    /**
     * 保存
     *
     * @param consumablesCheckEntity
     * @param employeeIdList
     * @param consumablesIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(ConsumablesCheckEntity consumablesCheckEntity, List<Long> employeeIdList, List<Long> consumablesIdList) {
        consumablesCheckEntity.setCheckNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_CONSUMABLE_CHECK));
        consumablesCheckDao.insert(consumablesCheckEntity);

        List<ConsumablesCheckEmployeeEntity> checkEmployeeList = employeeIdList.stream().map(employeeId -> {
            ConsumablesCheckEmployeeEntity consumablesCheckEmployeeEntity = new ConsumablesCheckEmployeeEntity();
            consumablesCheckEmployeeEntity.setCheckId(consumablesCheckEntity.getCheckId());
            consumablesCheckEmployeeEntity.setEmployeeId(employeeId);
            return consumablesCheckEmployeeEntity;
        }).collect(Collectors.toList());
        consumablesCheckEmployeeManager.saveBatch(checkEmployeeList);

        List<ConsumablesCheckItemEntity> itemList = consumablesIdList.stream().map(consumablesId -> {
            ConsumablesCheckItemEntity consumablesCheckItemEntity = new ConsumablesCheckItemEntity();
            consumablesCheckItemEntity.setConsumablesId(consumablesId);
            consumablesCheckItemEntity.setCheckId(consumablesCheckEntity.getCheckId());
            consumablesCheckItemEntity.setStatus(ConsumablesCheckStatusEnum.NOT_CHECK.getValue());
            return consumablesCheckItemEntity;
        }).collect(Collectors.toList());
        consumablesCheckItemManager.saveBatch(itemList);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void completeCheck(ConsumablesCheckEntity updateEntity, ConsumablesCheckEntity dbCheckEntity) {
        getBaseMapper().updateById(updateEntity);

        List<ConsumablesCheckItemEntity> checkItemList = consumablesCheckItemManager.getBaseMapper().queryByCheckId(updateEntity.getCheckId());
        Map<Long, Integer> checkCountMap = checkItemList.stream().collect(Collectors.toMap(ConsumablesCheckItemEntity::getConsumablesId, ConsumablesCheckItemEntity::getCount));

        List<Long> consumablesIdList = checkItemList.stream().map(ConsumablesCheckItemEntity::getConsumablesId).collect(Collectors.toList());
        List<ConsumablesStockEntity> consumablesStockEntityList = consumablesStockManager.getBaseMapper().selectByConsumablesIdListAndLocationId(consumablesIdList, updateEntity.getLocationId());

        List<ConsumablesStockRecordEntity> stockRecordEntityList = Lists.newArrayList();
        List<ConsumablesStockEntity> updateList = consumablesStockEntityList.stream().map(dbStockEntity -> {
            ConsumablesStockEntity stockEntity = new ConsumablesStockEntity();
            stockEntity.setId(dbStockEntity.getId());
            stockEntity.setStockCount(checkCountMap.getOrDefault(dbStockEntity.getConsumablesId(), NumberConst.ZERO));

            stockRecordEntityList.add(buildRecordEntity(dbCheckEntity, dbStockEntity, stockEntity.getStockCount()));
            return stockEntity;
        }).collect(Collectors.toList());
        consumablesStockManager.updateBatchById(updateList);
        consumablesStockRecordManager.saveBatch(stockRecordEntityList);
    }


    private ConsumablesStockRecordEntity buildRecordEntity(ConsumablesCheckEntity checkEntity, ConsumablesStockEntity dbStockEntity, Integer afterCount) {
        ConsumablesStockRecordEntity recordEntity = new ConsumablesStockRecordEntity();

        recordEntity.setRecordType(ConsumablesStockRecordTypeEnum.CHECK.getValue());
        recordEntity.setOrderId(checkEntity.getCheckId());
        recordEntity.setOrderNo(checkEntity.getCheckNo());
        recordEntity.setLocationId(checkEntity.getLocationId());
        recordEntity.setCreateUserId(checkEntity.getCreateUserId());
        recordEntity.setCreateUserName(checkEntity.getCreateUserName());

        recordEntity.setConsumablesId(dbStockEntity.getConsumablesId());
        recordEntity.setBeforeCount(dbStockEntity.getStockCount());
        recordEntity.setChangeCount(NumberConst.ZERO);
        recordEntity.setAfterCount(afterCount);

        recordEntity.setBeforeAveragePrice(dbStockEntity.getAveragePrice());
        recordEntity.setPrice(BigDecimal.ZERO);
        recordEntity.setAfterAveragePrice(dbStockEntity.getAveragePrice());
        return recordEntity;
    }
}
