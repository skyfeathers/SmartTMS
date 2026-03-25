package net.lab1024.tms.fixedasset.module.business.transfer;

import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.transfer.constants.TransferDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferAddForm;
import net.lab1024.tms.fixedasset.module.business.transfer.domain.TransferVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 资产转移的操作日志
 *
 * @author lidoudou
 * @date 2023/3/23 下午2:29
 */
@Service
public class TransferDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(TransferAddForm addForm, TransferVO transferVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(transferVO.getTransferId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_TRANSFER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getTransferContent(null), this.getTransferContent(transferVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(TransferAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }

    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(TransferVO beforeData, TransferVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getTransferId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_TRANSFER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getTransferContent(beforeData), this.getTransferContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(TransferVO.class, beforeData, afterData));

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    private String getTransferContent(TransferVO transferVO) {
        if (null == transferVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getAssetBaseContent(transferVO);
        String assetContent = this.getAssetContent(transferVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param transferVO
     * @return
     */
    private String getAssetBaseContent(TransferVO transferVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【转出位置】").append(DataTracerConstant.SPLIT).append(transferVO.getFromLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【转入位置】").append(DataTracerConstant.SPLIT).append(transferVO.getToLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(transferVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 关联资产
     *
     * @param transferVO
     * @return
     */
    private String getAssetContent(TransferVO transferVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("转移资产:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        for (AssetVO assetVO : transferVO.getAssetList()) {
            builder.append(assetVO.getAssetName()).append(StringConst.SEPARATOR);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 完成资产转移
     *
     * @param transferId
     * @param dataTracerRequestForm
     */
    @Async
    public void completeLog(Long transferId, DataTracerRequestForm dataTracerRequestForm) {

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(transferId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_TRANSFER);
        dataTracerForm.setOperateType(TransferDataTracerOperateTypeEnum.COMPLETE);
        dataTracerForm.setOperateContent(TransferDataTracerOperateTypeEnum.COMPLETE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 驳回资产转移申请
     *
     * @param transferId
     * @param dataTracerRequestForm
     */
    @Async
    public void rejectLog(Long transferId, DataTracerRequestForm dataTracerRequestForm) {

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(transferId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_TRANSFER);
        dataTracerForm.setOperateType(TransferDataTracerOperateTypeEnum.REJECT);
        dataTracerForm.setOperateContent(TransferDataTracerOperateTypeEnum.REJECT.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

}
