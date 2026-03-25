package net.lab1024.tms.fixedasset.module.business.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.material.category.CategoryCommonDao;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetAddForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetDataTracerService;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import net.lab1024.tms.fixedasset.module.business.purchase.dao.PurchaseDao;
import net.lab1024.tms.fixedasset.module.business.purchase.dao.PurchaseItemDao;
import net.lab1024.tms.fixedasset.module.business.purchase.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资产采购
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:14
 */
@Service
public class PurchaseService {

    @Autowired
    private PurchaseDao purchaseDao;
    @Autowired
    private PurchaseItemDao purchaseItemDao;
    @Autowired
    private PurchaseManager purchaseManager;
    @Autowired
    private CategoryCommonDao commonCategoryDao;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private AssetService assetService;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private AssetDataTracerService assetDataTracerService;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<PurchaseVO> queryPage(PurchaseQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<PurchaseEntity> purchaseEntityList = purchaseDao.queryPage(page, queryForm);
        List<PurchaseVO> list = buildList(purchaseEntityList);
        PageResult<PurchaseVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private List<PurchaseVO> buildList(List<PurchaseEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<Long> purchaseIdList = list.stream().map(PurchaseEntity::getPurchaseId).collect(Collectors.toList());
        List<PurchaseItemEntity> purchaseItemEntityList = purchaseItemDao.queryByPurchaseIdList(purchaseIdList);
        Map<Long, List<Long>> purchaseAssetIdMap = purchaseItemEntityList.stream().collect(Collectors.groupingBy(PurchaseItemEntity::getPurchaseId, Collectors.mapping(PurchaseItemEntity::getAssetId, Collectors.toList())));

        List<Long> assetIdList = purchaseItemEntityList.stream().map(PurchaseItemEntity::getAssetId).collect(Collectors.toList());
        List<AssetVO> assetVOList = assetService.queryDetailByIdList(assetIdList);


        Set<Long> enterpriseIdSet = list.stream().map(PurchaseEntity::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        return list.stream().map(e -> {
            PurchaseVO purchaseVO = SmartBeanUtil.copy(e, PurchaseVO.class);
            List<Long> purchaseAssetIdList = purchaseAssetIdMap.getOrDefault(purchaseVO.getPurchaseId(), CommonConst.EMPTY_LIST);
            List<AssetVO> purchaseAssetVOList = assetVOList.stream().filter(asset -> purchaseAssetIdList.contains(asset.getAssetId())).collect(Collectors.toList());
            purchaseVO.setAssetList(purchaseAssetVOList);
            purchaseVO.setEnterpriseName(enterpriseNameMap.getOrDefault(e.getEnterpriseId(), StringConst.EMPTY));
            return purchaseVO;
        }).collect(Collectors.toList());
    }

    /**
     * 添加
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> add(PurchaseAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        if (SmartStringUtil.isBlank(addForm.getPurchaseNo())) {
            addForm.setPurchaseNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_PURCHASE));
        }
        List<AssetAddForm> assetList = addForm.getAssetList();
        assetList.forEach(e->e.setEnterpriseId(addForm.getEnterpriseId()));
        ResponseDTO<String> validateResponse = validate(assetList);
        if (!validateResponse.getOk()) {
            return validateResponse;
        }
        PurchaseEntity purchaseEntity = SmartBeanUtil.copy(addForm, PurchaseEntity.class);
        purchaseEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        purchaseEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        purchaseManager.save(purchaseEntity, assetList);

        List<PurchaseItemEntity> purchaseItemEntityList = purchaseItemDao.queryByPurchaseIdList(Lists.newArrayList(purchaseEntity.getPurchaseId()));
        List<Long> assetIdList = purchaseItemEntityList.stream().map(PurchaseItemEntity::getAssetId).collect(Collectors.toList());
        List<AssetVO> assetVOList = assetService.queryDetailByIdList(assetIdList);
        assetDataTracerService.batchSaveByPurchaseLog(assetVOList, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    private ResponseDTO<String> validate(List<AssetAddForm> assetList) {
        Set<Long> enterpriseIdSet = assetList.stream().map(AssetAddForm::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseEntityList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        List<Long> dbEnterpriseIdList = enterpriseEntityList.stream().map(EnterpriseEntity::getEnterpriseId).collect(Collectors.toList());

        Set<Long> locationIdSet = assetList.stream().map(AssetAddForm::getLocationId).collect(Collectors.toSet());
        List<LocationEntity> locationEntityList = locationDao.selectByLocationIdList(locationIdSet, Boolean.FALSE, Boolean.FALSE);
        List<Long> dbLocationIdList = locationEntityList.stream().map(LocationEntity::getLocationId).collect(Collectors.toList());


        Set<Long> categoryIdSet = assetList.stream().map(AssetAddForm::getCategoryId).collect(Collectors.toSet());
        List<CategoryEntity> categoryEntityList = commonCategoryDao.selectBatchIds(categoryIdSet);
        List<Long> dbCategoryIdList = categoryEntityList.stream().map(CategoryEntity::getCategoryId).collect(Collectors.toList());

        for (AssetAddForm asset : assetList) {
            if (!dbEnterpriseIdList.contains(asset.getEnterpriseId())) {
                return ResponseDTO.userErrorParam(String.format("【%s】所属公司不存在", asset.getAssetName()));
            }
            if (!dbLocationIdList.contains(asset.getLocationId())) {
                return ResponseDTO.userErrorParam(String.format("【%s】所属位置不存在", asset.getAssetName()));
            }
            if (!dbCategoryIdList.contains(asset.getCategoryId())) {
                return ResponseDTO.userErrorParam(String.format("【%s】所属分类不存在", asset.getAssetName()));
            }
        }
        return ResponseDTO.ok();
    }

}
