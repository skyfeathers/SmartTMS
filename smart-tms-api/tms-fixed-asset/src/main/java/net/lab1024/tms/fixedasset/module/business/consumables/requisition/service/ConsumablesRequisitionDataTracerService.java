package net.lab1024.tms.fixedasset.module.business.consumables.requisition.service;

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
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form.ConsumablesRequisitionAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.vo.ConsumablesRequisitionItemVO;
import net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.vo.ConsumablesRequisitionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 易耗品领用
 *
 * @author lidoudou
 * @date 2023/4/14 上午10:37
 */
@Service
public class ConsumablesRequisitionDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(ConsumablesRequisitionAddForm addForm, ConsumablesRequisitionVO consumablesRequisitionVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(consumablesRequisitionVO.getRequisitionId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONSUMABLES_STOCK_REQUISITION);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(StringConst.EMPTY, this.getOperateContent(consumablesRequisitionVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(ConsumablesRequisitionAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getOperateContent(ConsumablesRequisitionVO consumablesRequisitionVO) {
        if (null == consumablesRequisitionVO) {
            return "";
        }
        // 基本信息
        String baseContent = "";
        baseContent = this.getRequisitionBaseContent(consumablesRequisitionVO);
        String assetContent = this.getAssetContent(consumablesRequisitionVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param consumablesRequisitionVO
     * @return
     */
    private String getRequisitionBaseContent(ConsumablesRequisitionVO consumablesRequisitionVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(consumablesRequisitionVO.getUseTime())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用公司】").append(DataTracerConstant.SPLIT).append(consumablesRequisitionVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属位置】").append(DataTracerConstant.SPLIT).append(consumablesRequisitionVO.getLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用部门】").append(DataTracerConstant.SPLIT).append(consumablesRequisitionVO.getDepartmentName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【领用人】").append(DataTracerConstant.SPLIT).append(consumablesRequisitionVO.getUserName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【申请说明】").append(DataTracerConstant.SPLIT).append(consumablesRequisitionVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 申请领用易耗品
     *
     * @param consumablesRequisitionVO
     * @return
     */
    private String getAssetContent(ConsumablesRequisitionVO consumablesRequisitionVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("领取耗材:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        for (ConsumablesRequisitionItemVO stockVO : consumablesRequisitionVO.getItemList()) {
            builder.append("耗材名称：").append(stockVO.getConsumablesName()).append("，领取数量：").append(stockVO.getCount()).append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

}
