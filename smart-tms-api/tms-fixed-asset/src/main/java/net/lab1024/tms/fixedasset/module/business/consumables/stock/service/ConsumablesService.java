package net.lab1024.tms.fixedasset.module.business.consumables.stock.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.material.category.CategoryCommonDao;
import net.lab1024.tms.common.module.business.material.category.domain.CategoryEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.dao.ConsumablesDao;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.dao.ConsumablesStockDao;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity.ConsumablesStockEntity;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesQueryForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesUpdateForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesDetailVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesStockStatisticVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesStockVO;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesVO;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 库存service
 *
 * @author lidoudou
 * @date 2023/4/12 上午11:25
 */
@Service
public class ConsumablesService {

    @Autowired
    private ConsumablesDao consumablesDao;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private CategoryCommonDao categoryCommonDao;
    @Autowired
    private ConsumablesDataTracerService dataTracerService;
    @Autowired
    private ConsumablesStockDao consumablesStockDao;
    @Autowired
    private LocationDao locationDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ConsumablesVO> queryPage(ConsumablesQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ConsumablesVO> entityList = consumablesDao.queryPage(page, queryForm);
        List<ConsumablesVO> list = SmartBeanUtil.copyList(entityList, ConsumablesVO.class);
        buildList(list, queryForm.getLocationId());
        PageResult<ConsumablesVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private void buildList(List<ConsumablesVO> list, Long locationId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<Long> categoryIdList = list.stream().map(ConsumablesVO::getCategoryId).collect(Collectors.toSet());
        List<CategoryEntity> categoryList = categoryCommonDao.selectBatchIds(categoryIdList);
        Map<Long, String> categoryNameMap = categoryList.stream().collect(Collectors.toMap(CategoryEntity::getCategoryId, CategoryEntity::getCategoryName));

        Set<Long> enterpriseIdSet = list.stream().map(ConsumablesVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));


        List<Long> consumablesIdList = list.stream().map(ConsumablesVO::getConsumablesId).collect(Collectors.toList());
        List<ConsumablesStockStatisticVO> consumablesStockList = consumablesStockDao.sumByConsumablesIdListAndLocationId(consumablesIdList, locationId);
        Map<Long, ConsumablesStockStatisticVO> statisticVOMap = consumablesStockList.stream().collect(Collectors.toMap(ConsumablesStockStatisticVO::getConsumablesId, Function.identity()));

        for (ConsumablesVO item : list) {
            item.setEnterpriseName(enterpriseNameMap.getOrDefault(item.getEnterpriseId(), StringConst.EMPTY));
            item.setCategoryName(categoryNameMap.getOrDefault(item.getCategoryId(), StringConst.EMPTY));

            ConsumablesStockStatisticVO consumablesStockStatisticVO = statisticVOMap.get(item.getConsumablesId());
            item.setAveragePrice(BigDecimal.ZERO);
            item.setStockCount(NumberConst.ZERO);
            item.setTotalAmount(BigDecimal.ZERO);
            if (null != consumablesStockStatisticVO) {
                item.setAveragePrice(consumablesStockStatisticVO.getAveragePrice());
                item.setStockCount(consumablesStockStatisticVO.getStockCount());
                item.setTotalAmount(consumablesStockStatisticVO.getTotalAmount());
            }
        }
    }

