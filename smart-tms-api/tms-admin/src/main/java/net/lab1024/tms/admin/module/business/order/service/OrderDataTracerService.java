package net.lab1024.tms.admin.module.business.order.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderGoodsDTO;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderCreateForm;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderGoodsVO;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderVO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerDiffBO;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerExtraData;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerFieldService;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/9/7 17:55
 */
@Service
public class OrderDataTracerService {

    @Autowired
    private DataTracerService dataTracerService;
    @Autowired
    private DataTracerFieldService dataTracerFieldService;
    @Autowired
    private DictCacheService dictCacheService;

    /**
     * 取消订单日志
     *
     * @param orderId
     * @param dataTracerRequestForm
     */
    @Async
    public void cancelLog(Long orderId, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(orderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ORDER);
        dataTracerForm.setOperateType(OrderDataTracerOperateTypeEnum.CANCEL);
        dataTracerForm.setOperateContent(OrderDataTracerOperateTypeEnum.CANCEL.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 新建日志
     *
     * @param orderId
     * @param addForm
     * @param dataTracerRequestForm
     */
    @Async
    public void saveLog(Long orderId, OrderCreateForm addForm, OrderVO orderVO, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(orderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ORDER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.SAVE);
        dataTracerForm.setOperateContent(DataTracerOperateTypeEnum.Common.SAVE.getDesc());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getOrderContent(null), this.getOrderContent(orderVO)));
        dataTracerForm.setExtraData(new DataTracerExtraData(OrderCreateForm.class, null, addForm));
        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 更新日志
     *
     * @param beforeData
     * @param afterData
     * @param dataTracerRequestForm
     */
    @Async
    public void updateLog(OrderVO beforeData, OrderVO afterData, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(beforeData.getOrderId());
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ORDER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent(dataTracerFieldService.beanParse(beforeData, afterData));
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getOrderContent(beforeData), this.getOrderContent(afterData)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());
        dataTracerForm.setExtraData(new DataTracerExtraData(OrderVO.class, beforeData, afterData));

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getOrderContent(OrderVO orderVO) {
        if (null == orderVO) {
            return "";
        }
        // 基本信息
        String baseContent = this.getOrderBaseContent(orderVO);
        // 货物信息
        String itemContent = this.getOrderGoodsContent(orderVO.getGoodsList());
        // 收货地址
        String locationContent = this.getOrderLocation(orderVO.getPathList());

        StringBuilder builder = new StringBuilder();
        builder.append(baseContent)
                .append(locationContent)
                .append(itemContent);
        return builder.toString();
    }

    /**
     * 更新订单货主Log
     *
     * @param orderId
     * @param oldShipperEntity
     * @param newShipperEntity
     * @param dataTracerRequestForm
     */
    @Async
    public void updateShipperLog(Long orderId, ShipperEntity oldShipperEntity, ShipperEntity newShipperEntity, DataTracerRequestForm dataTracerRequestForm) {
        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(orderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ORDER);
        dataTracerForm.setOperateType(DataTracerOperateTypeEnum.Common.UPDATE);
        dataTracerForm.setOperateContent("订单货主由：" + oldShipperEntity.getConsignor() + " 变更为：" + newShipperEntity.getConsignor());
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    /**
     * 基本信息
     *
     * @param orderVO
     * @return
     */
    private String getOrderBaseContent(OrderVO orderVO) {
        String latestPackingTime = "";
        if (null != orderVO.getLatestPackingTime()) {
            latestPackingTime = SmartLocalDateUtil.formatYMDHMS(orderVO.getLatestPackingTime());
        }
        String loadTime = "";
        if (null != orderVO.getLoadTime()) {
            loadTime = SmartLocalDateUtil.formatYMDHMS(orderVO.getLoadTime());
        }

        StringBuilder builder = new StringBuilder();
        builder.append("基本信息:").append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【货主名称】").append(DataTracerConstant.SPLIT).append(orderVO.getConsignor()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【订单所属公司】").append(DataTracerConstant.SPLIT).append(orderVO.getEnterpriseName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【本单调度员】").append(DataTracerConstant.SPLIT).append(orderVO.getScheduleName()).append(DataTracerConstant.LINE)

                .append(DataTracerConstant.TAB).append("【运输类型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(orderVO.getBusinessTypeCode(), TransportationTypeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【所属区域】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCodeSplit(orderVO.getAreaId())).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【业务类型】").append(DataTracerConstant.SPLIT).append(orderVO.getContainerBusinessTypeName()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【柜型】").append(DataTracerConstant.SPLIT).append(orderVO.getCabinetName()).append(DataTracerConstant.LINE)

                .append(DataTracerConstant.TAB).append("【订单类型】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(orderVO.getOrderType(), OrderTypeEnum.class)).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【运输距离】").append(DataTracerConstant.SPLIT).append(orderVO.getDistance()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【税点】").append(DataTracerConstant.SPLIT).append(orderVO.getTaxPoint()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【装/卸货时间】").append(DataTracerConstant.SPLIT).append(loadTime).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【客户联系人】").append(DataTracerConstant.SPLIT).append(orderVO.getShipperContact()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【客户订单号】").append(DataTracerConstant.SPLIT).append(orderVO.getShipperOrderNumber()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【最迟提箱时间】").append(DataTracerConstant.SPLIT).append(latestPackingTime).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【单趟应收金额】").append(DataTracerConstant.SPLIT).append(orderVO.getSingleTripReceiveAmount()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【单趟应付金额】").append(DataTracerConstant.SPLIT).append(orderVO.getSingleTripFreightAmount()).append(DataTracerConstant.LINE)
                .append(DataTracerConstant.TAB).append("【备注】").append(DataTracerConstant.SPLIT).append(orderVO.getRemark()).append(DataTracerConstant.LINE);
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 订单
     *
     * @param itemList
     * @return
     */
    private String getOrderGoodsContent(List<OrderGoodsVO> itemList) {
        StringBuilder builder = new StringBuilder();
        builder.append("货物信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(itemList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (OrderGoodsVO item : itemList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【货物类型】").append(DataTracerConstant.SPLIT).append(dictCacheService.selectValueNameByValueCode(item.getGoodsType())).append(DataTracerConstant.BLANK)
                    .append("【货物名称】").append(DataTracerConstant.SPLIT).append(item.getGoodsName()).append(DataTracerConstant.BLANK)
                    .append("【货物量】").append(DataTracerConstant.SPLIT).append(item.getGoodsQuantity()).append(DataTracerConstant.BLANK)
                    .append("【单位】").append(DataTracerConstant.SPLIT).append(SmartBaseEnumUtil.getEnumDescByValue(item.getGoodsUnit(), GoodsUnitTypeEnum.class)).append(DataTracerConstant.BLANK)
                    .append("【备注】").append(DataTracerConstant.SPLIT).append(item.getRemark()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }

    /**
     * 设置路线信息
     *
     * @param pathList
     * @return
     */
    private String getOrderLocation(List<OrderPathDTO> pathList) {
        StringBuilder builder = new StringBuilder();
        builder.append("路线信息:").append(DataTracerConstant.LINE);
        if (CollectionUtils.isEmpty(pathList)) {
            builder.append(DataTracerConstant.TAB).append(DataTracerConstant.LINE);
            return builder.toString();
        }
        for (OrderPathDTO item : pathList) {
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

    private String getAddress(OrderPathDTO pathDTO) {
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



    /**
     * 批量导入
     *
     * @param orderIdList
     * @param dataTracerRequestForm
     */
    @Async
    public void importLog(Collection<Long> orderIdList, DataTracerRequestForm dataTracerRequestForm) {
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (Long orderId : orderIdList) {
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(orderId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ORDER);
            dataTracerForm.setOperateType(OrderDataTracerOperateTypeEnum.IMPORT_ORDER);
            dataTracerForm.setOperateContent(OrderDataTracerOperateTypeEnum.IMPORT_ORDER.getDesc());
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
     * 更新分配状态
     *
     * @param orderId
     * @param beforeOrderGoodsList
     * @param afterOrderGoodsList
     * @param dataTracerRequestForm
     */
    @Async
    public void updateScheduleFlagLog(Long orderId, List<OrderGoodsEntity> beforeOrderGoodsList, List<OrderGoodsEntity> afterOrderGoodsList, DataTracerRequestForm dataTracerRequestForm) {
        String content = "订单货物更新";

        DataTracerForm dataTracerForm = new DataTracerForm();
        dataTracerForm.setBusinessId(orderId);
        dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.ORDER);
        dataTracerForm.setOperateType(OrderDataTracerOperateTypeEnum.UPDATE_SCHEDULE_FLAG);
        dataTracerForm.setOperateContent(content);
        dataTracerForm.setDiff(new DataTracerDiffBO(this.getUpdateOrderGoodsContent(beforeOrderGoodsList), this.getUpdateOrderGoodsContent(afterOrderGoodsList)));
        dataTracerForm.setIp(dataTracerRequestForm.getIp());
        dataTracerForm.setUserAgent(dataTracerRequestForm.getUserAgent());
        dataTracerForm.setOperateTime(dataTracerRequestForm.getOperateTime());

        Long operatorId = dataTracerRequestForm.getOperatorId();
        String operatorName = dataTracerRequestForm.getOperatorName();
        Integer operateType = dataTracerRequestForm.getOperatorType();
        dataTracerService.saveOperateRecord(dataTracerForm, operatorId, operatorName, operateType);
    }

    private String getUpdateOrderGoodsContent(List<OrderGoodsEntity> orderGoodsList){
        StringBuilder builder = new StringBuilder();
        builder.append("货物信息:").append(DataTracerConstant.LINE);
        for (OrderGoodsEntity item : orderGoodsList) {
            builder.append(DataTracerConstant.TAB)
                    .append("【货物名称】").append(DataTracerConstant.SPLIT).append(item.getGoodsName()).append(DataTracerConstant.BLANK)
                    .append("【货物量】").append(DataTracerConstant.SPLIT).append(item.getGoodsQuantity()).append(DataTracerConstant.BLANK)
                    .append(DataTracerConstant.LINE);
        }
        return builder.toString().replaceAll("null", "");
    }
}
