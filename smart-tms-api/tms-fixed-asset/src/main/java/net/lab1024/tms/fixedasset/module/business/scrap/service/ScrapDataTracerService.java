package net.lab1024.tms.fixedasset.module.business.scrap.service;

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
import net.lab1024.tms.fixedasset.common.constants.CommonConst;
import net.lab1024.tms.fixedasset.module.business.scrap.constant.AssetScrapStatusEnum;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.form.ScrapAddForm;
import net.lab1024.tms.fixedasset.module.business.scrap.domain.vo.ScrapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产报废的操作记录
 *
 * @author lidoudou
 * @date 2023/3/27 上午9:59
 */
@Service
public class ScrapDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 新建日志
     *
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(ScrapAddForm addForm, ScrapVO scrapVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(scrapVO.getScrapId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_CLEAR);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getDiffContent(null), this.getDiffContent(scrapVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(ScrapAddForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getDiffContent(ScrapVO scrapVO) {
        if (null == scrapVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getBaseContent(scrapVO);
        String assetContent = this.getAssetContent(scrapVO);

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(assetContent);
        return builder.toString();
    }

    /**
     * 基本信息
     *
     * @param scrapVO
     * @return
     */
    private String getBaseContent(ScrapVO scrapVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【报废时间】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMDHMS(scrapVO.getScrapTime())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【报废说明】").append(DataTracerConstant.SPLIT).append(scrapVO.getScrapExplain()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 包含资产
     *
     * @param scrapVO
     * @return
     */
    private String getAssetContent(ScrapVO scrapVO) {
        StringBuilder builder = new StringBuilder();
        builder.append("资产清单:").append(DataTracerConstant.LINE).append(DataTracerConstant.TAB);
        scrapVO.getAssetList().forEach(item -> {
            builder.append(item.getAssetName()).append(CommonConst.SEPARATOR);
        });
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 批量审核
     *
     * @param scrapIdList
     * @param status
     * @param dataTracerRequestForm
     */
    @Async
    public void batchAuditLog(List<Long> scrapIdList, AssetScrapStatusEnum status, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long scrapId : scrapIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(scrapId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ASSET_CLEAR);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.AUDIT);
            dataTracerForm.setOperateContent(status.getDesc());
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
