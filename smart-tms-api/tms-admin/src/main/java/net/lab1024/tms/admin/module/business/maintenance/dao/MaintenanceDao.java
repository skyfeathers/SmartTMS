package net.lab1024.tms.admin.module.business.maintenance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceQueryForm;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceVO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MaintenanceDao extends BaseMapper<MaintenanceEntity> {

    /**
     * 保养分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<MaintenanceVO> queryByPage(Page page, @Param("queryForm") MaintenanceQueryForm queryForm);

    /**
     * 删除保养记录
     *
     * @param maintenanceId
     * @param deletedFlag
     */
    void updateDeletedFlag(@Param("maintenanceId") Long maintenanceId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据车辆ID获取最后一条保养信息
     *
     * @param vehicleIdList
     * @param deletedFlag
     * @return
     */
    List<Long> selectByVehicleIds(@Param("vehicleIdList") List<Long> vehicleIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据自有车统计金额
     *
     * @param queryForm
     * @param moduleType
     * @return
     */
    List<CarCostVehicleMonthAmountDTO> sumByModuleIdListAndType(@Param("queryForm") CarCostMonthStatisticQueryForm queryForm, @Param("moduleType") Integer moduleType);

}