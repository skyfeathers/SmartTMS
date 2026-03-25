package net.lab1024.tms.admin.module.business.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单货物
 *
 * @author lidoudou
 * @date 2022/7/13 下午7:58
 */
@Mapper
@Component
public interface OrderGoodsDao extends BaseMapper<OrderGoodsEntity> {

    /**
     * 根据订单获取货物列表
     *
     * @param orderIdList
     * @return
     */
    List<OrderGoodsEntity> selectByOrderIdList(@Param("orderIdList") List<Long> orderIdList);

    /**
     * 根据订单删除货物信息
     *
     * @param orderId
     */
    void deleteByOrderId(@Param("orderId") Long orderId);
}