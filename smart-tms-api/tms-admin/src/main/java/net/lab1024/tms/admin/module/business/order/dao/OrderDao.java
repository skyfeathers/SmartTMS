package net.lab1024.tms.admin.module.business.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderQueryForm;
import net.lab1024.tms.admin.module.business.order.domain.vo.OrderVO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeeScheduleOrderDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.CustomerServiceDayStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.ScheduleDayStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.vo.EmployeeCustomerServiceOrderDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto.ShipperOrderCountDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperDayStatisticForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperOrderQueryForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo.ShipperCountMonthVO;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperSimpleVO;
import net.lab1024.tms.admin.module.system.datascope.strategy.OrderDataScopePowerStrategy;
import net.lab1024.tms.common.module.business.order.domain.OrderEntity;
import net.lab1024.tms.common.module.business.order.domain.OrderGoodsEntity;
import net.lab1024.tms.common.module.system.datascope.DataScope;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeWhereInTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 订单基础信息
 *
 * @author lidoudou
 * @date 2022/7/13 下午2:43
 */
@Mapper
@Component
public interface OrderDao extends BaseMapper<OrderEntity> {

    Long selectIdByNumber(@Param("orderNo") String orderNo);
    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    @DataScope(joinSqlImplClazz = OrderDataScopePowerStrategy.class, whereInType = DataScopeWhereInTypeEnum.CUSTOM_STRATEGY, dataScopeType = DataScopeTypeEnum.ORDER)
    List<OrderVO> queryByPage(Page page, @Param("queryForm") OrderQueryForm queryForm);

    /**
     * 批量查询
     * @param orderIdList
     * @return
     */
    List<OrderVO> selectByIdList( @Param("orderIdList")Collection<Long> orderIdList);

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    OrderVO getOrder(@Param("orderId") Long orderId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据订单ID列表查询
     *
     * @param orderIdList
     * @return
     */
    List<OrderEntity> selectByOrderIdList(@Param("orderIdList") List<Long> orderIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 客户下单量分页
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ShipperSimpleVO> queryShipperByPage(Page page, @Param("queryForm") ShipperOrderQueryForm queryForm);

    /**
     * 根据时间以及货主查询每月下单量
     *
     * @param shipperIdList
     * @return
     */
    List<ShipperCountMonthVO> countByShipperAndYear(@Param("shipperIdList") List<Long> shipperIdList, @Param("queryForm") ShipperOrderQueryForm queryForm);

    /**
     * 根据时间以及货主查询每月下单量-合计
     *
     * @param queryForm
     * @param shipperIdList
     * @return
     */
    List<ShipperCountMonthVO> totalByShipperAndYear(@Param("queryForm") ShipperOrderQueryForm queryForm, @Param("shipperIdList") List<Long> shipperIdList);

    /**
     * 客服日报表查询订单
     *
     * @param queryForm
     * @return
     */
    List<EmployeeCustomerServiceOrderDTO> selectByCustomerService(@Param("queryForm") CustomerServiceDayStatisticQueryForm queryForm);

    /**
     * 根据调度查询订单
     *
     * @param queryForm
     * @return
     */
    List<EmployeeScheduleOrderDTO> selectBySchedule(@Param("queryForm") ScheduleDayStatisticQueryForm queryForm);

    /**
     * 根据货主查询取消订单数
     *
     * @param queryForm
     * @return
     */
    List<ShipperOrderCountDTO> selectCancelByShipper(@Param("queryForm") ShipperDayStatisticForm queryForm);

    /**
     * 订单数量
     * @param enterpriseId
     * @param excludeStatus
     * @return
     */
    Integer countOrder(@Param("enterpriseId") Long enterpriseId, @Param("excludeStatus") Integer excludeStatus);
}