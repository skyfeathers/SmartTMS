package net.lab1024.tms.admin.module.business.carcost.ureapay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.CarCostFlowExportVO;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostUreaPayEntity;
import net.lab1024.tms.admin.module.business.carcost.ureapay.domain.CarCostUreaPayVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface CarCostUreaPayDao extends BaseMapper<CarCostUreaPayEntity> {

    /**
     * 根据运单ID获取尿素支出列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostUreaPayVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 计算运单中油卡已审核通过的尿素支出
     *
     * @param waybillId
     * @param oilCardId
     * @param payMode
     * @param auditStatus
     * @return
     */
    BigDecimal selectAmount(@Param("waybillId") Long waybillId, @Param("oilCardId") Long oilCardId, @Param("payMode") Integer payMode, @Param("auditStatus") Integer auditStatus);

    /**
     * 尿素支出详情
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
    List<CarCostUreaPayEntity> selectByWaybillIdListAndAuditStatus(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatus") Integer auditStatus);

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
     * @param ureaPayId
     * @param waybillId
     */
    void updateWaybillId(@Param("ureaPayId") Long ureaPayId, @Param("waybillId") Long waybillId);

    /**
     * 根据审核列表ID查询费用详情
     *
     * @param tabulationIdList
     * @return
     */
    List<CarCostTabulationVO> selectDetailByTabulationIdList(@Param("tabulationIdList") List<Long> tabulationIdList);

}
