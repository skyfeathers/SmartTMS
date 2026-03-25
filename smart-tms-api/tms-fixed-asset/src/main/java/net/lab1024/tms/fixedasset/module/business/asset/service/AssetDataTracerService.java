package net.lab1024.tms.fixedasset.module.business.asset.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.dao.CommonEnterpriseDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.tms.fixedasset.module.business.allocation.constants.AllocationDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.asset.constants.AssetStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.entity.AssetEntity;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetAddForm;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowBackDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.location.LocationDao;
import net.lab1024.tms.fixedasset.module.business.location.domain.LocationEntity;
import net.lab1024.tms.fixedasset.module.business.repair.domain.entity.RepairEntity;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairAssetVO;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionRevertDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.entity.ScrapEntity;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapAssetVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产的操作日志
 *
 * @author lidoudou
 * @date 2023/3/23 下午2:29
 */
@Service
public class AssetDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private CommonEnterpriseDao commonEnterpriseDao;
    @Autowired
    private LocationDao locationDao;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(AssetAddForm addForm, AssetVO assetVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(assetVO.getAssetId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getAssetContent(null), this.getAssetContent(assetVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(AssetAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(AssetVO beforeData, AssetVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(beforeData.getAssetId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getAssetContent(beforeData), this.getAssetContent(afterData)));
        dataTracerForm.setExtraData(new DataTracerExtraData(AssetVO.class, beforeData, afterData));

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    private String getAssetContent(AssetVO assetVO) {
        if (null == assetVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getAssetBaseContent(assetVO);
        String supplierContent = this.getAssetSupplierContent(assetVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(supplierContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param assetVO
     * @return
     */
    private String getAssetBaseContent(AssetVO assetVO) {
        String purchaseTime = "";
        if (null != assetVO.getPurchaseTime()) {
            purchaseTime = SmartLocalDateUtil.formatYMD(assetVO.getPurchaseTime());
        }
        String expireTime = "";
        if (null != assetVO.getExpireTime()) {
            expireTime = SmartLocalDateUtil.formatYMD(assetVO.getExpireTime());
        }

        String sourceName = "";
        DictValueVO dictValueVO = dictCacheService.selectValueByValueCode(assetVO.getSourceId());
        if (dictValueVO != null) {
            sourceName = dictValueVO.getValueName();
        }

        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属分类】").append(DataTracerConstant.SPLIT).append(assetVO.getCategoryName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【资产编号】").append(DataTracerConstant.SPLIT).append(assetVO.getAssetNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【资产名称】").append(DataTracerConstant.SPLIT).append(assetVO.getAssetName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【资产来源】").append(DataTracerConstant.SPLIT).append(sourceName).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【品牌】").append(DataTracerConstant.SPLIT).append(assetVO.getBrand()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【规格型号】").append(DataTracerConstant.SPLIT).append(assetVO.getModel()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【设备序列号】").append(DataTracerConstant.SPLIT).append(assetVO.getSerialId()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【计量单位】").append(DataTracerConstant.SPLIT).append(assetVO.getUnit()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【存放位置】").append(DataTracerConstant.SPLIT).append(assetVO.getLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(assetVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用部门】").append(DataTracerConstant.SPLIT).append(assetVO.getDepartmentName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用人】").append(DataTracerConstant.SPLIT).append(assetVO.getUserName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用期限（月）】").append(DataTracerConstant.SPLIT).append(assetVO.getMonthCount()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【购入日期】").append(DataTracerConstant.SPLIT).append(purchaseTime).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【到期日期】").append(DataTracerConstant.SPLIT).append(expireTime).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(assetVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 供应商
     *
     * @param assetVO
     * @return
     */
    private String getAssetSupplierContent(AssetVO assetVO) {
        String expireTime = "";
        if (null != assetVO.getPurchaseTime()) {
            expireTime = SmartLocalDateUtil.formatYMD(assetVO.getExpireTime());
        }

        StringBuilder builder = new StringBuilder();
        builder.append("维保信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【供应商】").append(DataTracerConstant.SPLIT).append(assetVO.getSupplierName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【供应商联系人】").append(DataTracerConstant.SPLIT).append(assetVO.getSupplierContactName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【供应商联系方式】").append(DataTracerConstant.SPLIT).append(assetVO.getSupplierContactPhone()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【负责人】").append(DataTracerConstant.SPLIT).append(assetVO.getManagerName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维保到期】").append(DataTracerConstant.SPLIT).append(expireTime).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维保说明】").append(DataTracerConstant.SPLIT).append(assetVO.getSupplierRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 批量删除日志
     *
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchDeleteLog(List<Long> assetIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(assetIdList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 删除日志
     *
     * @param assetId
     * @param dataTracerRequestForm
     */
    @Async
    public void deleteLog(Long assetId, DataTracerRequestForm dataTracerRequestForm) {
        if (null == assetId) {
            return;
        }
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(assetId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 批量更新状态
     *
     * @param assetList
     * @param scrapList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchCancelLog(List<ScrapAssetVO> assetList, List<ScrapEntity> scrapList, DataTracerRequestForm dataTracerRequestForm) {
        Map<Long, String> scrapNoMap = scrapList.stream().collect(Collectors.toMap(ScrapEntity::getScrapId, ScrapEntity::getScrapCode));


        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        String operateContentFormat = "报废单【%s】审核通过，资产状态更新为：【" + AssetStatusEnum.CANCEL.getDesc() + "】";
        for (ScrapAssetVO scrapAssetVO : assetList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(scrapAssetVO.getAssetId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.ASSET_CANCEL);
            dataTracerForm.setOperateContent(String.format(operateContentFormat, scrapNoMap.getOrDefault(scrapAssetVO.getScrapId(), StringConst.EMPTY)));
            dataTracerList.add(dataTracerForm);
        }

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 新建日志
     *
     * @param dataTracerRequestForm
     */
    @Async
    public void batchSaveByPurchaseLog(List<AssetVO> assetVOList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (AssetVO asset : assetVOList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(asset.getAssetId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
            dataTracerForm.setOperateContent("采购保存");
            dataTracerForm.setDiff(new DataTracerDiffBO(this.getAssetContent(null), this.getAssetContent(asset)));
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 发起领用申请
     *
     * @param requisitionRevertNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void applyRequisitionLog(String requisitionRevertNo, List<Long> assetIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_REQUISITION);
            dataTracerForm.setOperateContent("发起领用，单据号为：" + requisitionRevertNo + "，资产状态更新为【" + AssetStatusEnum.TO_BE_REQUISITION.getDesc() + "】");
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 通过、驳回领用申请
     *
     * @param requisitionRevertNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void passRejectRequisitionLog(String requisitionRevertNo, List<Long> assetIdList, RequisitionRevertDataTracerOperateTypeEnum requisitionRevertDataTracerOperateTypeEnum, AssetStatusEnum assetStatusEnum, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();

        String operateContentFormat = "%s ，单据号为：%s，资产状态更新为【%s】";
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_REQUISITION);
            dataTracerForm.setOperateContent(String.format(operateContentFormat, requisitionRevertDataTracerOperateTypeEnum.getDesc(), requisitionRevertNo, assetStatusEnum.getDesc()));
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }


    /**
     * 发起资产退还申请
     *
     * @param requisitionRevertNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void applyRevertLog(String requisitionRevertNo, List<Long> assetIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_REVERT);
            dataTracerForm.setOperateContent("资产退还，单据号为：" + requisitionRevertNo + "，资产状态更新为【" + AssetStatusEnum.UNUSED.getDesc() + "]");
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }


    /**
     * 发起借用申请
     *
     * @param borrowBackNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void applyBorrowLog(String borrowBackNo, List<Long> assetIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_BORROW);
            dataTracerForm.setOperateContent("发起借用，单据号为：" + borrowBackNo + "，资产状态更新为【" + AssetStatusEnum.TO_BE_BORROW.getDesc() + "】");
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 通过、驳回借用申请
     *
     * @param borrowBackNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void passRejectBorrowBackLog(String borrowBackNo, List<Long> assetIdList, BorrowBackDataTracerOperateTypeEnum borrowBackDataTracerOperateTypeEnum, AssetStatusEnum assetStatusEnum, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();

        String operateContentFormat = "%s ，单据号为：%s，资产状态更新为【%s】";
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_BORROW);
            dataTracerForm.setOperateContent(String.format(operateContentFormat, borrowBackDataTracerOperateTypeEnum.getDesc(), borrowBackNo, assetStatusEnum.getDesc()));
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }


    /**
     * 发起资产退还申请
     *
     * @param borrowBackNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void applyBackLog(String borrowBackNo, List<Long> assetIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_REVERT);
            dataTracerForm.setOperateContent("资产退还，单据号为：" + borrowBackNo + "，资产状态更新为【" + AssetStatusEnum.UNUSED.getDesc() + "】");
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }


    /**
     * 发起调拨申请
     *
     * @param allocationNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void applyAllocationLog(String allocationNo, List<Long> assetIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_ALLOCATION);
            dataTracerForm.setOperateContent("申请调拨，单据号为：" + allocationNo + "，资产状态更新为【" + AssetStatusEnum.ALLOCATION.getDesc() + "】");
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 通过、驳回调拨申请
     *
     * @param allocationNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void passRejectAllocationLog(String allocationNo, List<Long> assetIdList, AllocationDataTracerOperateTypeEnum allocationDataTracerOperateTypeEnum, AssetStatusEnum assetStatusEnum, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();

        String operateContentFormat = "%s ，单据号为：%s，资产状态更新为【%s】";
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_ALLOCATION);
            dataTracerForm.setOperateContent(String.format(operateContentFormat, allocationDataTracerOperateTypeEnum.getDesc(), allocationNo, assetStatusEnum.getDesc()));
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 发起转移申请
     *
     * @param transferNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void applyTransferLog(String transferNo, List<Long> assetIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_TRANSFER);
            dataTracerForm.setOperateContent("申请调拨，单据号为：" + transferNo + "，资产状态更新为【" + AssetStatusEnum.ALLOCATION.getDesc() + "】");
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }


    /**
     * 通过转移申请
     *
     * @param allocationNo
     * @param assetIdList
     * @param locationId
     * @param dataTracerRequestForm
     */
    @Async
    public void passTransferLog(String allocationNo, List<Long> assetIdList, Long locationId, DataTracerRequestForm dataTracerRequestForm) {

        String locationName = "";
        LocationEntity locationEntity = locationDao.selectById(locationId);
        locationName = null != locationEntity ? locationEntity.getLocationName() : "";

        List<DataTracerForm> dataTracerList = Lists.newArrayList();

        String operateContentFormat = "转移成功，单据号为：%s，存放位置：%s";
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_TRANSFER);
            dataTracerForm.setOperateContent(String.format(operateContentFormat, allocationNo, locationName));
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 驳回转移申请
     *
     * @param allocationNo
     * @param assetIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void rejectTransferLog(String allocationNo, List<Long> assetIdList, AssetStatusEnum assetStatusEnum, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();

        String operateContentFormat = "驳回转移申请，单据号为：%s，资产状态更新为【%s】";
        for (Long assetId : assetIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.APPLY_TRANSFER);
            dataTracerForm.setOperateContent(String.format(operateContentFormat, allocationNo, assetStatusEnum.getDesc()));
            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }


    /**
     * 批量更新状态
     *
     * @param assetList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchRepairLog(List<AssetEntity> assetList, String repairNo, DataTracerRequestForm dataTracerRequestForm) {

        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        String operateContent = "申请维修，单据号为：【" + repairNo + "】,资产状态更新为：【" + AssetStatusEnum.REPAIR.getDesc() + "】";
        for (AssetEntity assetEntity : assetList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(assetEntity.getAssetId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.ASSET_REPAIR);
            dataTracerForm.setOperateContent(operateContent);
            dataTracerList.add(dataTracerForm);
        }

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    /**
     * 维修完成批量更新状态
     *
     * @param assetList
     * @param dataTracerRequestForm
     */
    @Async
    public void finishRepairLog(List<RepairAssetVO> assetList, List<RepairEntity> repairList, String operateName, DataTracerRequestForm dataTracerRequestForm) {
        Map<Long, String> scrapNoMap = repairList.stream().collect(Collectors.toMap(RepairEntity::getRepairId, RepairEntity::getRepairCode));

        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        String operateContentFormat = "维修单【%s】%s，资产状态更新为：【%s】";
        for (RepairAssetVO scrapAssetVO : assetList) {
            DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
            dataTracerForm.setBusinessId(scrapAssetVO.getAssetId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET);
            dataTracerForm.setOperateType(AssetDataTracerOperateTypeEnum.ASSET_REPAIR);
            String assetStatus = SmartBaseEnumUtil.getEnumDescByValue(scrapAssetVO.getStatus(), AssetStatusEnum.class);
            dataTracerForm.setOperateContent(String.format(operateContentFormat, scrapNoMap.getOrDefault(scrapAssetVO.getRepairId(), StringConst.EMPTY), operateName, assetStatus));
            dataTracerList.add(dataTracerForm);
        }

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }
}
