package net.lab1024.tms.admin.module.business.waybill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.tms.admin.module.business.driver.domain.dto.DriverWaybillCountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.CarCostWaybillMonthAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostDayStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.CarCostFlowExportVO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeeCustomerServiceWaybillDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeeSalesWaybillDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeeScheduleWaybillDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.WaybillEmployeeAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.*;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto.ShipperOrderWaybillDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperDayStatisticForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperOrderQueryForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo.ShipperCountMonthVO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo.ShipperDayStatisticVO;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto.WaybillReceiveCostAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto.WaybillShipperAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto.WaybillShipperCountDTO;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.form.CustomerWaybillCountQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.form.ShipperProfitPageQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo.*;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperSimpleVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.dto.VehicleWaybillCountDTO;
import net.lab1024.tms.admin.module.business.waybill.domain.form.WaybillQueryForm;
import net.lab1024.tms.admin.module.business.waybill.domain.form.WaybillUpdateForm;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillStatisticAmountVO;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.WaybillVO;
import net.lab1024.tms.admin.module.system.datascope.strategy.WaybillDataScopePowerStrategy;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.system.datascope.DataScope;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeWhereInTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:18 下午
 */
@Mapper
@Component
public interface WaybillDao extends BaseMapper<WaybillEntity> {


    /**
     * 修改结算类型
     *
     * @param updateForm
     */
    void updateBasic(@Param("form") WaybillUpdateForm updateForm);

    /**
     * 更新运单状态
     *
     * @param waybillId
     * @param waybillStatus
     */
    void updateWaybillStatus(@Param("waybillId") Long waybillId, @Param("waybillStatus") Integer waybillStatus);

    /**
     * 更新审核状态
     *
     * @param waybillId
     * @param auditStatus
     */
    void updateAuditStatus(@Param("waybillId") Long waybillId, @Param("auditStatus") Integer auditStatus);

    /**
     * 更新流程实例id
     *
     * @param waybillId
     * @param flowInstanceId
     */
    void updateFlowInstanceId(@Param("waybillId") Long waybillId, @Param("flowInstanceId") Long flowInstanceId);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    @DataScope(joinSqlImplClazz = WaybillDataScopePowerStrategy.class, whereInType = DataScopeWhereInTypeEnum.CUSTOM_STRATEGY, dataScopeType = DataScopeTypeEnum.WAYBILL)
    List<WaybillVO> query(Page page, @Param("queryForm") WaybillQueryForm queryForm);


    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    @DataScope(joinSqlImplClazz = WaybillDataScopePowerStrategy.class, whereInType = DataScopeWhereInTypeEnum.CUSTOM_STRATEGY, dataScopeType = DataScopeTypeEnum.WAYBILL)
    WaybillStatisticAmountVO statisticAmount(@Param("queryForm") WaybillQueryForm queryForm);

    /**
     * 根据条件查询总数量
     *
     * @param queryForm
     * @return
     */
    @DataScope(joinSqlImplClazz = WaybillDataScopePowerStrategy.class, whereInType = DataScopeWhereInTypeEnum.CUSTOM_STRATEGY, dataScopeType = DataScopeTypeEnum.WAYBILL)
    Integer countWaybill(@Param("queryForm") WaybillQueryForm queryForm);


