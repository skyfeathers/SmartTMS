package net.lab1024.tms.admin.module.business.drivervehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.drivervehicle.domain.DriverVehicleVO;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 司机车联关联
 *
 * @author lihaifan
 * @date 2022/7/11 11:24
 */
@Mapper
@Component
public interface DriverVehicleDao extends BaseMapper<DriverVehicleEntity> {

    /**
     * 根据司机ID删除
     *
     * @param driverIdList
     */
    void deleteByDriverId(@Param("driverIdList") List<Long> driverIdList);

    /**
     * 根据车辆ID删除
     *
     * @param vehicleIdList
     */
    void deleteByVehicleId(@Param("vehicleIdList") List<Long> vehicleIdList);

    /**
     * 根据司机以及车辆ID查询
     *
     * @param driverId
     * @param vehicleId
     * @return
     */
    DriverVehicleEntity selectByDriverAndVehicle(@Param("driverId") Long driverId, @Param("vehicleId") Long vehicleId);

    /**
     * 根据司机ID查询关联信息
     *
     * @param driverId
     * @param deletedFlag
     * @return
     */
    List<DriverVehicleVO> selectByDriverId(@Param("driverId") Long driverId, @Param("disableFlag") Boolean disableFlag, @Param("auditStatus") Integer auditStatus, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据司机ID查询关联信息
     *
     * @param driverIdList
     * @param deletedFlag
     * @return
     */
    List<DriverVehicleVO> selectByDriverIdList(@Param("driverIdList") List<Long> driverIdList, @Param("disableFlag") Boolean disableFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据车辆ID查询关联信息
     *
     * @param vehicleIdList
     * @param deletedFlag
     * @return
     */
    List<DriverVehicleVO> selectByVehicleIdList(@Param("vehicleIdList") List<Long> vehicleIdList, @Param("disableFlag") Boolean disableFlag, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据车辆ID查询关联信息
     *
     * @param vehicleId
     * @param status
     * @param deletedFlag
     * @return
     */
    List<DriverVehicleVO> selectByVehicleId(@Param("vehicleId") Long vehicleId, @Param("status") Integer status, @Param("deletedFlag") Boolean deletedFlag);
}
