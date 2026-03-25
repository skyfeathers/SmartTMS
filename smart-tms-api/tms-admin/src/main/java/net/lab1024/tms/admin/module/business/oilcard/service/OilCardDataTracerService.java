package net.lab1024.tms.admin.module.business.oilcard.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class OilCardDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;
    @Autowired
    private OilCardDao oilCardDao;

    /**
     * 新增日志
     *
     * @param oilCardEntity
     */
    @Async
    public void saveLog(OilCardEntity oilCardEntity, DataTracerRequestForm dataTracerRequestForm) {

        String content = dataTracerFieldService.beanObjectParse(DataTracerOperateTypeEnum.Common.SAVE.getDesc(), oilCardEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilCardEntity.getOilCardId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(content);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    @Async
    public void updateLog(OilCardEntity beforeEntity, OilCardEntity alterEntity, DataTracerRequestForm dataTracerRequestForm) {

        String content = dataTracerFieldService.beanParse(beforeEntity, alterEntity);

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeEntity.getOilCardId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(content);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    @Async
    public void batchDeleteLog(List<Long> oilCardIdList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(oilCardIdList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long oilCardId : oilCardIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(oilCardId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
            dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.DELETE);
            dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.DELETE.getDesc());
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


    @Async
    public void balanceChangeLog(Long oilCardId, String content, OilCardDataTracerOperateTypeEnum operateType, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilCardId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
        dataTracerForm.setOperateType(operateType);
        dataTracerForm.setOperateContent(content);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }

    @Async
    public void batchDisabledFlagLog(Long masterOilCardId, DataTracerRequestForm dataTracerRequestForm) {

        List<OilCardEntity> oilCardList = oilCardDao.selectByMasterOilCardId(masterOilCardId, Boolean.FALSE);
        List<OilCardEntity> validList = oilCardList.stream().filter(e -> !e.getDisabledFlag()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validList)) {
            return;
        }
        List<Long> oilCardIdList = validList.stream().map(OilCardEntity::getOilCardId).collect(Collectors.toList());
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long oilCardId : oilCardIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(oilCardId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
            dataTracerForm.setOperateType(OilCardDataTracerOperateTypeEnum.MASTER_DISABLED);
            dataTracerForm.setOperateContent(OilCardDataTracerOperateTypeEnum.MASTER_DISABLED.getDesc());
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

    @Async
    public void batchImportLog(List<OilCardEntity> oilCardEntityList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(oilCardEntityList)) {
            return;
        }
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (OilCardEntity oilCard : oilCardEntityList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(oilCard.getOilCardId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
            dataTracerForm.setOperateType(OilCardDataTracerOperateTypeEnum.BATCH_IMPORT);
            dataTracerForm.setOperateContent(OilCardDataTracerOperateTypeEnum.BATCH_IMPORT.getDesc());
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

    @Async
    public void setPreRechargeAmountLog(Long oilCardId, BigDecimal preRechargeAmount, BigDecimal originAmount, BigDecimal newAmount, DataTracerRequestForm dataTracerRequestForm) {
        String operateContent = "增加计划充值金额：" + preRechargeAmount + "，原金额："+ originAmount + ", 充值后金额" + newAmount;
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilCardId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
        dataTracerForm.setOperateType(OilCardDataTracerOperateTypeEnum.UPDATE_PRE_RECHARGE_AMOUNT);
        dataTracerForm.setOperateContent(operateContent);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }

    @Async
    public void updatePreRechargeAmountLog(Long oilCardId, BigDecimal preRechargeAmount, DataTracerRequestForm dataTracerRequestForm) {
        String operateContent = "修改计划充值金额：" + preRechargeAmount;
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilCardId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
        dataTracerForm.setOperateType(OilCardDataTracerOperateTypeEnum.UPDATE_PRE_RECHARGE_AMOUNT);
        dataTracerForm.setOperateContent(operateContent);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }

    @Async
    public void rechargeByPreRechargeAmountLog(Long oilCardId,BigDecimal beforePreRechargeAmount, BigDecimal afterPreRechargeAmount, DataTracerRequestForm dataTracerRequestForm) {
        String operateContent = "原计划充值金额为：" + beforePreRechargeAmount + "，充值后计划充值金额为：" + afterPreRechargeAmount;
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(oilCardId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.OIL_CARD);
        dataTracerForm.setOperateType(OilCardDataTracerOperateTypeEnum.RECHARGE_BY_PRE_RECHARGE_AMOUNT);
        dataTracerForm.setOperateContent(operateContent);

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }
}