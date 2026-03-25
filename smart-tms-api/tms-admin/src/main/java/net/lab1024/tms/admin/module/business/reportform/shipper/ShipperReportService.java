package net.lab1024.tms.admin.module.business.reportform.shipper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderWaybillDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderInvoiceDao;
import net.lab1024.tms.admin.module.business.receive.dao.ReceiveOrderVerificationDao;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderAmountSumDTO;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderShipperAmountDTO;
import net.lab1024.tms.admin.module.business.receive.domain.dto.ReceiveOrderVerificationAmountDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto.ShipperOrderCountDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto.ShipperOrderWaybillDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto.ShipperReceiveOrderDTO;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperAddQueryForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperDayStatisticForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.form.ShipperOrderQueryForm;
import net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo.*;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.vo.ShipperSimpleVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillCostDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.*;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceStatusEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 货主报表查询
 *
 * @author lidoudou
 * @date 2022/9/20 下午5:20
 */
@Service
public class ShipperReportService {

    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WaybillCostDao waybillCostDao;
    @Autowired
    private ReceiveOrderInvoiceDao receiveOrderInvoiceDao;
    @Autowired
    private ReceiveOrderWaybillDao receiveOrderWaybillDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private ReceiveOrderDao receiveOrderDao;
    @Autowired
    private ReceiveOrderVerificationDao receiveOrderVerificationDao;

