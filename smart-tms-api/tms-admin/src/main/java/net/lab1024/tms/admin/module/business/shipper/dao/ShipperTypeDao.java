package net.lab1024.tms.admin.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:47
 */
@Mapper
@Component
public interface ShipperTypeDao extends BaseMapper<ShipperTypeEntity> {

    /**
     * 查询货主下的联系人信息
     *
     * @param shipperId
     * @return
     */
    List<ShipperTypeEntity> selectByShipperId(@Param("shipperId") Long shipperId);

    List<ShipperTypeEntity> selectByShipperIdList(@Param("shipperIdList") List<Long> shipperIdList);

    /**
     * 根据机构删除信息
     * @param shipperId
     */
    void deleteByShipperId(@Param("shipperId") Long shipperId);
}
