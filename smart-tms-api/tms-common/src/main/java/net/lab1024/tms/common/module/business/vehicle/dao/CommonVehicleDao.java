package net.lab1024.tms.common.module.business.vehicle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author yandy
 */
@Mapper
@Component
public interface CommonVehicleDao extends BaseMapper<VehicleEntity> {
}
