package net.lab1024.tms.common.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.driver.domain.DriverBalanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author yandy
 */
@Component
@Mapper
public interface CommonDriverBalanceDao extends BaseMapper<DriverBalanceEntity> {

    /**
     *
     * @param enterpriseId
     * @param driverId
     * @return
     */
    DriverBalanceEntity selectByEnterpriseIdAndDriverId(@Param("enterpriseId") Long enterpriseId, @Param("driverId") Long driverId);

    /**
     *
     * @param driverBalanceId
     * @param changeAmount
     * @param updateVersion
     * @return
     */
    Integer updateBalanceAmount(@Param("driverBalanceId") Long driverBalanceId, @Param("changeAmount")  BigDecimal changeAmount, @Param("updateVersion") Integer updateVersion);
}
