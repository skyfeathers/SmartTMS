package net.lab1024.tms.driver.module.business.waybill.service;

import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherForm;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class WaybillDataTracerService {
    
    @Resource
    private DataTracerService dataTracerService;
    
    /**
     * 上传凭证
     *
     * @param waybillVoucherForm
     * @param dataTracerRequestForm
     */
    @Async
    public void addVoucherLog(WaybillVoucherForm waybillVoucherForm, DataTracerRequestForm dataTracerRequestForm) {
        String voucherContent = this.generateVoucherContent(waybillVoucherForm);
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillVoucherForm.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.ADD_VOUCHER);
        dataTracerForm.setOperateContent(voucherContent);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operatorType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operatorType);
    }
    
    /**
     * 生成新建运单内容
     *
     * @param waybillVoucherForm
     * @return
     */
    private String generateVoucherContent(WaybillVoucherForm waybillVoucherForm) {
        WaybillVoucherTypeEnum waybillVoucherTypeEnum = SmartBaseEnumUtil.getEnumByValue(waybillVoucherForm.getType(), WaybillVoucherTypeEnum.class);
        StringBuilder content = new StringBuilder();
        content.append("【凭证类型】").append(DataTracerConstant.SPLIT).append(waybillVoucherTypeEnum.getDesc()).append(DataTracerConstant.LINE)
                .append("【凭证附件】").append(DataTracerConstant.SPLIT).append(waybillVoucherForm.getAttachment()).append(DataTracerConstant.LINE)
                .append("【位置】").append(DataTracerConstant.SPLIT).append(waybillVoucherForm.getLocation()).append(DataTracerConstant.LINE)
                .append("【坐标】").append(DataTracerConstant.SPLIT).append(waybillVoucherForm.getLatitude() + "/" + waybillVoucherForm.getLongitude()).append(DataTracerConstant.LINE);
        return content.toString().replaceAll("null", "");
    }
}