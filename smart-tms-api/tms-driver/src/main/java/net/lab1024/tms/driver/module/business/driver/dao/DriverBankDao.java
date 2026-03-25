package net.lab1024.tms.driver.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DriverBankDao extends BaseMapper<DriverBankEntity> {

    /**
     * 查询司机银行列表
     *
     * @param driverId
     * @return
     */
    List<DriverBankEntity> selectByDriverId(@Param("driverId") Long driverId);

    /**
     * 查询司机默认银行
     *
     * @param driverId
     * @param defaultFlag
     * @return
     */
    DriverBankEntity selectDefaultBankByDriverId(@Param("driverId") Long driverId, @Param("defaultFlag") Boolean defaultFlag);

}
