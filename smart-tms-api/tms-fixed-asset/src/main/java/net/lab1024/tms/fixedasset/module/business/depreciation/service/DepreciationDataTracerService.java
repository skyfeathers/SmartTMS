package net.lab1024.tms.fixedasset.module.business.depreciation.service;

import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.fixedasset.module.business.depreciation.DepreciationDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.form.DepreciationAddForm;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo.DepreciationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 折旧操作记录
 *
 * @author lidoudou
 * @date 2023/4/11 上午10:36
 */
@Service
public class DepreciationDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param depreciationVO
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(DepreciationAddForm addForm, DepreciationVO depreciationVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(depreciationVO.getDepreciationId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_DEPRECIATION);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(StringConst.EMPTY, this.getDepreciationContent(depreciationVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(DepreciationAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }

    private String getDepreciationContent(DepreciationVO depreciationVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【计提日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYM(depreciationVO.getDepreciationDate().atStartOfDay())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(depreciationVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(depreciationVO.getRemark()).append(DataTracerConstant.LINE);
        builder.append("包含资产").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        depreciationVO.getAssetList().forEach(asset -> {
            builder.append(asset.getAssetName()).append(StringConst.SEPARATOR);
        });
        return builder.toString().replaceAll("null", "");
    }

    @Async
    public void cancelLog(Long depreciationId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(depreciationId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_DEPRECIATION);
        dataTracerForm.setOperateType(DepreciationDataTracerOperateTypeEnum.CANCEL);
        dataTracerForm.setOperateContent(DepreciationDataTracerOperateTypeEnum.CANCEL.getDesc());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }
}
