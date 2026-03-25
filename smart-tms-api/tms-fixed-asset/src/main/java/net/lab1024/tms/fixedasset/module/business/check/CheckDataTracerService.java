package net.lab1024.tms.fixedasset.module.business.check;

import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.check.constants.CheckDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.check.constants.CheckStatusEnum;
import net.lab1024.tms.fixedasset.module.business.check.constants.CheckTypeEnum;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckAddForm;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckCompleteForm;
import net.lab1024.tms.fixedasset.module.business.check.domain.form.CheckForm;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckDetailVO;
import net.lab1024.tms.fixedasset.module.business.check.domain.vo.CheckEmployeeVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 盘点的操作记录
 *
 * @author lidoudou
 * @date 2023/3/27 上午9:59
 */
@Service
public class CheckDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(CheckAddForm addForm, CheckDetailVO assetVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(assetVO.getCheckId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_CHECK);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDiffContent(null), this.getDiffContent(assetVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(CheckAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getDiffContent(CheckDetailVO checkDetailVO) {
        if (null == checkDetailVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getBaseContent(checkDetailVO);
        String assetContent = this.getAssetContent(checkDetailVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param checkDetailVO
     * @return
     */
    private String getBaseContent(CheckDetailVO checkDetailVO) {
        StringBuilder builder = new StringBuilder();
        String checkEmployee = "";
        if (CollectionUtils.isNotEmpty(checkDetailVO.getEmployeeList())) {
            checkEmployee = checkDetailVO.getEmployeeList().stream().map(CheckEmployeeVO::getActualName).collect(Collectors.joining(CommonConst.SEPARATOR));
        }

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【盘点名称】").append(DataTracerConstant.SPLIT).append(checkDetailVO.getCheckName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【盘点类型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(checkDetailVO.getCheckType(), CheckTypeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【盘点位置】").append(DataTracerConstant.SPLIT).append(checkDetailVO.getLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【盘点人员】").append(DataTracerConstant.SPLIT).append(checkEmployee).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(checkDetailVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 包含资产
     *
     * @param checkDetailVO
     * @return
     */
    private String getAssetContent(CheckDetailVO checkDetailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("资产清单:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        checkDetailVO.getAssetList().forEach(item -> {
            builder.append(item.getAssetName()).append(CommonConst.SEPARATOR);
        });
        return builder.toString().replaceAll("null", "");
    }

    @Async
    public void updateCheckLog(CheckDetailVO beforeData, CheckDetailVO afterData, CheckForm checkForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(beforeData.getCheckId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_CHECK);
        dataTracerForm.setOperateType(CheckDataTracerOperateTypeEnum.CHECK);
        dataTracerForm.setOperateContent(CheckDataTracerOperateTypeEnum.CHECK.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getCheckContent(beforeData), this.getCheckContent(afterData)));
        dataTracerForm.setExtraData(new DataTracerExtraData(CheckForm.class, null, checkForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 包含资产
     *
     * @param checkDetailVO
     * @return
     */
    private String getCheckContent(CheckDetailVO checkDetailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("资产清单:").append(DataTracerConstant.LINE);
        checkDetailVO.getAssetList().forEach(item -> {
            builder.append(DataTracerConstant.TAB).append("【资产名称】").append(DataTracerConstant.SPLIT).append(item.getAssetName())
                    .append(DataTracerConstant.TAB).append("【盘点状态】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(item.getStatus(), CheckStatusEnum.class))
                    .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(item.getRemark())
                    .append(DataTracerConstant.LINE);
        });
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 完成日志
     *
     * @param checkCompleteForm
     * @param dataTracerRequestForm
     */
    @Async
    public void completeLog(CheckCompleteForm checkCompleteForm, DataTracerRequestForm dataTracerRequestForm) {
        String operateContent = "完成盘点时间为：" + SmartLocalDateUtil.formatYMDHMS(checkCompleteForm.getCompleteTime());

        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(checkCompleteForm.getCheckId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_CHECK);
        dataTracerForm.setOperateType(CheckDataTracerOperateTypeEnum.COMPLETE);
        dataTracerForm.setOperateContent(operateContent);

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

}
