package net.lab1024.tms.admin.module.business.carcost.oilpay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.carcost.oilpay.domain.CarCostOilPayVO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthOilAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthOilConsumptionDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.CarCostFlowExportVO;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface CarCostOilPayDao extends BaseMapper<CarCostOilPayEntity> {

    /**
     * 根据运单ID查询油费记录
     *
     * @param waybillId
     * @return
     */
    List<CarCostOilPayVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 计算运单中油卡已审核通过的油费支出
     *
     * @param waybillId
     * @param oilCardId
     * @param payMode
     * @param auditStatus
     * @return
     */
    BigDecimal selectAmount(@Param("waybillId") Long waybillId, @Param("oilCardId") Long oilCardId, @Param("payMode") Integer payMode, @Param("auditStatus") Integer auditStatus);

    /**
     * 油费支出详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 获取费用流水内容
     *
     * @param waybillIdList
     * @param auditStatus
     * @return
     */
    List<CarCostFlowExportVO> selectCostFlowByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatus") Integer auditStatus);

    /**
     * 获取费用流水内容
     *
     * @param waybillIdList
     * @param auditStatus
     * @return
     */
    List<CarCostOilPayEntity> selectByWaybillIdListAndAuditStatus(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatus") Integer auditStatus);

    List<CarCostVehicleMonthOilAmountDTO> sumMonthAmountByVehicle(@Param("queryForm")CarCostMonthStatisticQueryForm queryForm);

    List<CarCostVehicleMonthOilConsumptionDTO> sumMonthOilConsumptionByVehicle(@Param("queryForm")CarCostMonthStatisticQueryForm queryForm);

    /**
     * 更新运单ID
     *
     * @param oilPayId
     * @param waybillId
     */
    void updateWaybillId(@Param("oilPayId") Long oilPayId, @Param("waybillId") Long waybillId);

    /**
     * 根据审核列表ID查询费用详情
     *
     * @param tabulationIdList
     * @return
     */
    List<CarCostTabulationVO> selectDetailByTabulationIdList(@Param("tabulationIdList") List<Long> tabulationIdList);

}
