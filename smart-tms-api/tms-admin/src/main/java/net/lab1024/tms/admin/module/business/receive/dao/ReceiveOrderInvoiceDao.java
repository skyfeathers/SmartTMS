package net.lab1024.tms.admin.module.business.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderShipperAmountDTO;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderInvoiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 应收帐款关联的开票信息
 *
 * @author lidoudou
 * @date 2022/8/15 下午5:09
 */
@Mapper
@Component
public interface ReceiveOrderInvoiceDao extends BaseMapper<ReceiveOrderInvoiceEntity> {

    /**
     * 根据应收ID查询开票记录
     *
     * @param receiveOrderId
     * @return
     */
    List<ReceiveOrderInvoiceEntity> selectByReceiveOrderId(@Param("receiveOrderId") Long receiveOrderId);

    /**
     * 根据应收ID查询开票记录
     *
     * @param receiveOrderIdList
     * @return
     */
    List<ReceiveOrderInvoiceEntity> selectByReceiveOrderIdList(@Param("receiveOrderIdList") Collection<Long> receiveOrderIdList);

    /**
     * 根据收款单批量更新  开票状态、开票人
     *
     * @param receiveOrderId
     * @param invoiceStatus
     * @param invoiceUserId
     */
    void batchUpdateInvoiceStatusByReceiveOrderId(@Param("receiveOrderId") Long receiveOrderId, @Param("invoiceStatus") Integer invoiceStatus,
                                                  @Param("invoiceUserId") Long invoiceUserId, @Param("remark") String remark);

    /**
     * 根据运单ID、开票状态统计开票金额
     *
     * @param waybillIdList
     * @param makeInvoiceFlag
     * @param invoiceStatus
     * @return
     */
    List<ReceiveOrderShipperAmountDTO> sumInvoiceAmountByBusinessDateAndShipper(@Param("waybillIdList") List<Long> waybillIdList, @Param("makeInvoiceFlag") Boolean makeInvoiceFlag, @Param("invoiceStatus") Integer invoiceStatus, @Param("excludeCheckStatus") Integer excludeCheckStatus);

    /**
     * 查询已开票未核销金额
     *
     * @param waybillIdList
     * @param verificationFlag
     * @param invoiceStatus
     * @return
     */
    List<ReceiveOrderShipperAmountDTO> selectNoVerificationAmount(@Param("waybillIdList") List<Long> waybillIdList, @Param("verificationFlag") Boolean verificationFlag, @Param("invoiceStatus") Integer invoiceStatus);
}
