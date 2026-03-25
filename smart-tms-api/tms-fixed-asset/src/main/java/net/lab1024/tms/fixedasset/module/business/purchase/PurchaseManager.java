package net.lab1024.tms.fixedasset.module.business.purchase;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetAddForm;
import net.lab1024.tms.fixedasset.module.business.asset.manager.AssetManager;
import net.lab1024.tms.fixedasset.module.business.purchase.dao.PurchaseDao;
import net.lab1024.tms.fixedasset.module.business.purchase.domain.PurchaseEntity;
import net.lab1024.tms.fixedasset.module.business.purchase.domain.PurchaseItemEntity;
import net.lab1024.tms.fixedasset.module.business.purchase.manager.AssetPurchaseItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 固定资产采购  Manager
 *
 * @author lidoudou
 * @date 2023/3/20 上午10:47
 */
@Service
public class PurchaseManager extends ServiceImpl<PurchaseDao, PurchaseEntity> {

    @Autowired
    private PurchaseDao purchaseDao;
    @Autowired
    private AssetManager assetManager;
    @Autowired
    private AssetPurchaseItemManager assetPurchaseItemManager;
    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 保存
     *
     * @param purchaseEntity
     * @param assetList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(PurchaseEntity purchaseEntity, List<AssetAddForm> assetList) {
        purchaseDao.insert(purchaseEntity);

        List<AssetEntity> assetEntityList = assetList.stream().map(e -> {
            AssetEntity assetEntity = SmartBeanUtil.copy(e, AssetEntity.class);
            assetEntity.setEnterpriseId(purchaseEntity.getEnterpriseId());
            assetEntity.setAssetNo(serialNumberService.generate(SerialNumberIdEnum.ASSET));
            assetEntity.setSourceId(purchaseEntity.getSourceId());
            assetEntity.setStatus(AssetStatusEnum.UNUSED.getValue());
            assetEntity.setCreateUserName(purchaseEntity.getCreateUserName());
            assetEntity.setCreateUserId(purchaseEntity.getCreateUserId());
            return assetEntity;
        }).collect(Collectors.toList());
        assetManager.saveBatch(assetEntityList);

        List<PurchaseItemEntity> itemList = assetEntityList.stream().map(assetEntity -> {
            PurchaseItemEntity purchaseItemEntity = new PurchaseItemEntity();
            purchaseItemEntity.setAssetId(assetEntity.getAssetId());
            purchaseItemEntity.setPurchaseId(purchaseEntity.getPurchaseId());
            return purchaseItemEntity;
        }).collect(Collectors.toList());
        assetPurchaseItemManager.saveBatch(itemList);
    }

}
