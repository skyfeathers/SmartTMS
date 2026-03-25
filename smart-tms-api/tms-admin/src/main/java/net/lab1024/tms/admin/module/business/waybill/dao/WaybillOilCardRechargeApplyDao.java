package net.lab1024.tms.admin.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.oilcard.domain.dto.OilCardAmountDTO;
import net.lab1024.tms.admin.module.business.waybill.domain.dto.WaybillOilCardRechargeAmountDTO;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillOilCardRechargeApplyVO;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillOilCardRechargeApplyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/20 4:49 下午
 */
@Mapper
@Component
public interface WaybillOilCardRechargeApplyDao extends BaseMapper<WaybillOilCardRechargeApplyEntity> {

    /**
     * 查询充值申请
     * @param waybillIdList
     * @return
     */
    List<WaybillOilCardRechargeApplyVO> selectByWaybillId(@Param("waybillIdList") List<Long> waybillIdList);

    /**
     * 查询运单油卡提交金额
     * @param waybillIdList
     * @param auditStatusList
     * @param rechargeFlag 可为空，为空查询运单全部金额
     * @param disabledFlag
     * @return
     */
    List<WaybillOilCardRechargeAmountDTO> selectWaybillOilCardRechargeAmount(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatusList") List<Integer> auditStatusList, @Param("rechargeFlag") Boolean rechargeFlag, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 根据运单ID以及审核状态查询有效单据
     *
     * @param waybillId
     * @param auditStatusList
     * @param disabledFlag
     * @return
     */
    List<WaybillOilCardRechargeApplyEntity> selectValidByWaybillId(@Param("waybillId") Long waybillId, @Param("auditStatusList") List<Integer> auditStatusList, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 查询运单油卡已提交充值的总金额
     *
     * @param waybillIdList
     * @param auditStatusList
     * @param disabledFlag
     * @return
     */
    List<WaybillOilCardRechargeAmountDTO> selectWaybillOilCardSubmitAmount(@Param("waybillIdList") List<Long> waybillIdList, @Param("auditStatusList") List<Integer> auditStatusList, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 统计油卡已审核通过未充值的金额
     *
     * @param oilCardIdList
     * @param auditStatus
     * @param rechargeFlag
     * @param disabledFlag
     * @return
     */
    List<OilCardAmountDTO> selectAmountByOilCardList(@Param("oilCardIdList") List<Long> oilCardIdList, @Param("auditStatus") Integer auditStatus, @Param("rechargeFlag") Boolean rechargeFlag, @Param("disabledFlag") Boolean disabledFlag);
}