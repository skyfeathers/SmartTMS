package net.lab1024.tms.admin.module.business.carcost.oilcardreceive;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostVehicleMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardReceiveEntity;
import net.lab1024.tms.admin.module.business.carcost.oilcardreceive.domain.CarCostOilCardReceiveVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface CarCostOilCardReceiveDao extends BaseMapper<CarCostOilCardReceiveEntity> {

    /**
     * 根据运单ID获取油卡收入列表
     *
     * @param waybillId
     * @return
     */
    List<CarCostOilCardReceiveVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 获取运单和油卡审核通过的收入
     *
     * @param waybillId
     * @param oilCardId
     * @param auditStatus
     * @return
     */
    BigDecimal selectAmount(@Param("waybillId") Long waybillId, @Param("oilCardId") Long oilCardId, @Param("auditStatus") Integer auditStatus);

    /**
     * 油卡收入详情
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
    List<CarCostOilCardReceiveEntity> selectByWaybillIdListAndAuditStatus(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatus") Integer auditStatus);

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
     * @param oilCardReceiveId
     * @param waybillId
     */
    void updateWaybillId(@Param("oilCardReceiveId") Long oilCardReceiveId, @Param("waybillId") Long waybillId);

    /**
     * 根据审核列表ID查询费用详情
     *
     * @param tabulationIdList
     * @return
     */
    List<CarCostTabulationVO> selectDetailByTabulationIdList(@Param("tabulationIdList") List<Long> tabulationIdList);

}