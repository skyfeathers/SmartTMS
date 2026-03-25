package net.lab1024.tms.admin.module.business.waybill.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.manager.WaybillReceiveCostManager;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillReceiveCostEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

class WaybillShortBargeReceiveCostServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderCostDao orderCostDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillReceiveCostManager waybillReceiveCostManager;

    /**
     * 订单增加短驳费用、订单增加对应应收费用类型
     */
    @Test
    public void updateWaybillCost() {
        List<OrderEntity> orderEntityList = orderDao.selectList(null);
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return;
        }
        List<Long> orderIdList = orderEntityList.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());
        for (Long orderId : orderIdList) {
            List<Long> waybillIdList = waybillDao.selectIdByOrderId(orderId, Lists.newArrayList());
            if (CollectionUtils.isEmpty(waybillIdList)) {
                continue;
            }

            OrderCostEntity orderCostEntity = new OrderCostEntity();
            orderCostEntity.setOrderId(orderId);
            orderCostEntity.setCostItemId(30L);
            orderCostEntity.setCostItemType(CostItemTypeEnum.RECEIVE.getValue());
            orderCostEntity.setCategory(CostItemCategoryEnum.SHORT_BARGE.getValue());
            orderCostEntity.setCostItemName("短驳费用");
            orderCostEntity.setCostAmount(BigDecimal.ZERO);
            orderCostDao.insert(orderCostEntity);

            List<WaybillReceiveCostEntity> receiveOrderCostList = waybillIdList.stream().map(waybillId -> {
                WaybillReceiveCostEntity waybillReceiveCostEntity = SmartBeanUtil.copy(orderCostEntity, WaybillReceiveCostEntity.class);
                waybillReceiveCostEntity.setWaybillId(waybillId);
                waybillReceiveCostEntity.setOrderCostId(orderCostEntity.getOrderCostId());
                return waybillReceiveCostEntity;
            }).collect(Collectors.toList());
            waybillReceiveCostManager.saveBatch(receiveOrderCostList);

        }
    }

}