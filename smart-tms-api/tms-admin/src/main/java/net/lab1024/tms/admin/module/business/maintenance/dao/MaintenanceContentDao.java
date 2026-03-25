package net.lab1024.tms.admin.module.business.maintenance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MaintenanceContentDao extends BaseMapper<MaintenanceContentEntity> {

    /**
     * 根据保养表ID获取保养内容
     *
     * @param maintenanceIdList
     * @return
     */
    List<MaintenanceContentEntity> selectByMaintenanceIds(@Param("maintenanceIdList") List<Long> maintenanceIdList);

    /**
     * 根据保养表ID删除保养内容
     *
     * @param maintenanceId
     */
    void deleteByMaintenanceId(@Param("maintenanceId") Long maintenanceId);
}
