package net.lab1024.tms.driver.module.business.shipper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ShipperDao extends BaseMapper<ShipperEntity> {

}
