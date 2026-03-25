package net.lab1024.tms.common.module.business.material.repairplant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author yandy
 */
@Component
@Mapper
public interface CommonRepairPlantDao extends BaseMapper<RepairPlantEntity> {
}
