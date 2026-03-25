package net.lab1024.tms.driver.module.business.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单路线信息
 *
 * @author lidoudou
 * @date 2022/7/13 下午3:10
 */
@Mapper
@Component
public interface OrderPathDao extends BaseMapper<OrderPathEntity> {

    /**
     * 根据订单获取路线信息
     *
     * @param orderIdList
     * @return
     */
    List<OrderPathEntity> selectByOrderIdList(@Param("orderIdList") List<Long> orderIdList);

}