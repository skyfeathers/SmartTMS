package net.lab1024.tms.admin.module.business.order.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderGoodsDao;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderGoodsDTO;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderGoodsVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillGoodsDao;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillGoodsEntity;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 订单商品操作
 *
 * @author lidoudou
 * @date 2022/7/13 下午8:00
 */
@Service
public class OrderGoodsService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private WaybillGoodsDao waybillGoodsDao;
    @Autowired
    private DictCacheService dictCacheService;

    /**
     * 根据订单ID查询货物列表
     *
     * @param orderIdList
     * @return
     */
    public List<OrderGoodsVO> selectByOrderIdList(List<Long> orderIdList) {
        List<OrderGoodsEntity> orderGoodsEntityList = orderGoodsDao.selectByOrderIdList(orderIdList);
        if (CollectionUtils.isEmpty(orderGoodsEntityList)) {
            return Lists.newArrayList();
        }
        List<OrderGoodsVO> orderGoodsList = SmartBeanUtil.copyList(orderGoodsEntityList, OrderGoodsVO.class).stream().map(item -> {
            item.setGoodsTypeName(dictCacheService.selectValueNameByValueCode(item.getGoodsType()));
            return item;
        }).collect(Collectors.toList());
        List<Long> orderGoodsIdList = orderGoodsList.stream().map(OrderGoodsVO::getOrderGoodsId).collect(Collectors.toList());
        //查询关联的运单
        List<WaybillGoodsEntity> waybillGoodsEntityList = waybillGoodsDao.selectByOrderGoodsIdList(orderGoodsIdList, WaybillStatusEnum.CANCEL.getValue());
        if (CollectionUtils.isEmpty(waybillGoodsEntityList)) {
            orderGoodsList.forEach(e -> {
                e.setWaybillGoodsQuantity(BigDecimal.ZERO);
                e.setRemainGoodsQuantity(e.getGoodsQuantity());
            });
            return orderGoodsList;
        }
        //计算运单使用的重量及数量
        Map<Long, List<WaybillGoodsEntity>> listMap = waybillGoodsEntityList.stream().collect(groupingBy(WaybillGoodsEntity::getOrderGoodsId));
        for (OrderGoodsVO orderGoodsVO : orderGoodsList) {
            Long orderGoodsId = orderGoodsVO.getOrderGoodsId();
            List<WaybillGoodsEntity> waybillGoodsList = listMap.get(orderGoodsId);
            if (CollectionUtils.isEmpty(waybillGoodsList)) {
                orderGoodsVO.setWaybillGoodsQuantity(BigDecimal.ZERO);
                orderGoodsVO.setRemainGoodsQuantity(orderGoodsVO.getGoodsQuantity());
            } else {
                BigDecimal totalWaybillGoodsQuantity = waybillGoodsList.stream().map(e -> e.getGoodsQuantity()).reduce(BigDecimal.ZERO, BigDecimal::add);
                orderGoodsVO.setWaybillGoodsQuantity(BigDecimal.ZERO);
                orderGoodsVO.setRemainGoodsQuantity(orderGoodsVO.getGoodsQuantity().subtract(totalWaybillGoodsQuantity));
            }
        }
        return orderGoodsList;
    }

    /**
     * 根据订单获取分配状态
     *
     * @param orderId
     * @return
     */
    public BigDecimal getOrderGoodsRemainQuantity(Long orderId) {
        List<OrderGoodsVO> orderGoodsList = this.selectByOrderIdList(Arrays.asList(orderId));
        if (CollectionUtils.isEmpty(orderGoodsList)) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalRemainQuantity = orderGoodsList.stream().map(e -> e.getRemainGoodsQuantity()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalRemainQuantity;
    }

    /**
     * 更新订单的分配状态
     *
     * @param orderId
     */
    public void updateOrderScheduleFlag(Long orderId) {
        BigDecimal remainQuantity = this.getOrderGoodsRemainQuantity(orderId);

        OrderEntity updateEntity = new OrderEntity();
        updateEntity.setOrderId(orderId);
        if (remainQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            updateEntity.setScheduleFlag(true);
            updateEntity.setStatus(OrderStatusEnum.COMPLETE.getValue());
        }else {
            updateEntity.setScheduleFlag(false);
        }
        orderDao.updateById(updateEntity);
    }
}
