package net.lab1024.tms.fixedasset.module.business.repair.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.repair.constant.AssetRepairStatusEnum;
import net.lab1024.tms.fixedasset.module.business.repair.constant.RepairDataTracerOperateTypeEnum;
import net.lab1024.tms.fixedasset.module.business.repair.domain.form.RepairAddForm;
import net.lab1024.tms.fixedasset.module.business.repair.domain.vo.RepairDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 维修的操作记录
 *
 * @author lidoudou
 * @date 2023/3/27 上午9:59
 */
@Service
public class RepairDataTracerService {
    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DictCacheService dictCacheService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(RepairAddForm addForm, RepairDetailVO detailVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(detailVO.getRepairId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_REPAIR);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDiffContent(null), this.getDiffContent(detailVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(RepairAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getDiffContent(RepairDetailVO detailVO) {
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
     * @param repairDetailVO
     * @return
     */
    private String getBaseContent(RepairDetailVO repairDetailVO) {

        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【业务时间】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMD(repairDetailVO.getBusinessDate())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维修厂家】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCode(repairDetailVO.getRepairCompany())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维修内容】").append(DataTracerConstant.SPLIT).append(repairDetailVO.getContent()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【申请人员】").append(DataTracerConstant.SPLIT).append(repairDetailVO.getApplyUserName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【维修花费】").append(DataTracerConstant.SPLIT).append(repairDetailVO.getRepairCost()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(repairDetailVO.getRemark()).append(DataTracerConstant.LINE)
        ;
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 包含资产
     *
     * @param detailVO
     * @return
     */
    private String getAssetContent(RepairDetailVO detailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("资产清单:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        detailVO.getAssetList().forEach(item -> {
            builder.append(item.getAssetName()).append(CommonConst.SEPARATOR);
        });
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 批量审核
     *
     * @param repairIdList
     * @param status
     * @param dataTracerRequestForm
     */
    @Async
    public void batchAuditLog(List<Long> repairIdList, AssetRepairStatusEnum status, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long scrapId : repairIdList) {
            String operateContent = status.getDesc();
            if (AssetRepairStatusEnum.REPAIRING.equalsValue(status.getValue())) {
                operateContent = "审核通过，状态更新为：" + status.getDesc();
            }
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(scrapId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_REPAIR);
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


    /**
     * 完成日志
     *
     * @param repairIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void finishLog(List<Long> repairIdList, DataTracerRequestForm dataTracerRequestForm) {

        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long repairId : repairIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(repairId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_REPAIR);
            dataTracerForm.setOperateType(RepairDataTracerOperateTypeEnum.COMPLETE);
            dataTracerForm.setOperateContent(RepairDataTracerOperateTypeEnum.COMPLETE.getDesc());
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
