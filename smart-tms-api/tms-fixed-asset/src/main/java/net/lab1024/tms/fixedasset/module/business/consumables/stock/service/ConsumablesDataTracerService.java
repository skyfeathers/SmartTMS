package net.lab1024.tms.fixedasset.module.business.consumables.stock.service;

import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form.ConsumablesUpdateForm;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 易耗品清单
 *
 * @author lidoudou
 * @date 2023/4/13 下午5:02
 */
@Service
public class ConsumablesDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(ConsumablesAddForm addForm, ConsumablesVO consumablesVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(consumablesVO.getConsumablesId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONSUMABLES_STOCK);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO("", this.getContent(consumablesVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(ConsumablesAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 基本信息
     *
     * @param consumablesVO
     * @return
     */
    private String getContent(ConsumablesVO consumablesVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(consumablesVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【耗材名称】").append(DataTracerConstant.SPLIT).append(consumablesVO.getConsumablesName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属分类】").append(DataTracerConstant.SPLIT).append(consumablesVO.getCategoryName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【库存预警值】").append(DataTracerConstant.SPLIT).append(consumablesVO.getStockWarningValue()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(consumablesVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 新建日志
     *
     * @param updateForm
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(ConsumablesUpdateForm updateForm, ConsumablesVO beforeDetail, ConsumablesVO afterDetail, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(afterDetail.getConsumablesId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONSUMABLES_STOCK);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.UPDATE.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getContent(beforeDetail), this.getContent(afterDetail)));
        dataTracerForm.setExtraData(new DataTracerExtraData(ConsumablesAddForm.class, null, updateForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }
}
