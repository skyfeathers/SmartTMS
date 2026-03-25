package net.lab1024.tms.admin.module.business.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderAmountSumDTO;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderWaybillDTO;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveInvoiceQueryForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.ReceiveQueryForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.ShipperReceiveQueryForm;
import net.lab1024.tms.admin.module.business.receive.domain.form.WaitReceiveQueryForm;
import net.lab1024.tms.admin.module.business.receive.domain.vo.*;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperDayStatisticForm;
import net.lab1024.tms.common.module.business.receive.domain.ReceiveOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 应收帐款
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:25
 */
@Mapper
@Component
public interface ReceiveOrderDao extends BaseMapper<ReceiveOrderEntity> {


    /**
     * 根据运单查询
     *
     * @param waybillId
     * @param excludeCheckStatus
     * @return
     */
    List<ReceiveOrderEntity> selectByWaybillId(@Param("waybillId") Long waybillId, @Param("excludeCheckStatus") Integer excludeCheckStatus);

    /**
     * 根据运单查询
     *
     * @param waybillId
     * @param excludeCheckStatus
     * @return
     */
    List<ReceiveOrderEntity> selectValidByWaybillId(@Param("waybillId") Long waybillId, @Param("excludeCheckStatus") Integer excludeCheckStatus, @Param("excludeInvoiceStatus") Integer excludeInvoiceStatus);

    /**
     * 批量查询
     *
     * @param waybillIdList
     * @param excludeCheckStatus
     * @return
     */
    List<ReceiveOrderWaybillDTO> selectByWaybillIdList(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("excludeCheckStatus") Integer excludeCheckStatus);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
     List<ReceiveOrderVO> queryByPage(Page page, @Param("queryForm") ReceiveQueryForm queryForm);

    /**
     * 分页查询 申请开票列表
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ReceiveOrderInvoiceListVO> queryInvoiceByPage(Page page, @Param("queryForm") ReceiveInvoiceQueryForm queryForm);

    /**
     * 获取详情
     *
     * @param receiveOrderId
     * @return
     */
    ReceiveOrderDetailVO getDetail(@Param("receiveOrderId") Long receiveOrderId);

    /**
     * 查询待收款的订单列表
     *
     * @param queryForm
     * @return
     */
    List<ReceiveOrderAmountVO> queryWaitReceiveList(Page page, @Param("queryForm") WaitReceiveQueryForm queryForm);

    /**
     * 根据货主查询应收
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ReceiveOrderVO> queryPageByShipper(Page page, @Param("queryForm") ShipperReceiveQueryForm queryForm);

    /**
     * 根据运单ID查询业务日期
     *
     * @param waybillIdList
     * @param excludeCheckStatus
     * @return
     */
    List<WaybillBusinessDateVO> selectBusinessDateByWaybillIdList(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("excludeCheckStatus") Integer excludeCheckStatus);

    /**
     * 获取应收总额
     *
     * @param queryForm
     * @return
     */
    BigDecimal getReceivableTotalAmount(@Param("queryForm") ReceiveQueryForm queryForm);

    /**
     * 获取已销金额总额
     *
     * @param queryForm
     * @return
     */
    BigDecimal getVerificationTotalAmount(@Param("queryForm") ReceiveQueryForm queryForm);

    /**
     * 查询货主超过账期未核销金额
     *
     * @param queryForm
     * @return
     */
    List<ReceiveOrderAmountSumDTO> selectTotalAmountByAccountPeriodDate(@Param("queryForm") ShipperDayStatisticForm queryForm);

    Long selectIdByNumber(@Param("receiveOrderNumber") String receiveOrderNumber);
}
