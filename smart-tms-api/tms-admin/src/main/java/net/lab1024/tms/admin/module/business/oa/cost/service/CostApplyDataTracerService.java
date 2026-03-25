package net.lab1024.tms.admin.module.business.oa.cost.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oa.cost.constant.CostApplyStatusEnum;
import net.lab1024.tms.admin.module.business.oa.cost.domain.form.CostApplyAddForm;
import net.lab1024.tms.admin.module.business.oa.cost.domain.vo.CostApplyDetailVO;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 费用申请的操作记录
 *
 * @author lidoudou
 * @date 2023/3/29 下午6:12
 */
@Service
public class CostApplyDataTracerService {
    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(CostApplyAddForm addForm, CostApplyDetailVO detailVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(detailVO.getApplyId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OA_COST_APPLY);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDiffContent(null), this.getDiffContent(detailVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(CostApplyAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getDiffContent(CostApplyDetailVO detailVO) {
        if (null == detailVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getBaseContent(detailVO);
        String assetContent = this.getAssetContent(detailVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param costApplyDetailVO
     * @return
     */
    private String getBaseContent(CostApplyDetailVO costApplyDetailVO) {

        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【申请日期】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(costApplyDetailVO.getApplyDate())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(costApplyDetailVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属部门】").append(DataTracerConstant.SPLIT).append(costApplyDetailVO.getDepartmentName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【申请人】").append(DataTracerConstant.SPLIT).append(costApplyDetailVO.getApplyUserName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【申请说明】").append(DataTracerConstant.SPLIT).append(costApplyDetailVO.getRemark()).append(DataTracerConstant.LINE)
        ;
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 明细
     *
     * @param detailVO
     * @return
     */
    private String getAssetContent(CostApplyDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("明细:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        detailVO.getItemList().forEach(item -> {
            builder.append("报销项目：").append(item.getApplyItemName()).append("，摘要：").append(item.getRemark()).append("，报销金额：").append(item.getApplyAmount()).append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        });
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 批量审核
     *
     * @param applyIdList
     * @param status
     * @param dataTracerRequestForm
     */
    @Async
    public void batchAuditLog(List<Long> applyIdList, CostApplyStatusEnum status, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long scrapId : applyIdList) {
            String operateContent = "审核，状态更新为：" + status.getDesc();
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(scrapId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OA_COST_APPLY);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.AUDIT);
            dataTracerForm.setOperateContent(operateContent);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(dataTracerForm);
        }

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operateType);
    }


}
