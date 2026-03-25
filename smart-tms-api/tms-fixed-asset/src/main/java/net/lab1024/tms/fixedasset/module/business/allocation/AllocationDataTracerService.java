package net.lab1024.tms.fixedasset.module.business.allocation;

import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.fixedasset.module.business.allocation.constants.AllocationDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationAddForm;
import net.lab1024.tms.fixedasset.module.business.allocation.domain.AllocationVO;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 资产调拨的操作记录
 *
 * @author lidoudou
 * @date 2023/3/27 下午5:31
 */
@Service
public class AllocationDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(AllocationAddForm addForm, AllocationVO allocationVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(allocationVO.getAllocationId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_ALLOCATION);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getTransferContent(null), this.getTransferContent(allocationVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(AllocationAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getTransferContent(AllocationVO allocationVO) {
        if (null == allocationVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getAssetBaseContent(allocationVO);
        String assetContent = this.getAssetContent(allocationVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param allocationVO
     * @return
     */
    private String getAssetBaseContent(AllocationVO allocationVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(allocationVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【调出位置】").append(DataTracerConstant.SPLIT).append(allocationVO.getFromLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【调入位置】").append(DataTracerConstant.SPLIT).append(allocationVO.getToLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(allocationVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 关联资产
     *
     * @param allocationVO
     * @return
     */
    private String getAssetContent(AllocationVO allocationVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("调拨资产:").append(DataTracerConstant.LINE);
        for (AssetVO assetVO : allocationVO.getAssetList()) {
            builder.append(assetVO.getAssetName()).append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 完成资产调拨
     *
     * @param allocationId
     * @param dataTracerRequestForm
     */
    @Async
    public void completeLog(Long allocationId, DataTracerRequestForm dataTracerRequestForm) {

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(allocationId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_ALLOCATION);
        dataTracerForm.setOperateType(AllocationDataTracerOperateTypeEnum.COMPLETE);
        dataTracerForm.setOperateContent(AllocationDataTracerOperateTypeEnum.COMPLETE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }


    /**
     * 驳回资产调拨申请
     *
     * @param allocationId
     * @param dataTracerRequestForm
     */
    @Async
    public void rejectLog(Long allocationId, DataTracerRequestForm dataTracerRequestForm) {

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(allocationId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_ALLOCATION);
        dataTracerForm.setOperateType(AllocationDataTracerOperateTypeEnum.REJECT);
        dataTracerForm.setOperateContent(AllocationDataTracerOperateTypeEnum.REJECT.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

}
