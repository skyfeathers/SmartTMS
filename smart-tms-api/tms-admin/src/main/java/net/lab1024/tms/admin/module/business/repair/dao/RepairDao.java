package net.lab1024.tms.admin.module.business.repair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.repair.domain.RepairQueryForm;
import net.lab1024.tms.admin.module.business.repair.domain.RepairVO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.common.module.business.repair.entity.RepairEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/***
 * 车辆维修Dao
 *
 * @author lidoudou
 * @date 2022/6/24 下午5:48
 */
@Mapper
@Component
public interface RepairDao extends BaseMapper<RepairEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<RepairVO> queryByPage(Page page, @Param("queryForm") RepairQueryForm queryForm);


    /**
     * 逻辑删除 更新删除标识
     *
     * @param repairId
     * @param deletedFlag
     * @return
     */
    Integer updateDeletedFlag(@Param("repairId") Long repairId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除 更新删除标识
     *
     * @param moduleIdList
     * @param moduleType
     * @param deletedFlag
     * @return
     */
    Integer updateDeletedFlagByModuleIdAndType(@Param("moduleIdList") List<Long> moduleIdList, @Param("moduleType") Integer moduleType, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据模块ID删除
     *
     * @param moduleId
     * @return
     */
    Integer deleteByModuleIdAndType(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType);

    /**
     * 根据自有车统计金额
     *
     * @param queryForm
     * @param moduleType
     * @return
     */
    List<CarCostVehicleMonthAmountDTO> sumByModuleIdListAndType(@Param("queryForm") CarCostMonthStatisticQueryForm queryForm, @Param("moduleType") Integer moduleType);

    /**
     * 根据维修厂家查询维修ID
     *
     * @param repairPlantId
     * @param deletedFlag
     * @return
     */
    List<Long> selectByRepairPlantId(@Param("repairPlantId") Long repairPlantId, @Param("deletedFlag") Boolean deletedFlag);

}