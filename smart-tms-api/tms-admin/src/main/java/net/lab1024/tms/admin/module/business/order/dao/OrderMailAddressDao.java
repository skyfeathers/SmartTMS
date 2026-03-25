package net.lab1024.tms.admin.module.business.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.order.domain.OrderMailAddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 订单收货信息
 *
 * @author lidoudou
 * @date 2022/8/25 下午3:31
 */
@Mapper
@Component
public interface OrderMailAddressDao extends BaseMapper<OrderMailAddressEntity> {

    /**
     * 根据订单ID查询收货信息
     *
     * @param orderId
     * @return
     */
    OrderMailAddressEntity selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单ID删除
     *
     * @param orderId
     */
    void deleteByOrderId(@Param("orderId") Long orderId);
}