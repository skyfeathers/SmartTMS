package net.lab1024.tms.admin.module.business.order.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderPathDao;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderCreateForm;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderUpdateForm;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerForm;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单操作
 *
 * @author lidoudou
 * @date 2022/7/13 下午7:48
 */
@Service
public class OrderManager extends ServiceImpl<OrderDao, OrderEntity> {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderPathDao orderPathDao;
    @Autowired
    private OrderGoodsManager orderGoodsManager;
    @Autowired
    private OrderPathManager orderPathManager;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private DataTracerService dataTracerService;
    /**
     * 保存
     *
     * @param createForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveOrder(OrderEntity orderEntity, OrderCreateForm createForm) {
        this.updateLocation(orderEntity, createForm.getPathList());
        orderDao.insert(orderEntity);
        this.saveRelateInfo(createForm, orderEntity.getOrderId());
    }



    /**
     * 更新订单信息
     *
     * @param updateForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateOrder(OrderUpdateForm updateForm, List<Long> deleteGoodsIdList, DataTracerRequestForm dataTracerRequestForm) {
        OrderEntity orderEntity = SmartBeanUtil.copy(updateForm, OrderEntity.class);
        this.updateLocation(orderEntity, updateForm.getPathList());
        orderDao.updateById(orderEntity);
        //删除原路线信息
        orderPathDao.deleteByOrderId(orderEntity.getOrderId());
        //删除原商品信息
        if(CollectionUtils.isNotEmpty(deleteGoodsIdList)) {
            orderGoodsManager.removeByIds(deleteGoodsIdList);
        }
        //保存管理的路线，商品信息
        this.saveRelateInfo(updateForm, orderEntity.getOrderId());
        // 同步运单信息 - 同步货主以及所属公司
        List<WaybillEntity> waybillEntityList = waybillManager.getBaseMapper().selectByOrderId(orderEntity.getOrderId(), WaybillStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isEmpty(waybillEntityList)) {
            return;
        }
        List<WaybillEntity> updateList = waybillEntityList.stream().map(e -> {
            WaybillEntity updateEntity = new WaybillEntity();
            updateEntity.setWaybillId(e.getWaybillId());
            updateEntity.setShipperId(orderEntity.getShipperId());
            return updateEntity;
        }).collect(Collectors.toList());
        waybillManager.updateBatchById(updateList);
        this.updateOrderWaybillLog(updateList, dataTracerRequestForm);
    }

    public void updateOrderWaybillLog(List<WaybillEntity> updateList, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(updateList)) {
            return;
        }
        Long someOneWaybillId = updateList.get(0).getWaybillId();
        WaybillEntity someOne = waybillDao.selectById(someOneWaybillId);
        ShipperEntity shipperEntity = shipperDao.selectById(someOne.getShipperId());
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(someOne.getEnterpriseId());
        String content = "因订单更新，修改货主为：" + shipperEntity.getConsignor() +
                "，企业为：" + enterpriseEntity.getEnterpriseName();
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (WaybillEntity waybillEntity : updateList) {
            Long waybillId = waybillEntity.getWaybillId();
            String waybillContent = content +  "，利润为：" + waybillEntity.getProfitAmount();
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(waybillId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
            dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.DRIVER_GOODS);
            dataTracerForm.setOperateContent(waybillContent);
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
     * 订单更新货主
     *
     * @param orderId
     * @param shipperEntity
     */
    public void updateOrderShipperHandler(Long orderId, ShipperEntity shipperEntity, DataTracerRequestForm dataTracerRequestForm) {
        Long shipperId = shipperEntity.getShipperId();
        Long enterpriseId = shipperEntity.getEnterpriseId();
        // 更新订单
        OrderEntity updateOrderEntity = new OrderEntity();
        updateOrderEntity.setOrderId(orderId);
        updateOrderEntity.setShipperId(shipperId);
        updateOrderEntity.setEnterpriseId(enterpriseId);
        orderDao.updateById(updateOrderEntity);

        // 更新运单
        List<WaybillEntity> waybillEntityList = waybillDao.selectByOrderId(orderId, WaybillStatusEnum.CANCEL.getValue());
        List<WaybillEntity> updateWaybillEntityList = waybillEntityList.stream().map(e -> {
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(e.getWaybillId());
            updateWaybillEntity.setShipperId(shipperId);
            updateWaybillEntity.setEnterpriseId(enterpriseId);
            return updateWaybillEntity;
        }).collect(Collectors.toList());
        waybillManager.updateBatchById(updateWaybillEntityList);
        this.waybillShipperUpdateLog(waybillEntityList, shipperId, dataTracerRequestForm);
    }

