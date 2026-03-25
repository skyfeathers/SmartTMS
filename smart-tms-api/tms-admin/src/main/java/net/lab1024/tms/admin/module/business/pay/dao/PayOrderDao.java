package net.lab1024.tms.admin.module.business.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.pay.domain.bo.PayOrderNumBO;
import net.lab1024.tms.admin.module.business.pay.domain.bo.PayOrderWaybillBO;
import net.lab1024.tms.admin.module.business.pay.domain.form.PayOrderQueryForm;
import net.lab1024.tms.admin.module.business.pay.domain.vo.PayOrderAmountStatisticVO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeePayOrderCostDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.CustomerServiceDayStatisticQueryForm;
import net.lab1024.tms.common.module.business.pay.domain.PayOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 3:06 下午
 */
@Mapper
@Component
public interface PayOrderDao extends BaseMapper<PayOrderEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param payOrderQueryForm
     * @return
     */
    List<PayOrderEntity> query(Page page, @Param("queryForm") PayOrderQueryForm payOrderQueryForm);

    /**
     * 统计付款单的应付、已付合计
     *
     * @param payOrderQueryForm
     * @return
     */
    PayOrderAmountStatisticVO queryAmountStatistic(@Param("queryForm") PayOrderQueryForm payOrderQueryForm);


    /**
     * 根据运单查询付款单
     *
     * @param waybillId
     * @return
     */
    List<PayOrderEntity> queryByWaybillId(@Param("waybillId") Long waybillId, @Param("payOrderStatusList") List<Object> payOrderStatusList);

    /**
     * 根据运单查询付款单
     *
     * @param waybillIdList
     * @return
     */
    List<PayOrderEntity> queryByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList, @Param("payOrderStatusList") List<Object> payOrderStatusList);

    /**
     * 根据运单查询付款单数量
     *
     * @param waybillIdList
     * @return
     */
    List<PayOrderNumBO> selectPayOrderNumByWaybillId(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("payOrderStatusList") List<Object> payOrderStatusList);


    /**
     * 根据运单和付款单状态查询
     *
     * @param waybillIdList
     * @param payOrderStatusList
     * @return
     */
    List<Long> selectWayBillByWaybillIdAndStatus(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("payOrderStatusList") List<Object> payOrderStatusList);

    /**
     * 根据运单和付款单状态查询
     *
     * @param waybillIdList
     * @param payOrderStatusList
     * @param excludePayOrderId
     * @return
     */
    List<Long> selectWayBillByWaybillIdAndStatusAndExcludePayOrderId(@Param("waybillIdList") Collection<Long> waybillIdList, @Param("payOrderStatusList") List<Object> payOrderStatusList, @Param("excludePayOrderId") Long excludePayOrderId);

    /**
     * 根据付款单状态查询
     *
     * @param payOrderIdList
     * @param payOrderStatusList
     * @return
     */
    List<Long> selectWayBillByPayOrderIdAndStatus(@Param("payOrderIdList") Collection<Long> payOrderIdList, @Param("payOrderStatusList") List<Object> payOrderStatusList);


    /**
     * 根据付款单查询运单id
     *
     * @param payOrderIdList
     * @return
     */
    List<PayOrderWaybillBO> selectWayBillByPayOrderId(@Param("payOrderIdList") Collection<Long> payOrderIdList);

    List<PayOrderEntity> selectByWaybillIdAndFleetDriverId(@Param("waybillId") Long waybillId, @Param("fleetId") Integer fleetId, @Param("driverId") Long driverId, @Param("exPayOrderStatus") Integer exPayOrderStatus);

    List<PayOrderEntity> selectByWaybillId(@Param("waybillId") Long waybillId, @Param("exPayOrderStatus") Integer exPayOrderStatus);

    List<PayOrderEntity> selectByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList, @Param("exPayOrderStatus") Integer exPayOrderStatus);


    PayOrderEntity selectByWaybillIdAndPayType(@Param("waybillId") Long waybillId, @Param("payOrderType") Integer payOrderType, @Param("exPayOrderStatus") Integer exPayOrderStatus);

    Long selectIdByNumber(@Param("payOrderNumber") String payOrderNumber);


    /**
     * 客服日报表查询付款单
     *
     * @param queryForm
     * @return
     */
    List<EmployeePayOrderCostDTO> selectByCustomerService(@Param("queryForm") CustomerServiceDayStatisticQueryForm queryForm);

}