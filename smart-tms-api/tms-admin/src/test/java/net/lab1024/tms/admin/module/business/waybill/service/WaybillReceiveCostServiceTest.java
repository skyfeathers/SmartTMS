package net.lab1024.tms.admin.module.business.waybill.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderGoodsDao;
import net.lab1024.tms.admin.module.business.order.manager.OrderGoodsManager;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillManager;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillReceiveCostManager;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillReceiveCostEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

class WaybillReceiveCostServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private OrderCostDao orderCostDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private OrderGoodsManager orderGoodsManager;
    @Autowired
    private OrderCostManager orderCostManager;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillManager waybillManager;
    @Autowired
    private WaybillReceiveCostManager waybillReceiveCostManager;

    /**
     * 第一步 -运单应收费用项，运单合计应收，订单应收费用项
     */
    @Test
    public void updateWaybillCost() {
        List<OrderEntity> orderEntityList = orderDao.selectList(null);
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return;
        }
        List<Long> orderIdList = orderEntityList.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());
        //订单应收
        List<OrderCostEntity> orderCostEntityList = orderCostDao.selectByOrderIdList(orderIdList);
        Map<Long, List<OrderCostEntity>> orderCostEntityListMap = orderCostEntityList.stream().collect(groupingBy(OrderCostEntity::getOrderId));
        System.out.println("共需处理订单数:" + orderIdList.size());
        int i = 1;
        for (Long orderId : orderIdList) {
            System.out.println("正在处理第" + i + "条数据。订单id：" + orderId);
            List<Long> waybillIdList = waybillDao.selectIdByOrderId(orderId, Lists.newArrayList());
            List<OrderCostEntity> orderCostEntities = orderCostEntityListMap.get(orderId);
            if(CollectionUtils.isEmpty(waybillIdList)){
                this.noWaybillOrderReceive(orderCostEntities);
            }else{
                this.existWaybillOrderReceive(orderCostEntities, waybillIdList);
            }

        }
    }

    private void noWaybillOrderReceive(List<OrderCostEntity> orderCostEntities){
        List<OrderCostEntity> updateOrderCostList = Lists.newArrayList();
        for (OrderCostEntity orderCostEntity : orderCostEntities) {
            OrderCostEntity updateOrderCost = new OrderCostEntity();
            updateOrderCost.setOrderCostId(orderCostEntity.getOrderCostId());
            updateOrderCost.setCostAmount(BigDecimal.ZERO);
            updateOrderCostList.add(updateOrderCost);
        }
        orderCostManager.updateBatchById(updateOrderCostList);
    }

    private void existWaybillOrderReceive(List<OrderCostEntity> orderCostEntities,List<Long> waybillIdList){
        for (Long waybillId : waybillIdList) {
            List<WaybillReceiveCostEntity> waybillReceiveCostEntityList = Lists.newArrayList();
            for (OrderCostEntity orderCostEntity : orderCostEntities) {
                WaybillReceiveCostEntity waybillReceiveCostEntity = new WaybillReceiveCostEntity();
                waybillReceiveCostEntity.setWaybillId(waybillId);
                waybillReceiveCostEntity.setOrderCostId(orderCostEntity.getOrderCostId());
                waybillReceiveCostEntity.setCostItemId(orderCostEntity.getCostItemId());
                waybillReceiveCostEntity.setCostItemCategory(orderCostEntity.getCategory());
                waybillReceiveCostEntity.setCostItemType(orderCostEntity.getCostItemType());
                waybillReceiveCostEntity.setCostItemName(orderCostEntity.getCostItemName());
                waybillReceiveCostEntity.setCostAmount(orderCostEntity.getCostAmount());
                waybillReceiveCostEntityList.add(waybillReceiveCostEntity);
            }
            //运单应收费用项
            waybillReceiveCostManager.saveBatch(waybillReceiveCostEntityList);
            //运单应收总金额
            BigDecimal totalReceiveAmount = waybillReceiveCostEntityList.stream().map(e -> e.getCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            WaybillEntity updateWaybillEntity = new WaybillEntity();
            updateWaybillEntity.setWaybillId(waybillId);
            updateWaybillEntity.setReceiveAmount(totalReceiveAmount);
            waybillDao.updateById(updateWaybillEntity);
        }
        //订单应收费用项
        int size = waybillIdList.size();
        List<OrderCostEntity> updateOrderCostList = Lists.newArrayList();
        for (OrderCostEntity orderCostEntity : orderCostEntities) {
            OrderCostEntity updateOrderCost = new OrderCostEntity();
            updateOrderCost.setOrderCostId(orderCostEntity.getOrderCostId());
            updateOrderCost.setCostAmount(orderCostEntity.getCostAmount().multiply(new BigDecimal(size)));
            updateOrderCostList.add(updateOrderCost);
        }
        orderCostManager.updateBatchById(updateOrderCostList);
    }

    /**
     * 第二步-订单货物信息
     */
    @Test
    public void updateOrderGoods() {
        List<OrderEntity> orderEntityList = orderDao.selectList(null);
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return;
        }
        Map<Long,OrderEntity> orderEntityMap = orderEntityList.stream().collect(Collectors.toMap(OrderEntity::getOrderId, Function.identity()));
        List<Long> orderIdList = orderEntityList.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());
        // 订单商品
        List<OrderGoodsEntity> orderGoodsEntityList = orderGoodsDao.selectByOrderIdList(orderIdList);
        Map<Long, List<OrderGoodsEntity>>  orderGoodsListMap = orderGoodsEntityList.stream().collect(groupingBy(OrderGoodsEntity::getOrderId));

        for(Map.Entry<Long, List<OrderGoodsEntity>> entry : orderGoodsListMap.entrySet()){
            Long orderId = entry.getKey();
            OrderEntity orderEntity = orderEntityMap.get(orderId);
            List<OrderGoodsEntity> goodsList = entry.getValue();
            //订单商品单价以及每种商品的总价
            List<OrderGoodsEntity> updateOrderGoodsList = Lists.newArrayList();
            for (OrderGoodsEntity orderGoodsEntity : goodsList) {
                OrderGoodsEntity updateGoodsEntity = new OrderGoodsEntity();
                updateGoodsEntity.setOrderGoodsId(orderGoodsEntity.getOrderGoodsId());
//                updateGoodsEntity.setGoodsUnitPrice(orderEntity.getTotalAmount());
//                updateGoodsEntity.setGoodsTotalPrice(SmartBigDecimalUtil.multiply(orderEntity.getTotalAmount(), new BigDecimal(orderGoodsEntity.getGoodsCount()), 4));
                updateOrderGoodsList.add(updateGoodsEntity);
            }
            orderGoodsManager.updateBatchById(updateOrderGoodsList);
            //订单商品总价
//            BigDecimal totalGoodsPrice = updateOrderGoodsList.stream().map(e -> e.getGoodsTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
//            OrderEntity updateOrderEntity = new OrderEntity();
//            updateOrderEntity.setOrderId(orderId);
//            updateOrderEntity.setGoodsTotalAmount(totalGoodsPrice);
//            orderDao.updateById(updateOrderEntity);
        }

    }


    /**
     * 第三步-订单合计应收
     */
    @Test
    public void updateOrderTotalAmount() {
        List<OrderEntity> orderEntityList = orderDao.selectList(null);
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return;
        }
        List<Long> orderIdList = orderEntityList.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());
        System.out.println("共需处理订单数:" + orderIdList.size());
        int i = 1;
        for (Long orderId : orderIdList) {
            // 订单合计应收
            List<OrderCostEntity> orderCostList = orderCostDao.selectByOrderId(orderId);
            List<BigDecimal> costAmountList = orderCostList.stream().map(OrderCostEntity::getCostAmount).collect(Collectors.toList());
            BigDecimal totalAmount = costAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            OrderEntity updateOrderEntity = new OrderEntity();
            updateOrderEntity.setOrderId(orderId);
            updateOrderEntity.setTotalAmount(totalAmount);
            orderDao.updateById(updateOrderEntity);
        }
    }


    /**
     * 第四步-浮动提交付款状态
     */
    @Test
    public void updateWaybillPayOrder() {
        List<WaybillEntity> waybillEntityList = waybillDao.selectList(null);
        if (CollectionUtils.isEmpty(waybillEntityList)) {
            return;
        }
        List<Long> waybillIdList = waybillEntityList.stream().map(WaybillEntity::getWaybillId).collect(Collectors.toList());

        List<WaybillEntity> updateWaybillList = Lists.newArrayList();
        for (Long waybillId : waybillIdList) {
            WaybillEntity updateEntity = new WaybillEntity();
            updateEntity.setWaybillId(waybillId);

            List<Object> statusList = SmartBaseEnumUtil.differenceValueList(PayOrderStatusEnum.class, PayOrderStatusEnum.CANCEL);
            List<PayOrderEntity> payOrderEntityList = payOrderDao.queryByWaybillId(waybillId, statusList);
            if(CollectionUtils.isEmpty(payOrderEntityList)){
                updateEntity.setSubmitPayFlag(false);
                updateEntity.setPayOrderCount(0);
            }else {
                updateEntity.setSubmitPayFlag(Boolean.TRUE);
                updateEntity.setPayOrderCount(payOrderEntityList.size());
            }
            updateWaybillList.add(updateEntity);
        }
        waybillManager.updateBatchById(updateWaybillList);
    }
}