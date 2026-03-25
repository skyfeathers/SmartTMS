package net.lab1024.tms.admin.module.business.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderAmountSumDTO;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderShipperAmountDTO;
import net.lab1024.tms.admin.module.business.receive.domain.dto.WaybillOrderAmountSumDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto.ShipperReceiveOrderDTO;
import net.lab1024.tms.admin.module.business.waybill.domain.dto.WaybillCostAmountUpdateBO;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderWaybillEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 应收帐款的费用核销明细
 *
 * @author lidoudou
 * @date 2022/8/2 下午5:39
 */
@Mapper
@Component
public interface ReceiveOrderWaybillDao extends BaseMapper<ReceiveOrderWaybillEntity> {

    /**
     * 根据应收帐款查询运单id
     *
     * @param receiveOrderId
     * @return
     */
    List<Long> selectWaybillIdByReceiveOrderId(@Param("receiveOrderId") Long receiveOrderId);

    /**
     * 统计应收金额合计
     *
     * @param receiveOrderIdList
     * @return
     */
    List<ReceiveOrderAmountSumDTO> sumVerificationAmount(@Param("receiveOrderIdList") List<Long> receiveOrderIdList);

    /**
     * 统计应收金额合计
     *
     * @param receiveOrderIdList
     * @return
     */
    List<WaybillOrderAmountSumDTO> sumAmountByWaybillId(@Param("receiveOrderIdList") List<Long> receiveOrderIdList);
    /**
     * 查询无需开票已核算额
     *
     * @param waybillIdList
     * @param makeInvoiceFlag
     * @param checkStatus
     * @return
     */
    List<ReceiveOrderShipperAmountDTO> selectNoNeedInvoiceAndCheckAmountByWaybillIds(@Param("waybillIdList") List<Long> waybillIdList, @Param("makeInvoiceFlag") Boolean makeInvoiceFlag, @Param("checkStatus") Integer checkStatus);

    /**
     * 根据运单查询已提交核算金额
     *
     * @param waybillIdList
     * @param excludeCheckStatus
     * @param excludeInvoiceStatus
     * @return
     */
    List<ShipperReceiveOrderDTO> selectAmountByWaybillIds(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("excludeCheckStatus") Integer excludeCheckStatus, @Param("excludeInvoiceStatus") Integer excludeInvoiceStatus);
}
