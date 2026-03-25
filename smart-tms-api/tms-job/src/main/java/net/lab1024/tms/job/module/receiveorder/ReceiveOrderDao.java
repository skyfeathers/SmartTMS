package net.lab1024.tms.job.module.receiveorder;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import net.lab1024.tms.job.module.receiveorder.domain.ShipperSimpleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface ReceiveOrderDao extends BaseMapper<ReceiveOrderEntity> {

    /**
     * 根据ID查询
     *
     * @param shipperIdList
     * @return
     */
    List<ShipperSimpleDTO> selectByShipperIdList(@Param("shipperIdList") Collection<Long> shipperIdList);

    /**
     * 查询账期大于当前时间的
     */
    List<ReceiveOrderEntity> selectByAccountPeriodDate(@Param("verificationFlag") Boolean verificationFlag);
}
