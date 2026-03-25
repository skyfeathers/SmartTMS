package net.lab1024.tms.common.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author yandy
 */
@Component
@Mapper
public interface CommonDriverDao extends BaseMapper<DriverEntity> {
}
