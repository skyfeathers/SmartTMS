package net.lab1024.tms.admin.module.business.reportform.waybill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.material.businesstype.constant.TripTypeEnum;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderCostDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderWaybillDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderInvoiceDao;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderShipperAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.WaybillEmployeeAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.EmployeeSalesQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.OilCardRateQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.SalesReceiveAmountStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.WaybillVehicleCountQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto.WaybillShipperAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto.WaybillShipperCountDTO;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.form.CustomerWaybillCountQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.form.ShipperProfitPageQueryForm;
import net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo.*;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillOilCardRechargeApplyDao;
import net.lab1024.tms.admin.module.business.waybill.domain.dto.WaybillOilCardRechargeAmountDTO;
import net.lab1024.tms.admin.module.business.waybill.domain.form.WaybillQueryForm;
import net.lab1024.tms.admin.module.business.waybill.domain.vo.*;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillDetailService;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.*;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 运单相关的报表
 *
 * @author lidoudou
 * @date 2022/9/25 上午10:38
 */
@Service
public class WaybillReportService {
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private WaybillDetailService waybillDetailService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private WaybillOilCardRechargeApplyDao oilCardRechargeApplyDao;
    @Autowired
    private ReceiveOrderInvoiceDao receiveOrderInvoiceDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private PayOrderCostDao payOrderCostDao;
    @Autowired
    private ReceiveOrderWaybillDao receiveOrderWaybillDao;

    public List<WaybillProfitVO> queryList(WaybillQueryForm queryForm, Long employeeId) {
        if((null == queryForm.getStartTime() || null == queryForm.getEndTime()) &&
                (null == queryForm.getBusinessStartDate() || null == queryForm.getBusinessEndDate())){
            throw new BusinessException("创建时间、业务时间不能同时为空");
        }

        Boolean noCreateTimeSearchFlag = queryForm.getStartTime() == null || queryForm.getEndTime() == null;
        int maxCreateTimeSearchDays = 0;
        if (!noCreateTimeSearchFlag) {
            maxCreateTimeSearchDays = (int) ChronoUnit.DAYS.between(queryForm.getStartTime(), queryForm.getEndTime()) + 1;
        }
        if (maxCreateTimeSearchDays > 32) {
            throw new BusinessException("创建时间错误,最大支持一个月的查询");
        }

        Boolean noBusinessTimeSearchFlag = queryForm.getBusinessStartDate() == null || queryForm.getBusinessEndDate() == null;
        int maxBusinessTimeSearchDays = 0;
        if (!noBusinessTimeSearchFlag) {
            maxBusinessTimeSearchDays = (int) ChronoUnit.MONTHS.between(queryForm.getBusinessStartDate(), queryForm.getBusinessEndDate()) + 1;
        }
        if (maxBusinessTimeSearchDays > 1) {
            throw new BusinessException("业务时间错误,最大支持一个月的查询");
        }

        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());

        List<WaybillVO> list = waybillDao.query(null, queryForm);
        List<WaybillCostDetailVO> costCategoryAmountVOList = waybillDetailService.getWaybillCostByVoList(list);

        // 合计
        if (CollectionUtils.isNotEmpty(costCategoryAmountVOList)) {
            WaybillCostDetailVO costCategoryAmountVO = this.amountStatistic(costCategoryAmountVOList);
            costCategoryAmountVOList.add(costCategoryAmountVO);
        }
        List<WaybillProfitVO> result = Lists.newArrayList();

