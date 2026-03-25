package net.lab1024.tms.fixedasset.module.business.consumables.purchase.service;

import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form.ConsumablesPurchaseAddForm;
import net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo.ConsumablesPurchaseDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 易耗品采购的操作记录
 *
 * @author lidoudou
 * @date 2023/4/13 下午4:34
 */
@Service
public class ConsumablesPurchaseDataTracerService {

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
    public void saveLog(ConsumablesPurchaseAddForm addForm, ConsumablesPurchaseDetailVO purchaseDetailVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm(dataTracerRequestForm);
        dataTracerForm.setBusinessId(purchaseDetailVO.getPurchaseId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.CONSUMABLES_STOCK_PURCHASE);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDiffContent(null), this.getDiffContent(purchaseDetailVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(ConsumablesPurchaseAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getDiffContent(ConsumablesPurchaseDetailVO purchaseDetailVO) {
        if (null == purchaseDetailVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getBaseContent(purchaseDetailVO);
        String assetContent = this.getAssetContent(purchaseDetailVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param purchaseDetailVO
     * @return
     */
    private String getBaseContent(ConsumablesPurchaseDetailVO purchaseDetailVO) {
        StringBuilder builder = new StringBuilder();


        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(purchaseDetailVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属位置】").append(DataTracerConstant.SPLIT).append(purchaseDetailVO.getLocationName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【资产来源】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCode(purchaseDetailVO.getSourceId())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(purchaseDetailVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 包含资产
     *
     * @param purchaseDetailVO
     * @return
     */
    private String getAssetContent(ConsumablesPurchaseDetailVO purchaseDetailVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("采购列表:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        purchaseDetailVO.getItemList().forEach(item -> {
            builder.append("耗材名称：").append(item.getConsumablesName()).append("，采购价格：").append(item.getPrice()).append("，采购数量：").append(item.getCount()).append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        });
        return builder.toString().replaceAll("null", "");
    }

}
