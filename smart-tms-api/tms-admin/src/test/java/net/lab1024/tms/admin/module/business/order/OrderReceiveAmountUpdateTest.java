package net.lab1024.tms.admin.module.business.order;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderGoodsDTO;
import net.lab1024.tms.admin.module.business.order.manager.OrderManager;
import net.lab1024.tms.admin.module.business.order.service.OrderGoodsService;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.exception.BusinessException;
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
class OrderReceiveAmountUpdateTest extends TmsAdminApplicationTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private OrderManager orderManager;

    @Test
    void updateWaybillReceiveAmount() {
        List<OrderEntity> orderEntityList = orderDao.selectList(null);
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return;
        }
        List<Long> orderIdList = orderEntityList.stream().map(OrderEntity::getOrderId).collect(Collectors.toList());

        List<OrderGoodsDTO> goodsList = orderGoodsService.selectByOrderIdList(orderIdList);
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }

        Map<Long, List<OrderGoodsDTO>> orderGoodsMap = goodsList.stream().collect(groupingBy(OrderGoodsDTO::getOrderId));

        List<OrderEntity> updateList = Lists.newArrayList();
        for (OrderEntity order : orderEntityList) {
            Long orderId = order.getOrderId();
            if (!orderGoodsMap.containsKey(orderId)) {
                throw new BusinessException(order.getOrderNo() + "，商品不存在");
            }

            List<OrderGoodsDTO> orderGoodsList = orderGoodsMap.getOrDefault(orderId, Lists.newArrayList());
            if (CollectionUtils.isEmpty(orderGoodsList)) {
                throw new BusinessException(order.getOrderNo() + "，商品为空");
            }

            OrderGoodsDTO orderGoods = orderGoodsList.get(NumberConst.ZERO);
            // 如果商品的单价为0的话，则不处理
            if (null == orderGoods.getGoodsUnitPrice() || orderGoods.getGoodsUnitPrice().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            OrderEntity updateEntity = new OrderEntity();
            updateEntity.setOrderId(order.getOrderId());
            updateEntity.setWaybillReceiveAmount(orderGoods.getGoodsUnitPrice());
            updateList.add(updateEntity);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            orderManager.updateBatchById(updateList);
        }
    }
}