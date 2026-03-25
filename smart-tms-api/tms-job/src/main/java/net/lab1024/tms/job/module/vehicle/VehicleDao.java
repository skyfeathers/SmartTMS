package net.lab1024.tms.job.module.vehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface VehicleDao extends BaseMapper<VehicleEntity> {
}
