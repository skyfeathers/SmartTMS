package net.lab1024.tms.admin.module.business.order;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 更新订单的运单应收金额
 *
 * @author lidoudou
 * @date 2022/11/18 下午2:33
 */
class OrderTotalAmountUpdateTest extends TmsAdminApplicationTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private OrderCostDao orderCostDao;

    @Test
    void updateWaybillReceiveAmount() {
        List<OrderEntity> orderEntityList = orderDao.selectList(null);
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return;
        }

        // 更新订单的应收金额
        List<OrderCostEntity> orderCostList = orderCostDao.selectList(null);
        Map<Long, List<OrderCostEntity>> orderCostMap = orderCostList.stream().collect(groupingBy(OrderCostEntity::getOrderId));

        List<OrderEntity> updateList = Lists.newArrayList();
        for (Map.Entry<Long, List<OrderCostEntity>> entry : orderCostMap.entrySet()) {
            Long orderId = entry.getKey();
            List<OrderCostEntity> list = entry.getValue();
            List<BigDecimal> costAmountList = list.stream().map(OrderCostEntity::getCostAmount).collect(Collectors.toList());
            BigDecimal totalAmount = costAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderId(orderId);
            orderEntity.setTotalAmount(totalAmount);
            updateList.add(orderEntity);
        }

        if (CollectionUtils.isNotEmpty(updateList)) {
            orderManager.updateBatchById(updateList);
        }
    }
}