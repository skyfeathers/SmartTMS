package net.lab1024.tms.driver.module.business.driver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.driver.domain.DriverTaxRegisterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 税务登记人
 *
 * @author lidoudou
 * @date 2022/8/25 下午5:49
 */
@Mapper
@Component
public interface DriverTaxRegisterDao extends BaseMapper<DriverTaxRegisterEntity> {

    /**
     * 根据司机ID查询
     *
     * @param driverId
     * @return
     */
    DriverTaxRegisterEntity selectByDriverId(@Param("driverId") Long driverId);

    /**
     * 根据司机ID删除税务登记人
     *
     * @param driverId
     */
    void deleteByDriverId(Long driverId);

}
