package net.lab1024.tms.fixedasset.module.business.transfer.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.manager.AssetManager;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowStatusEnum;
import net.lab1024.tms.fixedasset.module.business.transfer.constants.TransferStatusEnum;
import net.lab1024.tms.fixedasset.module.business.transfer.dao.TransferDao;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferEntity;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转移manager
 *
 * @author lidoudou
 * @date 2023/3/21 上午10:04
 */
@Service
public class TransferManager extends ServiceImpl<TransferDao, TransferEntity> {

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private TransferItemManager transferItemManager;
    @Autowired
    private AssetManager assetManager;

    /**
     * 保存
     *
     * @param transferEntity
     * @param assetList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveTransfer(TransferEntity transferEntity, List<AssetEntity> assetList) {
        transferDao.insert(transferEntity);

        List<TransferItemEntity> itemList = assetList.stream().map(asset -> {
            TransferItemEntity transferItemEntity = new TransferItemEntity();
            transferItemEntity.setAssetId(asset.getAssetId());
            transferItemEntity.setTransferId(transferEntity.getTransferId());
            return transferItemEntity;
        }).collect(Collectors.toList());
        transferItemManager.saveBatch(itemList);

        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setStatus(AssetStatusEnum.ALLOCATION.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }

    /**
     * 确认转移
     *
     * @param transferEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void completeTransfer(TransferEntity transferEntity, List<Long> assetIdList) {
        transferEntity.setStatus(TransferStatusEnum.COMPLETE.getValue());
        transferDao.updateById(transferEntity);

        List<AssetEntity> updateList = assetIdList.stream().map(assetId -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(assetId);
            assetEntity.setLocationId(transferEntity.getToLocationId());
            assetEntity.setEnterpriseId(transferEntity.getEnterpriseId());
            assetEntity.setUserId(null);
            assetEntity.setDepartmentId(null);
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }


    /**
     * 驳回转移
     *
     * @param transferEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void rejectTransfer(TransferEntity transferEntity, List<Long> assetIdList) {
        transferEntity.setStatus(BorrowStatusEnum.REJECT.getValue());
        transferDao.updateById(transferEntity);

        List<AssetEntity> updateList = assetIdList.stream().map(assetId -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(assetId);
            assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }

}