    List<WaybillVO> selectByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList);

    /**
     * 详情
     *
     * @param waybillId
     * @return
     */
    WaybillVO detail(@Param("waybillId") Long waybillId);

    /**
     * 根据订单id查询运单
     *
     * @param orderId
     * @param statusList
     * @return
     */
    List<WaybillVO> queryByOrderId(@Param("orderId") Long orderId, @Param("statusList") List<Object> statusList);

    /**
     * 根据订单id查询运单
     *
     * @param orderId
     * @param statusList
     * @return
     */
    List<Long> selectIdByOrderId(@Param("orderId") Long orderId, @Param("statusList") List<Object> statusList);

    /**
     * 根据订单id查询运单
     *
     * @param orderIdList
     * @param statusList
     * @return
     */
    List<WaybillVO> queryByOrderIdList(@Param("orderIdList") List<Long> orderIdList, @Param("statusList") List<Object> statusList);


    /**
     * 根据时间以及货主查询每月下单量
     *
     * @param shipperIdList
     * @param queryForm
     * @return
     */
    List<ShipperCountMonthVO> countByShipperAndYear(@Param("shipperIdList") List<Long> shipperIdList, @Param("queryForm") ShipperOrderQueryForm queryForm);

    /**
     * 客户下单量分页
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ShipperSimpleVO> queryShipperByPage(Page page, @Param("queryForm") ShipperOrderQueryForm queryForm);

    /**
     * 根据时间以及货主查询每月下单量-合计
     *
     * @param excludeStatus
     * @param year
     * @param shipperIdList
     * @return
     */
    List<ShipperCountMonthVO> totalByShipperAndYear(@Param("year") Integer year, @Param("excludeStatus") Integer excludeStatus, @Param("shipperIdList") List<Long> shipperIdList);

    /**
     * @param driverId
     * @param statusList
     * @param enterpriseId
     * @return
     */
    Integer countByDriverIdStatusAndEnterpriseId(@Param("driverId") Long driverId, @Param("statusList") List<Integer> statusList, @Param("excludeEnterpriseId") Long enterpriseId);

    /**
     * 合计应收金额
     *
     * @param waybillIdList
     * @param excludeStatus
     * @return
     */
    BigDecimal sumReceiveAmount(@Param("waybillIdList") List<Long> waybillIdList, @Param("excludeStatus") Integer excludeStatus);

    /**
     * 根据订单ID查询
     *
     * @param orderId
     * @return
     */
    List<WaybillEntity> selectByOrderId(@Param("orderId") Long orderId, @Param("excludeStatus") Integer excludeStatus);

    /**
     * 查询销售完成金额
     *
     * @param queryForm
     * @return
     */
    List<WaybillEmployeeAmountDTO> queryReceiveAmountBySales(@Param("queryForm") EmployeeSalesQueryForm queryForm);

    /**
     * 查询客服金额
     *
     * @param queryForm
     * @return
     */
    List<WaybillEmployeeAmountDTO> queryAmountByCustomerService(@Param("queryForm") EmployeeSalesQueryForm queryForm);

    /**
     * 查询应付金额
     *
     * @param queryForm
     * @return
     */
    List<WaybillAmountVO> queryPayableAmountBySettleMode(@Param("queryForm") OilCardRateQueryForm queryForm);

    /**
     * 统计车辆运单数量
     *
     * @param queryForm
     * @return
     */
    List<WaybillVehicleCountVO> queryWaybillVehicleCount(Page page, @Param("queryForm") WaybillVehicleCountQueryForm queryForm);

    /**
     * 根据车辆以及业务时间查询运单
     *
     * @param vehicleIdList
     * @param businessStartDate
     * @param businessEndDate
     * @return
     */
    List<WaybillEntity> queryByVehicleCount(@Param("vehicleIdList") List<Long> vehicleIdList, @Param("businessStartDate") LocalDate businessStartDate, @Param("businessEndDate") LocalDate businessEndDate, @Param("excludeStatus") Integer excludeStatus);

    /**
     * 根据车辆ID查询是否存在有效运单
     *
     * @param vehicleIdList
     * @param excludeStatus
     * @return
     */
    List<VehicleWaybillCountDTO> selectCountByVehicleIdList(@Param("vehicleIdList") List<Long> vehicleIdList, @Param("excludeStatus") Integer excludeStatus);

    /**
     * 根据司机ID查询是否存在有效运单
     *
     * @param driverIdList
     * @param excludeStatus
     * @return
     */
    List<DriverWaybillCountDTO> selectCountByDriverIdList(@Param("driverIdList") List<Long> driverIdList, @Param("excludeStatus") Integer excludeStatus);

    /**
     * 更新运单的自有车关联状态
     *
     * @param waybillIdList
     * @param carCostFlag
     */
    void updateCarCostFlag(@Param("waybillIdList") List<Long> waybillIdList, @Param("carCostFlag") Boolean carCostFlag);


    /**
     * 查询销售应收统计
     * @param queryForm
     * @return
     */
    List<ReceiveAmountStatisticVO> queryReceiveAmountBySale(@Param("queryForm") SalesReceiveAmountStatisticQueryForm queryForm);

    /**
     * 查询客服日推进表数据列表
     *
     * @param queryForm
     * @return
     */
    List<WaybillCountVO> selectWaybillCountExportVO(@Param("queryForm") CustomerWaybillCountQueryForm queryForm);

    /**
     * 根据车辆ID查询运单
     *
     * @param queryForm
     * @return
     */
    List<WaybillEntity> selectByVehicleIdList(@Param("queryForm") CarCostDayStatisticQueryForm queryForm);

    /**
     *
     *
     * @param waybillIdList
     * @return
     */
    List<CarCostFlowExportVO> selectCostFlowByWaybillIdList(@Param("waybillIdList") List<Long> waybillIdList);

    /**
     * 根据自有车统计金额
     *
     * @param queryForm
     * @return
     */
    List<CarCostWaybillMonthAmountDTO> sumAmountByVehicleId(@Param("queryForm") CarCostMonthStatisticQueryForm queryForm);

    /**
     * 根据订单ID和订单类型查询
     *
     * @param orderId
     * @param orderType
     * @return
     */
    List<WaybillEntity> selectByOrderIdAndOrderType(@Param("orderId") Long orderId, @Param("orderType") Integer orderType);

    /**
     * 根据员工查询运单
     *
     * @param queryForm
     * @return
     */
    List<EmployeeSalesWaybillDTO> selectBySale(@Param("queryForm") SalesDayStatisticQueryForm queryForm);

    /**
     * 查询员工运单应收金额
     *
     * @param queryForm
     * @return
     */
    List<WaybillEmployeeAmountDTO> selectReceiveAmountBySale(@Param("queryForm") SalesDayStatisticQueryForm queryForm);

    /**
     * 客服日报表查询运单
     *
     * @param queryForm
     * @return
     */
    List<EmployeeCustomerServiceWaybillDTO> selectByCustomerService(@Param("queryForm") CustomerServiceDayStatisticQueryForm queryForm);

    /**
     * 调度日报表查询运单
     *
     * @param queryForm
     * @return
     */
    List<EmployeeScheduleWaybillDTO> selectBySchedule(@Param("queryForm") ScheduleDayStatisticQueryForm queryForm);

    /**
     * 根据货主查询运单
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ShipperDayStatisticVO> selectPageByShipper(Page page, @Param("queryForm") ShipperDayStatisticForm queryForm);

    /**
     * 根据货主查询运单ID
     *
     * @param queryForm
     * @return
     */
    List<ShipperOrderWaybillDTO> selectWaybillIdByShipper(@Param("queryForm") ShipperDayStatisticForm queryForm);

    /**
     * 根据货主查询运单
     *
     * @param queryForm
     * @return
     */
    List<ShipperDayStatisticVO> selectByShipper(@Param("queryForm") ShipperDayStatisticForm queryForm);

    /**
     * 分页查询货主ID
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ShipperProfitVO> queryShipperIdByProfit(Page page, @Param("queryForm") ShipperProfitPageQueryForm queryForm);

    /**
     * 根据客户核算毛利表的查询条件 查询运单ID
     *
     * @param queryForm
     * @return
     */
    List<Long> queryWaybillIdByShipperProfit(@Param("queryForm") ShipperProfitPageQueryForm queryForm);

    /**
     * 查询货主利润
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillShipperAmountDTO> queryShipperProfit(@Param("waybillIdList") List<Long> waybillIdList);


    /**
     * 查询货主数量
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillShipperCountDTO> queryShipperCount(@Param("waybillIdList") List<Long> waybillIdList);

    /**
     * 根据应收类型、结算方式统计货主应收金额
     *
     * @param waybillIdList
     * @return
     */
    List<WaybillReceiveCostAmountDTO> queryShipperReceiveAmount(@Param("waybillIdList") List<Long> waybillIdList, @Param("costItemType") Integer costItemType);

    Long selectIdByNumber(@Param("waybillNumber") String waybillNumber);

    /**
     * 汇总运单数量
     * @param enterpriseId
     * @param excludeStatus
     * @return
     */
    Integer countWaybillByExcludeStatus(@Param("enterpriseId") Long enterpriseId, @Param("excludeStatus") Integer excludeStatus);
}
