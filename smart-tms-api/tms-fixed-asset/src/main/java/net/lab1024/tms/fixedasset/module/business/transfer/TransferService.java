package net.lab1024.tms.fixedasset.module.business.transfer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.NumberConst;
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
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.dao.AssetDao;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetDataTracerService;
import net.lab1024.tms.fixedasset.module.business.asset.service.AssetService;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import net.lab1024.tms.fixedasset.module.business.transfer.constants.TransferStatusEnum;
import net.lab1024.tms.fixedasset.module.business.transfer.dao.TransferItemDao;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.*;
import net.lab1024.tms.fixedasset.module.business.transfer.manager.TransferManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 转移Service
 *
 * @author lidoudou‰
 * @date 2023/3/21 上午9:37
 */
@Service
public class TransferService {

    @Autowired
    private TransferItemDao transferItemDao;
    @Autowired
    private TransferManager transferManager;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetDao assetDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private CommonEnterpriseDao enterpriseDao;
    @Autowired
    private TransferDataTracerService transferDataTracerService;
    @Autowired
    private AssetDataTracerService assetDataTracerService;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<TransferVO> queryPage(TransferQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<TransferEntity> transferList = transferManager.getBaseMapper().queryPage(page, queryForm);
        List<TransferVO> list = buildList(transferList);
        PageResult<TransferVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    private List<TransferVO> buildList(List<TransferEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<Long> transferIdList = list.stream().map(TransferEntity::getTransferId).collect(Collectors.toList());
        List<TransferItemEntity> itemEntityList = transferItemDao.queryByTransferIdList(transferIdList);
        Map<Long, List<Long>> transferAssetIdMap = itemEntityList.stream().collect(Collectors.groupingBy(TransferItemEntity::getTransferId, Collectors.mapping(TransferItemEntity::getAssetId, Collectors.toList())));

        List<Long> assetIdList = itemEntityList.stream().map(TransferItemEntity::getAssetId).collect(Collectors.toList());
        List<AssetVO> assetVOList = assetService.queryDetailByIdList(assetIdList);

        Set<Long> locationIdSet = list.stream().map(TransferEntity::getFromLocationId).collect(Collectors.toSet());
        locationIdSet.addAll(list.stream().map(TransferEntity::getToLocationId).collect(Collectors.toSet()));
        List<LocationEntity> locationList = locationDao.selectBatchIds(locationIdSet);
        Map<Long, String> locationNameMap = locationList.stream().collect(Collectors.toMap(LocationEntity::getLocationId, LocationEntity::getLocationName));

        Set<Long> enterpriseIdList = list.stream().map(TransferEntity::getEnterpriseId).collect(Collectors.toSet());
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdList);
        Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        List<TransferVO> returnList = Lists.newArrayList();
        for (TransferEntity e : list) {
            TransferVO transferVO = SmartBeanUtil.copy(e, TransferVO.class);
            transferVO.setEnterpriseName(enterpriseNameMap.getOrDefault(transferVO.getEnterpriseId(), StringConst.EMPTY));
            transferVO.setFromLocationName(locationNameMap.getOrDefault(transferVO.getFromLocationId(), StringConst.EMPTY));
            transferVO.setToLocationName(locationNameMap.getOrDefault(transferVO.getToLocationId(), StringConst.EMPTY));
            // 设置资产列表
            List<Long> transferAssetIdList = transferAssetIdMap.getOrDefault(transferVO.getTransferId(), CommonConst.EMPTY_LIST);
            List<AssetVO> assetList = assetVOList.stream().filter(asset -> transferAssetIdList.contains(asset.getAssetId())).collect(Collectors.toList());
            transferVO.setAssetList(assetList);
            returnList.add(transferVO);
        }
        return returnList;
    }

