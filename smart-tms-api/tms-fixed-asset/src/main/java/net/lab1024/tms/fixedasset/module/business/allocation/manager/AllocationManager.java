package net.lab1024.tms.fixedasset.module.business.allocation.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.fixedasset.module.business.allocation.constants.AllocationStatusEnum;
import net.lab1024.tms.fixedasset.module.business.allocation.dao.AllocationDao;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationEntity;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationItemEntity;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.manager.AssetManager;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 调拨-退回manager
 *
 * @author lidoudou
 * @date 2023/3/21 上午10:04
 */
@Service
public class AllocationManager extends ServiceImpl<AllocationDao, AllocationEntity> {

    @Autowired
    private AllocationDao allocationDao;
    @Autowired
    private AllocationItemManager allocationItemManager;
    @Autowired
    private AssetManager assetManager;

    /**
     * 保存
     *
     * @param allocationEntity
     * @param assetList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(AllocationEntity allocationEntity, List<AssetEntity> assetList) {
        allocationDao.insert(allocationEntity);

        List<AllocationItemEntity> itemList = assetList.stream().map(asset -> {
            AllocationItemEntity allocationItemEntity = new AllocationItemEntity();
            allocationItemEntity.setAssetId(asset.getAssetId());
            allocationItemEntity.setAllocationId(allocationEntity.getAllocationId());
            return allocationItemEntity;
        }).collect(Collectors.toList());
        allocationItemManager.saveBatch(itemList);

        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setStatus(AssetStatusEnum.ALLOCATION.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }

    /**
     * 确认调拨
     *
     * @param allocationEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void complete(AllocationEntity allocationEntity, List<Long> assetIdList) {

        AllocationEntity updateEntity = new AllocationEntity();
        updateEntity.setAllocationId(allocationEntity.getAllocationId());
        updateEntity.setStatus(AllocationStatusEnum.COMPLETE.getValue());
        allocationDao.updateById(updateEntity);

        List<AssetEntity> updateList = assetIdList.stream().map(assetId -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(assetId);
            assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
            assetEntity.setEnterpriseId(allocationEntity.getEnterpriseId());
            assetEntity.setLocationId(allocationEntity.getToLocationId());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }


    /**
     * 驳回调拨
     *
     * @param allocationEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void reject(AllocationEntity allocationEntity, List<Long> assetIdList) {

        AllocationEntity updateEntity = new AllocationEntity();
        updateEntity.setAllocationId(allocationEntity.getAllocationId());
        allocationEntity.setStatus(BorrowStatusEnum.REJECT.getValue());
        allocationDao.updateById(updateEntity);

        List<AssetEntity> updateList = assetIdList.stream().map(assetId -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(assetId);
            assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }

}
