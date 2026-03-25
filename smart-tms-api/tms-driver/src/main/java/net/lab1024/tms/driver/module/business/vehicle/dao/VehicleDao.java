package net.lab1024.tms.driver.module.business.vehicle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverVehicleQueryForm;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverVehicleQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Component
public interface VehicleDao extends BaseMapper<VehicleEntity> {

    /**
     * 查询司机创建的车辆
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<DriverVehicleQueryVO> selectByList(Page page, @Param("queryForm") DriverVehicleQueryForm queryForm);

    /**
     * 排除部分id，根据车牌号查询车辆
     *
     * @param vehicleNumber
     * @param excludeIds
     * @param deleteFlag
     * @return
     */
    VehicleEntity selectByNumberExcludeIds(@Param("vehicleNumber") String vehicleNumber, @Param("excludeIds") ArrayList<Long> excludeIds, @Param("deleteFlag") Boolean deleteFlag);

    /**
     * 查询司机关联车辆
     *
     * @param driverId
     * @param deletedFlag
     * @return
     */
    List<VehicleEntity> selectByDriverRelate(@Param("driverId") Long driverId, @Param("deletedFlag") Boolean deletedFlag);

}