    /**
     * 添加资产转移
     *
     * @param dataTracerRequestForm
     * @param addForm
     */
    public ResponseDTO<String> addTransfer(TransferAddForm addForm, DataTracerRequestForm dataTracerRequestForm) {
        Long fromLocationId = addForm.getFromLocationId();
        Long toLocationId = addForm.getToLocationId();

        if (null == locationDao.selectById(fromLocationId)) {
            return ResponseDTO.userErrorParam("转出位置不存在");
        }
        if (null == locationDao.selectById(toLocationId)) {
            return ResponseDTO.userErrorParam("转入位置不存在");
        }
        if (fromLocationId.equals(toLocationId)) {
            return ResponseDTO.userErrorParam("转入与转出位置不能相同");
        }
        List<Long> assetIdList = addForm.getAssetIdList();
        List<AssetEntity> assetList = assetDao.selectByIdList(assetIdList, Boolean.FALSE);
        if (assetIdList.size() != assetList.size()) {
            return ResponseDTO.userErrorParam("资产数据异常，请刷新后重试～");
        }

        for (AssetEntity assetEntity : assetList) {
            if (!AssetStatusEnum.UNUSED.equalsValue(assetEntity.getStatus())) {
                String statusName = SmartBaseEnumUtil.getEnumDescByValue(assetEntity.getStatus(), AssetStatusEnum.class);
                return ResponseDTO.userErrorParam(String.format("%s处于%s状态，不能转移", assetEntity.getAssetName(), statusName));
            }
        }

        TransferEntity transferEntity = SmartBeanUtil.copy(addForm, TransferEntity.class);
        transferEntity.setTransferNo(serialNumberService.generate(SerialNumberIdEnum.ASSET_TRANSFER));
        transferEntity.setStatus(TransferStatusEnum.WAIT.getValue());
        transferEntity.setCreateUserId(dataTracerRequestForm.getOperatorId());
        transferEntity.setCreateUserName(dataTracerRequestForm.getOperatorName());
        transferManager.saveTransfer(transferEntity, assetList);
        TransferVO transferVO = getDetail(transferEntity.getTransferId());
        transferDataTracerService.saveLog(addForm, transferVO, dataTracerRequestForm);
        assetDataTracerService.applyTransferLog(transferEntity.getTransferNo(), addForm.getAssetIdList(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * @param transferId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> complete(Long transferId, DataTracerRequestForm dataTracerRequestForm) {
        TransferEntity transferEntity = transferManager.getBaseMapper().selectById(transferId);
        if (null == transferEntity) {
            return ResponseDTO.userErrorParam("转移单据不存在");
        }
        List<TransferItemEntity> itemList = transferItemDao.queryByTransferIdList(Arrays.asList(transferId));
        List<Long> assetIdList = itemList.stream().map(TransferItemEntity::getAssetId).collect(Collectors.toList());
        transferManager.completeTransfer(transferEntity, assetIdList);
        transferDataTracerService.completeLog(transferId, dataTracerRequestForm);
        assetDataTracerService.passTransferLog(transferEntity.getTransferNo(), assetIdList, transferEntity.getFromLocationId(), dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 驳回转移申请
     *
     * @param transferId
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> reject(Long transferId, DataTracerRequestForm dataTracerRequestForm) {
        TransferEntity transferEntity = transferManager.getBaseMapper().selectById(transferId);
        if (null == transferEntity) {
            return ResponseDTO.userErrorParam("单据不存在");
        }
        List<TransferItemEntity> itemList = transferItemDao.queryByTransferIdList(Arrays.asList(transferId));
        List<Long> assetIdList = itemList.stream().map(TransferItemEntity::getAssetId).collect(Collectors.toList());
        transferManager.rejectTransfer(transferEntity, assetIdList);
        transferDataTracerService.rejectLog(transferId, dataTracerRequestForm);
        assetDataTracerService.rejectTransferLog(transferEntity.getTransferNo(), assetIdList, AssetStatusEnum.UNUSED, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    /**
     * 获取详情
     *
     * @param transferId
     * @return
     */
    public TransferVO getDetail(Long transferId) {
        TransferEntity transferEntity = transferManager.getBaseMapper().selectById(transferId);
        if (null == transferEntity) {
            return null;
        }
        return buildList(Arrays.asList(transferEntity)).get(NumberConst.ZERO);
    }

}