    /**
     * 添加
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> add(ConsumablesAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        EnterpriseEntity enterpriseEntity = commonEnterpriseDao.selectById(addForm.getEnterpriseId());
        if (null == enterpriseEntity) {
            return ResponseDTO.userErrorParam("所属公司不存在");
        }
        // 查询分类名称是否存在
        List<ConsumablesEntity> consumablesStockEntityList = consumablesDao.selectByNameExcludeIds(addForm.getConsumablesName(), null, Boolean.FALSE, addForm.getEnterpriseId());
        if (CollectionUtils.isNotEmpty(consumablesStockEntityList)) {
            return ResponseDTO.userErrorParam("资产名称已存在");
        }
        ConsumablesEntity consumablesEntity = SmartBeanUtil.copy(addForm, ConsumablesEntity.class);
        consumablesEntity.setConsumablesNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_CONSUMABLE_STOCK));
        consumablesEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        consumablesEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        consumablesDao.insert(consumablesEntity);
        ConsumablesVO consumablesVO = get(consumablesEntity.getConsumablesId());
        dataTracerService.saveLog(addForm, consumablesVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }


    /**
     * 更新
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(ConsumablesUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        ConsumablesEntity dbConsumablesEntity = consumablesDao.selectById(updateForm.getConsumablesId());
        if (null == dbConsumablesEntity) {
            return ResponseDTO.userErrorParam("数据不存在");
        }

        // 查询分类名称是否存在
        List<ConsumablesEntity> consumablesStockEntityList = consumablesDao.selectByNameExcludeIds(updateForm.getConsumablesName(), Arrays.asList(updateForm.getConsumablesId()), Boolean.FALSE, updateForm.getEnterpriseId());
        if (CollectionUtils.isNotEmpty(consumablesStockEntityList)) {
            return ResponseDTO.userErrorParam("资产名称已存在");
        }
        ConsumablesEntity consumablesEntity = SmartBeanUtil.copy(updateForm, ConsumablesEntity.class);
        consumablesEntity.setEnterpriseId(dbConsumablesEntity.getEnterpriseId());
        ConsumablesVO beforeDetail = get(consumablesEntity.getConsumablesId());
        consumablesDao.updateById(consumablesEntity);
        ConsumablesVO afterDetail = get(consumablesEntity.getConsumablesId());
        dataTracerService.updateLog(updateForm, beforeDetail, afterDetail, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取详情
     *
     * @param consumablesId
     * @return
     */
    public ResponseDTO<ConsumablesDetailVO> getDetail(Long consumablesId) {
        return ResponseDTO.ok(get(consumablesId));
    }

    private ConsumablesDetailVO get(Long consumablesId) {
        ConsumablesEntity consumablesEntity = consumablesDao.selectById(consumablesId);
        if (null == consumablesEntity) {
            return null;
        }
        ConsumablesDetailVO consumablesVO = SmartBeanUtil.copy(consumablesEntity, ConsumablesDetailVO.class);
        buildList(Arrays.asList(consumablesVO), null);
        buildStockList(consumablesVO);
        return consumablesVO;
    }

    private void buildStockList(ConsumablesDetailVO consumablesDetailVO) {
        List<ConsumablesStockEntity> consumablesStockList = consumablesStockDao.selectByConsumablesIdList(Arrays.asList(consumablesDetailVO.getConsumablesId()));
        if (CollectionUtils.isEmpty(consumablesStockList)) {
            return;
        }
        List<Long> locationIdList = consumablesStockList.stream().map(ConsumablesStockEntity::getLocationId).collect(Collectors.toList());

        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdList);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));

        List<ConsumablesStockVO> consumablesStockVOList = consumablesStockList.stream().map(e -> {
            ConsumablesStockVO consumablesStockVO = SmartBeanUtil.copy(e, ConsumablesStockVO.class);
            consumablesStockVO.setLocationName(locationNameMap.getOrDefault(e.getLocationId(), StringConst.EMPTY));
            consumablesStockVO.setTotalAmount(SmartBigDecimalUtil.Amount.multiply(e.getAveragePrice(), BigDecimal.valueOf(e.getStockCount())));
            return consumablesStockVO;
        }).collect(Collectors.toList());
        consumablesDetailVO.setStockList(consumablesStockVOList);
    }


    /**
     * 根据资产ID列表
     *
     * @param consumablesIdList
     * @return
     */
    public List<ConsumablesVO> queryDetailByIdList(List<Long> consumablesIdList) {
        if (CollectionUtils.isEmpty(consumablesIdList)) {
            return CommonConst.EMPTY_LIST;
        }
        List<ConsumablesEntity> consumablesEntityList = consumablesDao.selectBatchIds(consumablesIdList);
        if (CollectionUtils.isEmpty(consumablesEntityList)) {
            return CommonConst.EMPTY_LIST;
        }
        List<ConsumablesVO> assetList = SmartBeanUtil.copyList(consumablesEntityList, ConsumablesVO.class);
        buildList(assetList, null);
        return assetList;
    }
}
