package net.lab1024.tms.admin.module.business.carcost.basicinfo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoQueryForm;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoVO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthBasicInfoDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostBasicInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CarCostBasicInfoDao extends BaseMapper<CarCostBasicInfoEntity> {

    /**
     * 根据运单ID查询自有车基本信息
     *
     * @param waybillId
     * @return
     */
    CarCostBasicInfoVO selectByWaybillId(@Param("waybillId") Long waybillId);

    CarCostBasicInfoEntity selectByWaybillIdAndConfirmFlag(@Param("waybillId") Long waybillId, @Param("confirmFlag") Boolean confirmFlag);

    /**
     * 更新确认标识
     *
     * @param basicInfoId
     * @param confirmFlag
     */
    void updateConfirmFlag(@Param("basicInfoId") Long basicInfoId, @Param("confirmFlag") Boolean confirmFlag);

    /**
     * 根据运单获取确认状态
     *
     * @param waybillId
     * @return
     */
    Boolean selectConfirmFlagByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 根据运单IDList获取基本信息
     *
     * @param waybillIdList
     * @return
     */
    List<CarCostBasicInfoVO> selectByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList);

    /**
     * 统计自有车信息
     *
     * @param queryForm
     * @return
     */
    List<CarCostVehicleMonthBasicInfoDTO> sumCarCostAmountByVehicle(@Param("queryForm") CarCostMonthStatisticQueryForm queryForm);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CarCostBasicInfoVO> selectByPage(Page page, @Param("queryForm") CarCostBasicInfoQueryForm queryForm);

}