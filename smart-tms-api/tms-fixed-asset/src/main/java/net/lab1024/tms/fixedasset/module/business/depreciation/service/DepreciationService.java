package net.lab1024.tms.fixedasset.module.business.depreciation.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.module.business.asset.dao.AssetDao;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.depreciation.dao.DepreciationDao;
import net.lab1024.tms.fixedasset.module.business.depreciation.dao.DepreciationItemDao;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity.DepreciationEntity;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity.DepreciationItemEntity;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.form.DepreciationAddForm;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.form.DepreciationQueryForm;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo.DepreciationItemVO;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo.DepreciationVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资产折旧
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:13
 */
@Service
public class DepreciationService {

    @Autowired
    private DepreciationDao depreciationDao;
    @Autowired
    private DepreciationItemDao depreciationItemDao;
    @Autowired
    private AssetDao assetDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private DepreciationManage depreciationManage;
    @Autowired
    private DepreciationDataTracerService dataTracerService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;

    public ResponseDTO<PageResult<DepreciationVO>> queryPage(DepreciationQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DepreciationVO> depreciationList = depreciationDao.queryPage(page, queryForm);
        buildList(depreciationList);
        PageResult<DepreciationVO> pageResult = SmartPageUtil.convert2PageResult(page, depreciationList);
        return ResponseDTO.ok(pageResult);
    }

