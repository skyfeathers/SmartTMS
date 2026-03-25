package net.lab1024.tms.fixedasset.module.business.requisitionrevert.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.manager.AssetManager;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionStatusEnum;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.dao.RequisitionRevertDao;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertEntity;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertItemEntity;
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
public class RequisitionManager extends ServiceImpl<RequisitionRevertDao, RequisitionRevertEntity> {

    @Autowired
    private RequisitionRevertDao requisitionDao;
    @Autowired
    private RequisitionItemManager requisitionRevertItemManager;
    @Autowired
    private AssetManager assetManager;

    /**
     * 保存
     *
     * @param requisitionEntity
     * @param assetList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveRequisition(RequisitionRevertEntity requisitionEntity, List<AssetEntity> assetList) {
        requisitionDao.insert(requisitionEntity);

        List<RequisitionRevertItemEntity> itemList = assetList.stream().map(asset -> {
            RequisitionRevertItemEntity requisitionRevertItemEntity = new RequisitionRevertItemEntity();
            requisitionRevertItemEntity.setAssetId(asset.getAssetId());
            requisitionRevertItemEntity.setRequisitionRevertId(requisitionEntity.getRequisitionRevertId());
            return requisitionRevertItemEntity;
        }).collect(Collectors.toList());
        requisitionRevertItemManager.saveBatch(itemList);

        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setStatus(AssetStatusEnum.TO_BE_REQUISITION.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }

    /**
     * 确认领用
     * @param requisitionEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void completeRequisition(RequisitionRevertEntity requisitionEntity, List<Long> assetIdList) {
        requisitionEntity.setStatus(RequisitionStatusEnum.COMPLETE.getValue());
        requisitionDao.updateById(requisitionEntity);

        List<AssetEntity> updateList = assetIdList.stream().map(assetId -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(assetId);
            assetEntity.setStatus(AssetStatusEnum.REQUISITION.getValue());
            assetEntity.setEnterpriseId(requisitionEntity.getEnterpriseId());
            assetEntity.setUserId(requisitionEntity.getUserId());
            assetEntity.setDepartmentId(requisitionEntity.getDepartmentId());
            assetEntity.setLocationId(requisitionEntity.getLocationId());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }



    /**
     * 驳回领用
     * @param requisitionEntity
     * @param assetIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void rejectRequisition(RequisitionRevertEntity requisitionEntity, List<Long> assetIdList) {
        requisitionEntity.setStatus(RequisitionStatusEnum.REJECT.getValue());
        requisitionDao.updateById(requisitionEntity);

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
     * @param revertEntity
     * @param assetList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveRevert(RequisitionRevertEntity revertEntity, List<AssetEntity> assetList) {
        requisitionDao.insert(revertEntity);

        List<RequisitionRevertItemEntity> itemList = assetList.stream().map(asset -> {
            RequisitionRevertItemEntity requisitionRevertItemEntity = new RequisitionRevertItemEntity();
            requisitionRevertItemEntity.setAssetId(asset.getAssetId());
            requisitionRevertItemEntity.setRequisitionRevertId(revertEntity.getRequisitionRevertId());
            return requisitionRevertItemEntity;
        }).collect(Collectors.toList());
        requisitionRevertItemManager.saveBatch(itemList);

        List<AssetEntity> updateList = assetList.stream().map(asset -> {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setAssetId(asset.getAssetId());
            assetEntity.setEnterpriseId(revertEntity.getEnterpriseId());
            assetEntity.setDepartmentId(revertEntity.getDepartmentId());
            assetEntity.setUserId(revertEntity.getUserId());
            assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.updateBatchById(updateList);
    }
}
