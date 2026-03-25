package net.lab1024.tms.fixedasset.module.business.borrowback.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.manager.AssetManager;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowStatusEnum;
import net.lab1024.tms.fixedasset.module.business.borrowback.dao.BorrowBackDao;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackEntity;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 领用-退回manager
 *
 * @author lidoudou
 * @date 2023/3/21 上午10:04
 */
@Service
public class BorrowBackManager extends ServiceImpl<BorrowBackDao, BorrowBackEntity> {

    @Autowired
    private BorrowBackDao borrowBackDao;
    @Autowired
    private BorrowBackItemManager borrowBackItemManager;
    @Autowired
    private AssetManager assetManager;

    /**
     * 保存
     *
     * @param borrowBackEntity
     * @param assetList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveBorrow(BorrowBackEntity borrowBackEntity, List<AssetEntity> assetList) {
        borrowBackDao.insert(borrowBackEntity);

        List<BorrowBackItemEntity> itemList = assetList.stream().map(asset -> {
            BorrowBackItemEntity borrowBackItemEntity = new BorrowBackItemEntity();
            borrowBackItemEntity.setAssetId(asset.getAssetId());
            borrowBackItemEntity.setBorrowBackId(borrowBackEntity.getBorrowBackId());
            return borrowBackItemEntity;
        }).collect(Collectors.toList());
        borrowBackItemManager.saveBatch(itemList);

        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setStatus(AssetStatusEnum.TO_BE_BORROW.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }

    /**
     * 确认领用
     * @param borrowBackEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void completeBorrow(BorrowBackEntity borrowBackEntity, List<Long> assetIdList) {
        borrowBackEntity.setStatus(BorrowStatusEnum.COMPLETE.getValue());
        borrowBackDao.updateById(borrowBackEntity);

        List<AssetEntity> updateList = assetIdList.stream().map(assetId -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(assetId);
            assetEntity.setStatus(AssetStatusEnum.BORROW.getValue());
            assetEntity.setEnterpriseId(borrowBackEntity.getEnterpriseId());
            assetEntity.setUserId(borrowBackEntity.getUserId());
            assetEntity.setDepartmentId(borrowBackEntity.getDepartmentId());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }



    /**
     * 驳回领用
     * @param borrowBackEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void rejectBorrow(BorrowBackEntity borrowBackEntity, List<Long> assetIdList) {
        borrowBackEntity.setStatus(BorrowStatusEnum.REJECT.getValue());
        borrowBackDao.updateById(borrowBackEntity);

        List<AssetEntity> updateList = assetIdList.stream().map(assetId -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(assetId);
            assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }


    /**
     * 保存
     *
     * @param backEntity
     * @param assetList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveBack(BorrowBackEntity backEntity, List<AssetEntity> assetList) {
        borrowBackDao.insert(backEntity);

        List<BorrowBackItemEntity> itemList = assetList.stream().map(asset -> {
            BorrowBackItemEntity borrowBackItemEntity = new BorrowBackItemEntity();
            borrowBackItemEntity.setAssetId(asset.getAssetId());
            borrowBackItemEntity.setBorrowBackId(backEntity.getBorrowBackId());
            return borrowBackItemEntity;
        }).collect(Collectors.toList());
        borrowBackItemManager.saveBatch(itemList);

        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setEnterpriseId(backEntity.getEnterpriseId());
            assetEntity.setDepartmentId(backEntity.getDepartmentId());
            assetEntity.setUserId(backEntity.getUserId());
            assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }
}
