package net.lab1024.tms.admin.module.business.fleet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.fleet.domain.form.FleetItemQueryForm;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.FleetDriverVO;
import net.lab1024.tms.admin.module.business.fleet.domain.vo.FleetVehicleVO;
import net.lab1024.tms.common.module.business.fleet.domain.FleetItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface FleetItemDao extends BaseMapper<FleetItemEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<FleetDriverVO> queryDriverByPage(Page page, @Param("queryForm") FleetItemQueryForm queryForm);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<FleetVehicleVO> queryVehicleByPage(Page page, @Param("queryForm") FleetItemQueryForm queryForm);

    /**
     * 根据类型以及ID查询关联项目
     *
     * @param fleetId
     * @param itemType
     * @return
     */
    List<Long> selectByFleetIdAndType(@Param("fleetId") Long fleetId, @Param("itemType") Integer itemType);

}
