package net.lab1024.tms.fixedasset.module.business.allocation;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.tms.common.module.support.serialnumber.service.SerialNumberService;
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.allocation.constants.AllocationDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.allocation.constants.AllocationStatusEnum;
import net.lab1024.tms.fixedasset.module.business.allocation.dao.AllocationItemDao;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.*;
import net.lab1024.tms.fixedasset.module.business.allocation.manager.AllocationManager;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.dao.AssetDao;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetDataTracerService;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 调拨Service
 *
 * @author lidoudou‰
 * @date 2023/3/21 上午9:37
 */
@Service
public class AllocationService {

    @Autowired
    private AllocationItemDao allocationItemDao;
    @Autowired
    private AllocationManager allocationManager;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetDao assetDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private AllocationDataTracerService allocationDataTracerService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private AssetDataTracerService assetDataTracerService;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<AllocationVO> queryPage(AllocationQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AllocationEntity> allocationList = allocationManager.getBaseMapper().queryPage(page, queryForm);
        List<AllocationVO> allocationVOList = SmartBeanUtil.copyList(allocationList, AllocationVO.class);
        buildList(allocationVOList);
        PageResult<AllocationVO> pageResult = SmartPageUtil.convert2PageResult(page, allocationVOList);
        return pageResult;
    }

    private void buildList(List<AllocationVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> allocationIdList = list.stream().map(AllocationVO::getAllocationId).collect(Collectors.toList());
        List<AllocationItemEntity> itemEntityList = allocationItemDao.queryByAllocationIdList(allocationIdList);
        Map<Long, List<Long>> allocationAssetIdMap = itemEntityList.stream().collect(Collectors.groupingBy(AllocationItemEntity::getAllocationId, Collectors.mapping(AllocationItemEntity::getAssetId, Collectors.toList())));

        List<Long> assetIdList = itemEntityList.stream().map(AllocationItemEntity::getAssetId).collect(Collectors.toList());
        List<AssetVO> assetVOList = assetService.queryDetailByIdList(assetIdList);


        Set<Long> locationIdSet = list.stream().map(AllocationVO::getFromLocationId).collect(Collectors.toSet());
        locationIdSet.addAll(list.stream().map(AllocationVO::getToLocationId).collect(Collectors.toSet()));
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));

        Set<Long> enterpriseIdSet = list.stream().map(AllocationVO::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseEntityList = commonEnterpriseDao.selectBatchIds(enterpriseIdSet);
        Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        for (AllocationVO allocationVO : list) {
            allocationVO.setFromLocationName(locationNameMap.getOrDefault(allocationVO.getFromLocationId(), StringConst.EMPTY));
            allocationVO.setToLocationName(locationNameMap.getOrDefault(allocationVO.getToLocationId(), StringConst.EMPTY));
            allocationVO.setEnterpriseName(enterpriseNameMap.getOrDefault(allocationVO.getEnterpriseId(), StringConst.EMPTY));
            // 设置资产列表
            List<Long> allocationAssetIdList = allocationAssetIdMap.getOrDefault(allocationVO.getAllocationId(), CommonConst.EMPTY_LIST);
            List<AssetVO> assetList = assetVOList.stream().filter(asset -> allocationAssetIdList.contains(asset.getAssetId())).collect(Collectors.toList());
            allocationVO.setAssetList(assetList);
        }
    }

    /**
     * 申请调拨
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> addAllocation(AllocationAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long fromLocationId = addForm.getFromLocationId();
        Long toLocationId = addForm.getToLocationId();

        if (null == locationDao.selectById(fromLocationId)) {
            return ResponseDTO.userErrorParam("调出位置不存在");
        }
        if (null == locationDao.selectById(toLocationId)) {
            return ResponseDTO.userErrorParam("调入位置不存在");
        }
        if (fromLocationId.equals(toLocationId)) {
            return ResponseDTO.userErrorParam("调入与调出位置不能相同");
        }

        List<Long> assetIdList = addForm.getAssetIdList();
        List<AssetEntity> assetList = assetDao.selectByIdList(assetIdList, Boolean.FALSE);
        if (assetIdList.size() != assetList.size()) {
            return ResponseDTO.userErrorParam("资产数据异常，请刷新后重试～");
        }

        for (AssetEntity assetEntity : assetList) {
            if (!AssetStatusEnum.UNUSED.equalsValue(assetEntity.getStatus())) {
                String statusName = SmartBaseEnumUtil.getEnumDescByValue(assetEntity.getStatus(), AssetStatusEnum.class);
                return ResponseDTO.userErrorParam(String.format("%s处于%s状态，不能借用", assetEntity.getAssetName(), statusName));
            }
        }

        AllocationEntity allocationEntity = SmartBeanUtil.copy(addForm, AllocationEntity.class);
        allocationEntity.setAllocationNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_ALLOCATION));
        allocationEntity.setStatus(AllocationStatusEnum.WAIT.getValue());
        allocationEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        allocationEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        allocationManager.save(allocationEntity, assetList);
        AllocationVO allocationVO = getDetail(allocationEntity.getAllocationId());
        allocationDataTracerService.saveLog(addForm, allocationVO, dataTracerRequestForm);
        assetDataTracerService.applyAllocationLog(allocationEntity.getAllocationNo(), addForm.getAssetIdList(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * @param allocationId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> complete(Long allocationId, DataTracerRequestForm dataTracerRequestForm) {
        AllocationEntity allocationEntity = allocationManager.getBaseMapper().selectById(allocationId);
        if (null == allocationEntity) {
            return ResponseDTO.userErrorParam("调拨单据不存在");
        }
        List<AllocationItemEntity> itemList = allocationItemDao.queryByAllocationIdList(Arrays.asList(allocationId));
        List<Long> assetIdList = itemList.stream().map(AllocationItemEntity::getAssetId).collect(Collectors.toList());
        allocationManager.complete(allocationEntity, assetIdList);
        allocationDataTracerService.completeLog(allocationId, dataTracerRequestForm);
        assetDataTracerService.passRejectAllocationLog(allocationEntity.getAllocationNo(), assetIdList, AllocationDataTracerOperateTypeEnum.COMPLETE, AssetStatusEnum.UNUSED, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 驳回调拨申请
     *
     * @param allocationId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> reject(Long allocationId, DataTracerRequestForm dataTracerRequestForm) {
        AllocationEntity allocationEntity = allocationManager.getBaseMapper().selectById(allocationId);
        if (null == allocationEntity) {
            return ResponseDTO.userErrorParam("单据不存在");
        }
        List<AllocationItemEntity> itemList = allocationItemDao.queryByAllocationIdList(Arrays.asList(allocationId));
        List<Long> assetIdList = itemList.stream().map(AllocationItemEntity::getAssetId).collect(Collectors.toList());
        allocationManager.reject(allocationEntity, assetIdList);
        allocationDataTracerService.rejectLog(allocationId, dataTracerRequestForm);
        assetDataTracerService.passRejectAllocationLog(allocationEntity.getAllocationNo(), assetIdList, AllocationDataTracerOperateTypeEnum.REJECT, AssetStatusEnum.UNUSED, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public AllocationVO getDetail(Long allocationId) {
        AllocationEntity allocationEntity = allocationManager.getBaseMapper().selectById(allocationId);
        if (null == allocationEntity) {
            return null;
        }
        AllocationVO allocationVO = SmartBeanUtil.copy(allocationEntity, AllocationVO.class);
        buildList(Arrays.asList(allocationVO));
        return allocationVO;
    }
}
