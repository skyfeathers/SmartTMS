package net.lab1024.tms.fixedasset.module.business.consumables.check.service;

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
import net.lab1024.tms.fixedasset.module.business.consumables.check.constants.ConsumablesCheckDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.consumables.check.constants.ConsumablesCheckStatusEnum;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckCompleteForm;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form.ConsumablesCheckForm;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesCheckEmployeeVO;
import net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo.ConsumablesConsumablesCheckDetailVO;
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
public class ConsumablesCheckDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(ConsumablesCheckAddForm addForm, ConsumablesConsumablesCheckDetailVO assetVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(assetVO.getCheckId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONSUMABLES_STOCK_CHECK);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDiffContent(null), this.getDiffContent(assetVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(ConsumablesCheckAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getDiffContent(ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO) {
        if (null == consumablesCheckDetailVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getBaseContent(consumablesCheckDetailVO);
        String assetContent = this.getAssetContent(consumablesCheckDetailVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param consumablesCheckDetailVO
     * @return
     */
    private String getBaseContent(ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO) {
        StringBuilder builder = new StringBuilder();
        String checkEmployee = "";
        if (CollectionUtils.isNotEmpty(consumablesCheckDetailVO.getEmployeeList())) {
            checkEmployee = consumablesCheckDetailVO.getEmployeeList().stream().map(ConsumablesCheckEmployeeVO::getActualName).collect(Collectors.joining(CommonConst.SEPARATOR));
        }

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【盘点名称】").append(DataTracerConstant.SPLIT).append(consumablesCheckDetailVO.getCheckName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【盘点位置】").append(DataTracerConstant.SPLIT).append(consumablesCheckDetailVO.getLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【盘点人员】").append(DataTracerConstant.SPLIT).append(checkEmployee).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(consumablesCheckDetailVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 包含易耗品
     *
     * @param consumablesCheckDetailVO
     * @return
     */
    private String getAssetContent(ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("易耗品清单:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        consumablesCheckDetailVO.getItemList().forEach(item -> {
            builder.append(item.getConsumablesName()).append(CommonConst.SEPARATOR);
        });
        return builder.toString().replaceAll("null", "");
    }

    @Async
    public void updateCheckLog(ConsumablesConsumablesCheckDetailVO beforeData, ConsumablesConsumablesCheckDetailVO afterData, ConsumablesCheckForm consumablesCheckForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(beforeData.getCheckId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONSUMABLES_STOCK_CHECK);
        dataTracerForm.setOperateType(ConsumablesCheckDataTracerOperateTypeEnum.CHECK);
        dataTracerForm.setOperateContent(ConsumablesCheckDataTracerOperateTypeEnum.CHECK.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getCheckContent(beforeData), this.getCheckContent(afterData)));
        dataTracerForm.setExtraData(new DataTracerExtraData(ConsumablesCheckForm.class, null, consumablesCheckForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 包含易耗品
     *
     * @param consumablesCheckDetailVO
     * @return
     */
    private String getCheckContent(ConsumablesConsumablesCheckDetailVO consumablesCheckDetailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("易耗品清单:").append(DataTracerConstant.LINE);
        consumablesCheckDetailVO.getItemList().forEach(item -> {
            builder.append(DataTracerConstant.TAB).append("【易耗品名称】").append(DataTracerConstant.SPLIT).append(item.getConsumablesName())
                    .append(DataTracerConstant.TAB).append("【盘点状态】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(item.getStatus(), ConsumablesCheckStatusEnum.class))
                    .append(DataTracerConstant.LINE);
        });
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 完成日志
     *
     * @param consumablesCheckCompleteForm
     * @param dataTracerRequestForm
     */
    @Async
    public void completeLog(ConsumablesCheckCompleteForm consumablesCheckCompleteForm, DataTracerRequestForm dataTracerRequestForm) {
        String operateContent = "完成盘点时间为：" + SmartLocalDateUtil.formatYMDHMS(consumablesCheckCompleteForm.getCompleteTime());

        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(consumablesCheckCompleteForm.getCheckId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONSUMABLES_STOCK_CHECK);
        dataTracerForm.setOperateType(ConsumablesCheckDataTracerOperateTypeEnum.COMPLETE);
        dataTracerForm.setOperateContent(operateContent);

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

}
