package net.lab1024.tms.common.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.driver.domain.DriverBalanceRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author renjipeng
 * @date 2022年09月02日 9:34
 */
@Mapper
@Component
public interface CommonDriverBalanceRecordDao extends BaseMapper<DriverBalanceRecordEntity> {


}
