package net.lab1024.tms.driver.module.business.fleet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.fleet.domain.FleetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FleetDao extends BaseMapper<FleetEntity> {
}
