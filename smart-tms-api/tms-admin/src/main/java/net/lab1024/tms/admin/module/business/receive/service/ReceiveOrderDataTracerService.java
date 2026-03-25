package net.lab1024.tms.admin.module.business.receive.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveCheckForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveOrderCancelForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveOrderInvoiceCancelForm;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.receive.constant.ReceiveOrderDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderInvoiceEntity;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderVerificationEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @date 2022/8/19 下午5:18
 */
@Service
public class ReceiveOrderDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private EnterpriseDao enterpriseDao;

    /**
     * 确认核算
     *
     * @param checkForm
     * @param dataTracerRequestForm
     */
    @Async
    public void checkLog(ReceiveCheckForm checkForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(checkForm.getReceiveOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
        dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.CHECK);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        StringBuilder builder = new StringBuilder();
        builder.append("确认核算:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【核算时间】").append(DataTracerConstant.SPLIT).append(checkForm.getCheckTime()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【核算凭证】").append(DataTracerConstant.SPLIT).append(checkForm.getCheckAttachment()).append(DataTracerConstant.LINE);
        dataTracerForm.setOperateContent(builder.toString().replaceAll("null", ""));

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 作废核算
     *
     * @param cancelForm
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelCheckLog(ReceiveOrderCancelForm cancelForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(cancelForm.getReceiveOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
        dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.CANCEL_CHECK);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent("作废原因为：" + cancelForm.getRemark());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 核销记录
     *
     * @param verificationEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void verificationLog(ReceiveOrderVerificationEntity verificationEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(verificationEntity.getReceiveOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
        dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.VERIFICATION);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent(getVerificationContent(verificationEntity));

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 增加批量核销记录
     *
     * @param recordList
     * @param dataTracerRequestForm
     */
    @Async
    public void batchVerificationLog(List<ReceiveOrderVerificationEntity> recordList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(recordList)) {
            return;
        }
        List<DataTracerForm> dataTracerFormList = recordList.stream().map(verificationEntity -> {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(verificationEntity.getReceiveOrderId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
            dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.VERIFICATION);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerForm.setOperateContent(getVerificationContent(verificationEntity));
            return dataTracerForm;
        }).collect(Collectors.toList());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerFormList, operatorId, operatorName, operatorType);
    }

    /**
     * 基本信息
     *
     * @param verificationEntity
     * @return
     */
    @Async
    private String getVerificationContent(ReceiveOrderVerificationEntity verificationEntity) {
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(verificationEntity.getEnterpriseId());
        StringBuilder builder = new StringBuilder();
        builder.append("【收款企业】").append(DataTracerConstant.SPLIT).append(enterpriseEntity.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append("【收款账户】").append(DataTracerConstant.SPLIT).append(verificationEntity.getBankName()).append(DataTracerConstant.LINE)
                .append("【收款账号】").append(DataTracerConstant.SPLIT).append(verificationEntity.getAccountNumber()).append(DataTracerConstant.LINE)
                .append("【流水单号】").append(DataTracerConstant.SPLIT).append(verificationEntity.getSequenceCode()).append(DataTracerConstant.LINE)
                .append("【核销金额】").append(DataTracerConstant.SPLIT).append(verificationEntity.getVerificationAmount()).append(DataTracerConstant.LINE)
                .append("【核销日期】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(verificationEntity.getVerificationTime(), TransportationTypeEnum.class)).append(DataTracerConstant.LINE)
                .append("【核销凭证】").append(DataTracerConstant.SPLIT).append(verificationEntity.getAttachment()).append(DataTracerConstant.LINE)

                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(verificationEntity.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 作废核算
     *
     * @param cancelForm
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelInvoiceLog(Long receiveOrderId, ReceiveOrderInvoiceCancelForm cancelForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(receiveOrderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
        dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.CANCEL_INVOICE);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent("作废原因为：" + cancelForm.getRemark());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }


    /**
     * 上传对账单
     *
     * @param receiveOrderId
     * @param billAttachment
     * @param dataTracerRequestForm
     */
    public void uploadBillLog(Long receiveOrderId, String billAttachment, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(receiveOrderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
        dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.UPLOAD_BILL);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent("上传对账单：" + billAttachment);

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 保存开票操作记录
     *
     * @param receiveOrderId
     * @param invoiceUserName
     * @param invoiceAmount
     * @param dataTracerRequestForm
     */
    @Async
    public void invoiceLog(Long receiveOrderId, String invoiceUserName, BigDecimal invoiceAmount, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(receiveOrderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
        dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.INVOICE);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent(invoiceUserName + "开票，开票金额为：" + invoiceAmount);

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }

    /**
     * 更新开票操作记录
     *
     * @param: receiveOrderId
     * @param: invoiceUserName
     * @param: invoiceAmount
     * @param: dataTracerRequestForm
     * @return: void
     **/
    @Async
    public void updateInvoiceLog(Long receiveOrderId, DataTracerRequestForm dataTracerRequestForm,ReceiveOrderInvoiceEntity beforeData,ReceiveOrderInvoiceEntity afterData) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(receiveOrderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
        dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.UPDATE_INVOICE);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent(dataTracerRequestForm.getOperatorName() + "修改发票" );
        dataTracerForm.setExtraData(new DataTracerExtraData(ReceiveOrderInvoiceEntity.class,beforeData,afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }
    /**
     * 保存开票操作记录
     *
     * @param receiveOrderInvoiceList
     * @param invoiceUserName
     * @param dataTracerRequestForm
     */
    @Async
    public void batchInvoiceLog(List<ReceiveOrderInvoiceEntity> receiveOrderInvoiceList, String invoiceUserName, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(receiveOrderInvoiceList)) {
            return;
        }

        List<DataTracerForm> dataTracerFormList = Lists.newArrayList();
        receiveOrderInvoiceList.forEach(receiveOrderInvoice -> {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(receiveOrderInvoice.getReceiveOrderId());
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.RECEIVE_ORDER);
            dataTracerForm.setOperateType(ReceiveOrderDataTracerOperateTypeEnum.INVOICE);
            dataTracerForm.setIp(dataTracerRequestForm.getIp());
            dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerForm.setOperateContent(invoiceUserName + "开票，开票金额为：" + receiveOrderInvoice.getInvoiceAmount());

            dataTracerFormList.add(dataTracerForm);
        });

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerFormList, operatorId, operatorName, operatorType);
    }
}
