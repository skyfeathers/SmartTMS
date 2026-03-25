package net.lab1024.tms.admin.module.business.waybill.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.fleet.dao.FleetDao;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.FleetDetailVO;
import net.lab1024.tms.admin.module.business.oa.invoice.InvoiceDao;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveOrderCreateCostForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveOrderForm;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillCostDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillReceiveCostDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillSplitTransportDao;
import net.lab1024.tms.admin.module.business.waybill.domain.form.*;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillSplitTransportVO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.oa.invoice.InvoiceEntity;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillVoucherTypeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillReceiveCostEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillVoucherEntity;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherForm;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/7/21 10:15 下午
 */
@Service
public class WaybillDataTracerService {

    @Autowired
    private DriverDao driverDao;
    @Autowired
    private FleetDao fleetDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private WaybillCostDao waybillCostDao;
    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private WaybillReceiveCostDao waybillReceiveCostDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private InvoiceDao invoiceDao;
    /**
     * 新增日志
     *
     * @param waybillEntity
     */
    @Async
    public void saveLog(WaybillEntity waybillEntity,DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillEntity.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(this.getBasicContent(waybillEntity));

        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 运单基本信息
     *
     * @param waybillEntity
     * @return
     */
    private String getBasicContent(WaybillEntity waybillEntity) {

        //结算类型
        WaybillSettleTypeEnum settleTypeEnum = SmartBaseEnumUtil.getEnumByValue(waybillEntity.getSettleType(), WaybillSettleTypeEnum.class);
        //司机
        DriverEntity driverEntity = driverDao.selectById(waybillEntity.getDriverId());
        //车队
        FleetEntity fleetEntity = null;
        if (waybillEntity.getFleetId() != null) {
            fleetEntity = fleetDao.selectById(waybillEntity.getFleetId());
        }
        //车辆
        VehicleEntity vehicleEntity = vehicleDao.selectById(waybillEntity.getVehicleId());

        StringBuilder builder = new StringBuilder();
        builder.append("【结算方式】").append(DataTracerConstant.SPLIT).append(settleTypeEnum.getDesc()).append(DataTracerConstant.LINE);
        builder.append("【运输方式】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(waybillEntity.getTransportMode(), WaybillTransportModeEnum.class)).append(DataTracerConstant.LINE);
        if (fleetEntity != null) {
            builder.append("【车队名称】").append(DataTracerConstant.SPLIT).append(fleetEntity.getFleetName()).append(DataTracerConstant.LINE)
                    .append("【车队电话】").append(DataTracerConstant.SPLIT).append(fleetEntity.getCaptainPhone()).append(DataTracerConstant.LINE);
        }
        if (driverEntity != null) {
            builder.append("【司机名称】").append(DataTracerConstant.SPLIT).append(driverEntity.getDriverName()).append(DataTracerConstant.LINE)
                    .append("【司机电话】").append(DataTracerConstant.SPLIT).append(driverEntity.getTelephone()).append(DataTracerConstant.LINE);
        }
        if (vehicleEntity != null) {
            builder.append("【车牌号】").append(DataTracerConstant.SPLIT).append(vehicleEntity.getVehicleNumber()).append(DataTracerConstant.LINE);
        }
        builder.append("【装/卸货时间】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMDHMS(waybillEntity.getLoadTime())).append(DataTracerConstant.LINE);
        builder.append("【业务时间】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYM(waybillEntity.getBusinessDate())).append(DataTracerConstant.LINE);
        builder.append("【应收金额】").append(DataTracerConstant.SPLIT).append(waybillEntity.getReceiveAmount()).append(DataTracerConstant.LINE);
        builder.append("【应付金额】").append(DataTracerConstant.SPLIT).append(waybillEntity.getPayableAmount()).append(DataTracerConstant.LINE);
        builder.append("【备注】").append(DataTracerConstant.SPLIT).append(waybillEntity.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }


    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(WaybillEntity beforeData, WaybillEntity afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(this.getBasicContent(afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getBasicContent(beforeData), this.getBasicContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(WaybillEntity.class, beforeData, afterData));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 更新箱号和铅封号Log
     *
     * @param updateForm
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLeadSealAndContainerNumberLog(WaybillLeadSealAndContainerNumberUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(updateForm.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.UPDATE_LEADSEAL_NUMBER);
        dataTracerForm.setOperateContent("修改箱号为：" + updateForm.getContainerNumber() + ", 修改铅封号为：" + updateForm.getLeadSealNumber());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }


    /**
     * 作废
     *
     * @param waybillId
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelLog(Long waybillId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.CANCEL);
        dataTracerForm.setOperateContent(WaybillDataTracerOperateTypeEnum.CANCEL.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 上传凭证
     *
     * @param waybillVoucherEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void addVoucherLog(WaybillVoucherEntity waybillVoucherEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillVoucherEntity.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.ADD_VOUCHER);

        dataTracerForm.setOperateContent(this.getVoucherLog(waybillVoucherEntity));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    @Async
    public void updateVoucherLog(WaybillVoucherEntity beforeData, WaybillVoucherEntity afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.UPDATE_VOUCHER);

        dataTracerForm.setOperateContent(WaybillDataTracerOperateTypeEnum.UPDATE_VOUCHER.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getVoucherLog(beforeData), this.getVoucherLog(afterData)));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }

    public void deleteVoucher(WaybillVoucherEntity waybillVoucherEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillVoucherEntity.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.DELETE_VOUCHER);

        dataTracerForm.setOperateContent(this.getVoucherLog(waybillVoucherEntity));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, dataTracerRequestForm.getOperatorType());
    }


    private String getVoucherLog(WaybillVoucherEntity voucherEntity){
        WaybillVoucherTypeEnum waybillVoucherTypeEnum = SmartBaseEnumUtil.getEnumByValue(voucherEntity.getType(), WaybillVoucherTypeEnum.class);
        StringBuilder builder = new StringBuilder();
        builder.append("【凭证类型】").append(DataTracerConstant.SPLIT).append(waybillVoucherTypeEnum.getDesc()).append(DataTracerConstant.LINE)
                .append("【凭证附件】").append(DataTracerConstant.SPLIT).append(voucherEntity.getAttachment()).append(DataTracerConstant.LINE)
                .append("【人车合影】").append(DataTracerConstant.SPLIT).append(voucherEntity.getSceneAttachment()).append(DataTracerConstant.LINE)
                .append("【榜单附件】").append(DataTracerConstant.SPLIT).append(voucherEntity.getPoundListAttachment()).append(DataTracerConstant.LINE)
                .append("【时间】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYMDHMS(voucherEntity.getCreateTime())).append(DataTracerConstant.LINE)
                .append("【位置】").append(DataTracerConstant.SPLIT).append(voucherEntity.getLocation()).append(DataTracerConstant.LINE)
                .append("【坐标】").append(DataTracerConstant.SPLIT).append(voucherEntity.getLatitude() + "/" + voucherEntity.getLongitude()).append(DataTracerConstant.LINE);
        return builder.toString();
    }

    /**
     * 费用维护
     *
     * @param waybillCostForm
     * @param dataTracerRequestForm
     */
    @Async
    public void costLog(WaybillCostForm waybillCostForm, WaybillEntity waybillEntity, DataTracerRequestForm dataTracerRequestForm) {
        List<WaybillCostItemForm> costItemFormList = waybillCostForm.getCostItemList();
        //查询费用
        List<Long> waybillCostIdList = costItemFormList.stream().map(WaybillCostItemForm::getWaybillCostId).collect(Collectors.toList());
        List<WaybillCostEntity> waybillCostEntityList = waybillCostDao.selectBatchIds(waybillCostIdList);
        Map<Long, WaybillCostEntity> waybillCostEntityMap = waybillCostEntityList.stream().collect(Collectors.toMap(WaybillCostEntity::getWaybillCostId, Function.identity()));

        StringBuilder builder = new StringBuilder();
        for (WaybillCostItemForm waybillCostItemForm : costItemFormList) {
            WaybillCostEntity waybillCostEntity = waybillCostEntityMap.get(waybillCostItemForm.getWaybillCostId());
            if (waybillCostEntity == null) {
                continue;
            }
            builder.append("【").append(waybillCostEntity.getCostItemName()).append("】").append(DataTracerConstant.SPLIT).append(waybillCostItemForm.getCostAmount()).append(DataTracerConstant.LINE);
        }

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillCostForm.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.UPDATE_COST);
        dataTracerForm.setOperateContent(builder.toString());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }


    /**
     * 运输完成
     *
     * @param completeForm
     * @param dataTracerRequestForm
     */
    @Async
    public void batchCompleteLog(WaybillCompleteForm completeForm, DataTracerRequestForm dataTracerRequestForm) {
        List<Long> waybillIdList = completeForm.getWaybillIdList();
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return;
        }
        String content = "运输完成时间为：" + SmartLocalDateUtil.formatYMDHMS(completeForm.getCompleteTime());
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long waybillId : waybillIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(waybillId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
            dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.COMPLETE);
            dataTracerForm.setOperateContent(content);
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
     * 记录提交应收日志
     *
     * @param waybillIdList
     * @param dataTracerRequestForm
     * @param receiveOrderId
     * @param receiveOrderCreateForm
     */
    public void submitReceiveLog(List<Long> waybillIdList, ReceiveOrderForm addForm, DataTracerRequestForm dataTracerRequestForm, Long receiveOrderId, ReceiveOrderForm receiveOrderCreateForm) {
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return;
        }
        // 增加订单的操作记录
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        String waybillContent = this.getSubmitReceiveLogContent(addForm);
        for (Long waybillId : waybillIdList) {
            DataTracerForm orderDataTracerForm = new DataTracerForm();
            orderDataTracerForm.setBusinessId(waybillId);
            orderDataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
            orderDataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.SUBMIT_RECEIVE_ORDER);
            orderDataTracerForm.setOperateContent(waybillContent);
            orderDataTracerForm.setIp(dataTracerRequestForm.getIp());
            orderDataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
            orderDataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
            dataTracerList.add(orderDataTracerForm);
        }
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveBatchOperateRecord(dataTracerList, operatorId, operatorName, operateType);
    }


    private String getSubmitReceiveLogContent(ReceiveOrderForm addForm){

        StringBuilder builder = new StringBuilder();
        builder.append("【业务时间】").append(DataTracerConstant.SPLIT).append(SmartLocalDateUtil.formatYM(addForm.getBusinessDate())).append(DataTracerConstant.LINE)
                .append("【税点】").append(DataTracerConstant.SPLIT).append(addForm.getTaxPoint()).append(DataTracerConstant.LINE)
                .append("【是否需要开票】").append(DataTracerConstant.SPLIT).append(addForm.getMakeInvoiceFlag()).append(DataTracerConstant.LINE)
                .append("【附件】").append(DataTracerConstant.SPLIT).append(addForm.getAttachment()).append(DataTracerConstant.LINE);
        return builder.toString();
    }

    /**
     * 上传回单
     *
     * @param updateForm
     * @param dataTracerRequestForm
     */
    @Async
    public void uploadReceiptAttachmentLog(WaybillReceiptUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm){
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(updateForm.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.UPLOAD_RECEIPT_ATTACHMENT);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent(updateForm.getReceiptAttachment());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 上传派车单
     *
     * @param updateForm
     * @param dataTracerRequestForm
     */
    @Async
    public void uploadTrunkOrderAttachmentLog(WaybillTruckOrderUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(updateForm.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.UPLOAD_TRUNK_ORDER_ATTACHMENT);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setOperateContent(updateForm.getTruckOrderAttachment());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 修改网络货运运单订单类型Log
     *
     * @param waybillId
     * @param dataTracerRequestForm
     */
    public void updateOrderTypeLog(Long waybillId, BigDecimal nftCost, DataTracerRequestForm dataTracerRequestForm) {
        StringBuilder sb = new StringBuilder();
        sb.append("运单的类型由【").append(OrderTypeEnum.NETWORK_FREIGHT.getDesc()).append("】，变更为【")
                .append(OrderTypeEnum.ORDINARY.getDesc()).append("。");
        sb.append("网络货运费由【").append(nftCost).append("】，变更为0。");

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(sb.toString());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    public void splitTransportDispatchLog(WaybillSplitTransportDispatchForm dispatchForm, DataTracerRequestForm dataTracerRequestForm) {
        WaybillSplitTransportVO waybillSplitTransportVO = SmartBeanUtil.copy(dispatchForm, WaybillSplitTransportVO.class);
        DriverEntity driverEntity = driverDao.selectById(dispatchForm.getDriverId());
        StringBuilder sb = new StringBuilder();
        sb.append("新增分段信息：司机【").append(driverEntity.getDriverName()).append("】，地址【")
                .append(this.getAddress(waybillSplitTransportVO)).append("】。");

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillSplitTransportVO.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.SPILT_TRANSPORT);
        dataTracerForm.setOperateContent(sb.toString());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    public void splitTransportDeleteLog(WaybillSplitTransportVO waybillSplitTransportVO, DataTracerRequestForm dataTracerRequestForm) {
        StringBuilder sb = new StringBuilder();
        sb.append("删除分段信息：司机【").append(waybillSplitTransportVO.getDriverName()).append("】，地址【")
                .append(this.getAddress(waybillSplitTransportVO)).append("】。");

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillSplitTransportVO.getWaybillId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.SPILT_TRANSPORT);
        dataTracerForm.setOperateContent(sb.toString());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getAddress(WaybillSplitTransportVO waybillSplitTransportVO) {
        if (waybillSplitTransportVO == null) {
            return "";
        }
        String mailArea = "";
        if (StringUtils.isNotBlank(waybillSplitTransportVO.getProvinceName())) {
            mailArea = mailArea + waybillSplitTransportVO.getProvinceName();
        }
        if (StringUtils.isNotBlank(waybillSplitTransportVO.getCityName())) {
            mailArea = mailArea + waybillSplitTransportVO.getCityName();
        }
        if (StringUtils.isNotBlank(waybillSplitTransportVO.getDistrictName())) {
            mailArea = mailArea + waybillSplitTransportVO.getDistrictName();
        }
        return mailArea;
    }


    private String getAddress(WaybillPathForm pathDTO) {
        if (pathDTO == null) {
            return "";
        }
        String mailArea = "";
        if (StringUtils.isNotBlank(pathDTO.getProvinceName())) {
            mailArea = mailArea + pathDTO.getProvinceName();
        }
        if (StringUtils.isNotBlank(pathDTO.getCityName())) {
            mailArea = mailArea + pathDTO.getCityName();
        }
        if (StringUtils.isNotBlank(pathDTO.getDistrictName())) {
            mailArea = mailArea + pathDTO.getDistrictName();
        }
        if (StringUtils.isNotBlank(pathDTO.getAddress())) {
            mailArea = mailArea + pathDTO.getAddress();
        }
        return mailArea;
    }

    @Async
    public void updatePathLog(WaybillPathUpdateForm waybillPathUpdateForm, DataTracerRequestForm dataTracerRequestForm) {
        Long waybillId = waybillPathUpdateForm.getWaybillId();

        String content = this.getWaybillPath(waybillPathUpdateForm);
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(waybillId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
        dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.UPDATE_PATH);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getWaybillPath(WaybillPathUpdateForm waybillPathUpdateForm) {
        // 操作记录
        StringBuilder builder = new StringBuilder();
        builder.append("路线信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB)
                .append("【运输距离】").
                append(DataTracerConstant.SPLIT).
                append(waybillPathUpdateForm.getDistance()).
                append(DataTracerConstant.BLANK);

        if (CollectionUtils.isEmpty(waybillPathUpdateForm.getPathList())) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (WaybillPathForm item : waybillPathUpdateForm.getPathList()) {
            builder.append(DataTracerConstant.TAB)
                    .append(SmartBaseEnumUtil.getEnumDescByValue(item.getType(), PathTypeEnum.class))
                    .append(DataTracerConstant.TAB)
                    .append("【地址】").append(DataTracerConstant.SPLIT).append(getAddress(item)).append(DataTracerConstant.BLANK)
                    .append("【联系人姓名】").append(DataTracerConstant.SPLIT).append(item.getContactName()).append(DataTracerConstant.BLANK)
                    .append("【联系人电话】").append(DataTracerConstant.SPLIT).append(item.getContactPhone()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }
}