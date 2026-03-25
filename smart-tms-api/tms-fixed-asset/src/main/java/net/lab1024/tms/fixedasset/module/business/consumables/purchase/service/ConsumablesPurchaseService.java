package net.lab1024.tms.fixedasset.module.business.consumables.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.category.CategoryCommonDao;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.dao.ConsumablesPurchaseDao;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.dao.ConsumablesPurchaseItemDao;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity.ConsumablesPurchaseEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity.ConsumablesPurchaseItemEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form.ConsumablesPurchaseAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form.ConsumablesPurchaseQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo.ConsumablesPurchaseDetailVO;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo.ConsumablesPurchaseItemVO;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo.ConsumablesPurchaseVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.dao.ConsumablesDao;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesEntity;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 易耗品采购
 *
 * @author lidoudou
 * @date 2023/4/13 上午8:58
 */
@Service
public class ConsumablesPurchaseService {

    @Autowired
    private ConsumablesPurchaseDao consumablesPurchaseDao;
    @Autowired
    private ConsumablesPurchaseItemDao consumablesPurchaseItemDao;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private ConsumablesDao consumablesDao;
    @Autowired
    private ConsumablesPurchaseManager consumablesPurchaseManager;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private CategoryCommonDao categoryCommonDao;
    @Autowired
    private ConsumablesPurchaseDataTracerService dataTracerService;
    @Autowired
    private LocationDao locationDao;


    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ConsumablesPurchaseVO> queryPage(ConsumablesPurchaseQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ConsumablesPurchaseVO> list = consumablesPurchaseDao.queryPage(page, queryForm);
        buildList(list);
        PageResult<ConsumablesPurchaseVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<ConsumablesPurchaseVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> enterpriseIdSet = list.stream().map(ConsumablesPurchaseVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        Set<Long> locationIdSet = list.stream().map(ConsumablesPurchaseVO::getLocationId).collect(Collectors.toSet());
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));

        list.forEach(item -> {
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
            item.setLocationName(locationNameMap.getOrDefault(item.getLocationId(), StringConst.EMPTY));
        });
    }


    /**
     * 新建
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(ConsumablesPurchaseAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long enterpriseId = addForm.getEnterpriseId();
        EnterpriseEntity enterpriseEntity = commonEnterpriseDao.selectById(enterpriseId);
        if (null == enterpriseEntity) {
            return ResponseDTO.userErrorParam("所属公司不存在");
        }
        LocationEntity locationEntity = locationDao.selectById(addForm.getLocationId());
        if (null == locationEntity) {
            return ResponseDTO.userErrorParam("所属位置不存在");
        }
        ConsumablesPurchaseEntity consumablesPurchaseEntity = SmartBeanUtil.copy(addForm, ConsumablesPurchaseEntity.class);
        consumablesPurchaseEntity.setPurchaseNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_CONSUMABLE_PURCHASE));
        consumablesPurchaseEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        consumablesPurchaseEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        consumablesPurchaseManager.save(consumablesPurchaseEntity, addForm.getItemList());
        ConsumablesPurchaseDetailVO purchaseDetailVO = get(consumablesPurchaseEntity.getPurchaseId());
        dataTracerService.saveLog(addForm, purchaseDetailVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取详情
     *
     * @param purchaseId
     * @return
     */
    public ResponseDTO<ConsumablesPurchaseDetailVO> getDetail(Long purchaseId) {
        return ResponseDTO.ok(get(purchaseId));
    }

    private ConsumablesPurchaseDetailVO get(Long purchaseId) {
        ConsumablesPurchaseEntity consumablesPurchaseEntity = consumablesPurchaseDao.selectById(purchaseId);
        if (null == consumablesPurchaseEntity) {
            return null;
        }
        ConsumablesPurchaseDetailVO consumablesPurchaseDetailVO = SmartBeanUtil.copy(consumablesPurchaseEntity, ConsumablesPurchaseDetailVO.class);
        // 设置公司名称
        EnterpriseEntity enterpriseEntity = commonEnterpriseDao.selectById(consumablesPurchaseEntity.getEnterpriseId());
        consumablesPurchaseDetailVO.setEnterpriseName(null != enterpriseEntity ? enterpriseEntity.getEnterpriseName() : StringConst.EMPTY);
        // 设置所属位置
        LocationEntity locationEntity = locationDao.selectById(consumablesPurchaseEntity.getLocationId());
        consumablesPurchaseDetailVO.setLocationName(null != locationEntity ? locationEntity.getLocationName() : StringConst.EMPTY);


        List<ConsumablesPurchaseItemEntity> itemList = consumablesPurchaseItemDao.queryByPurchaseIdList(Arrays.asList(purchaseId));
        List<Long> consumablesIdList = itemList.stream().map(ConsumablesPurchaseItemEntity::getConsumablesId).collect(Collectors.toList());

        List<ConsumablesEntity> stockList = consumablesDao.selectBatchIds(consumablesIdList);
        Map<Long, ConsumablesEntity> consumablesStockMap = stockList.stream().collect(Collectors.toMap(ConsumablesEntity::getConsumablesId, Function.identity()));

        Set<Long> categoryIdSet = stockList.stream().map(ConsumablesEntity::getCategoryId).collect(Collectors.toSet());
        List<CategoryEntity> categoryEntityList = categoryCommonDao.selectBatchIds(categoryIdSet);
        Map<Long, String> categoryNameMap = categoryEntityList.stream().collect(Collectors.toMap(CategoryEntity::getCategoryId, CategoryEntity::getCategoryName));

        List<ConsumablesPurchaseItemVO> itemVOList = itemList.stream().map(e -> {
            ConsumablesEntity consumablesEntity = consumablesStockMap.get(e.getConsumablesId());
            ConsumablesPurchaseItemVO consumablesPurchaseItemVO = SmartBeanUtil.copy(consumablesEntity, ConsumablesPurchaseItemVO.class);
            consumablesPurchaseItemVO.setCategoryName(categoryNameMap.getOrDefault(consumablesEntity.getCategoryId(), StringConst.EMPTY));
            consumablesPurchaseItemVO.setPrice(e.getPrice());
            consumablesPurchaseItemVO.setCount(e.getCount());
            return consumablesPurchaseItemVO;
        }).collect(Collectors.toList());
        consumablesPurchaseDetailVO.setItemList(itemVOList);

        return consumablesPurchaseDetailVO;
    }
}
