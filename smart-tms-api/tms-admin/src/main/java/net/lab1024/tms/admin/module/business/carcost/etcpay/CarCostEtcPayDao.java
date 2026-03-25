package net.lab1024.tms.admin.module.business.carcost.etcpay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.carcost.etcpay.domain.CarCostEtcPayVO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.CarCostFlowExportVO;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface CarCostEtcPayDao extends BaseMapper<CarCostEtcPayEntity> {

    /**
     * 根据运单ID获取ETC支出列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostEtcPayVO> selectByWaybillId(Long waybillId);

    /**
     * 根据运单ID获取ETC支出金额
     *
     * @param waybillId
     * @param payMode
     * @param auditStatus
     * @return
     */
    BigDecimal selectAmount(@Param("waybillId") Long waybillId, @Param("payMode") Integer payMode, @Param("auditStatus") Integer auditStatus);

    /**
     * ETC支出详情
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
     * 根据运单ID列表以及审核状态查询
     *
     * @param waybillIdList
     * @param auditStatus
     * @return
     */
    List<CarCostEtcPayEntity> selectByWaybillIdListAndAuditStatus(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatus") Integer auditStatus);

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
     * @param etcPayId
     * @param waybillId
     */
    void updateWaybillId(@Param("etcPayId") Long etcPayId, @Param("waybillId") Long waybillId);

    /**
     * 根据审核列表ID查询费用详情
     *
     * @param tabulationIdList
     * @return
     */
    List<CarCostTabulationVO> selectDetailByTabulationIdList(@Param("tabulationIdList") List<Long> tabulationIdList);

}