    private void buildList(List<DepreciationVO> depreciationList) {
        if (CollectionUtils.isEmpty(depreciationList)) {
            return;
        }
        List<Long> depreciationIdList = depreciationList.stream().map(DepreciationVO::getDepreciationId).collect(Collectors.toList());
        List<DepreciationItemEntity> depreciationItemEntityList = depreciationItemDao.selectByDepreciationIdList(depreciationIdList);
        Map<Long, List<DepreciationItemEntity>> depreciationItemMap = depreciationItemEntityList.stream().collect(Collectors.groupingBy(DepreciationItemEntity::getDepreciationId));
        // 查询资产
        Set<Long> assetIdList = depreciationItemEntityList.stream().map(DepreciationItemEntity::getAssetId).collect(Collectors.toSet());
        List<AssetEntity> assetList = assetDao.selectBatchIds(assetIdList);
        Map<Long, AssetEntity> assetMap =
                assetList.stream().collect(Collectors.toMap(AssetEntity::getAssetId, Function.identity()));

        // 查询所属公司
        Set<Long> enterpriseIdList = depreciationList.stream().map(DepreciationVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = commonEnterpriseDao.selectBatchIds(enterpriseIdList);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        depreciationList.forEach(depreciation -> {
            Long depreciationId = depreciation.getDepreciationId();
            List<DepreciationItemEntity> depreciationAssetList = depreciationItemMap.getOrDefault(depreciationId, Lists.newArrayList());
            List<DepreciationItemVO> depreciationItemList = depreciationAssetList.stream().map(e -> {
                DepreciationItemVO depreciationItemVO = SmartBeanUtil.copy(e, DepreciationItemVO.class);
                AssetEntity assetEntity = assetMap.get(e.getAssetId());
                depreciationItemVO.setAssetName(assetEntity.getAssetName());
                depreciationItemVO.setAssetNo(assetEntity.getAssetNo());
                return depreciationItemVO;
            }).collect(Collectors.toList());
            depreciation.setAssetList(depreciationItemList);
            depreciation.setEnterpriseName(enterpriseNameMap.getOrDefault(depreciation.getEnterpriseId(), StringConst.EMPTY));
        });
    }

    /**
     * 保存
     *
     * @param addForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> save(DepreciationAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        LocalDate depreciationDate = addForm.getDepreciationDate();
        DepreciationEntity depreciationEntity = depreciationDao.selectByDepreciationDate(addForm.getEnterpriseId(), depreciationDate, Boolean.FALSE);
        if (depreciationEntity != null) {
            return ResponseDTO.userErrorParam("已存在相同计提日期单据，不能重复提交");
        }

        List<DepreciationItemVO> assetList = queryAsset(addForm);
        if (CollectionUtils.isEmpty(assetList)) {
            return ResponseDTO.userErrorParam("当前公司下不存在资产");
        }

        String depreciationNo = serialNumberService.generate(SerialNumberIdEnum.ASSET_DEPRECIATION);
        DepreciationEntity insertEntity = SmartBeanUtil.copy(addForm, DepreciationEntity.class);
        insertEntity.setDepreciationNo(depreciationNo);
        insertEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        insertEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        depreciationManage.save(insertEntity, assetList);
        DepreciationVO depreciationVO = get(insertEntity.getDepreciationId());
        dataTracerService.saveLog(addForm, depreciationVO, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public ResponseDTO<DepreciationVO> getDetail(Long depreciationId) {
        DepreciationVO depreciationVO = get(depreciationId);
        if (null == depreciationVO) {
            return ResponseDTO.userErrorParam("折旧单不存在");
        }
        return ResponseDTO.ok(depreciationVO);
    }

    private DepreciationVO get(Long depreciationId) {
        DepreciationEntity depreciationEntity = depreciationDao.selectById(depreciationId);
        if (null == depreciationEntity) {
            return null;
        }
        DepreciationVO depreciationVO = SmartBeanUtil.copy(depreciationEntity, DepreciationVO.class);
        buildList(Arrays.asList(depreciationVO));
        return depreciationVO;
    }

    public ResponseDTO<String> cancel(Long depreciationId, DataTracerRequestForm dataTracerRequestForm) {
        DepreciationEntity depreciationEntity = depreciationDao.selectById(depreciationId);
        if (null == depreciationEntity || depreciationEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("折旧单不存在");
        }
        DepreciationEntity updateEntity = new DepreciationEntity();
        updateEntity.setDepreciationId(depreciationId);
        updateEntity.setDeletedFlag(Boolean.TRUE);
        depreciationDao.updateById(depreciationEntity);
        dataTracerService.cancelLog(depreciationId, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 根据所属公司查询资产并计算折旧信息
     *
     * @param addForm
     * @return
     */
    public List<DepreciationItemVO> queryAsset(DepreciationAddForm addForm) {
        List<AssetEntity> assetEntityList = assetDao.selectByEnterpriseId(addForm.getEnterpriseId(), Boolean.FALSE);
        if (CollectionUtils.isEmpty(assetEntityList)) {
            return Lists.newArrayList();
        }
        // 查询上一个周期的折旧
        LocalDate prevDepreciationDate = addForm.getDepreciationDate().plusMonths(-1);
        DepreciationEntity prevDepreciationEntity = depreciationDao.selectByDepreciationDate(addForm.getEnterpriseId(), prevDepreciationDate, Boolean.FALSE);
        List<DepreciationItemEntity> prevItemList = Lists.newArrayList();
        if (null != prevDepreciationEntity) {
            prevItemList = depreciationItemDao.selectByDepreciationIdList(Arrays.asList(prevDepreciationEntity.getDepreciationId()));
        }
        Map<Long, BigDecimal> prevDepreciationAmountMap = prevItemList.stream().collect(Collectors.toMap(DepreciationItemEntity::getAssetId, DepreciationItemEntity::getEndingDepreciationAmount));


        List<DepreciationItemVO> assetList = SmartBeanUtil.copyList(assetEntityList, DepreciationItemVO.class);
        assetList.forEach(asset -> {
            // 期初折旧金额
            BigDecimal initialDepreciationAmount = prevDepreciationAmountMap.getOrDefault(asset.getAssetId(), BigDecimal.ZERO);
            asset.setInitialDepreciationAmount(initialDepreciationAmount);
            // 本次折旧金额  金额 * (100 - 折旧率) / 100 / 预计使用期限（月）
            BigDecimal amount = asset.getPrice().multiply(BigDecimal.valueOf(100).subtract(asset.getResidualValueRate()));
            BigDecimal depreciationAmount = BigDecimal.ZERO;
            if (asset.getMonthCount() > 0) {
                depreciationAmount = SmartBigDecimalUtil.Amount.divide(amount.divide(BigDecimal.valueOf(100)), BigDecimal.valueOf(asset.getMonthCount()));
            }
            asset.setDepreciationAmount(depreciationAmount);
            // 月折旧率
            BigDecimal monthResidualValueRate = BigDecimal.ZERO;
            if (depreciationAmount.compareTo(BigDecimal.ZERO) > 0) {
                monthResidualValueRate = SmartBigDecimalUtil.Amount.divide(depreciationAmount.multiply(BigDecimal.valueOf(100)), asset.getPrice());
            }
            asset.setMonthResidualValueRate(monthResidualValueRate);
            // 期末折旧金额
            asset.setEndingDepreciationAmount(SmartBigDecimalUtil.Amount.add(initialDepreciationAmount, depreciationAmount));
        });

        return assetList;
    }
}