        for (int i = 0; i < costCategoryAmountVOList.size(); i++) {
            WaybillCostDetailVO item = costCategoryAmountVOList.get(i);

            WaybillProfitVO waybillProfitVO = SmartBeanUtil.copy(item, WaybillProfitVO.class);
            List<WaybillProfitCostVO> costList = SmartBeanUtil.copyList(item.getCostList(), WaybillProfitCostVO.class);
            waybillProfitVO.setCostList(costList.stream().filter(cost -> SmartBigDecimalUtil.isGreaterThan(cost.getCostAmount(), BigDecimal.ZERO)).collect(Collectors.toList()));
            List<WaybillProfitReceiveCostVO> receiveCostList = SmartBeanUtil.copyList(item.getReceiveCostList(), WaybillProfitReceiveCostVO.class);
            waybillProfitVO.setReceiveCostList(receiveCostList.stream().filter(cost -> SmartBigDecimalUtil.isGreaterThan(cost.getCostAmount(), BigDecimal.ZERO)).collect(Collectors.toList()));
            result.add(waybillProfitVO);
        }
        return result;
    }

    public WaybillCostDetailVO amountStatistic(List<WaybillCostDetailVO> costCategoryAmountVOList) {
        WaybillCostDetailVO waybillProfitVO = new WaybillCostDetailVO();
        waybillProfitVO.setShortName("合计");
        if (CollectionUtils.isEmpty(costCategoryAmountVOList)) {
            waybillProfitVO.setProfitAmount(BigDecimal.ZERO);
            waybillProfitVO.setTaxAmount(BigDecimal.ZERO);
            waybillProfitVO.setReceiveAmount(BigDecimal.ZERO);
            waybillProfitVO.setPayableAmount(BigDecimal.ZERO);
            waybillProfitVO.setSalaryAmount(BigDecimal.ZERO);
            waybillProfitVO.setCarCostAmount(BigDecimal.ZERO);
            return waybillProfitVO;
        }
        waybillProfitVO.setProfitAmount(costCategoryAmountVOList.stream().map(e -> e.getProfitAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        waybillProfitVO.setTaxAmount(costCategoryAmountVOList.stream().map(e -> e.getTaxAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        waybillProfitVO.setReceiveAmount(costCategoryAmountVOList.stream().map(e -> e.getReceiveAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        waybillProfitVO.setPayableAmount(costCategoryAmountVOList.stream().map(e -> e.getPayableAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        waybillProfitVO.setSalaryAmount(costCategoryAmountVOList.stream().map(e -> e.getSalaryAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        waybillProfitVO.setCarCostAmount(costCategoryAmountVOList.stream().map(e -> e.getCarCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        return waybillProfitVO;
    }


    public List<WaybillCostDetailExcelVO> query4Export(WaybillQueryForm exportForm, Long employeeId) {
        Boolean noCreateTimeSearchFlag = exportForm.getStartTime() == null || exportForm.getEndTime() == null;
        int maxSearchDays = 0;
        if (!noCreateTimeSearchFlag) {
            maxSearchDays = (int) ChronoUnit.DAYS.between(exportForm.getStartTime(), exportForm.getEndTime()) + 1;
        }
        if (maxSearchDays > 32) {
            throw new BusinessException("时间错误,最大支持一个月的导出");
        }
        List<WaybillProfitVO> list = queryList(exportForm, employeeId);
        List<WaybillCostDetailExcelVO> excelVOList = SmartBeanUtil.copyList(list, WaybillCostDetailExcelVO.class);
        for (int i = 0; i < excelVOList.size(); i++) {
            WaybillCostDetailExcelVO excelVO = excelVOList.get(i);
            excelVO.setIndex(i + 1);
            excelVO.setSettleModeDesc(SmartBaseEnumUtil.getEnumDescByValue(excelVO.getSettleMode(), WaybillSettleModeEnum.class));
            excelVO.setSettleTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(excelVO.getSettleType(), WaybillSettleTypeEnum.class));
            excelVO.setTransportModeName(SmartBaseEnumUtil.getEnumDescByValue(excelVO.getTransportMode(), WaybillTransportModeEnum.class));
        }
        return excelVOList;
    }

    /**
     * 统计客户毛利
     *
     * @param queryForm
     * @return
     */
    public PageResult<ShipperProfitVO> pageShipperProfit(ShipperProfitPageQueryForm queryForm) {
        // 根据查询条件分页的货主
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ShipperProfitVO> shipperProfitVOList = queryShipperProfitList(queryForm, page);
        PageResult<ShipperProfitVO> pageResult = SmartPageUtil.convert2PageResult(page, shipperProfitVOList);
        return pageResult;
    }

    public ResponseDTO<ShipperProfitSummaryVO> queryShipperProfitSummary(ShipperProfitPageQueryForm queryForm) {
        List<ShipperProfitVO> list = this.queryShipperProfitList(queryForm, null);

        ShipperProfitSummaryVO summaryVO = new ShipperProfitSummaryVO();
        summaryVO.setReceiveAmount(BigDecimal.ZERO);
        summaryVO.setWaitInvoiceAmount(BigDecimal.ZERO);
        summaryVO.setWaybillCount(0);
        summaryVO.setPayableAmount(BigDecimal.ZERO);
        summaryVO.setSalaryAmount(BigDecimal.ZERO);
        summaryVO.setCarCostAmount(BigDecimal.ZERO);
        summaryVO.setTaxAmount(BigDecimal.ZERO);
        summaryVO.setProfitAmount(BigDecimal.ZERO);
        summaryVO.setProfitRate(BigDecimal.ZERO);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseDTO.ok(summaryVO);
        }
        summaryVO.setReceiveAmount(list.stream().map(e -> e.getReceiveAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryVO.setWaitInvoiceAmount(list.stream().map(e -> e.getWaitInvoiceAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryVO.setWaybillCount(list.stream().mapToInt(e -> e.getWaybillCount()).sum());
        summaryVO.setPayableAmount(list.stream().map(e -> e.getPayableAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryVO.setSalaryAmount(list.stream().map(e -> e.getSalaryAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryVO.setCarCostAmount(list.stream().map(e -> e.getCarCostAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryVO.setTaxAmount(list.stream().map(e -> e.getTaxAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryVO.setProfitAmount(list.stream().map(e -> e.getProfitAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal profitRate = SmartBigDecimalUtil.Amount.divide(SmartBigDecimalUtil.Amount.multiply(summaryVO.getProfitAmount(), BigDecimal.valueOf(100)), summaryVO.getReceiveAmount());
        summaryVO.setProfitRate(profitRate);
        return ResponseDTO.ok(summaryVO);
    }

    public List<ShipperProfitVO> queryShipperProfitList(ShipperProfitPageQueryForm queryForm, Page page) {
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<ShipperProfitVO> shipperProfitVOList = waybillDao.queryShipperIdByProfit(page, queryForm);
        if (CollectionUtils.isNotEmpty(shipperProfitVOList)) {
            // 查询公司名称
            Set<Long> enterpriseIdSet = shipperProfitVOList.stream().map(ShipperProfitVO::getEnterpriseId).collect(Collectors.toSet());
            List<EnterpriseEntity> enterpriseList = enterpriseDao.selectBatchIds(enterpriseIdSet);
            Map<Long, String> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

            Set<Long> shipperIdSet = shipperProfitVOList.stream().map(ShipperProfitVO::getShipperId).collect(Collectors.toSet());
            // 根据查询条件，查询运单ID列表。根据运单ID列表统计金额信息
            List<Long> waybillIdList = waybillDao.queryWaybillIdByShipperProfit(queryForm);

            // 查询运单应付、应收
            List<WaybillShipperAmountDTO> shipperProfitList = waybillDao.queryShipperProfit(waybillIdList);
            String keyFormat = "%s_%s_%s";
            Map<String, WaybillShipperAmountDTO> shipperProfitMap =
                    shipperProfitList.stream().collect(Collectors.toMap(e -> String.format(keyFormat, e.getEnterpriseId(), e.getShipperId(), e.getSettleMode()), Function.identity()));

            // 查询已开票金额
            List<ReceiveOrderShipperAmountDTO> shipperInvoiceList = receiveOrderInvoiceDao.sumInvoiceAmountByBusinessDateAndShipper(waybillIdList, Boolean.TRUE, InvoiceStatusEnum.INVOICE.getValue(),CheckStatusEnum.CANCEL.getValue());
            // 查询无需开票已核算金额，也包含在开票金额里
            List<ReceiveOrderShipperAmountDTO> shipperCheckList = receiveOrderWaybillDao.selectNoNeedInvoiceAndCheckAmountByWaybillIds(waybillIdList, Boolean.FALSE, CheckStatusEnum.CHECK.getValue());
            shipperInvoiceList.addAll(shipperCheckList);
            Map<Long, List<BigDecimal>> shipperInvoiceMap = shipperInvoiceList.stream().collect(Collectors.groupingBy(ReceiveOrderShipperAmountDTO::getShipperId, Collectors.mapping(ReceiveOrderShipperAmountDTO::getAmount, Collectors.toList())));

            // 查询运单数量
            List<WaybillShipperCountDTO> shipperCountList = waybillDao.queryShipperCount(waybillIdList);
            Map<String, Integer> shipperCountMap =
                    shipperCountList.stream().collect(Collectors.toMap(e -> String.format(keyFormat, e.getEnterpriseId(), e.getShipperId(), e.getSettleMode()), WaybillShipperCountDTO::getCount));

            // 货主名称
            List<ShipperEntity> shipperList = shipperDao.selectBatchIds(shipperIdSet);
            Map<Long, String> shipperNameMap = shipperList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getConsignor));

            // 查询货主对应的负责人
            List<ShipperPrincipalDTO> shipperPrincipalList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdSet, PrincipalTypeEnum.MANAGER.getValue());
            Map<Long, List<String>> shipperManagerNameMap = shipperPrincipalList.stream().collect(Collectors.groupingBy(ShipperPrincipalDTO::getShipperId, Collectors.mapping(ShipperPrincipalDTO::getEmployeeName, Collectors.toList())));

            for (ShipperProfitVO shipperProfitVO : shipperProfitVOList) {
                Long shipperId = shipperProfitVO.getShipperId();
                Long enterpriseId = shipperProfitVO.getEnterpriseId();

                shipperProfitVO.setConsignor(shipperNameMap.getOrDefault(shipperId, StringConst.EMPTY));
                shipperProfitVO.setEnterpriseName(enterpriseNameMap.getOrDefault(enterpriseId, StringConst.EMPTY));
                List<String> managerNameList = shipperManagerNameMap.getOrDefault(shipperId, Lists.newArrayList());
                shipperProfitVO.setManagerName(StringUtils.join(managerNameList, CommonConst.SEPARATOR));
                // 设置开票金额
                BigDecimal invoiceAmount = shipperInvoiceMap.getOrDefault(shipperId, Lists.newArrayList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                shipperProfitVO.setInvoiceAmount(invoiceAmount);

                BigDecimal waitInvoiceAmount = SmartBigDecimalUtil.Amount.subtract(shipperProfitVO.getReceiveAmount(), invoiceAmount);
                shipperProfitVO.setWaitInvoiceAmount(waitInvoiceAmount);

                // 外调车
                String arriveKey = String.format(keyFormat, enterpriseId, shipperId, WaybillSettleModeEnum.ARRIVE_PAY.getValue());
                WaybillShipperAmountDTO arriveAmountDTO = shipperProfitMap.get(arriveKey);
                shipperProfitVO.setArrivePayReceiveAmount(BigDecimal.ZERO);
                shipperProfitVO.setArrivePayAmount(BigDecimal.ZERO);
                if (null != arriveAmountDTO) {
                    shipperProfitVO.setArrivePayReceiveAmount(arriveAmountDTO.getReceiveAmount());
                    shipperProfitVO.setArrivePayAmount(arriveAmountDTO.getPayAmount());
                }
                shipperProfitVO.setArrivePayWaybillCount(shipperCountMap.getOrDefault(arriveKey, NumberConst.ZERO));
                // 挂靠车
                String monthKey = String.format(keyFormat, enterpriseId, shipperId, WaybillSettleModeEnum.MONTH_SETTLE.getValue());
                WaybillShipperAmountDTO monthAmountDTO = shipperProfitMap.get(monthKey);
                shipperProfitVO.setMonthReceiveAmount(BigDecimal.ZERO);
                shipperProfitVO.setMonthPayAmount(BigDecimal.ZERO);
                if (null != monthAmountDTO) {
                    shipperProfitVO.setMonthReceiveAmount(monthAmountDTO.getReceiveAmount());
                    shipperProfitVO.setMonthPayAmount(monthAmountDTO.getPayAmount());
                }
                shipperProfitVO.setMonthWaybillCount(shipperCountMap.getOrDefault(monthKey, NumberConst.ZERO));
                // 自有车
                String selfVehicleKey = String.format(keyFormat, enterpriseId, shipperId, WaybillSettleModeEnum.SELF_VEHICLE_SETTLE.getValue());
                WaybillShipperAmountDTO selfVehicleAmountDTO = shipperProfitMap.get(selfVehicleKey);
                shipperProfitVO.setSelfVehicleReceiveAmount(BigDecimal.ZERO);
                shipperProfitVO.setSelfVehiclePayAmount(BigDecimal.ZERO);
                if (null != selfVehicleAmountDTO) {
                    shipperProfitVO.setSelfVehicleReceiveAmount(selfVehicleAmountDTO.getReceiveAmount());
                    shipperProfitVO.setSelfVehiclePayAmount(selfVehicleAmountDTO.getPayAmount());
                }
                shipperProfitVO.setSelfVehicleWaybillCount(shipperCountMap.getOrDefault(selfVehicleKey, NumberConst.ZERO));

                BigDecimal profitRate = SmartBigDecimalUtil.Amount.divide(SmartBigDecimalUtil.Amount.multiply(shipperProfitVO.getProfitAmount(), BigDecimal.valueOf(100)), shipperProfitVO.getReceiveAmount());
                shipperProfitVO.setProfitRate(profitRate);
            }
        }
        return shipperProfitVOList;
    }

    /**
     * 根据客服统计收入明细
     *
     * @param queryForm
     * @return
     */
    public List<WaybillEmployeeAmountStatisticVO> queryAmountByCustomerService(EmployeeSalesQueryForm queryForm) {
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<Long> employeeIdList = Lists.newArrayList();
        if (null != queryForm.getEmployeeId()) {
            employeeIdList = Lists.newArrayList(queryForm.getEmployeeId());
        }
        List<EmployeeVO> employeeList = employeeService.queryEmployeeList(queryForm.getEnterpriseId(), employeeIdList, SystemConfigKeyEnum.CUSTOMER_SERVICE_ROLE_CODE);
        if (CollectionUtils.isEmpty(employeeList)) {
            return Lists.newArrayList();
        }
        Set<Long> employeeIdSet = employeeList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toSet());
        queryForm.setEmployeeIdList(employeeIdSet);
        List<WaybillEmployeeAmountDTO> amountStatisticList = waybillDao.queryAmountByCustomerService(queryForm);
        return buildAmountVO(employeeList, amountStatisticList);
    }

    /**
     * 根据销售查询收入金额明细
     *
     * @param queryForm
     * @return
     */
    public List<WaybillEmployeeAmountStatisticVO> queryAmountBySale(EmployeeSalesQueryForm queryForm) {
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<Long> employeeIdList = Lists.newArrayList();
        if (null != queryForm.getEmployeeId()) {
            employeeIdList = Lists.newArrayList(queryForm.getEmployeeId());
        }
        List<EmployeeVO> employeeList = employeeService.queryEmployeeList(queryForm.getEnterpriseId(), employeeIdList, SystemConfigKeyEnum.SALES_ROLE_CODE);
        if (CollectionUtils.isEmpty(employeeList)) {
            return Lists.newArrayList();
        }
        Set<Long> employeeIdSet = employeeList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toSet());
        queryForm.setEmployeeIdList(employeeIdSet);
        List<WaybillEmployeeAmountDTO> amountStatisticList = waybillDao.queryReceiveAmountBySales(queryForm);
        return buildAmountVO(employeeList, amountStatisticList);
    }

    private List<WaybillEmployeeAmountStatisticVO> buildAmountVO(List<EmployeeVO> employeeList, List<WaybillEmployeeAmountDTO> amountStatisticList) {
        Map<Long, BigDecimal> receiveAmountMap = amountStatisticList.stream().collect(Collectors.toMap(WaybillEmployeeAmountDTO::getEmployeeId, WaybillEmployeeAmountDTO::getReceiveAmount));
        Map<Long, BigDecimal> payableAmountMap = amountStatisticList.stream().collect(Collectors.toMap(WaybillEmployeeAmountDTO::getEmployeeId, WaybillEmployeeAmountDTO::getPayableAmount));
        Map<Long, BigDecimal> salaryAmountMap = amountStatisticList.stream().collect(Collectors.toMap(WaybillEmployeeAmountDTO::getEmployeeId, WaybillEmployeeAmountDTO::getSalaryAmount));
        Map<Long, BigDecimal> carCostAmountMap = amountStatisticList.stream().collect(Collectors.toMap(WaybillEmployeeAmountDTO::getEmployeeId, WaybillEmployeeAmountDTO::getCarCostAmount));
        Map<Long, BigDecimal> taxAmountMap = amountStatisticList.stream().collect(Collectors.toMap(WaybillEmployeeAmountDTO::getEmployeeId, WaybillEmployeeAmountDTO::getTaxAmount));

        List<WaybillEmployeeAmountStatisticVO> employeeAmountStatisticList = employeeList.stream().map(e -> {
            Long employeeId = e.getEmployeeId();

            WaybillEmployeeAmountStatisticVO employeeAmountStatistic = SmartBeanUtil.copy(e, WaybillEmployeeAmountStatisticVO.class);
            employeeAmountStatistic.setEmployeeName(e.getActualName());
            employeeAmountStatistic.setReceiveAmount(receiveAmountMap.getOrDefault(employeeId, BigDecimal.ZERO));
            employeeAmountStatistic.setPayableAmount(payableAmountMap.getOrDefault(employeeId, BigDecimal.ZERO));
            employeeAmountStatistic.setSalaryAmount(salaryAmountMap.getOrDefault(employeeId, BigDecimal.ZERO));
            employeeAmountStatistic.setCarCostAmount(carCostAmountMap.getOrDefault(employeeId, BigDecimal.ZERO));
            employeeAmountStatistic.setTaxAmount(taxAmountMap.getOrDefault(employeeId, BigDecimal.ZERO));

            // 计算利润
            BigDecimal profitAmount = SmartBigDecimalUtil.Amount.subtract(employeeAmountStatistic.getReceiveAmount(), employeeAmountStatistic.getPayableAmount());
            profitAmount = SmartBigDecimalUtil.Amount.subtract(profitAmount, employeeAmountStatistic.getTaxAmount());

            employeeAmountStatistic.setProfitAmount(profitAmount);
            employeeAmountStatistic.setProfitRate(BigDecimal.ZERO);
            if (employeeAmountStatistic.getReceiveAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal profitRate = SmartBigDecimalUtil.Amount.divide(profitAmount.multiply(BigDecimal.valueOf(100)), employeeAmountStatistic.getReceiveAmount());
                employeeAmountStatistic.setProfitRate(profitRate);
            }
            return employeeAmountStatistic;
        }).collect(Collectors.toList());
        return employeeAmountStatisticList;
    }

    /**
     * 查询外调车油卡比例
     *
     * @param queryForm
     * @return
     */
    public List<EmployeeOilCardRateVO> queryOilCard(OilCardRateQueryForm queryForm) {
        queryForm.setSettleMode(WaybillSettleModeEnum.ARRIVE_PAY.getValue());
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<WaybillAmountVO> amountList = waybillDao.queryPayableAmountBySettleMode(queryForm);
        if (CollectionUtils.isEmpty(amountList)) {
            return Lists.newArrayList();
        }

        // 查询员工姓名
        Set<Long> employeeIdList = amountList.stream().map(WaybillAmountVO::getEmployeeId).collect(Collectors.toSet());
        List<EmployeeEntity> employeeList = employeeDao.selectBatchIds(employeeIdList);
        Map<Long, String> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));

        // 查询油卡充值金额
        List<Long> waybillIdList = amountList.stream().map(WaybillAmountVO::getWaybillId).collect(Collectors.toList());
        List<Integer> auditStatusList = Lists.newArrayList(FlowAuditStatusEnum.PASS.getValue(), FlowAuditStatusEnum.WAIT.getValue(), FlowAuditStatusEnum.PROCESSING.getValue());
        List<WaybillOilCardRechargeAmountDTO> oilCardRechargeAmountList = oilCardRechargeApplyDao.selectWaybillOilCardRechargeAmount(waybillIdList, auditStatusList, Boolean.TRUE, Boolean.FALSE);

        Map<Long, List<WaybillAmountVO>> amountMap = amountList.stream().collect(Collectors.groupingBy(WaybillAmountVO::getEmployeeId));

        List<EmployeeOilCardRateVO> oilCardRateList = Lists.newArrayList();
        for (Map.Entry<Long, List<WaybillAmountVO>> entry : amountMap.entrySet()) {
            Long employeeId = entry.getKey();

            EmployeeOilCardRateVO rateVO = new EmployeeOilCardRateVO();
            rateVO.setEmployeeName(employeeNameMap.getOrDefault(employeeId, StringConst.EMPTY));
            //计算应付金额
            List<WaybillAmountVO> waybillAmountList = entry.getValue();
            BigDecimal payableAmount = waybillAmountList.stream().map(WaybillAmountVO::getPayableAmount).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            rateVO.setPayableAmount(payableAmount);
            // 计算油卡金额
            List<Long> employeeWaybillIdList = waybillAmountList.stream().map(WaybillAmountVO::getWaybillId).collect(Collectors.toList());
            List<BigDecimal> oilCardAmountList = oilCardRechargeAmountList.stream().filter(e -> employeeWaybillIdList.contains(e.getWaybillId())).map(WaybillOilCardRechargeAmountDTO::getAmount).collect(Collectors.toList());
            BigDecimal oilCardTotalAmount = oilCardAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            rateVO.setOilCardAmount(oilCardTotalAmount);
            // 计算油卡比例
            BigDecimal oilCardRate = SmartBigDecimalUtil.Amount.divide(oilCardTotalAmount.multiply(BigDecimal.valueOf(100)), payableAmount);
            rateVO.setOilCardRate(oilCardRate);
            oilCardRateList.add(rateVO);
        }
        return oilCardRateList;
    }

    public ResponseDTO<List<ReceiveAmountStatisticVO>> queryReceiveAmountBySale(SalesReceiveAmountStatisticQueryForm queryForm) {
        queryForm.setWaybillStatus(WaybillStatusEnum.CANCEL.getValue());
        queryForm.setOrderStatus(OrderStatusEnum.CANCEL.getValue());
        queryForm.setDeletedFlag(Boolean.FALSE);

        List<ReceiveAmountStatisticVO> receiveAmountStatisticVOS = waybillDao.queryReceiveAmountBySale(queryForm);

        //将应收金额统计出来
        BigDecimal totalReceiveAmount = receiveAmountStatisticVOS.stream()
                .map(ReceiveAmountStatisticVO::getReceiveAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 计算并设置receiveAmountRate
        for (ReceiveAmountStatisticVO employeeStats : receiveAmountStatisticVOS) {
            BigDecimal employeeReceiveAmount = employeeStats.getReceiveAmount();
            BigDecimal rate = BigDecimal.ZERO;

            if (totalReceiveAmount.compareTo(BigDecimal.ZERO) != 0) {
                rate = employeeReceiveAmount.divide(totalReceiveAmount, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }
            employeeStats.setReceiveAmountRate(rate);
        }
        return ResponseDTO.ok(receiveAmountStatisticVOS);
    }

    /**
     * 统计车辆运单数量
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<WaybillVehicleCountVO>> queryWaybillVehicleCount(WaybillVehicleCountQueryForm queryForm) {
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<WaybillVehicleCountVO> waybillVehicleCountVOList = queryWaybillVehicle(page, queryForm);
        PageResult<WaybillVehicleCountVO> pageResult = SmartPageUtil.convert2PageResult(page, waybillVehicleCountVOList);
        return ResponseDTO.ok(pageResult);
    }

    private List<WaybillVehicleCountVO> queryWaybillVehicle(Page page, WaybillVehicleCountQueryForm queryForm) {
        List<WaybillVehicleCountVO> waybillVehicleCountVOList = waybillDao.queryWaybillVehicleCount(page, queryForm);
        if (CollectionUtils.isNotEmpty(waybillVehicleCountVOList)) {
            List<Long> vehicleIdList = waybillVehicleCountVOList.stream().map(WaybillVehicleCountVO::getVehicleId).collect(Collectors.toList());

            List<WaybillEntity> waybillList = waybillDao.queryByVehicleCount(vehicleIdList, queryForm.getBusinessStartDate(), queryForm.getBusinessEndDate(), queryForm.getExcludeStatus());

            Set<Long> driverIdSet = waybillList.stream().map(WaybillEntity::getDriverId).collect(Collectors.toSet());
            List<DriverEntity> driverEntityList = driverDao.selectBatchIds(driverIdSet);
            Map<Long, String> driverNameMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getDriverName));

            Map<Long, Set<Long>> vehicleDriverMap = waybillList.stream().collect(Collectors.groupingBy(WaybillEntity::getVehicleId, Collectors.mapping(WaybillEntity::getDriverId, Collectors.toSet())));

            // 查询车辆车牌号
            List<VehicleEntity> vehicleList = vehicleDao.selectBatchIds(vehicleIdList);
            Map<Long, String> vehicleNumberMap = vehicleList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleId, VehicleEntity::getVehicleNumber));
            waybillVehicleCountVOList.forEach(item -> {
                item.setVehicleNumber(vehicleNumberMap.get(item.getVehicleId()));

                Set<Long> driverIdList = vehicleDriverMap.get(item.getVehicleId());
                List<String> driverNameList = driverIdList.stream().map(e -> {
                    return driverNameMap.get(e);
                }).collect(Collectors.toList());
                item.setDriverNameList(driverNameList);
            });
        }
        return waybillVehicleCountVOList;
    }

    public List<WaybillVehicleCountExportVO> exportWaybillVehicleCount(WaybillVehicleCountQueryForm exportForm) {
        exportForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<WaybillVehicleCountVO> waybillVehicleCountVOList = queryWaybillVehicle(null, exportForm);

        List<WaybillVehicleCountExportVO> exportList = Lists.newArrayList();
        for (int i = 1; i <= waybillVehicleCountVOList.size(); i++) {
            WaybillVehicleCountVO waybillVehicleCountVO = waybillVehicleCountVOList.get(i - 1);
            WaybillVehicleCountExportVO exportVO = SmartBeanUtil.copy(waybillVehicleCountVO, WaybillVehicleCountExportVO.class);
            exportVO.setDriverName(SmartStringUtil.join(CommonConst.SEPARATOR, waybillVehicleCountVO.getDriverNameList()));
            exportVO.setRank(i);
            exportList.add(exportVO);
        }
        return exportList;
    }

    /**
     * 客服日推进表导出
     *
     * @param queryForm
     * @return
     */
    public List<WaybillCountVO> queryCustomerWaybillCountList(CustomerWaybillCountQueryForm queryForm) {
        queryForm.setExcludeOrderStatus(OrderStatusEnum.CANCEL.getValue());
        queryForm.setExcludeWaybillStatus(WaybillStatusEnum.CANCEL.getValue());
        List<WaybillCountVO> exportVOList = waybillDao.selectWaybillCountExportVO(queryForm);
        this.buildNameAndDesc(exportVOList);
        return exportVOList;
    }

    /**
     * 设置客服名字和枚举类描述
     *
     * @param exportVOList
     */
    private void buildNameAndDesc(List<WaybillCountVO> exportVOList) {
        if (CollectionUtils.isEmpty(exportVOList)) {
            return;
        }
        exportVOList.stream().forEach(exportVO -> {
            exportVO.setTripTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(exportVO.getTripType(), TripTypeEnum.class));
        });
    }
}