    public void waybillShipperUpdateLog(List<WaybillEntity> waybillEntityList, Long shipperId, DataTracerRequestForm dataTracerRequestForm) {
        if (CollectionUtils.isEmpty(waybillEntityList)) {
            return;
        }
        ShipperEntity shipperEntity = shipperDao.selectById(shipperId);
        String content = "因订单货主变动修改货主为：" + shipperEntity.getConsignor();
        List<DataTracerForm> dataTracerList = Lists.newArrayList();
        for (WaybillEntity waybillEntity : waybillEntityList) {
            Long waybillId = waybillEntity.getWaybillId();
            DataTracerForm dataTracerForm = new DataTracerForm();
            dataTracerForm.setBusinessId(waybillId);
            dataTracerForm.setBusinessType(DataTracerBusinessTypeEnum.WAYBILL);
            dataTracerForm.setOperateType(WaybillDataTracerOperateTypeEnum.ORDER_SHIPPER_UPDATE);
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
     * 更新地址信息
     *
     * @param orderEntity
     * @param pathList
     */
    public void updateLocation(OrderEntity orderEntity, List<OrderPathDTO> pathList) {
        orderEntity.setContainerLocation(StringConst.EMPTY);
        orderEntity.setPlacingLocation(StringConst.EMPTY);
        orderEntity.setUnloadingLocation(StringConst.EMPTY);
        orderEntity.setReturnContainerLocation(StringConst.EMPTY);

        Map<Integer, List<OrderPathDTO>> pathMap = pathList.stream().collect(Collectors.groupingBy(OrderPathDTO::getType));
        for (Map.Entry<Integer, List<OrderPathDTO>> entry : pathMap.entrySet()) {
            Integer type = entry.getKey();
            List<OrderPathDTO> typePathList = entry.getValue();
            List<String> locationList = Lists.newArrayList();
            typePathList.forEach(e -> {
                String location = e.getProvinceName() + e.getCityName() + e.getDistrictName() + e.getAddress();
                locationList.add(location.replaceAll("null", ""));
            });
            String allLocation = SmartStringUtil.join(CommonConst.SEPARATOR, locationList);
            PathTypeEnum typeEnum = SmartBaseEnumUtil.getEnumByValue(type, PathTypeEnum.class);

            switch (typeEnum) {
                case CONTAINER_LOCATION:
                    orderEntity.setContainerLocation(allLocation);
                    break;
                case PLACING_LOADING:
                    orderEntity.setPlacingLocation(allLocation);
                    break;
                case UNLOADING_LOCATION:
                    orderEntity.setUnloadingLocation(allLocation);
                    break;
                case RETURN_CONTAINER_LOCATION:
                    orderEntity.setReturnContainerLocation(allLocation);
                    break;
            }
        }
    }

    /**
     * 保存关联信息
     *
     * @param createForm
     * @param orderId
     */
    private void saveRelateInfo(OrderCreateForm createForm, Long orderId) {
        // 保存路线信息
        if (CollectionUtils.isNotEmpty(createForm.getPathList())) {
            List<OrderPathEntity> pathList = SmartBeanUtil.copyList(createForm.getPathList(), OrderPathEntity.class);
            pathList.forEach(e -> e.setOrderId(orderId));
            orderPathManager.saveBatch(pathList);
        }

        // 保存货物信息
        if (CollectionUtils.isNotEmpty(createForm.getGoodsList())) {
            List<OrderGoodsEntity> itemList = SmartBeanUtil.copyList(createForm.getGoodsList(), OrderGoodsEntity.class);
            itemList.forEach(e -> e.setOrderId(orderId));
            orderGoodsManager.saveOrUpdateBatch(itemList);
        }
    }

}
