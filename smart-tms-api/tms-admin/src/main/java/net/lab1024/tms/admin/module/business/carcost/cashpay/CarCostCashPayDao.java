package net.lab1024.tms.admin.module.business.carcost.cashpay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.CarCostFlowExportVO;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.admin.module.business.carcost.cashpay.domain.CarCostCashPayVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface CarCostCashPayDao extends BaseMapper<CarCostCashPayEntity> {

    /**
     * 根据运单ID获取现金支出列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostCashPayVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 根据运单获取现金支出金额
     *
     * @param waybillId
     * @param auditStatus
     * @return
     */
    BigDecimal selectAmount(@Param("waybillId") Long waybillId, @Param("auditStatus") Integer auditStatus);

    /**
     * 现金支出详情
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
     * 根据运单ID获取运单下的现金收入
     *
     * @param waybillIdList
     * @return
     */
    List<CarCostCashPayEntity> selectByWaybillIdListAndAuditStatus(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatus") Integer auditStatus);

    /**
     * 查询月统计
     *
     * @param queryForm
     * @return
     */
    List<CarCostVehicleMonthAmountDTO> sumMonthAmountByVehicle(@Param("queryForm") CarCostMonthStatisticQueryForm queryForm);

    /**
     * 更新运单ID
     *
     * @param cashPayId
     * @param waybillId
     */
    void updateWaybillId(@Param("cashPayId") Long cashPayId, @Param("waybillId") Long waybillId);

    /**
     * 根据审核列表ID查询费用详情
     *
     * @param tabulationIdList
     * @return
     */
    List<CarCostTabulationVO> selectDetailByTabulationIdList(@Param("tabulationIdList") List<Long> tabulationIdList);

}