    /**
     * 查询货主每月定单量
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ShipperOrderVO>> queryOrderByMonth(ShipperOrderQueryForm queryForm) {
        // 设置查询默认值
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setExcludeStatus(OrderStatusEnum.CANCEL.getValue());
        Integer year = queryForm.getYear();
        //货主
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ShipperSimpleVO> shipperList = orderDao.queryShipperByPage(page, queryForm);
        //每月数据
        List<Long> shipperIdList = shipperList.stream().map(ShipperSimpleVO::getShipperId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(shipperIdList)){
            shipperIdList.add(NumberConst.DEFAULT_PARENT_ID);
        }
        List<ShipperCountMonthVO> orderCountList = orderDao.countByShipperAndYear(shipperIdList, queryForm);

        List<ShipperOrderVO> shipperOrderList = buildShipperDataList(shipperList, orderCountList, year);
        PageResult<ShipperOrderVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, shipperOrderList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 查询货主每月定单量
     *
     * @param queryForm
     * @param employeeId
     * @return
     */
    public ResponseDTO<List<ShipperCountMonthVO>> queryOrderByMonthTotal(ShipperOrderQueryForm queryForm, Long employeeId) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setExcludeStatus(OrderStatusEnum.CANCEL.getValue());
        if (null == queryForm.getYear()) {
            queryForm.setYear(LocalDate.now().getYear());
        }
        List<ShipperSimpleVO> shipperList = orderDao.queryShipperByPage(null, queryForm);
        //每月数据
        List<Long> shipperIdList = shipperList.stream().map(ShipperSimpleVO::getShipperId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(shipperIdList)){
            shipperIdList.add(NumberConst.DEFAULT_PARENT_ID);
        }
        //每月数据
        List<ShipperCountMonthVO> orderCountTotalList = orderDao.totalByShipperAndYear(queryForm, shipperIdList);
        return ResponseDTO.ok(orderCountTotalList);
    }


    /**
     * 查询货主每月运单量
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ShipperOrderVO>> queryWaybillByMonth(ShipperOrderQueryForm queryForm) {

        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        if (null == queryForm.getYear()) {
            queryForm.setYear(LocalDate.now().getYear());
        }
        Integer year = queryForm.getYear();
        //货主
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ShipperSimpleVO> shipperList = waybillDao.queryShipperByPage(page, queryForm);
        //每月数据
        List<Long> shipperIdList = shipperList.stream().map(ShipperSimpleVO::getShipperId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(shipperIdList)) {
            shipperIdList.add(NumberConst.DEFAULT_PARENT_ID);
        }
        List<ShipperCountMonthVO> waybillCountList = waybillDao.countByShipperAndYear(shipperIdList, queryForm);

        List<ShipperOrderVO> shipperOrderList = buildShipperDataList(shipperList, waybillCountList, year);
        PageResult<ShipperOrderVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, shipperOrderList);
        return ResponseDTO.ok(pageResultDTO);
    }


    /**
     * 查询货主每月运单量
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<ShipperCountMonthVO>> queryWaybillByMonthTotal(ShipperOrderQueryForm queryForm) {
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        queryForm.setDeletedFlag(Boolean.FALSE);
        if (null == queryForm.getYear()) {
            queryForm.setYear(LocalDate.now().getYear());
        }
        Integer year = queryForm.getYear();
        List<ShipperSimpleVO> shipperList = waybillDao.queryShipperByPage(null, queryForm);
        //每月数据
        List<Long> shipperIdList = shipperList.stream().map(ShipperSimpleVO::getShipperId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(shipperIdList)) {
            shipperIdList.add(NumberConst.DEFAULT_PARENT_ID);
        }
        //每月数据
        List<ShipperCountMonthVO> orderCountTotalList = waybillDao.totalByShipperAndYear(year, WaybillStatusEnum.CANCEL.getValue(), shipperIdList);
        return ResponseDTO.ok(orderCountTotalList);
    }


    private List<ShipperOrderVO> buildShipperDataList(List<ShipperSimpleVO> shipperList, List<ShipperCountMonthVO> countList, Integer year) {
        if (CollectionUtils.isEmpty(shipperList)) {
            return Collections.emptyList();
        }
        List<ShipperOrderVO> shipperOrderList = new ArrayList<>(shipperList.size());

        Map<String, Integer> shipperOrderCountMap = countList.stream().collect(Collectors.toMap(e -> e.getShipperId() + "_" + e.getMonth(), ShipperCountMonthVO::getCount));
        List<String> monthList = getMonthList(year);
        shipperList.forEach(item -> {
            ShipperOrderVO shipperOrderVO = SmartBeanUtil.copy(item, ShipperOrderVO.class);
            Long shipperId = shipperOrderVO.getShipperId();
            // 设置月份
            List<ShipperCountMonthVO> orderCountMonthList = monthList.stream().map(month -> {
                String key = shipperId + "_" + month;
                ShipperCountMonthVO monthVO = new ShipperCountMonthVO();
                monthVO.setMonth(month);
                monthVO.setCount(shipperOrderCountMap.getOrDefault(key, NumberConst.ZERO));
                return monthVO;
            }).collect(Collectors.toList());
            shipperOrderVO.setMonthList(orderCountMonthList);
            shipperOrderList.add(shipperOrderVO);
        });
        return shipperOrderList;
    }

    /**
     * 返回当前年份的1-12月列表
     *
     * @param year
     * @return
     */
    private List<String> getMonthList(Integer year) {
        List<String> monthList = new ArrayList<>(12);
        LocalDate initDate = LocalDate.now().withYear(year);
        for (int i = 0; i < 12; i++) {
            monthList.add(initDate.withMonth(i + 1).format(SmartLocalDateUtil.FORMATTER_YM));
        }
        return monthList;
    }

    /**
     * 查询货主每月新增数量
     *
     * @param addQueryForm
     * @return
     */
    public ResponseDTO<List<ShipperCountMonthVO>> queryNewCountByMonth(ShipperAddQueryForm addQueryForm) {
        addQueryForm.setManagerType(PrincipalTypeEnum.MANAGER.getValue());
        List<ShipperCountMonthVO> shipperCountMonthVOList = shipperDao.countByMonth(addQueryForm, Boolean.FALSE);
        Map<String, Integer> monthCountMap = shipperCountMonthVOList.stream().collect(Collectors.toMap(ShipperCountMonthVO::getMonth, ShipperCountMonthVO::getCount));
        List<String> monthList = getMonthList(addQueryForm.getYear());
        List<ShipperCountMonthVO> result = monthList.stream().map(month -> {
            ShipperCountMonthVO shipperCountMonthVO = new ShipperCountMonthVO();
            shipperCountMonthVO.setMonth(month);
            shipperCountMonthVO.setCount(monthCountMap.getOrDefault(month, NumberConst.ZERO));
            return shipperCountMonthVO;
        }).collect(Collectors.toList());

        return ResponseDTO.ok(result);
    }

    /**
     * 订单量导出
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<ShipperOrderExcelVO>> orderCountExport(ShipperOrderQueryForm queryForm) {
        // 设置查询默认值
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setExcludeStatus(OrderStatusEnum.CANCEL.getValue());
        if (null == queryForm.getYear()) {
            queryForm.setYear(LocalDate.now().getYear());
        }
        Integer year = queryForm.getYear();
        //货主
        List<ShipperSimpleVO> shipperList = orderDao.queryShipperByPage(null, queryForm);
        //每月数据
        List<Long> shipperIdList = shipperList.stream().map(ShipperSimpleVO::getShipperId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(shipperIdList)){
            shipperIdList.add(NumberConst.DEFAULT_PARENT_ID);
        }
        List<ShipperCountMonthVO> orderCountList = orderDao.countByShipperAndYear(shipperIdList, queryForm);
        List<ShipperOrderVO> shipperOrderList = buildShipperDataList(shipperList, orderCountList, year);
        List<ShipperOrderExcelVO> excelVOList = this.buildExportExcelVO(shipperOrderList);
        return ResponseDTO.ok(excelVOList);
    }


    /**
     * 运单量导出
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<ShipperOrderExcelVO>> waybillCountExport(ShipperOrderQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        if (null == queryForm.getYear()) {
            queryForm.setYear(LocalDate.now().getYear());
        }
        Integer year = queryForm.getYear();
        //货主
        List<ShipperSimpleVO> shipperList = waybillDao.queryShipperByPage(null, queryForm);
        //每月数据
        List<Long> shipperIdList = shipperList.stream().map(ShipperSimpleVO::getShipperId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(shipperIdList)) {
            shipperIdList.add(NumberConst.DEFAULT_PARENT_ID);
        }
        List<ShipperCountMonthVO> waybillCountList = waybillDao.countByShipperAndYear(shipperIdList, queryForm);

        List<ShipperOrderVO> shipperOrderList = buildShipperDataList(shipperList, waybillCountList, year);
        List<ShipperOrderExcelVO> excelVOList = this.buildExportExcelVO(shipperOrderList);
        return ResponseDTO.ok(excelVOList);
    }

    private List<ShipperOrderExcelVO> buildExportExcelVO(List<ShipperOrderVO> shipperOrderList) {
        if (CollectionUtils.isEmpty(shipperOrderList)) {
            return Lists.newArrayList();
        }
        List<ShipperOrderExcelVO> resultList = Lists.newArrayList();
        for (ShipperOrderVO shipperOrderVO : shipperOrderList) {
            ShipperOrderExcelVO excelVO = new ShipperOrderExcelVO();
            excelVO.setShipperId(shipperOrderVO.getShipperId());
            excelVO.setConsignor(shipperOrderVO.getConsignor());
            excelVO.setShortName(shipperOrderVO.getShortName());

            List<ShipperCountMonthVO> monthList = shipperOrderVO.getMonthList();
            for (ShipperCountMonthVO countMonthVO : monthList) {
                String month = countMonthVO.getMonth();
                YearMonth yearMonth = SmartLocalDateUtil.parseYearMonth(month);
                int monthValue = yearMonth.getMonthValue();
                switch (monthValue) {
                    case 1:
                        excelVO.setJanuaryCount(countMonthVO.getCount());
                        break;
                    case 2:
                        excelVO.setFebruaryCount(countMonthVO.getCount());
                        break;
                    case 3:
                        excelVO.setMarchCount(countMonthVO.getCount());
                        break;
                    case 4:
                        excelVO.setAprilCount(countMonthVO.getCount());
                        break;
                    case 5:
                        excelVO.setMayCount(countMonthVO.getCount());
                        break;
                    case 6:
                        excelVO.setJuneCount(countMonthVO.getCount());
                        break;
                    case 7:
                        excelVO.setJulyCount(countMonthVO.getCount());
                        break;
                    case 8:
                        excelVO.setAugustCount(countMonthVO.getCount());
                        break;
                    case 9:
                        excelVO.setSeptemberCount(countMonthVO.getCount());
                        break;
                    case 10:
                        excelVO.setOctoberCount(countMonthVO.getCount());
                        break;
                    case 11:
                        excelVO.setNovemberCount(countMonthVO.getCount());
                        break;
                    case 12:
                        excelVO.setDecemberCount(countMonthVO.getCount());
                }
            }
            resultList.add(excelVO);
        }
        return resultList;
    }

    public static void main(String[] args) {
        YearMonth yearMonth = SmartLocalDateUtil.parseYearMonth("2023-01");
        System.out.println(yearMonth.getYear());
        System.out.println(yearMonth.getMonthValue());
        System.out.println(yearMonth.getMonth().getValue());
    }


    /**
     * 货主日报表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<ShipperDayStatisticVO>> queryShipperDayList(ShipperDayStatisticForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<ShipperDayStatisticVO> statisticVOList = waybillDao.selectPageByShipper(page, queryForm);
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return ResponseDTO.ok(SmartPageUtil.convert2PageResult(page, Lists.newArrayList()));
        }

        Set<Long> shipperIdSet = statisticVOList.stream().map(ShipperDayStatisticVO::getShipperId).collect(Collectors.toSet());
        queryForm.setShipperIdList(shipperIdSet);
        // 查询货主对应的负责人
        List<ShipperPrincipalDTO> shipperPrincipalList = shipperPrincipalDao.selectByShipperIdListAndType(shipperIdSet, PrincipalTypeEnum.MANAGER.getValue());
        Map<Long, String> shipperManagerNameMap = shipperPrincipalList.stream().collect(Collectors.toMap(ShipperPrincipalDTO::getShipperId, ShipperPrincipalDTO::getEmployeeName));

        // 公司名字
        List<EnterpriseEntity> enterpriseEntityList = Lists.newArrayList();
        Set<Long> enterpriseIdSet = statisticVOList.stream().map(ShipperDayStatisticVO::getEnterpriseId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(enterpriseIdSet)) {
            enterpriseEntityList = enterpriseDao.selectBatchIds(enterpriseIdSet);
        }
        Map<Long, String> enterpriseNameMap = enterpriseEntityList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseId, EnterpriseEntity::getEnterpriseName));

        // 取消的订单数
        queryForm.setOrderStatus(OrderStatusEnum.CANCEL.getValue());
        List<ShipperOrderCountDTO> orderCountDTOList = orderDao.selectCancelByShipper(queryForm);
        Map<Long, Integer> orderCountMap = orderCountDTOList.stream().collect(Collectors.toMap(ShipperOrderCountDTO::getShipperId, ShipperOrderCountDTO::getOrderCount));

        // 运单费用
        List<ShipperOrderWaybillDTO> waybillDTOList = waybillDao.selectWaybillIdByShipper(queryForm);
        Map<Long, List<Long>> waybillIdLMap = waybillDTOList.stream().collect(Collectors.groupingBy(ShipperOrderWaybillDTO::getShipperId, Collectors.mapping(ShipperOrderWaybillDTO::getWaybillId, Collectors.toList())));
        List<Long> waybillIdList = waybillDTOList.stream().map(ShipperOrderWaybillDTO::getWaybillId).collect(Collectors.toList());
        List<WaybillCostEntity> costEntityList = waybillCostDao.selectByWaybillIdList(waybillIdList);

        // 去年同期下单
        queryForm.setStartDate(queryForm.getStartDate().minusYears(1));
        queryForm.setEndDate(queryForm.getEndDate().minusYears(1));
        List<ShipperDayStatisticVO> lastYearStatisticVOList = waybillDao.selectByShipper(queryForm);
        Map<Long, ShipperDayStatisticVO> lastYearStatisticMap = lastYearStatisticVOList.stream().collect(Collectors.toMap(ShipperDayStatisticVO::getShipperId, Function.identity()));

        // 查询已开票金额
        List<ReceiveOrderShipperAmountDTO> shipperInvoiceList = receiveOrderInvoiceDao.sumInvoiceAmountByBusinessDateAndShipper(waybillIdList, Boolean.TRUE, InvoiceStatusEnum.INVOICE.getValue(), CheckStatusEnum.CANCEL.getValue());
        // 查询无需开票已核算金额，也包含在开票金额里
        List<ReceiveOrderShipperAmountDTO> shipperCheckList = receiveOrderWaybillDao.selectNoNeedInvoiceAndCheckAmountByWaybillIds(waybillIdList, Boolean.FALSE, CheckStatusEnum.CHECK.getValue());
        shipperInvoiceList.addAll(shipperCheckList);
        Map<Long, List<BigDecimal>> shipperInvoiceMap = shipperInvoiceList.stream().collect(Collectors.groupingBy(ReceiveOrderShipperAmountDTO::getShipperId, Collectors.mapping(ReceiveOrderShipperAmountDTO::getAmount, Collectors.toList())));

        // 货主已过账期金额
        queryForm.setAccountPeriodDate(LocalDate.now());
        queryForm.setVerificationFlag(false);
        List<ReceiveOrderAmountSumDTO> overAccountPeriodDateTotalAmountList = receiveOrderDao.selectTotalAmountByAccountPeriodDate(queryForm);
        List<Long> receiveOrderIdList = overAccountPeriodDateTotalAmountList.stream().map(ReceiveOrderAmountSumDTO::getReceiveOrderId).collect(Collectors.toList());
        Map<Long, BigDecimal> verificationAmountMap = null;
        if (CollectionUtils.isNotEmpty(receiveOrderIdList)) {
            List<ReceiveOrderVerificationAmountDTO> verificationAmountDTOList = receiveOrderVerificationDao.selectAmountByReceiveOrderIdList(receiveOrderIdList);
            verificationAmountMap = verificationAmountDTOList.stream()
                    .collect(Collectors.groupingBy(ReceiveOrderVerificationAmountDTO::getReceiveOrderId, Collectors.reducing(BigDecimal.ZERO, ReceiveOrderVerificationAmountDTO::getVerificationAmount, BigDecimal::add)));
        }
        if (verificationAmountMap != null) {
            for (ReceiveOrderAmountSumDTO totalAmountDTO : overAccountPeriodDateTotalAmountList) {
                Long receiveOrderId = totalAmountDTO.getReceiveOrderId();
                BigDecimal verificationAmount = verificationAmountMap.getOrDefault(receiveOrderId, BigDecimal.ZERO);
                totalAmountDTO.setTotalAmount(totalAmountDTO.getTotalAmount().subtract(verificationAmount));
            }
        }
        Map<Long, BigDecimal> overAccountPeriodDateAmountMap = overAccountPeriodDateTotalAmountList.stream()
                .collect(Collectors.groupingBy(ReceiveOrderAmountSumDTO::getShipperId, Collectors.reducing(BigDecimal.ZERO, ReceiveOrderAmountSumDTO::getTotalAmount, BigDecimal::add)));

        // 查询运单已提交核算金额
        List<ShipperReceiveOrderDTO> receiveOrderDTOList = receiveOrderWaybillDao.selectAmountByWaybillIds(waybillIdList, CheckStatusEnum.CANCEL.getValue(), InvoiceStatusEnum.CANCEL.getValue());

        for (ShipperDayStatisticVO statisticVO : statisticVOList) {
            Long shipperId = statisticVO.getShipperId();
            Integer cancelOrderCount = orderCountMap.getOrDefault(shipperId, NumberConst.ZERO);
            List<Long> waybillIds = waybillIdLMap.getOrDefault(shipperId, Lists.newArrayList());
            List<WaybillCostEntity> costList = costEntityList.stream().filter(e -> waybillIds.contains(e.getWaybillId())).collect(Collectors.toList());
            ShipperDayStatisticVO lastYearStatisticVO = lastYearStatisticMap.get(shipperId);
            BigDecimal invoiceAmount = shipperInvoiceMap.getOrDefault(shipperId, Lists.newArrayList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal receiveCostAmount = receiveOrderDTOList.stream().filter(e -> waybillIds.contains(e.getWaybillId())).map(ShipperReceiveOrderDTO::getReceiveAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal freightAmount = costList.stream()
                    .filter(e -> !CostItemCategoryEnum.EXCEPTION_COST.equalsValue(e.getCostItemCategory()))
                    .map(WaybillCostEntity::getCostAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal exceptionAmount = costList.stream()
                    .filter(e -> CostItemCategoryEnum.EXCEPTION_COST.equalsValue(e.getCostItemCategory()))
                    .map(WaybillCostEntity::getCostAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            statisticVO.setEnterpriseName(enterpriseNameMap.getOrDefault(statisticVO.getEnterpriseId(), StringConst.EMPTY));
            statisticVO.setManagerName(shipperManagerNameMap.getOrDefault(shipperId, StringConst.EMPTY));
            statisticVO.setCancelOrderCount(cancelOrderCount);
            statisticVO.setFreightAmount(freightAmount);
            statisticVO.setExceptionAmount(exceptionAmount);
            statisticVO.setTaxRate(new BigDecimal("0.09"));
            if (lastYearStatisticVO == null) {
                statisticVO.setLastYearWaybillCount(NumberConst.ZERO);
                statisticVO.setLastYearWReceiveAmount(BigDecimal.ZERO);
            } else {
                statisticVO.setLastYearWaybillCount(lastYearStatisticVO.getWaybillCount());
                statisticVO.setLastYearWReceiveAmount(lastYearStatisticVO.getReceiveAmount());
            }
            statisticVO.setRate(SmartBigDecimalUtil.dividePercent(SmartBigDecimalUtil.subtract(statisticVO.getReceiveAmount(), statisticVO.getLastYearWReceiveAmount(), 4), statisticVO.getLastYearWReceiveAmount()));
            statisticVO.setReceiveCostAmount(receiveCostAmount);
            statisticVO.setInvoiceAmount(invoiceAmount);
            BigDecimal waitInvoiceAmount = SmartBigDecimalUtil.Amount.subtract(statisticVO.getReceiveAmount(), invoiceAmount);
            statisticVO.setWaitInvoiceAmount(waitInvoiceAmount);
            statisticVO.setUnRecoveredAmount(overAccountPeriodDateAmountMap.getOrDefault(shipperId, BigDecimal.ZERO));
        }
        PageResult<ShipperDayStatisticVO> pageResult = SmartPageUtil.convert2PageResult(page, statisticVOList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 货主日报表导出
     *
     * @param queryForm
     * @return
     */
    public List<ShipperDayStatisticExportVO> exportShipperDayList(ShipperDayStatisticForm queryForm) {
        LocalDate startDate = queryForm.getStartDate();
        LocalDate endDate = queryForm.getEndDate();
        if (startDate == null || endDate == null) {
            throw new BusinessException("请选择查询时间");
        }

        ResponseDTO<PageResult<ShipperDayStatisticVO>> resp = this.queryShipperDayList(queryForm);
        if (!resp.getOk()) {
            throw new BusinessException(resp.getMsg());
        }
        List<ShipperDayStatisticVO> statisticVOList = resp.getData().getList();
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return Lists.newArrayList();
        }

        // 设置序号
        List<ShipperDayStatisticExportVO> exportVOList = SmartBeanUtil.copyList(statisticVOList, ShipperDayStatisticExportVO.class);
        int index = 1;
        for (ShipperDayStatisticExportVO exportVO : exportVOList) {
            exportVO.setSerialNo(index++);
        }
        return exportVOList;
    }
}
