package net.lab1024.tms.admin.module.business.etc;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.etc.dao.EtcDao;
import net.lab1024.tms.admin.module.business.etc.domain.vo.EtcVO;
import net.lab1024.tms.common.module.business.etc.domain.EtcEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lidoudou
 * @date 2022/9/8 下午3:50
 */
@Service
public class EtcDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;
    @Autowired
    private EtcDao etcDao;

    /**
     * 新增日志
     *
     * @param etcEntity
     */
    @Async
    public void saveLog(EtcEntity etcEntity, DataTracerRequestForm dataTracerRequestForm) {

        String content = dataTracerFieldService.beanObjectParse(DataTracerOperateTypeEnum.Common.SAVE.getDesc(), etcEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(etcEntity.getEtcId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ETC);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(content);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    @Async
    public void updateLog(EtcVO beforeData, EtcVO afterData, DataTracerRequestForm dataTracerRequestForm) {

        String content = dataTracerFieldService.beanParse(beforeData, afterData);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getEtcId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ETC);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getContent(beforeData), this.getContent(afterData)));

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }
    /**
     * 基本信息
     *
     * @param etcVO
     * @return
     */
    private String getContent(EtcVO etcVO) {
        StringBuilder builder = new StringBuilder();

        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【ETC卡号】").append(DataTracerConstant.SPLIT).append(etcVO.getEtcNo()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属公司】").append(DataTracerConstant.SPLIT).append(etcVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【使用司机】").append(DataTracerConstant.SPLIT).append(etcVO.getUserName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【绑定车辆】").append(DataTracerConstant.SPLIT).append(etcVO.getUseVehicleNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【状态】").append(DataTracerConstant.SPLIT).append(etcVO.getDisabledFlag() ? "禁用" : "启用").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(etcVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    @Async
    public void batchDeleteLog(List<Long> etcIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(etcIdList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long etcId : etcIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(etcId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ETC);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }

    @Async
    public void batchImportLog(List<EtcEntity> etcEntityList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(etcEntityList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (EtcEntity etcEntity : etcEntityList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(etcEntity.getEtcId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ETC);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

            dataTracerList.add(dataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operatorType);
    }
}