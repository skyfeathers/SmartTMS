package net.lab1024.tms.admin.module.business.reportform.employee;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.pay.dao.PayOrderDao;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.*;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.entity.EmployeeSalesTargetEntity;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.form.*;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.vo.*;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillCostDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.RequestUser;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillCostEntity;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 员工 业绩目标 业务
 *
 * @author Turbolisten
 * @date 2021/8/14 11:28
 */
@Slf4j
@Service
public class EmployeeSalesTargetService {

    @Autowired
    private EmployeeSalesTargetDao salesTargetDao;
    @Autowired
    private EmployeeSalesTargetManager salesTargetManager;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WaybillCostDao waybillCostDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    /**
     * 更新 员工业绩目标 设置
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(EmployeeSalesTargetUpdateForm updateForm) {
        /**
         * 校验是否重复
         */
        List<EmployeeSalesTargetMonthDTO> monthList = updateForm.getMonthList();
        long count = monthList.stream().map(EmployeeSalesTargetMonthDTO::getMonth).distinct().count();
        if (count != monthList.size()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "月份数据不能重复哦");
        }

        // 校验员工
        Long employeeId = updateForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "员工不存在");
        }

        /**
         * 根据员工 年份 查询已有数据
         * 新增 和 更新
         * 之所以没有简单粗暴的 全删再新增 是因为还有其他字段
         */

        Integer year = updateForm.getYear();
        List<EmployeeSalesTargetEntity> dbTargetList = salesTargetDao.listByYear(employeeId, year);
        Map<Integer, EmployeeSalesTargetEntity> dbTargetMap = dbTargetList.stream().collect(Collectors.toMap(EmployeeSalesTargetEntity::getMonth, Function.identity()));

        List<EmployeeSalesTargetEntity> insertList = Lists.newArrayList();
        List<EmployeeSalesTargetEntity> updateList = Lists.newArrayList();
        for (EmployeeSalesTargetMonthDTO monthDTO : monthList) {
            EmployeeSalesTargetEntity targetTemp = dbTargetMap.get(monthDTO.getMonth());

            EmployeeSalesTargetEntity newTarget = new EmployeeSalesTargetEntity();
            newTarget.setTarget(monthDTO.getTarget());
            newTarget.setRemark(monthDTO.getRemark());
            newTarget.setUpdateName(updateForm.getUpdateName());

            // 新增
            if (null == targetTemp) {
                newTarget.setEmployeeId(employeeId);
                newTarget.setYear(year);
                newTarget.setMonth(monthDTO.getMonth());
                insertList.add(newTarget);
                continue;
            }

            // 更新
            newTarget.setTargetId(targetTemp.getTargetId());
            updateList.add(newTarget);
        }

        // 更新数据
        salesTargetManager.updateData(insertList, updateList);
        return ResponseDTO.ok();
    }

    /**
     * 查询 员工业绩目标 列表
     *
     * @param queryForm
     * @return
     */
    public List<EmployeeSalesTargetVO> query(EmployeeSalesTargetQueryForm queryForm, RequestUser requestUser) {
        List<Long> employeeIdList = queryForm.getEmployeeIdList();
        if (employeeIdList == null) {
            employeeIdList = Lists.newArrayList();
        }
        if (queryForm.getEmployeeId() != null) {
            employeeIdList.add(queryForm.getEmployeeId());
        }
        List<EmployeeVO> employeeList = employeeService.queryEmployeeList(queryForm.getEnterpriseId(), employeeIdList, SystemConfigKeyEnum.SALES_ROLE_CODE);

        List<Long> finialEmployeeIdList = employeeList.stream().map(EmployeeVO::getEmployeeId).distinct().collect(Collectors.toList());
        queryForm.setEmployeeIdList(finialEmployeeIdList);

        List<EmployeeSalesTargetEntity> targetEntityList = salesTargetDao.query(queryForm);
        /**
         * 处理为前端展示结构
         * 以公司id_员工id 将月份目标分组设置
         */
        Map<Long, List<EmployeeSalesTargetEntity>> targetMap = targetEntityList.stream().collect(Collectors.groupingBy(EmployeeSalesTargetEntity::getEmployeeId));

        // 循环处理
        List<EmployeeSalesTargetVO> targetVOList = employeeList.stream().map(e -> {
            // 获取目标设置情况
            List<EmployeeSalesTargetEntity> targetList = targetMap.get(e.getEmployeeId());
            List<EmployeeSalesTargetMonthVO> monthList = SmartBeanUtil.copyList(targetList, EmployeeSalesTargetMonthVO.class);

            // 获取员工信息
            EmployeeSalesTargetVO targetVO = SmartBeanUtil.copy(e, EmployeeSalesTargetVO.class);
            targetVO.setMonthList(monthList);
            targetVO.setUpdateName(requestUser.getUserName());
            targetVO.setUpdateTime(LocalDateTime.now());
            targetVO.setCreateTime(LocalDateTime.now());
            if (CollectionUtils.isNotEmpty(targetList)) {
                EmployeeSalesTargetEntity employeeSalesTargetEntity = targetList.get(0);
                targetVO.setUpdateName(employeeSalesTargetEntity.getUpdateName());
                targetVO.setUpdateTime(employeeSalesTargetEntity.getUpdateTime());
                targetVO.setCreateTime(employeeSalesTargetEntity.getCreateTime());
            }
            return targetVO;
        }).collect(Collectors.toList());

        return targetVOList;
    }

    /**
     * 销售日报表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<SaleDayStatisticVO>> querySaleDayStatisticList(SalesDayStatisticQueryForm queryForm) {
        List<EmployeeVO> employeeVOList = employeeService.queryEmployeeList(queryForm.getEnterpriseId(), null, SystemConfigKeyEnum.SALES_ROLE_CODE);
        if (CollectionUtil.isEmpty(employeeVOList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }

        List<Long> employeeIdList = employeeVOList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        List<Long> salesIdList = queryForm.getSalesIdList();
        if (CollectionUtils.isNotEmpty(salesIdList)) {
            boolean hasNotContainFlag = salesIdList.stream().anyMatch(e -> !employeeIdList.contains(e));
            if (hasNotContainFlag) {
                return ResponseDTO.ok(Lists.newArrayList());
            }
            queryForm.setEmployeeIdList(Lists.newArrayList(salesIdList));
            employeeVOList = employeeVOList.stream().filter(e -> salesIdList.contains(e.getEmployeeId())).collect(Collectors.toList());
        } else {
            queryForm.setEmployeeIdList(employeeIdList);
        }

        List<SaleDayStatisticVO> statisticVOList = employeeVOList.stream()
                .map(e -> {
                    SaleDayStatisticVO statisticVO = new SaleDayStatisticVO();
                    statisticVO.setEmployeeId(e.getEmployeeId());
                    statisticVO.setEmployeeName(e.getActualName());
                    statisticVO.setDepartmentId(e.getDepartmentId());
                    statisticVO.setDepartmentName(e.getDepartmentName());
                    return statisticVO;
                }).collect(Collectors.toList());

        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        this.buildTargetAndRateInfo(queryForm, statisticVOList);
        this.buildWaybillAndAmountInfo(queryForm, statisticVOList);
        statisticVOList = this.buildSaleStatisticInfo(statisticVOList);
        return ResponseDTO.ok(statisticVOList);
    }

    /**
     * 根据部门设置总计信息
     *
     * @param statisticVOList
     */
    private List<SaleDayStatisticVO> buildSaleStatisticInfo(List<SaleDayStatisticVO> statisticVOList) {
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return Lists.newArrayList();
        }

        // 部门
        Set<Long> departmentIdSet = statisticVOList.stream().map(SaleDayStatisticVO::getDepartmentId).collect(Collectors.toSet());
        List<DepartmentEntity> departmentEntityList = departmentDao.selectBatchIds(departmentIdSet);
        Map<Long, DepartmentEntity> departmentMap = departmentEntityList.stream()
                .collect(Collectors.toMap(DepartmentEntity::getDepartmentId, Function.identity()));

        // 部门负责人
        Set<Long> managerIdList = departmentEntityList.stream().map(DepartmentEntity::getManagerId).collect(Collectors.toSet());
        List<EmployeeEntity> managerList = employeeDao.selectBatchIds(managerIdList);
        Map<Long, String> managerNameMap = managerList.stream()
                .collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));

        List<SaleDayStatisticVO> resultList = Lists.newArrayList();
        Map<Long, List<SaleDayStatisticVO>> statisticVOMap = statisticVOList.stream().collect(Collectors.groupingBy(SaleDayStatisticVO::getDepartmentId));
        for (Map.Entry<Long, List<SaleDayStatisticVO>> entity : statisticVOMap.entrySet()) {
            Long departmentId = entity.getKey();
            List<SaleDayStatisticVO> statisticList = entity.getValue();
            resultList.addAll(statisticList);

            DepartmentEntity departmentEntity = departmentMap.get(departmentId);
            if (departmentEntity == null) {
                continue;
            }
            Long managerId = departmentEntity.getManagerId();
            String managerName = managerNameMap.getOrDefault(managerId, StringConst.EMPTY);

            BigDecimal targetAmount = statisticList.stream().map(SaleDayStatisticVO::getTargetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal completedAmount = statisticList.stream().map(SaleDayStatisticVO::getCompletedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal lastYearCompletedAmount = statisticList.stream().map(SaleDayStatisticVO::getLastYearCompletedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal receiveAmount = statisticList.stream().map(SaleDayStatisticVO::getReceiveAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal salaryAmount = statisticList.stream().map(SaleDayStatisticVO::getSalaryAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal carCostAmount = statisticList.stream().map(SaleDayStatisticVO::getCarCostAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal profitAmount = statisticList.stream().map(SaleDayStatisticVO::getProfitAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 部门合计信息
            SaleDayStatisticVO statisticVO = new SaleDayStatisticVO();
            statisticVO.setEmployeeId(managerId);
            statisticVO.setEmployeeName(managerName + "_合计");
            statisticVO.setDepartmentId(departmentId);
            statisticVO.setDepartmentName(departmentEntity.getName());
            statisticVO.setTargetAmount(targetAmount);
            statisticVO.setCompletedAmount(completedAmount);
            statisticVO.setInCompleteAmount(targetAmount.compareTo(completedAmount) < 0 ?
                    BigDecimal.ZERO : SmartBigDecimalUtil.subtract(targetAmount, completedAmount, 4));
            BigDecimal completeRate = SmartBigDecimalUtil.dividePercent(statisticVO.getCompletedAmount(), statisticVO.getTargetAmount());
            statisticVO.setCompleteRate(completeRate);
            statisticVO.setLastYearTargetAmount(statisticList.stream().map(SaleDayStatisticVO::getLastYearTargetAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setLastYearCompletedAmount(lastYearCompletedAmount);
            BigDecimal rate = SmartBigDecimalUtil.dividePercent(SmartBigDecimalUtil.subtract(completedAmount, lastYearCompletedAmount, 4), lastYearCompletedAmount);
            statisticVO.setRate(rate);
            statisticVO.setWaybillCount(statisticList.stream().map(SaleDayStatisticVO::getWaybillCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setFreightAmount(statisticList.stream().map(SaleDayStatisticVO::getFreightAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setOilCardAmount(statisticList.stream().map(SaleDayStatisticVO::getOilCardAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setExceptionAmount(statisticList.stream().map(SaleDayStatisticVO::getExceptionAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setReceiveAmount(receiveAmount);
            statisticVO.setSalaryAmount(salaryAmount);
            statisticVO.setCarCostAmount(carCostAmount);
            statisticVO.setProfitAmount(profitAmount);
            statisticVO.setProfitRate( SmartBigDecimalUtil.dividePercent(profitAmount, receiveAmount));
            resultList.add(statisticVO);
        }
        return resultList;
    }

    /**
     * 设置运单及金额信息
     *
     * @param queryForm
     * @param statisticVOList
     */
    private void buildWaybillAndAmountInfo(SalesDayStatisticQueryForm queryForm, List<SaleDayStatisticVO> statisticVOList) {
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return;
        }
        List<EmployeeSalesWaybillDTO> salesWaybillDTOList = waybillDao.selectBySale(queryForm);
        if (CollectionUtil.isEmpty(salesWaybillDTOList)) {
            statisticVOList.stream().forEach(statisticVO -> {
                this.buildDefaultValue(statisticVO);
            });
            return;
        }

        Map<Long, List<EmployeeSalesWaybillDTO>> salesWaybillMap = salesWaybillDTOList.stream()
                .collect(Collectors.groupingBy(EmployeeSalesWaybillDTO::getEmployeeId));
        List<Long> waybillIdList = salesWaybillDTOList.stream().map(EmployeeSalesWaybillDTO::getWaybillId).collect(Collectors.toList());
        // 运单费用
        List<WaybillCostEntity> costEntityList = waybillCostDao.selectByWaybillIdList(waybillIdList);

        // 运单费用
        for (SaleDayStatisticVO statisticVO : statisticVOList) {
            Long employeeId = statisticVO.getEmployeeId();
            List<EmployeeSalesWaybillDTO> salesWaybillList = salesWaybillMap.getOrDefault(employeeId, Lists.newArrayList());
            if (CollectionUtil.isEmpty(salesWaybillList)) {
                this.buildDefaultValue(statisticVO);
                continue;
            }

            // 运单合计应收和利润金额
            BigDecimal receiveAmount = salesWaybillList.stream().map(EmployeeSalesWaybillDTO::getReceiveAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal salaryAmount = salesWaybillList.stream().map(EmployeeSalesWaybillDTO::getSalaryAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal carCostAmount = salesWaybillList.stream().map(EmployeeSalesWaybillDTO::getCarCostAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal profitAmount = salesWaybillList.stream().map(EmployeeSalesWaybillDTO::getProfitAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 运单合计运费，油卡，异常金额
            List<Long> waybillIds = salesWaybillList.stream().map(EmployeeSalesWaybillDTO::getWaybillId).collect(Collectors.toList());
            List<WaybillCostEntity> costList = costEntityList.stream().filter(e -> waybillIds.contains(e.getWaybillId())).collect(Collectors.toList());
            BigDecimal freightAmount = costList.stream()
                    .filter(e -> CostItemCategoryEnum.FREIGHT_COST.equalsValue(e.getCostItemCategory()))
                    .map(WaybillCostEntity::getCostAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal oilCardAmount = costList.stream()
                    .filter(e -> CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory()))
                    .map(WaybillCostEntity::getCostAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal exceptionAmount = costList.stream()
                    .filter(e -> CostItemCategoryEnum.EXCEPTION_COST.equalsValue(e.getCostItemCategory())
                            || CostItemCategoryEnum.OTHER.equalsValue(e.getCostItemCategory()))
                    .map(WaybillCostEntity::getCostAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


            statisticVO.setWaybillCount(salesWaybillList.size());
            statisticVO.setFreightAmount(freightAmount);
            statisticVO.setOilCardAmount(oilCardAmount);
            statisticVO.setExceptionAmount(exceptionAmount);
            statisticVO.setReceiveAmount(receiveAmount);
            statisticVO.setSalaryAmount(salaryAmount);
            statisticVO.setCarCostAmount(carCostAmount);
            statisticVO.setProfitAmount(profitAmount);
            statisticVO.setProfitRate(SmartBigDecimalUtil.dividePercent(profitAmount, receiveAmount));
        }
    }

    /**
     * 设置默认值
     *
     * @param statisticVO
     */
    private void buildDefaultValue(SaleDayStatisticVO statisticVO) {
        statisticVO.setWaybillCount(NumberConst.ZERO);
        statisticVO.setFreightAmount(BigDecimal.ZERO);
        statisticVO.setOilCardAmount(BigDecimal.ZERO);
        statisticVO.setExceptionAmount(BigDecimal.ZERO);
        statisticVO.setReceiveAmount(BigDecimal.ZERO);
        statisticVO.setProfitAmount(BigDecimal.ZERO);
        statisticVO.setProfitRate(BigDecimal.ZERO);
        statisticVO.setSalaryAmount(BigDecimal.ZERO);
        statisticVO.setCarCostAmount(BigDecimal.ZERO);
    }

    /**
     * 销售日报表设置指标和比例信息
     *
     * @param queryForm
     * @param statisticVOList
     */
    private void buildTargetAndRateInfo(SalesDayStatisticQueryForm queryForm, List<SaleDayStatisticVO> statisticVOList) {
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return;
        }
        // 使用新的queryForm，避免影响原有queryForm
        SalesDayStatisticQueryForm newQueryForm = SmartBeanUtil.copy(queryForm, SalesDayStatisticQueryForm.class);
        // 月指标
        List<EmployeeSalesTargetEntity> salesTargetEntityList = salesTargetDao.querySalesTarget(newQueryForm);
        Map<Long, BigDecimal> salesTargetMap = salesTargetEntityList.stream()
                .collect(Collectors.groupingBy(EmployeeSalesTargetEntity::getEmployeeId, Collectors.reducing(BigDecimal.ZERO, EmployeeSalesTargetEntity::getTarget, BigDecimal::add)));
        // 已完成指标
        List<WaybillEmployeeAmountDTO> completedDTOList = waybillDao.selectReceiveAmountBySale(newQueryForm);
        Map<Long, BigDecimal> completedMap = completedDTOList.stream()
                .collect(Collectors.toMap(WaybillEmployeeAmountDTO::getEmployeeId, WaybillEmployeeAmountDTO::getReceiveAmount));

        // 上年同期月指标
        newQueryForm.setStartDate(newQueryForm.getStartDate().minusYears(1));
        newQueryForm.setEndDate(newQueryForm.getEndDate().minusYears(1));
        List<EmployeeSalesTargetEntity> lastYearSalesTargetEntityList = salesTargetDao.querySalesTarget(newQueryForm);
        Map<Long, BigDecimal> lastYearSalesTargetMap = lastYearSalesTargetEntityList.stream()
                .collect(Collectors.groupingBy(EmployeeSalesTargetEntity::getEmployeeId, Collectors.reducing(BigDecimal.ZERO, EmployeeSalesTargetEntity::getTarget, BigDecimal::add)));
        // 上年同期已完成指标
        List<WaybillEmployeeAmountDTO> lastYearCompletedDTOList = waybillDao.selectReceiveAmountBySale(newQueryForm);
        Map<Long, BigDecimal> lastYearCompletedMap = lastYearCompletedDTOList.stream()
                .collect(Collectors.toMap(WaybillEmployeeAmountDTO::getEmployeeId, WaybillEmployeeAmountDTO::getReceiveAmount));

        for (SaleDayStatisticVO statisticVO : statisticVOList) {
            Long employeeId = statisticVO.getEmployeeId();
            BigDecimal targetAmount = salesTargetMap.getOrDefault(employeeId, BigDecimal.ZERO);
            BigDecimal completeAmount = completedMap.getOrDefault(employeeId, BigDecimal.ZERO);
            BigDecimal lastYearCompletedAmount = lastYearCompletedMap.getOrDefault(employeeId, BigDecimal.ZERO);

            statisticVO.setTargetAmount(targetAmount);
            statisticVO.setCompletedAmount(completeAmount);
            statisticVO.setInCompleteAmount(targetAmount.compareTo(completeAmount) < 0 ?
                    BigDecimal.ZERO : SmartBigDecimalUtil.subtract(targetAmount, completeAmount, 4));
            statisticVO.setCompleteRate(SmartBigDecimalUtil.dividePercent(completeAmount, targetAmount));
            statisticVO.setLastYearTargetAmount(lastYearSalesTargetMap.getOrDefault(employeeId, BigDecimal.ZERO));
            statisticVO.setLastYearCompletedAmount(lastYearCompletedAmount);
            BigDecimal rate = SmartBigDecimalUtil.dividePercent(SmartBigDecimalUtil.subtract(completeAmount, lastYearCompletedAmount,  4), lastYearCompletedAmount);
            statisticVO.setRate(rate);
        }
    }

    /**
     * 销售日报表导出
     *
     * @param queryForm
     * @return
     */
    public List<SaleDayStatisticExportVO> exportSaleDayStatisticList(SalesDayStatisticQueryForm queryForm) {
        LocalDate startDate = queryForm.getStartDate();
        LocalDate endDate = queryForm.getEndDate();
        if (startDate == null || endDate == null) {
            throw new BusinessException("请选择查询时间");
        }
        if (startDate.getYear() != endDate.getYear() || startDate.getMonthValue() != endDate.getMonthValue()) {
            throw new BusinessException("最多只支持一个月内的查询");
        }
        ResponseDTO<List<SaleDayStatisticVO>> resp = this.querySaleDayStatisticList(queryForm);
        if (!resp.getOk()) {
            throw new BusinessException(resp.getMsg());
        }

        List<SaleDayStatisticVO> statisticVOList = resp.getData();
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return Lists.newArrayList();
        }
        List<SaleDayStatisticExportVO> exportVOList = SmartBeanUtil.copyList(statisticVOList, SaleDayStatisticExportVO.class);

        // 设置序号
        int index = 1;
        Long departmentId = statisticVOList.get(0).getDepartmentId();
        for (int i = 0; i < exportVOList.size(); i++) {
            if (i != exportVOList.size() - 1 && !Objects.equals(exportVOList.get(i + 1).getDepartmentId(),departmentId)) {
                index = 1;
                departmentId = exportVOList.get(i + 1).getDepartmentId();
                exportVOList.get(i).setSerialNo("部门合计");
                continue;
            }
            if (i == exportVOList.size() - 1) {
                exportVOList.get(i).setSerialNo("部门合计");
                continue;
            }
            exportVOList.get(i).setSerialNo(String.valueOf(index++));
        }
        return exportVOList;
    }

    /**
     * 客服日报表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<CustomerServiceDayStatisticVO>> queryCustomerServiceDayList(CustomerServiceDayStatisticQueryForm queryForm) {
        List<EmployeeVO> employeeVOList = employeeService.queryEmployeeList(queryForm.getEnterpriseId(), null, SystemConfigKeyEnum.CUSTOMER_SERVICE_ROLE_CODE);
        if (CollectionUtil.isEmpty(employeeVOList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }

        List<Long> employeeIdList = employeeVOList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        List<Long> customerServiceIdList = queryForm.getCustomerServiceIdList();
        if (CollectionUtils.isNotEmpty(customerServiceIdList)) {
            boolean hasNotContainFlag = customerServiceIdList.stream().anyMatch(e -> !employeeIdList.contains(e));
            if (hasNotContainFlag) {
                return ResponseDTO.ok(Lists.newArrayList());
            }
            queryForm.setEmployeeIdList(customerServiceIdList);
            employeeVOList = employeeVOList.stream().filter(e -> customerServiceIdList.contains(e.getEmployeeId())).collect(Collectors.toList());
        } else {
            queryForm.setEmployeeIdList(employeeIdList);
        }

        List<CustomerServiceDayStatisticVO> statisticVOList = employeeVOList.stream()
                .map(employeeVO -> {
                    CustomerServiceDayStatisticVO statisticVO = new CustomerServiceDayStatisticVO();
                    statisticVO.setEmployeeId(employeeVO.getEmployeeId());
                    statisticVO.setEmployeeName(employeeVO.getActualName());
                    statisticVO.setDepartmentId(employeeVO.getDepartmentId());
                    statisticVO.setDepartmentName(employeeVO.getDepartmentName());
                    return statisticVO;
                }).collect(Collectors.toList());

        this.buildCustomerServiceDayStatisticInfo(queryForm, statisticVOList);
        statisticVOList = this.buildCustomerServiceStatisticInfo(statisticVOList);
        return ResponseDTO.ok(statisticVOList);
    }

    /**
     * 设置客服日报表信息
     *
     * @param queryForm
     * @param statisticVOList
     */
    private void buildCustomerServiceDayStatisticInfo(CustomerServiceDayStatisticQueryForm queryForm, List<CustomerServiceDayStatisticVO> statisticVOList) {
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return;
        }

        // 订单
        List<EmployeeCustomerServiceOrderDTO> orderDTOList = orderDao.selectByCustomerService(queryForm);
        Map<Long, List<EmployeeCustomerServiceOrderDTO>> orderVOMap = orderDTOList.stream()
                .collect(Collectors.groupingBy(EmployeeCustomerServiceOrderDTO::getCustomerServiceId));

        // 运单
        queryForm.setExcludeWaybillStatus(WaybillStatusEnum.CANCEL.getValue());
        List<EmployeeCustomerServiceWaybillDTO> waybillDTOList = waybillDao.selectByCustomerService(queryForm);
        Map<Long, List<EmployeeCustomerServiceWaybillDTO>> waybillDTOMap = waybillDTOList.stream()
                .collect(Collectors.groupingBy(EmployeeCustomerServiceWaybillDTO::getCustomerServiceId));

        // 业务所属公司
        List<EnterpriseEntity> enterpriseEntityList = Lists.newArrayList();
        Set<Long> allEnterpriseSet = waybillDTOList.stream().map(EmployeeCustomerServiceWaybillDTO::getEnterpriseId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(allEnterpriseSet)) {
            enterpriseEntityList = enterpriseDao.selectBatchIds(allEnterpriseSet);
        }

        // 付款单
        queryForm.setExcludePayOrderStatus(PayOrderStatusEnum.CANCEL.getValue());
        List<EmployeePayOrderCostDTO> costDTOList = payOrderDao.selectByCustomerService(queryForm);
        Map<Long, List<EmployeePayOrderCostDTO>> costDTOMap = costDTOList.stream().collect(Collectors.groupingBy(EmployeePayOrderCostDTO::getCustomerServiceId));

        // 上年同期运单
        queryForm.setStartDate(queryForm.getStartDate().minusYears(1));
        queryForm.setEndDate(queryForm.getEndDate().minusYears(1));
        List<EmployeeCustomerServiceWaybillDTO> lastYearWaybillDTOList = waybillDao.selectByCustomerService(queryForm);
        Map<Long, List<EmployeeCustomerServiceWaybillDTO>> lastYearWaybillDTOMap = lastYearWaybillDTOList.stream()
                .collect(Collectors.groupingBy(EmployeeCustomerServiceWaybillDTO::getCustomerServiceId));

        for (CustomerServiceDayStatisticVO statisticVO : statisticVOList) {
            Long employeeId = statisticVO.getEmployeeId();
            List<EmployeeCustomerServiceOrderDTO> orderVOS = orderVOMap.getOrDefault(employeeId, Lists.newArrayList());
            List<EmployeeCustomerServiceWaybillDTO> waybillDTOS = waybillDTOMap.getOrDefault(employeeId, Lists.newArrayList());
            List<EmployeePayOrderCostDTO> costDTOS = costDTOMap.getOrDefault(employeeId, Lists.newArrayList());
            List<EmployeeCustomerServiceWaybillDTO> lastYearWaybillDTOS = lastYearWaybillDTOMap.getOrDefault(employeeId, Lists.newArrayList());

            Set<Long> enterpriseIdSet = waybillDTOS.stream().map(EmployeeCustomerServiceWaybillDTO::getEnterpriseId).collect(Collectors.toSet());
            List<String> enterpriseNameList = enterpriseEntityList.stream()
                    .filter(e -> enterpriseIdSet.contains(e.getEnterpriseId()))
                    .map(EnterpriseEntity::getEnterpriseName)
                    .collect(Collectors.toList());

            String enterpriseName = StrUtil.join(StringConst.SEPARATOR, enterpriseNameList);
            BigDecimal receiveAmount = waybillDTOS.stream().map(EmployeeCustomerServiceWaybillDTO::getReceiveAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal payableAmount = waybillDTOS.stream().map(EmployeeCustomerServiceWaybillDTO::getPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal lastYearWaybillAmount = lastYearWaybillDTOS.stream().map(EmployeeCustomerServiceWaybillDTO::getReceiveAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            statisticVO.setEnterpriseName(enterpriseName);
            statisticVO.setOrderCount(orderVOS.stream().filter(e -> !OrderStatusEnum.CANCEL.equalsValue(e.getOrderStatus())).collect(Collectors.toList()).size());
            statisticVO.setWaybillCount(waybillDTOS.size());
            statisticVO.setTotalReceiveAmount(receiveAmount);
            statisticVO.setTotalPayableAmount(payableAmount);
            statisticVO.setCancelOrderCount((int) orderVOS.stream().filter(e -> OrderStatusEnum.CANCEL.equalsValue(e.getOrderStatus())).count());
            statisticVO.setPayOrderCount((int) costDTOS.stream().map(EmployeePayOrderCostDTO::getPayOrderId).distinct().count());
            statisticVO.setPayableAmount(costDTOS.stream().map(EmployeePayOrderCostDTO::getPayableTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setLastYearWaybillCount(lastYearWaybillDTOS.size());
            statisticVO.setLastYearWaybillAmount(lastYearWaybillAmount);
            // 比例
            BigDecimal waybillCount = new BigDecimal(statisticVO.getWaybillCount());
            BigDecimal lastYearWaybillCount = new BigDecimal(statisticVO.getLastYearWaybillCount());
            BigDecimal rate = SmartBigDecimalUtil.dividePercent(SmartBigDecimalUtil.subtract(waybillCount, lastYearWaybillCount, 4), lastYearWaybillCount);
            statisticVO.setRate(rate);
        }
    }

    /**
     * 根据部门设置总计信息
     *
     * @param statisticVOList
     */
    private List<CustomerServiceDayStatisticVO> buildCustomerServiceStatisticInfo(List<CustomerServiceDayStatisticVO> statisticVOList) {
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return Lists.newArrayList();
        }

        // 部门
        Set<Long> departmentIdSet = statisticVOList.stream().map(CustomerServiceDayStatisticVO::getDepartmentId).collect(Collectors.toSet());
        List<DepartmentEntity> departmentEntityList = departmentDao.selectBatchIds(departmentIdSet);
        Map<Long, DepartmentEntity> departmentMap = departmentEntityList.stream()
                .collect(Collectors.toMap(DepartmentEntity::getDepartmentId, Function.identity()));

        // 部门负责人
        Set<Long> managerIdList = departmentEntityList.stream().map(DepartmentEntity::getManagerId).collect(Collectors.toSet());
        List<EmployeeEntity> managerList = employeeDao.selectBatchIds(managerIdList);
        Map<Long, String> managerNameMap = managerList.stream()
                .collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));

        List<CustomerServiceDayStatisticVO> resultList = Lists.newArrayList();
        Map<Long, List<CustomerServiceDayStatisticVO>> statisticVOMap = statisticVOList.stream()
                .collect(Collectors.groupingBy(CustomerServiceDayStatisticVO::getDepartmentId));
        for (Map.Entry<Long, List<CustomerServiceDayStatisticVO>> entity : statisticVOMap.entrySet()) {
            Long departmentId = entity.getKey();
            List<CustomerServiceDayStatisticVO> statisticList = entity.getValue();
            resultList.addAll(statisticList);

            DepartmentEntity departmentEntity = departmentMap.get(departmentId);
            if (departmentEntity == null) {
                continue;
            }
            Long managerId = departmentEntity.getManagerId();
            String managerName = managerNameMap.getOrDefault(managerId, StringConst.EMPTY);
            String enterpriseName = this.buildCustomerServiceEnterpriseName(statisticList);

            // 部门合计信息
            CustomerServiceDayStatisticVO statisticVO = new CustomerServiceDayStatisticVO();
            statisticVO.setEmployeeId(managerId);
            statisticVO.setEmployeeName(managerName + "_合计");
            statisticVO.setDepartmentId(departmentId);
            statisticVO.setDepartmentName(departmentEntity.getName());
            statisticVO.setEnterpriseName(enterpriseName);

            statisticVO.setOrderCount(statisticList.stream().map(CustomerServiceDayStatisticVO::getOrderCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setWaybillCount(statisticList.stream().map(CustomerServiceDayStatisticVO::getWaybillCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setTotalReceiveAmount(statisticList.stream().map(CustomerServiceDayStatisticVO::getTotalReceiveAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setTotalPayableAmount(statisticList.stream().map(CustomerServiceDayStatisticVO::getTotalPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setCancelOrderCount(statisticList.stream().map(CustomerServiceDayStatisticVO::getCancelOrderCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setPayOrderCount(statisticList.stream().map(CustomerServiceDayStatisticVO::getPayOrderCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setPayableAmount(statisticList.stream().map(CustomerServiceDayStatisticVO::getPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setLastYearWaybillCount(statisticList.stream().map(CustomerServiceDayStatisticVO::getLastYearWaybillCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setLastYearWaybillAmount(statisticList.stream().map(CustomerServiceDayStatisticVO::getLastYearWaybillAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 比例
            BigDecimal waybillCount = new BigDecimal(statisticVO.getWaybillCount());
            BigDecimal lastYearWaybillCount = new BigDecimal(statisticVO.getLastYearWaybillCount());
            BigDecimal rate = SmartBigDecimalUtil.dividePercent(SmartBigDecimalUtil.subtract(waybillCount, lastYearWaybillCount, 4), waybillCount);
            statisticVO.setRate(rate);
            resultList.add(statisticVO);
        }
        return resultList;
    }

    /**
     * 拼接公司名字
     *
     * @param statisticList
     * @return
     */
    private String buildCustomerServiceEnterpriseName(List<CustomerServiceDayStatisticVO> statisticList) {
        return statisticList.stream()
                .map(CustomerServiceDayStatisticVO::getEnterpriseName)
                .flatMap(names -> Arrays.stream(names.split(StringConst.SEPARATOR)))
                .distinct()
                .collect(Collectors.joining(StringConst.SEPARATOR));
    }

    /**
     * 客服日报表导出
     *
     * @param queryForm
     * @return
     */
    public List<CustomerServiceDayStatisticExportVO> exportCustomerServiceDayList(CustomerServiceDayStatisticQueryForm queryForm) {
        LocalDate startDate = queryForm.getStartDate();
        LocalDate endDate = queryForm.getEndDate();
        if (startDate == null || endDate == null) {
            throw new BusinessException("请选择查询时间");
        }
        ResponseDTO<List<CustomerServiceDayStatisticVO>> resp = this.queryCustomerServiceDayList(queryForm);
        if (!resp.getOk()) {
            throw new BusinessException(resp.getMsg());
        }
        List<CustomerServiceDayStatisticVO> statisticVOList = resp.getData();
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return Lists.newArrayList();
        }

        // 设置序号
        List<CustomerServiceDayStatisticExportVO> exportVOList = SmartBeanUtil.copyList(statisticVOList, CustomerServiceDayStatisticExportVO.class);
        int index = 1;
        Long departmentId = statisticVOList.get(0).getDepartmentId();
        for (int i = 0; i < exportVOList.size(); i++) {
            if (i != exportVOList.size() - 1 && !Objects.equals(exportVOList.get(i + 1).getDepartmentId(),departmentId)) {
                index = 1;
                departmentId = exportVOList.get(i + 1).getDepartmentId();
                exportVOList.get(i).setSerialNo("部门合计");
                continue;
            }
            if (i == exportVOList.size() - 1) {
                exportVOList.get(i).setSerialNo("部门合计");
                continue;
            }
            exportVOList.get(i).setSerialNo(String.valueOf(index++));
        }
        return exportVOList;
    }

    /**
     * 调度日报表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<ScheduleDayStatisticVO>> queryScheduleDayList(ScheduleDayStatisticQueryForm queryForm) {
        List<EmployeeVO> employeeVOList = employeeService.queryEmployeeList(queryForm.getEnterpriseId(), null, SystemConfigKeyEnum.CUSTOMER_SERVICE_ROLE_CODE);
        if (CollectionUtil.isEmpty(employeeVOList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }

        List<Long> employeeIdList = employeeVOList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        List<Long> scheduleIdList = queryForm.getScheduleIdList();
        if (CollectionUtils.isNotEmpty(scheduleIdList)) {
            boolean hasNotContainFlag = scheduleIdList.stream().anyMatch(scheduleId -> !employeeIdList.contains(scheduleId));
            if (hasNotContainFlag) {
                return ResponseDTO.ok(Lists.newArrayList());
            }
            queryForm.setEmployeeIdList(scheduleIdList);
            employeeVOList = employeeVOList.stream().filter(e -> scheduleIdList.contains(e.getEmployeeId())).collect(Collectors.toList());
        } else {
            queryForm.setEmployeeIdList(employeeIdList);
        }

        List<ScheduleDayStatisticVO> statisticVOList = employeeVOList.stream()
                .map(employeeVO -> {
                    ScheduleDayStatisticVO statisticVO = new ScheduleDayStatisticVO();
                    statisticVO.setEmployeeId(employeeVO.getEmployeeId());
                    statisticVO.setEmployeeName(employeeVO.getActualName());
                    statisticVO.setDepartmentId(employeeVO.getDepartmentId());
                    statisticVO.setDepartmentName(employeeVO.getDepartmentName());

                    statisticVO.setWaybillCount(0);
                    statisticVO.setPayableAmount(BigDecimal.ZERO);
                    statisticVO.setFreightAmount(BigDecimal.ZERO);
                    statisticVO.setOilCardAmount(BigDecimal.ZERO);
                    statisticVO.setOilCardRate(BigDecimal.ZERO);
                    statisticVO.setInCompleteOrderCount(0);
                    statisticVO.setLastYearWaybillCount(0);
                    statisticVO.setLastYearFreightAmount(BigDecimal.ZERO);
                    statisticVO.setRate(BigDecimal.ZERO);
                    return statisticVO;
                }).collect(Collectors.toList());

        this.buildScheduleDayStatisticInfo(queryForm, statisticVOList);
        statisticVOList = this.buildScheduleStatisticInfo(statisticVOList);
        return ResponseDTO.ok(statisticVOList);
    }

    /**
     * 调度日报表统计信息
     *
     * @param statisticVOList
     * @return
     */
    private List<ScheduleDayStatisticVO> buildScheduleStatisticInfo(List<ScheduleDayStatisticVO> statisticVOList) {
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return Lists.newArrayList();
        }

        // 部门
        Set<Long> departmentIdSet = statisticVOList.stream().map(ScheduleDayStatisticVO::getDepartmentId).collect(Collectors.toSet());
        List<DepartmentEntity> departmentEntityList = departmentDao.selectBatchIds(departmentIdSet);
        Map<Long, DepartmentEntity> departmentMap = departmentEntityList.stream()
                .collect(Collectors.toMap(DepartmentEntity::getDepartmentId, Function.identity()));

        // 部门负责人
        Set<Long> managerIdList = departmentEntityList.stream().map(DepartmentEntity::getManagerId).collect(Collectors.toSet());
        List<EmployeeEntity> managerList = employeeDao.selectBatchIds(managerIdList);
        Map<Long, String> managerNameMap = managerList.stream()
                .collect(Collectors.toMap(EmployeeEntity::getEmployeeId, EmployeeEntity::getActualName));

        List<ScheduleDayStatisticVO> resultList = Lists.newArrayList();
        Map<Long, List<ScheduleDayStatisticVO>> statisticVOMap = statisticVOList.stream()
                .collect(Collectors.groupingBy(ScheduleDayStatisticVO::getDepartmentId));
        for (Map.Entry<Long, List<ScheduleDayStatisticVO>> entity : statisticVOMap.entrySet()) {
            Long departmentId = entity.getKey();
            List<ScheduleDayStatisticVO> statisticList = entity.getValue();
            resultList.addAll(statisticList);

            DepartmentEntity departmentEntity = departmentMap.get(departmentId);
            if (departmentEntity == null) {
                continue;
            }
            Long managerId = departmentEntity.getManagerId();
            String managerName = managerNameMap.getOrDefault(managerId, StringConst.EMPTY);

            // 部门合计信息
            ScheduleDayStatisticVO statisticVO = new ScheduleDayStatisticVO();
            statisticVO.setEmployeeName(managerName + "_合计");
            statisticVO.setDepartmentId(departmentId);
            statisticVO.setDepartmentName(departmentEntity.getName());

            statisticVO.setWaybillCount(statisticList.stream().map(ScheduleDayStatisticVO::getWaybillCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setPayableAmount(statisticList.stream().map(ScheduleDayStatisticVO::getPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setFreightAmount(statisticList.stream().map(ScheduleDayStatisticVO::getFreightAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setOilCardAmount(statisticList.stream().map(ScheduleDayStatisticVO::getOilCardAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setOilCardRate(SmartBigDecimalUtil.dividePercent(statisticVO.getOilCardAmount(), statisticVO.getPayableAmount()));
            statisticVO.setInCompleteOrderCount(statisticList.stream().map(ScheduleDayStatisticVO::getInCompleteOrderCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setLastYearWaybillCount(statisticList.stream().map(ScheduleDayStatisticVO::getLastYearWaybillCount).reduce(NumberConst.ZERO, Integer::sum));
            statisticVO.setLastYearFreightAmount(statisticList.stream().map(ScheduleDayStatisticVO::getLastYearFreightAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 比例
            BigDecimal waybillCount = new BigDecimal(statisticVO.getWaybillCount());
            BigDecimal lastYearWaybillCount = new BigDecimal(statisticVO.getLastYearWaybillCount());
            BigDecimal rate = SmartBigDecimalUtil.dividePercent(SmartBigDecimalUtil.subtract(waybillCount, lastYearWaybillCount, 4), lastYearWaybillCount);
            statisticVO.setRate(rate);
            resultList.add(statisticVO);
        }
        return resultList;
    }



    /**
     * 调度日报表信息
     *
     * @param queryForm
     * @param statisticVOList
     */
    private void buildScheduleDayStatisticInfo(ScheduleDayStatisticQueryForm queryForm, List<ScheduleDayStatisticVO> statisticVOList) {
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return;
        }

        // 订单，查询调度未分配完的订单数量
        queryForm.setScheduleFlag(Boolean.FALSE);
        queryForm.setExcludeOrderStatus(OrderStatusEnum.CANCEL.getValue());
        List<EmployeeScheduleOrderDTO> orderDTOList = orderDao.selectBySchedule(queryForm);
        Map<Long, Integer> orderCountMap = orderDTOList.stream()
                .collect(Collectors.groupingBy(EmployeeScheduleOrderDTO::getScheduleId, Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)));

        // 运单
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<EmployeeScheduleWaybillDTO> waybillDTOList = waybillDao.selectBySchedule(queryForm);
        if (CollectionUtils.isEmpty(waybillDTOList)) {
            return;
        }
        Map<Long, List<EmployeeScheduleWaybillDTO>> waybillDTOMap = waybillDTOList.stream().collect(Collectors.groupingBy(EmployeeScheduleWaybillDTO::getScheduleId));

        // 所属公司
        List<EnterpriseEntity> enterpriseEntityList = Lists.newArrayList();
        Set<Long> allEnterpriseIdSet = waybillDTOList.stream().map(EmployeeScheduleWaybillDTO::getEnterpriseId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(allEnterpriseIdSet)) {
            enterpriseEntityList = enterpriseDao.selectBatchIds(allEnterpriseIdSet);
        }

        // 运单费用
        List<Long> waybillIdList = waybillDTOList.stream().map(EmployeeScheduleWaybillDTO::getWaybillId).collect(Collectors.toList());
        List<WaybillCostEntity> costEntityList = waybillCostDao.selectByWaybillIdList(waybillIdList);

        // 去年同期派车量
        queryForm.setStartDate(queryForm.getStartDate().minusYears(1));
        queryForm.setEndDate(queryForm.getEndDate().minusYears(1));
        List<EmployeeScheduleWaybillDTO> lastYearWaybillDTOList = waybillDao.selectBySchedule(queryForm);
        Map<Long, List<EmployeeScheduleWaybillDTO>> lastYearWaybillDTOMap = lastYearWaybillDTOList.stream().collect(Collectors.groupingBy(EmployeeScheduleWaybillDTO::getScheduleId));


        for (ScheduleDayStatisticVO statisticVO : statisticVOList) {
            Long scheduleId = statisticVO.getEmployeeId();
            List<EmployeeScheduleWaybillDTO> waybillList = waybillDTOMap.getOrDefault(scheduleId, Lists.newArrayList());
            BigDecimal payableAmount = waybillList.stream().map(EmployeeScheduleWaybillDTO::getPayableAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            Set<Long> enterpriseIdSet = waybillList.stream().map(EmployeeScheduleWaybillDTO::getEnterpriseId).collect(Collectors.toSet());
            String enterpriseName = enterpriseEntityList.stream()
                    .filter(e -> enterpriseIdSet.contains(e.getEnterpriseId()))
                    .map(EnterpriseEntity::getEnterpriseName)
                    .collect(Collectors.joining(StringConst.SEPARATOR));

            List<Long> waybillIds = waybillList.stream().map(EmployeeScheduleWaybillDTO::getWaybillId).collect(Collectors.toList());
            List<WaybillCostEntity> costList = costEntityList.stream().filter(e -> waybillIds.contains(e.getWaybillId())).collect(Collectors.toList());
            BigDecimal freightAmount = costList.stream()
                    .filter(e -> !CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory()))
                    .map(WaybillCostEntity::getCostAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal oilCardAmount = costList.stream()
                    .filter(e -> CostItemCategoryEnum.OIL_CARD_COST.equalsValue(e.getCostItemCategory()))
                    .map(WaybillCostEntity::getCostAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<EmployeeScheduleWaybillDTO> lastYearWaybillList = lastYearWaybillDTOMap.getOrDefault(scheduleId, Lists.newArrayList());
            BigDecimal lastYearAmount = lastYearWaybillList.stream()
                    .map(EmployeeScheduleWaybillDTO::getPayableAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            statisticVO.setEnterpriseName(enterpriseName);
            statisticVO.setWaybillCount(waybillList.size());
            statisticVO.setPayableAmount(payableAmount);
            statisticVO.setFreightAmount(freightAmount);
            statisticVO.setOilCardAmount(oilCardAmount);
            statisticVO.setOilCardRate(SmartBigDecimalUtil.dividePercent(oilCardAmount, payableAmount));
            statisticVO.setInCompleteOrderCount(orderCountMap.getOrDefault(scheduleId, NumberConst.ZERO));
            statisticVO.setLastYearWaybillCount((int) lastYearWaybillDTOMap.getOrDefault(scheduleId, Lists.newArrayList()).stream().count());
            statisticVO.setLastYearFreightAmount(lastYearAmount);
            // 比例
            BigDecimal waybillCount = new BigDecimal(statisticVO.getWaybillCount());
            BigDecimal lastYearWaybillCount = new BigDecimal(statisticVO.getLastYearWaybillCount());
            BigDecimal rate = SmartBigDecimalUtil.dividePercent(SmartBigDecimalUtil.subtract(waybillCount, lastYearWaybillCount, 4), lastYearWaybillCount);
            statisticVO.setRate(rate);
        }

    }

    /**
     * 调度日报表导出
     *
     * @param queryForm
     * @return
     */
    public List<ScheduleDayStatisticExportVO> exportScheduleDayList(ScheduleDayStatisticQueryForm queryForm) {
        LocalDate startDate = queryForm.getStartDate();
        LocalDate endDate = queryForm.getEndDate();
        if (startDate == null || endDate == null) {
            throw new BusinessException("请选择查询时间");
        }
        ResponseDTO<List<ScheduleDayStatisticVO>> resp = this.queryScheduleDayList(queryForm);
        if (!resp.getOk()) {
            throw new BusinessException(resp.getMsg());
        }
        List<ScheduleDayStatisticVO> statisticVOList = resp.getData();
        if (CollectionUtil.isEmpty(statisticVOList)) {
            return Lists.newArrayList();
        }

        // 设置序号
        List<ScheduleDayStatisticExportVO> exportVOList = SmartBeanUtil.copyList(statisticVOList, ScheduleDayStatisticExportVO.class);
        int index = 1;
        Long departmentId = statisticVOList.get(0).getDepartmentId();
        for (int i = 0; i < exportVOList.size(); i++) {
            if (i != exportVOList.size() - 1 && !Objects.equals(exportVOList.get(i + 1).getDepartmentId(), departmentId)) {
                index = 1;
                departmentId = exportVOList.get(i + 1).getDepartmentId();
                exportVOList.get(i).setSerialNo("部门合计");
                continue;
            }
            if (i == exportVOList.size() - 1) {
                exportVOList.get(i).setSerialNo("部门合计");
                continue;
            }
            exportVOList.get(i).setSerialNo(String.valueOf(index++));
        }
        return exportVOList;
    }
}
