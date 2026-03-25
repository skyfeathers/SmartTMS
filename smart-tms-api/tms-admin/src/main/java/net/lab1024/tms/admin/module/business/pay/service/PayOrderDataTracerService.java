package net.lab1024.tms.admin.module.business.pay.service;

import net.lab1024.tms.admin.module.business.pay.domain.form.OilCardRechargeForm;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderPaymentEntity;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderVerificationEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class PayOrderDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;

    /**
     * 新增日志
     *
     * @param payOrderEntity
     */
    @Async
    public void saveLog(PayOrderEntity payOrderEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(payOrderEntity.getPayOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.PAY_ORDER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 付款日志
     *
     * @param payOrderPaymentEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void payLog(PayOrderPaymentEntity payOrderPaymentEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(payOrderPaymentEntity.getPayOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.PAY_ORDER);
        dataTracerForm.setOperateType(PayOrderDataTracerOperateTypeEnum.PAY);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanObjectParse(PayOrderDataTracerOperateTypeEnum.PAY.getDesc(), payOrderPaymentEntity));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 油卡日志
     *
     * @param rechargeForm
     * @param dataTracerRequestForm
     */
    @Async
    public void oilCardPayLog(OilCardRechargeForm rechargeForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(rechargeForm.getPayOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.PAY_ORDER);
        dataTracerForm.setOperateType(PayOrderDataTracerOperateTypeEnum.PAY);
        dataTracerForm.setOperateContent("油卡充值");
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 付款日志
     *
     * @param payOrderPaymentEntityList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchPayLog(List<PayOrderPaymentEntity> payOrderPaymentEntityList, DataTracerRequestForm dataTracerRequestForm) {
        for (PayOrderPaymentEntity payOrderPaymentEntity : payOrderPaymentEntityList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(payOrderPaymentEntity.getPayOrderId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.PAY_ORDER);
            dataTracerForm.setOperateType(PayOrderDataTracerOperateTypeEnum.PAY);
            dataTracerForm.setOperateContent(dataTracerFieldService.beanObjectParse(PayOrderDataTracerOperateTypeEnum.PAY.getDesc(), payOrderPaymentEntity));
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            Long operatorId = dataTracerRequestForm.getOperatorId();
            String operatorName = dataTracerRequestForm.getOperatorName();
            Integer operatorType = dataTracerRequestForm.getOperatorType();
            dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
        }
    }

    /**
     * 核销日志
     *
     * @param payOrderVerificationEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void verificationLog(PayOrderVerificationEntity payOrderVerificationEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(payOrderVerificationEntity.getPayOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.PAY_ORDER);
        dataTracerForm.setOperateType(PayOrderDataTracerOperateTypeEnum.VERIFICATION);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanObjectParse(PayOrderDataTracerOperateTypeEnum.VERIFICATION.getDesc(), payOrderVerificationEntity));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 核销日志
     *
     * @param verificationEntityList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchVerificationLog(List<PayOrderVerificationEntity> verificationEntityList, DataTracerRequestForm dataTracerRequestForm) {
        for (PayOrderVerificationEntity verificationEntity : verificationEntityList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(verificationEntity.getPayOrderId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.PAY_ORDER);
            dataTracerForm.setOperateType(PayOrderDataTracerOperateTypeEnum.VERIFICATION);
            dataTracerForm.setOperateContent(dataTracerFieldService.beanObjectParse(PayOrderDataTracerOperateTypeEnum.VERIFICATION.getDesc(), verificationEntity));
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            Long operatorId = dataTracerRequestForm.getOperatorId();
            String operatorName = dataTracerRequestForm.getOperatorName();
            Integer operatorType = dataTracerRequestForm.getOperatorType();
            dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
        }
    }

    /**
     * 应付作废日志
     *
     * @param payOrderId
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelLog(Long payOrderId, String remark, DataTracerRequestForm dataTracerRequestForm) {
        String content = PayOrderDataTracerOperateTypeEnum.CANCEL.getDesc();
        if (StringUtils.isNotBlank(remark)) {
            content = content + ":" + remark;
        }
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(payOrderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.PAY_ORDER);
        dataTracerForm.setOperateType(PayOrderDataTracerOperateTypeEnum.CANCEL);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

}