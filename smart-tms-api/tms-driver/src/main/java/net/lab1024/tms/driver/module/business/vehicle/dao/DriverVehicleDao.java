package net.lab1024.tms.driver.module.business.vehicle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DriverVehicleDao extends BaseMapper<DriverVehicleEntity> {

    /**
     * 根据司机以及车辆ID查询
     *
     * @param driverId
     * @param vehicleId
     * @return
     */
    DriverVehicleEntity selectByDriverAndVehicle(@Param("driverId") Long driverId,
                                                 @Param("vehicleId") Long vehicleId);

}
