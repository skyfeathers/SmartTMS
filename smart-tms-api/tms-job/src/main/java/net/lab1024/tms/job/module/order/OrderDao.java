package net.lab1024.tms.job.module.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Component
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 根据装货时间以及分配状态查询订单
     *
     * @param loadTime
     * @param scheduleFlag
     * @return
     */
    List<OrderBO> selectByScheduleFlagAndLoadTime(@Param("loadTime") LocalDateTime loadTime, @Param("scheduleFlag") Boolean scheduleFlag, @Param("excludeStatus") Integer excludeStatus);
}
