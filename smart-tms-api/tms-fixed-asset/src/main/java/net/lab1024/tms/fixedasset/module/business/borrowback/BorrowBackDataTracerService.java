package net.lab1024.tms.fixedasset.module.business.borrowback;

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
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowBackDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.borrowback.constants.BorrowBackTypeEnum;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackAddForm;
import net.lab1024.tms.fixedasset.module.business.borrowback.domain.BorrowBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 资产借用、归还
 *
 * @author lidoudou
 * @date 2023/3/28 下午2:48
 */
@Service
public class BorrowBackDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(BorrowBackAddForm addForm, BorrowBackVO borrowBackVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerBusinessTypeEnum dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.ASSET_BORROW;
        if (BorrowBackTypeEnum.BACK.equalsValue(borrowBackVO.getType())) {
            dataTracerBusinessTypeEnum = DataTracerBusinessTypeEnum.ASSET_BACK;
        }
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(borrowBackVO.getBorrowBackId());
        dataTracerForm.setBusinessType(dataTracerBusinessTypeEnum);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getBorrowContent(null), this.getBorrowContent(borrowBackVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(BorrowBackAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getBorrowContent(BorrowBackVO borrowBackVO) {
        if (null == borrowBackVO) {
            return "";
        }
        // 基本信息
        String baseContent = "";
        if (BorrowBackTypeEnum.BORROW.equalsValue(borrowBackVO.getType())) {
            baseContent = this.getBorrowBaseContent(borrowBackVO);
        } else {
            baseContent = this.getBackBaseContent(borrowBackVO);
        }
        String assetContent = this.getAssetContent(borrowBackVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param borrowBackVOVO
     * @return
     */
    private String getBackBaseContent(BorrowBackVO borrowBackVOVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【归还日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(borrowBackVOVO.getUseTime())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【归还公司】").append(DataTracerConstant.SPLIT).append(borrowBackVOVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【归还部门】").append(DataTracerConstant.SPLIT).append(borrowBackVOVO.getDepartmentName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【归还人】").append(DataTracerConstant.SPLIT).append(borrowBackVOVO.getUserName()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 基本信息
     *
     * @param borrowBackVOVO
     * @return
     */
    private String getBorrowBaseContent(BorrowBackVO borrowBackVOVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【借用日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(borrowBackVOVO.getUseTime())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【借用公司】").append(DataTracerConstant.SPLIT).append(borrowBackVOVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【借用部门】").append(DataTracerConstant.SPLIT).append(borrowBackVOVO.getDepartmentName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【借用人】").append(DataTracerConstant.SPLIT).append(borrowBackVOVO.getUserName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【预计归还日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(borrowBackVOVO.getBackTime())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【申请说明】").append(DataTracerConstant.SPLIT).append(borrowBackVOVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 关联资产
     *
     * @param borrowBackVO
     * @return
     */
    private String getAssetContent(BorrowBackVO borrowBackVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("资产:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        for (AssetVO assetVO : borrowBackVO.getAssetList()) {
            builder.append(assetVO.getAssetName()).append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 完成资产转移
     *
     * @param borrowBackId
     * @param operateTypeEnum
     * @param dataTracerRequestForm
     */
    @Async
    public void operateBorrowLog(Long borrowBackId, BorrowBackDataTracerOperateTypeEnum operateTypeEnum, DataTracerRequestForm dataTracerRequestForm) {

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(borrowBackId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_BORROW);
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
