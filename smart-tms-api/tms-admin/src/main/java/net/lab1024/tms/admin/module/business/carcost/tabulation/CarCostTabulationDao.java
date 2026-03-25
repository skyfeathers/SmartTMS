package net.lab1024.tms.admin.module.business.carcost.tabulation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.carcost.tabulation.domain.CarCostTabulationQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.CarCostItemInfoVO;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
@Component
public interface CarCostTabulationDao extends BaseMapper<CarCostTabulationEntity> {

    /**
     * 更新金额
     *
     * @param moduleId
     * @param moduleType
     * @param amount
     */
    void updateAmount(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType, @Param("amount") BigDecimal amount);

    /**
     * 更新审核状态
     *
     * @param moduleId
     * @param moduleType
     * @param auditStatus
     * @param auditRemark
     */
    void updateAuditInfo(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType, @Param("auditStatus") Integer auditStatus, @Param("auditRemark") String auditRemark);

    /**
     * 更新金额和分类ID
     *
     * @param moduleId
     * @param moduleType
     * @param amount
     */
    void updateAmountAndCategory(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType, @Param("amount") BigDecimal amount, @Param("categoryId") Long categoryId);

    /**
     * 获取费用列表ID
     *
     * @param moduleId
     * @param moduleType
     */
    Long selectTabulationIdByModule(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType);


    CarCostTabulationEntity selectByModule(@Param("moduleId") Long moduleId, @Param("moduleType") Integer moduleType);
    /**
     * 自有车费用列表-简单列表
     *
     * @param limitNum
     * @return
     */
    List<CarCostTabulationSimpleVO> selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId, @Param("limitNum") Integer limitNum);

    /**
     * 自有车费用列表-列表
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<CarCostTabulationSimpleVO> selectByPage(Page page, @Param("queryForm") CarCostTabulationQueryForm queryForm);

    List<CarCostTabulationSimpleVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    List<CarCostTabulationEntity> selectByWaybillIdAndAuditStatus(@Param("waybillId") Long waybillId, @Param("auditStatus") Integer auditStatus);
    /**
     * 查询运单待审核费用数量
     *
     * @param waybillId
     * @param auditStatus
     * @return
     */
    Integer selectWaitAuditCount(@Param("waybillId") Long waybillId, @Param("auditStatus") Integer auditStatus);

    /**
     * 根据运单以及费用类型查询费用
     *
     * @param waybillIdList
     * @param moduleTypeList
     * @param auditStatus
     * @return
     */
    List<CarCostItemInfoVO> selectCarCostItem(@Param("waybillIdList") List<Long> waybillIdList, @Param("moduleTypeList") List<Integer> moduleTypeList, @Param("auditStatus") Integer auditStatus);

    /**
     *
     *
     * @param tabulationId
     * @param waybillId
     */
    void updateWaybillIdAndVehicleId(@Param("tabulationId") Long tabulationId, @Param("waybillId") Long waybillId, @Param("vehicleId") Long vehicleId);

    /**
     * 更新运单ID
     *
     * @param tabulationId
     * @param waybillId
     */
    void updateWaybillId(@Param("tabulationId") Long tabulationId, @Param("waybillId") Long waybillId);

    List<Long> selectVehicleIdByTime(@Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime);
}
