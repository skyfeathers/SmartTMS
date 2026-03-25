package net.lab1024.tms.fixedasset.module.business.requisitionrevert.service;

import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionRevertDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants.RequisitionRevertTypeEnum;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertAddForm;
import net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain.RequisitionRevertVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 资产领用、退还
 *
 * @author lidoudou
 * @date 2023/3/28 下午4:22
 */
@Service
public class RequisitionRevertDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(RequisitionRevertAddForm addForm, RequisitionRevertVO requisitionRevertVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.ASSET_REQUISITION;
        if (RequisitionRevertTypeEnum.REVERT.equalsValue(requisitionRevertVO.getType())) {
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.ASSET_REVERT;
        }
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(requisitionRevertVO.getRequisitionRevertId());
        dataTracerForm.setBusinessType(dataTracerBusinessTypeEnum);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getOperateContent(null), this.getOperateContent(requisitionRevertVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(RequisitionRevertAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getOperateContent(RequisitionRevertVO requisitionRevertVO) {
        if (null == requisitionRevertVO) {
            return "";
        }
        // 基本信息
        String baseContent = "";
        if (RequisitionRevertTypeEnum.REQUISITION.equalsValue(requisitionRevertVO.getType())) {
            baseContent = this.getRequisitionBaseContent(requisitionRevertVO);
        } else {
            baseContent = this.getRevertBaseContent(requisitionRevertVO);
        }
        String assetContent = this.getAssetContent(requisitionRevertVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param requisitionRevertVO
     * @return
     */
    private String getRequisitionBaseContent(RequisitionRevertVO requisitionRevertVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(requisitionRevertVO.getUseTime())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用公司】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用部门】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getDepartmentName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用人】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getUserName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用后存放位置】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【申请说明】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 基本信息
     *
     * @param requisitionRevertVO
     * @return
     */
    private String getRevertBaseContent(RequisitionRevertVO requisitionRevertVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【退还日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(requisitionRevertVO.getUseTime())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【退还公司】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【退还部门】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getDepartmentName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【退还人】").append(DataTracerConstant.SPLIT).append(requisitionRevertVO.getUserName()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 关联资产
     *
     * @param requisitionRevertVO
     * @return
     */
    private String getAssetContent(RequisitionRevertVO requisitionRevertVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("资产:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        for (AssetVO assetVO : requisitionRevertVO.getAssetList()) {
            builder.append(assetVO.getAssetName()).append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 完成资产领用
     *
     * @param requisitionId
     * @param operateTypeEnum
     * @param dataTracerRequestForm
     */
    @Async
    public void operateRequisitionLog(Long requisitionId, RequisitionRevertDataTracerOperateTypeEnum operateTypeEnum, DataTracerRequestForm dataTracerRequestForm) {

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(requisitionId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_REQUISITION);
        dataTracerForm.setOperateType(operateTypeEnum);
        dataTracerForm.setOperateContent(operateTypeEnum.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }


}
