package net.lab1024.tms.admin.module.business.carcost.cashreceive;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashReceiveEntity;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.domain.CarCostCashReceiveVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationDetailVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostTabulationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface CarCostCashReceiveDao extends BaseMapper<CarCostCashReceiveEntity> {

    /**
     * 根据运单ID获取运单下的现金收入
     *
     * @param waybillId
     * @return
     */
    List<CarCostCashReceiveVO> selectByWaybillId(@Param("waybillId") Long waybillId);

    /**
     * 根据运单ID获取现金收入之和
     *
     * @param waybillId
     * @param auditStatus
     * @return
     */
    BigDecimal selectAmount(@Param("waybillId") Long waybillId, @Param("auditStatus") Integer auditStatus);

    /**
     * 现金收入详情
     *
     * @param moduleId
     * @return
     */
    CarCostTabulationDetailVO selectDetail(@Param("moduleId") Long moduleId);

    /**
     * 根据运单ID获取运单下的现金收入
     *
     * @param waybillIdList
     * @return
     */
    List<CarCostCashReceiveEntity> selectByWaybillIdListAndAuditStatus(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatus") Integer auditStatus);

    /**
     * 修改运单ID
     *
     * @param cashReceiveId
     * @param waybillId
     */
    void updateWaybillId(@Param("cashReceiveId") Long cashReceiveId, @Param("waybillId") Long waybillId);

    /**
     * 根据审核列表ID查询费用详情
     *
     * @param tabulationIdList
     * @return
     */
    List<CarCostTabulationVO> selectDetailByTabulationIdList(@Param("tabulationIdList") List<Long> tabulationIdList);

}