package net.lab1024.tms.common.module.business.drivervehicle;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 司机车联关联表
 *
 * @author lihaifan
 * @date 2022/7/11 11:24
 */
@Data
@TableName("t_driver_vehicle")
public class DriverVehicleEntity {

    /**
     * 司机车辆ID
     */
    private Long driverVehicleId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
