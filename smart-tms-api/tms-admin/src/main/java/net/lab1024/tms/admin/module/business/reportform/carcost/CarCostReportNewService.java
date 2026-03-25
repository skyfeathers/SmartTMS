package net.lab1024.tms.admin.module.business.reportform.carcost;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.CarCostBasicInfoDao;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoVO;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndDao;
import net.lab1024.tms.admin.module.business.carcost.cashpay.CarCostCashPayDao;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.CarCostCashReceiveDao;
import net.lab1024.tms.admin.module.business.carcost.category.CarCostCategoryDao;
import net.lab1024.tms.admin.module.business.carcost.etcpay.CarCostEtcPayDao;
import net.lab1024.tms.admin.module.business.carcost.oilcardinitialend.CarCostOilCardInitialEndDao;
import net.lab1024.tms.admin.module.business.carcost.oilcardreceive.CarCostOilCardReceiveDao;
import net.lab1024.tms.admin.module.business.carcost.oilpay.CarCostOilPayDao;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.carcost.ureapay.CarCostUreaPayDao;
import net.lab1024.tms.admin.module.business.driver.service.DriverManager;
import net.lab1024.tms.admin.module.business.insurance.InsuranceService;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceVO;
import net.lab1024.tms.admin.module.business.maintenance.dao.MaintenanceContentDao;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceVO;
import net.lab1024.tms.admin.module.business.maintenance.service.MaintenanceService;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderPathDao;
import net.lab1024.tms.admin.module.business.repair.domain.RepairContentVO;
import net.lab1024.tms.admin.module.business.repair.domain.RepairVO;
import net.lab1024.tms.admin.module.business.repair.service.RepairService;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto.*;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostDayOrMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostDayStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.form.CarCostMonthStatisticQueryForm;
import net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo.*;
import net.lab1024.tms.admin.module.business.review.domain.ReviewVO;
import net.lab1024.tms.admin.module.business.review.service.ReviewService;
import net.lab1024.tms.admin.module.business.shipper.manager.ShipperManager;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleSimpleVO;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.*;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCategoryVO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostOilCardInitialEndVO;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceTypeEnum;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceContentEntity;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.order.domain.OrderPathEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author lidoudou
 * @description:
 * @date 2023/11/10 19:52
 */
@Service
public class CarCostReportNewService {
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private OrderPathDao orderPathDao;
    @Autowired
    private RepairService repairService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MaintenanceService maintenanceService;
    @Autowired
    private CarCostBasicInfoDao carCostBasicInfoDao;
    @Autowired
    private CarCostOilCardInitialEndDao carCostOilCardInitialEndDao;
    @Autowired
    private CarCostCashInitialEndDao carCostCashInitialEndDao;
    @Autowired
    private DriverManager driverManager;
    @Autowired
    private CarCostCashPayDao carCostCashPayDao;
    @Autowired
    private CarCostOilPayDao carCostOilPayDao;
    @Autowired
    private CarCostEtcPayDao carCostEtcPayDao;
    @Autowired
    private CarCostUreaPayDao carCostUreaPayDao;
    @Autowired
    private CarCostTabulationDao carCostTabulationDao;
    @Autowired
    private ShipperManager shipperManager;
    @Autowired
    private MaintenanceContentDao maintenanceContentDao;
    @Autowired
    private CarCostCategoryDao carCostCategoryDao;
    @Autowired
    private CarCostOilCardReceiveDao carCostOilCardReceiveDao;
    @Autowired
    private CarCostCashReceiveDao carCostCashReceiveDao;

    public ResponseDTO<List<CarCostDayReportVO>> queryDayCarCost(CarCostDayStatisticQueryForm queryForm) {
        LocalDate startTime = queryForm.getStartTime();
        LocalDate endTime = queryForm.getEndTime();
        if (null == startTime || null == endTime) {
            return ResponseDTO.userErrorParam("请选择创建时间");
        }
        List<Long> vehicleIdList = queryForm.getVehicleIdList();
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            List<VehicleSimpleVO> vehicleList = vehicleDao.selectByBusinessModeAndDisableFlag(queryForm.getEnterpriseId(), BusinessModeEnum.INNER_MANAGEMENT.getValue(), Boolean.FALSE, Boolean.FALSE);
            vehicleIdList = vehicleList.stream().map(VehicleSimpleVO::getVehicleId).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            return ResponseDTO.userErrorParam("请选择车辆后查询");
        }
        // 车牌号Map
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        Map<Long, String> vehicleNumberMap = vehicleEntityList.stream()
                .collect(Collectors.toMap(e -> e.getVehicleId(), e -> e.getVehicleNumber()));

        // 维修信息
        List<CarCostDayReportVO> repairVOList = getRepairList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);
        // 保险
        List<CarCostDayReportVO> insuranceVOList = getInsuranceList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);
        // 审车
        List<CarCostDayReportVO> reviewVOList = getReviewList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);
        // 保养
        List<CarCostDayReportVO> maintenanceVOList = getMaintenanceList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);
        // 运单费用
        List<CarCostDayReportVO> waybillCarCostList = queryWaybillCarCostList(queryForm);

        List<CarCostDayReportVO> resultList = Lists.newArrayList();
        resultList.addAll(repairVOList);
        resultList.addAll(insuranceVOList);
        resultList.addAll(reviewVOList);
        resultList.addAll(maintenanceVOList);
        resultList.addAll(waybillCarCostList);
        resultList.sort((Comparator.comparing(CarCostDayReportVO::getCreateTime)));

        resultList.forEach(item -> {
            if (item.getCreateTime() != null) {
                item.setCreateTimeStr(SmartLocalDateUtil.formatYMDHM(item.getCreateTime()));
            }
            // 设置车牌号
            item.setVehicleNumber(vehicleNumberMap.get(item.getVehicleId()));
            // 统计金额计算
            // 工资支出计算
            BigDecimal salaryAmount = BigDecimal.ZERO;
            if (null != item.getCarCostBasicInfoVO()) {
                salaryAmount = item.getCarCostBasicInfoVO().getSalaryTotal();
            }
            // 油费支出计算
            BigDecimal oilPayAmount = BigDecimal.ZERO;
            // 油卡支出计算
            BigDecimal oilCardPayAmount = BigDecimal.ZERO;
            BigDecimal oilConsumption = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(item.getOilCardInfoVOList())) {
                oilCardPayAmount = item.getOilCardInfoVOList().stream().map(OilCardInfoVO::getOilCardPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal cashPayAmount = item.getOilCardInfoVOList().stream().map(OilCardInfoVO::getCashPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                oilPayAmount = SmartBigDecimalUtil.Amount.add(oilCardPayAmount, cashPayAmount);

                oilConsumption = item.getOilCardInfoVOList().stream().map(OilCardInfoVO::getOilConsumption).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            // 现金支出计算
            BigDecimal cashAmount = BigDecimal.ZERO;
            if (null != item.getCashInfoVO()) {
                cashAmount = item.getCashInfoVO().getSubtotal();
            }

            // ETC支出金额
            BigDecimal etcAmount = BigDecimal.ZERO;
            if (null != item.getEtcVO()) {
                etcAmount = item.getEtcVO().getEtcAmount();
            }
            // 油卡支出金额
            BigDecimal ureaAmount = BigDecimal.ZERO;
            if (null != item.getUreaVO()) {
                ureaAmount = item.getUreaVO().getUreaAmount();
            }

            if (null != item.getCarCostBasicInfoVO()) {
                BigDecimal gpsMileage = item.getCarCostBasicInfoVO().getGpsMileage();
                // 油耗元： 加油金额/ GPS公里数
                item.setAverageOilAmount(SmartBigDecimalUtil.Amount.divide(oilPayAmount, gpsMileage));

                //尿素耗元：尿素/GPS公里数
                item.setAverageUreaAmount(SmartBigDecimalUtil.Amount.divide(ureaAmount, gpsMileage));

                //运费元/km：单趟应收运费/GPS公里数
                item.setAverageFrightAmount(SmartBigDecimalUtil.Amount.divide(item.getPayableAmount(), gpsMileage));

                item.setAverageHundredKmFrightAmount(SmartBigDecimalUtil.Amount.divide(oilConsumption, gpsMileage));
            }


            if (item.getPayableAmount() == null) {
                // 维修
                BigDecimal repairTotalAmount = item.getRepairAmount() == null ? BigDecimal.ZERO : item.getRepairAmount();
                // 保险
                BigDecimal insuranceAmount = item.getInsuranceAmount() == null ? BigDecimal.ZERO : item.getInsuranceAmount();
                // 审车
                BigDecimal reviewAmount = item.getReviewAmount() == null ? BigDecimal.ZERO : item.getReviewAmount();
                // 保养
                BigDecimal maintenanceAmount = item.getMaintenanceAmount() == null ? BigDecimal.ZERO : item.getMaintenanceAmount();
                // 定点维修等单独在车上维护的内容
                item.setProfitAmount(BigDecimal.ZERO.subtract(repairTotalAmount).subtract(insuranceAmount).subtract(reviewAmount).subtract(maintenanceAmount));
            }else {
                //毛利：单趟应收运费 - 司机工资支出 - 油卡支出 - 现金支出 - ETC - 尿素
                item.setProfitAmount(item.getPayableAmount().subtract(salaryAmount).subtract(oilPayAmount).subtract(cashAmount).subtract(etcAmount).subtract(ureaAmount));
            }
            item.setOilConsumption(oilConsumption);
        });
        // 添加统计行
        this.addSummaryRow(resultList);
        return ResponseDTO.ok(resultList);
    }


    private void addSummaryRow(List<CarCostDayReportVO> statisticVOList) {
        CarCostDayReportVO summaryRow = new CarCostDayReportVO();
        summaryRow.setCreateTimeStr("合计");
        summaryRow.setTotalWeight(statisticVOList.stream().map(e -> e.getTotalWeight() == null ? BigDecimal.ZERO : e.getTotalWeight()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryRow.setPayableAmount(statisticVOList.stream().map(e -> e.getPayableAmount() == null ? BigDecimal.ZERO : e.getPayableAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));

        CarCostBasicInfoVO summaryCarCostBasicInfoVO = summaryBasicInfo(statisticVOList);
        summaryRow.setCarCostBasicInfoVO(summaryCarCostBasicInfoVO);

        List<OilCardInfoVO> summaryOilCardInfoVOList = summaryOilCardInfoVO(statisticVOList);
        summaryRow.setOilCardInfoVOList(summaryOilCardInfoVOList);

        CashInfoVO summaryCashInfoVO = summaryCashInfoVO(statisticVOList);
        summaryRow.setCashInfoVO(summaryCashInfoVO);

        CarCostDayEtcVO etcVO = summaryEtcVO(statisticVOList);
        summaryRow.setEtcVO(etcVO);

        CarCostDayUreaVO ureaVO = summaryUreaVO(statisticVOList);
        summaryRow.setUreaVO(ureaVO);

        summaryRow.setRepairAmount(statisticVOList.stream().map(e -> e.getRepairAmount() == null ? BigDecimal.ZERO : e.getRepairAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryRow.setInsuranceAmount(statisticVOList.stream().map(e -> e.getInsuranceAmount() == null ? BigDecimal.ZERO : e.getInsuranceAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryRow.setReviewAmount(statisticVOList.stream().map(e -> e.getReviewAmount() == null ? BigDecimal.ZERO : e.getReviewAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryRow.setMaintenanceAmount(statisticVOList.stream().map(e -> e.getMaintenanceAmount() == null ? BigDecimal.ZERO : e.getMaintenanceAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal gpsMileage = summaryRow.getCarCostBasicInfoVO().getGpsMileage();
        // 油耗元： 加油金额/ GPS公里数
        BigDecimal oilPayAmount = BigDecimal.ZERO;
        BigDecimal oilConsumption = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(summaryRow.getOilCardInfoVOList())) {
            BigDecimal oilCardPayAmount = summaryRow.getOilCardInfoVOList().stream().map(OilCardInfoVO::getOilCardPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal cashPayAmount = summaryRow.getOilCardInfoVOList().stream().map(OilCardInfoVO::getCashPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            oilPayAmount = SmartBigDecimalUtil.Amount.add(oilCardPayAmount, cashPayAmount);
            oilConsumption = summaryRow.getOilCardInfoVOList().stream().map(OilCardInfoVO::getOilConsumption).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        summaryRow.setAverageOilAmount(SmartBigDecimalUtil.Amount.divide(oilPayAmount, gpsMileage));
        //尿素耗元：尿素/GPS公里数
        summaryRow.setAverageUreaAmount(SmartBigDecimalUtil.Amount.divide(summaryRow.getUreaVO().getUreaAmount(), gpsMileage));
        //运费元/km：单趟应收运费/GPS公里数
        summaryRow.setAverageFrightAmount(SmartBigDecimalUtil.Amount.divide(summaryRow.getPayableAmount(), gpsMileage));
        summaryRow.setAverageHundredKmFrightAmount(SmartBigDecimalUtil.Amount.divide(oilConsumption, gpsMileage));

        summaryRow.setProfitAmount(statisticVOList.stream().map(e -> e.getProfitAmount() == null ? BigDecimal.ZERO : e.getProfitAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
        summaryRow.setOilConsumption(statisticVOList.stream().map(e -> e.getOilConsumption() == null ? BigDecimal.ZERO : e.getOilConsumption()).reduce(BigDecimal.ZERO, BigDecimal::add));

        statisticVOList.add(summaryRow);
    }

    private CarCostDayUreaVO summaryUreaVO(List<CarCostDayReportVO> statisticVOList) {
        BigDecimal ureaAmount = BigDecimal.ZERO;
        for (CarCostDayReportVO carCostDayReportVO : statisticVOList) {
            CarCostDayUreaVO carCostDayUreaVO = carCostDayReportVO.getUreaVO();
            if (carCostDayUreaVO == null) {
                continue;
            }
            ureaAmount = ureaAmount.add(carCostDayUreaVO.getUreaAmount() == null ? BigDecimal.ZERO : carCostDayUreaVO.getUreaAmount());
        }
        CarCostDayUreaVO carCostDayUreaVO = new CarCostDayUreaVO();
        carCostDayUreaVO.setUreaAmount(ureaAmount);
        return carCostDayUreaVO;
    }

    private CarCostDayEtcVO summaryEtcVO(List<CarCostDayReportVO> statisticVOList) {
        BigDecimal etcAmount = BigDecimal.ZERO;
        for (CarCostDayReportVO carCostDayReportVO : statisticVOList) {
            CarCostDayEtcVO etcVO = carCostDayReportVO.getEtcVO();
            if (etcVO == null) {
                continue;
            }
            etcAmount = etcAmount.add(etcVO.getEtcAmount() == null ? BigDecimal.ZERO : etcVO.getEtcAmount());
        }
        CarCostDayEtcVO etcVO = new CarCostDayEtcVO();
        etcVO.setEtcAmount(etcAmount);
        return etcVO;
    }

    private CashInfoVO summaryCashInfoVO(List<CarCostDayReportVO> statisticVOList) {
        BigDecimal rechargeAmount = BigDecimal.ZERO;
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CarCostDayReportVO carCostDayReportVO : statisticVOList) {
            CashInfoVO cashInfoVO = carCostDayReportVO.getCashInfoVO();
            if (cashInfoVO == null) {
                continue;
            }
            rechargeAmount = rechargeAmount.add(cashInfoVO.getRechargeAmount() == null ? BigDecimal.ZERO : cashInfoVO.getRechargeAmount());
            subtotal = subtotal.add(cashInfoVO.getSubtotal() == null ? BigDecimal.ZERO : cashInfoVO.getSubtotal());
        }
        CashInfoVO cashInfoVO = new CashInfoVO();
        cashInfoVO.setRechargeAmount(rechargeAmount);
        cashInfoVO.setSubtotal(subtotal);
        return cashInfoVO;
    }

    private List<OilCardInfoVO> summaryOilCardInfoVO(List<CarCostDayReportVO> statisticVOList) {
        List<OilCardInfoVO> allOilList = Lists.newArrayList();
        statisticVOList.forEach(e->{
            if (CollectionUtils.isNotEmpty(e.getOilCardInfoVOList())){
                allOilList.addAll(e.getOilCardInfoVOList());
            }
        });
        List<OilCardInfoVO> resultList = Lists.newArrayList();
        Map<Integer, List<OilCardInfoVO>> listMap = allOilList.stream().collect(groupingBy(OilCardInfoVO::getFuelType));
        for(Map.Entry<Integer, List<OilCardInfoVO>> entry : listMap.entrySet()) {
            Integer fuelType = entry.getKey();
            List<OilCardInfoVO> infoVOList = entry.getValue();

            OilCardInfoVO oilCardInfoVO = new OilCardInfoVO();
            oilCardInfoVO.setFuelType(fuelType);
            oilCardInfoVO.setOilCardReceiveAmount(infoVOList.stream().map(e -> e.getOilCardReceiveAmount() == null ? BigDecimal.ZERO : e.getOilCardReceiveAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            oilCardInfoVO.setOilCardPayAmount(infoVOList.stream().map(e -> e.getOilCardPayAmount() == null ? BigDecimal.ZERO : e.getOilCardPayAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            oilCardInfoVO.setCashPayAmount(infoVOList.stream().map(e -> e.getCashPayAmount() == null ? BigDecimal.ZERO : e.getCashPayAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            oilCardInfoVO.setSubtotal(infoVOList.stream().map(e -> e.getSubtotal() == null ? BigDecimal.ZERO : e.getSubtotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
            oilCardInfoVO.setCashPayOilConsumption(infoVOList.stream().map(e -> e.getCashPayOilConsumption() == null ? BigDecimal.ZERO : e.getCashPayOilConsumption()).reduce(BigDecimal.ZERO, BigDecimal::add));
            oilCardInfoVO.setOilCardPayOilConsumption(infoVOList.stream().map(e -> e.getOilCardPayOilConsumption() == null ? BigDecimal.ZERO : e.getOilCardPayOilConsumption()).reduce(BigDecimal.ZERO, BigDecimal::add));
            oilCardInfoVO.setOilConsumption(infoVOList.stream().map(e -> e.getOilConsumption() == null ? BigDecimal.ZERO : e.getOilConsumption()).reduce(BigDecimal.ZERO, BigDecimal::add));
            resultList.add(oilCardInfoVO);
        }
        return resultList;
    }

    private CarCostBasicInfoVO summaryBasicInfo(List<CarCostDayReportVO> statisticVOList) {
        BigDecimal highSpeedMileage = BigDecimal.ZERO;
        BigDecimal lowSpeedMileage = BigDecimal.ZERO;
        BigDecimal gpsMileage = BigDecimal.ZERO;
        BigDecimal oilConsumption = BigDecimal.ZERO;
        BigDecimal basicSalary = BigDecimal.ZERO;
        BigDecimal allowance = BigDecimal.ZERO;
        BigDecimal commissionSalary = BigDecimal.ZERO;
        Integer attendanceDays = 0;
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CarCostDayReportVO carCostDayReportVO : statisticVOList) {
            CarCostBasicInfoVO basicInfoVO = carCostDayReportVO.getCarCostBasicInfoVO();
            if (basicInfoVO == null) {
                continue;
            }
            highSpeedMileage = highSpeedMileage.add(basicInfoVO.getHighSpeedMileage() == null ? BigDecimal.ZERO : basicInfoVO.getHighSpeedMileage());
            lowSpeedMileage = lowSpeedMileage.add(basicInfoVO.getLowSpeedMileage() == null ? BigDecimal.ZERO : basicInfoVO.getLowSpeedMileage());
            gpsMileage = gpsMileage.add(basicInfoVO.getGpsMileage() == null ? BigDecimal.ZERO : basicInfoVO.getGpsMileage());
            oilConsumption = oilConsumption.add(basicInfoVO.getOilConsumption() == null ? BigDecimal.ZERO : basicInfoVO.getOilConsumption());
            basicSalary = basicSalary.add(basicInfoVO.getBasicSalary() == null ? BigDecimal.ZERO : basicInfoVO.getBasicSalary());
            allowance = allowance.add(basicInfoVO.getAllowance() == null ? BigDecimal.ZERO : basicInfoVO.getAllowance());
            commissionSalary = commissionSalary.add(basicInfoVO.getCommissionSalary() == null ? BigDecimal.ZERO : basicInfoVO.getCommissionSalary());
            attendanceDays = attendanceDays + (basicInfoVO.getAttendanceDays() == null ? 0 : basicInfoVO.getAttendanceDays());
            subtotal = subtotal.add(basicInfoVO.getSalaryTotal() == null ? BigDecimal.ZERO : basicInfoVO.getSalaryTotal());
        }
        CarCostBasicInfoVO summaryCarCostBasicInfoVO = new CarCostBasicInfoVO();
        summaryCarCostBasicInfoVO.setHighSpeedMileage(highSpeedMileage);
        summaryCarCostBasicInfoVO.setLowSpeedMileage(lowSpeedMileage);
        summaryCarCostBasicInfoVO.setGpsMileage(gpsMileage);
        summaryCarCostBasicInfoVO.setOilConsumption(oilConsumption);
        summaryCarCostBasicInfoVO.setBasicSalary(basicSalary);
        summaryCarCostBasicInfoVO.setAllowance(allowance);
        summaryCarCostBasicInfoVO.setCommissionSalary(commissionSalary);
        summaryCarCostBasicInfoVO.setAttendanceDays(attendanceDays);
        summaryCarCostBasicInfoVO.setSalaryTotal(subtotal);
        return summaryCarCostBasicInfoVO;
    }

    /**
     * 自有车费用统计
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<CarCostDayOrMonthStatisticVO>> dayOrMonthStatistic(CarCostDayOrMonthStatisticQueryForm queryForm) {
        Integer businessMode = BusinessModeEnum.INNER_MANAGEMENT.getValue();
        Long enterpriseId = queryForm.getEnterpriseId();

        List<VehicleSimpleVO> vehicleList = vehicleDao.selectByBusinessModeAndDisableFlag(enterpriseId, BusinessModeEnum.INNER_MANAGEMENT.getValue(), Boolean.FALSE, Boolean.FALSE);
        if (CollectionUtils.isEmpty(vehicleList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        String keyword = queryForm.getKeyword();
        List<Long> vehicleIdList = vehicleList.stream().map(VehicleSimpleVO::getVehicleId).collect(Collectors.toList());
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectByVehicleIdAndKeyword(keyword, enterpriseId, vehicleIdList, false);
        // 自有车费用
        vehicleIdList = vehicleEntityList.stream().map(e -> e.getVehicleId()).collect(Collectors.toList());
        CarCostDayStatisticQueryForm dayStatisticQueryForm = new CarCostDayStatisticQueryForm();
        dayStatisticQueryForm.setVehicleIdList(vehicleIdList);
        dayStatisticQueryForm.setStartTime(queryForm.getStartTime());
        dayStatisticQueryForm.setEndTime(queryForm.getEndTime());
        List<CarCostDayReportVO> allCarCostList = this.queryWaybillCarCostList(dayStatisticQueryForm);
        Map<Long, List<CarCostDayReportVO>> carCostMap = allCarCostList.stream().collect(groupingBy(e -> e.getVehicleId()));

        List<CarCostDayOrMonthStatisticVO> statisticVOList = vehicleEntityList.stream().map(vehicle -> {
            Long vehicleId = vehicle.getVehicleId();
            List<CarCostDayReportVO> carCostList = carCostMap.getOrDefault(vehicleId, Lists.newArrayList());
            // 工资
            BigDecimal salaryAmount = carCostList.stream()
                    .filter(e -> e.getCarCostBasicInfoVO() != null)
                    .map(e -> e.getCarCostBasicInfoVO().getSalaryTotal())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 油费支出
            BigDecimal oilCardPayAmount = carCostList.stream()
                    .filter(e -> CollUtil.isNotEmpty(e.getOilCardInfoVOList()))
                    .map(e -> e.getOilCardInfoVOList().stream()
                            .map(OilCardInfoVO::getOilCardPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal cashPayAmount = carCostList.stream()
                    .filter(e -> CollUtil.isNotEmpty(e.getOilCardInfoVOList()))
                    .map(e -> e.getOilCardInfoVOList().stream()
                            .map(OilCardInfoVO::getCashPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal oilAmount = SmartBigDecimalUtil.Amount.add(oilCardPayAmount, cashPayAmount);
            // 现金支出
            BigDecimal cashAmount = carCostList.stream()
                    .filter(e -> e.getCashInfoVO() != null)
                    .map(e -> e.getCashInfoVO().getSubtotal())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // ETC支出
            BigDecimal etcAmount = carCostList.stream()
                    .filter(e -> e.getEtcVO() != null)
                    .map(e -> e.getEtcVO().getEtcAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 尿素支出
            BigDecimal ureaAmount = carCostList.stream()
                    .filter(e -> e.getUreaVO() != null)
                    .map(e -> e.getUreaVO().getUreaAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            CarCostDayOrMonthStatisticVO statisticVO = new CarCostDayOrMonthStatisticVO();
            statisticVO.setVehicleId(vehicleId);
            statisticVO.setVehicleNumber(vehicle.getVehicleNumber());
            statisticVO.setIncome(carCostList.stream().map(e -> e.getPayableAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            statisticVO.setExpenditure(salaryAmount.add(oilAmount).add(cashAmount).add(etcAmount).add(ureaAmount));
            statisticVO.setProfit(statisticVO.getIncome().subtract(statisticVO.getExpenditure()));
            return statisticVO;
        }).sorted(Comparator.comparing(CarCostDayOrMonthStatisticVO::getProfit).reversed()).collect(Collectors.toList());

        Boolean exceptionFlag = queryForm.getExceptionFlag();
        if (exceptionFlag != null) {
            statisticVOList = statisticVOList.stream()
                    .filter(e -> (exceptionFlag ?
                            e.getProfit().compareTo(BigDecimal.ZERO) < 0 : 
                            e.getProfit().compareTo(BigDecimal.ZERO) >= 0))
                    .collect(Collectors.toList());
        }

        return ResponseDTO.ok(statisticVOList);
    }


    /**
     * 导出日统计报表
     *
     * @param queryForm
     * @return
     */
    public Workbook exportDayStatisticsReport(CarCostDayStatisticQueryForm queryForm) {
        ResponseDTO<List<CarCostDayReportVO>> carCostDayListResp = queryDayCarCost(queryForm);
        if (!carCostDayListResp.getOk()) {
            throw new BusinessException(carCostDayListResp.getMsg());
        }

        // 设置导出列
        List<ExcelExportEntity> columnList = buildDayExportColumnList();

        // 获取导出table
        List<CarCostDayReportVO> carCostDayReportList = carCostDayListResp.getData();


        List<CarCostCategoryVO> cashCarCostCategoryList = carCostCategoryDao.selectByCostType(CarCostCategoryCostTypeEnum.CASH_COST.getValue(), Boolean.FALSE);
        Field[] fields = CarCostDayReportVO.class.getDeclaredFields();
        Field[] basicFields = CarCostBasicInfoVO.class.getDeclaredFields();
        Field[] oilCardFields = OilCardInfoVO.class.getDeclaredFields();

        // 设置导出内容
        List<Map<String, Object>> list = new ArrayList<>();
        for (CarCostDayReportVO carCostVO : carCostDayReportList) {
            Map map = new HashMap<String, Object>();
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(carCostVO));
            for (Field field : fields) {
                field.setAccessible(true);
                Excel excelField = field.getAnnotation(Excel.class);
                if (null == excelField) {
                    continue;
                }
                map.put(field.getName(), jsonObject.getString(field.getName()));
            }

            if (null != carCostVO.getCarCostBasicInfoVO()) {
                JSONObject basicJsonObject = JSON.parseObject(JSON.toJSONString(carCostVO.getCarCostBasicInfoVO()));
                for (Field field : basicFields) {
                    field.setAccessible(true);
                    Excel excelField = field.getAnnotation(Excel.class);
                    if (null == excelField) {
                        continue;
                    }
                    map.put(field.getName(), basicJsonObject.getString(field.getName()));
                }
            }

            List<OilCardInfoVO> carCostVOList = carCostVO.getOilCardInfoVOList();
            if (CollectionUtils.isNotEmpty(carCostVOList)) {
                for (int i = 1; i <= carCostVOList.size(); i++) {
                    OilCardInfoVO oilCardInfoVO = carCostVOList.get(i - 1);
                    JSONObject oilCardJsonObject = JSON.parseObject(JSON.toJSONString(oilCardInfoVO));
                    for (Field field : oilCardFields) {
                        field.setAccessible(true);
                        Excel excelField = field.getAnnotation(Excel.class);
                        if (null == excelField) {
                            continue;
                        }
                        map.put(i + "_" + field.getName(), oilCardJsonObject.getString(field.getName()));
                    }
                }
            }

            if (null != carCostVO.getCashInfoVO()) {
                CashInfoVO cashInfoVO = carCostVO.getCashInfoVO();
                map.put("initialAmount", cashInfoVO.getInitialAmount());
                map.put("rechargeAmount", cashInfoVO.getRechargeAmount());
                map.put("rechargeRemark", cashInfoVO.getRechargeRemark());

                List<CarCostItemInfoVO> cashItemList = cashInfoVO.getItemInfoVOList();
                Map<Long, BigDecimal> cashItemMap = Maps.newHashMap();
                if (CollectionUtils.isNotEmpty(cashItemList)) {
                    cashItemMap = cashItemList.stream().collect(Collectors.toMap(CarCostItemInfoVO::getCategoryId, CarCostItemInfoVO::getAmount));
                }
                for (CarCostCategoryVO cashCategory : cashCarCostCategoryList) {
                    map.put(cashCategory.getCategoryId(), cashItemMap.getOrDefault(cashCategory.getCategoryId(), BigDecimal.ZERO));
                }
                map.put("subtotal", cashInfoVO.getSubtotal());
                map.put("endAmount", cashInfoVO.getEndAmount());
            }

            if (null != carCostVO.getEtcVO()) {
                map.put("etcAmount", carCostVO.getEtcVO().getEtcAmount());
            }
            if (null != carCostVO.getUreaVO()) {
                map.put("etcAmount", carCostVO.getUreaVO().getUreaAmount());
            }

            list.add(map);
        }
        // 设置批注
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("自有车费用登记", "自有车费用登记"), columnList, list);
        buildComment(workbook, carCostDayReportList, columnList);
        return workbook;
    }

    private void buildComment(Workbook workbook, List<CarCostDayReportVO> carCostDayReportList, List<ExcelExportEntity> columnList) {
        Drawing patriarch = workbook.getSheetAt(NumberConst.ZERO).createDrawingPatriarch();

        Map<Object, Integer> columnOrderNumMap = columnList.stream().collect(Collectors.toMap(e -> e.getKey(), ExcelExportEntity::getOrderNum));
        // 起始行数
        int row = 3;
        if (CollectionUtils.isNotEmpty(carCostDayReportList)) {
            for (CarCostDayReportVO carCostVO : carCostDayReportList) {

                List<OilCardInfoVO> oilCardInfoVOList = carCostVO.getOilCardInfoVOList();
                if (CollectionUtils.isNotEmpty(oilCardInfoVOList)) {
                    for (OilCardInfoVO oilCardInfoVO : oilCardInfoVOList) {
                        Integer fuelType = oilCardInfoVO.getFuelType();
                        setComment(workbook, patriarch, row, columnOrderNumMap.get(fuelType + "_oilCardReceiveAmount"), oilCardInfoVO.getOilCardReceiveRemark());
                        setComment(workbook, patriarch, row, columnOrderNumMap.get(fuelType + "_oilCardPayAmount"), oilCardInfoVO.getOilCardPayRemark());
                        setComment(workbook, patriarch, row, columnOrderNumMap.get(fuelType + "_cashPayAmount"), oilCardInfoVO.getCashPayRemark());
                    }

                }

                // 设置现金支出的批注（备注）
                CashInfoVO cashInfoVO = carCostVO.getCashInfoVO();
                if (null != cashInfoVO) {
                    setComment(workbook, patriarch, row, columnOrderNumMap.get("rechargeAmount"), cashInfoVO.getRechargeRemark());

                    List<CarCostItemInfoVO> cashCarCostItemList = cashInfoVO.getItemInfoVOList();
                    if (CollectionUtils.isNotEmpty(cashCarCostItemList)) {
                        for (CarCostItemInfoVO carCostItemInfoVO : cashCarCostItemList) {
                            int cellNo = columnOrderNumMap.getOrDefault(carCostItemInfoVO.getCategoryId(), NumberConst.ZERO);
                            setComment(workbook, patriarch, row, cellNo, carCostItemInfoVO.getRemark());
                        }
                    }
                }

                row++;
            }
        }
    }

    private void setComment(Workbook workbook, Drawing patriarch, Integer row, Integer cellNo, String remark) {
        if (null == cellNo) {
            return;
        }
        if (StringUtils.isBlank(remark)) {
            return;
        }
        Cell cell = workbook.getSheetAt(NumberConst.ZERO).getRow(row).getCell(cellNo);
        HSSFComment comment = (HSSFComment) patriarch.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
        //输入批注信息
        comment.setString(new HSSFRichTextString(remark));
        //将批注添加到单元格对象中
        cell.setCellComment(comment);
    }

    /**
     * 构建导出列
     *
     * @return
     */
    private List<ExcelExportEntity> buildDayExportColumnList() {
        List<ExcelExportEntity> columnList = Lists.newArrayList();
        Field[] fields = CarCostDayReportVO.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Excel excelField = field.getAnnotation(Excel.class);
            if (null == excelField) {
                continue;
            }
            ExcelExportEntity excelExportEntity = new ExcelExportEntity(excelField.name(), field.getName());
            excelExportEntity.setWidth(excelField.width());
            excelExportEntity.setType(excelField.type());
            excelExportEntity.setFormat(excelField.format());
            String orderNum = excelField.orderNum();
            if (StringUtils.isNotBlank(orderNum)) {
                excelExportEntity.setOrderNum(Integer.parseInt(orderNum));
            }
            columnList.add(excelExportEntity);
        }

        Integer beginOrderNum = columnList.size();

        Field[] basicInfoFields = CarCostBasicInfoVO.class.getDeclaredFields();
        for (Field field : basicInfoFields) {
            field.setAccessible(true);
            Excel excelField = field.getAnnotation(Excel.class);
            if (null == excelField) {
                continue;
            }
            ExcelExportEntity excelExportEntity = new ExcelExportEntity(excelField.name(), field.getName());
            excelExportEntity.setWidth(excelField.width());
            excelExportEntity.setType(excelField.type());
            excelExportEntity.setOrderNum(beginOrderNum++);
            columnList.add(excelExportEntity);
        }

        // 设置油卡
        List<OilCardFuelTypeEnum> fuelTypeEnumList = SmartBaseEnumUtil.enumList(OilCardFuelTypeEnum.class);
        for (OilCardFuelTypeEnum item : fuelTypeEnumList) {
            Field[] oilCardFields = OilCardInfoVO.class.getDeclaredFields();
            for (Field field : oilCardFields) {
                beginOrderNum++;
                field.setAccessible(true);
                Excel excelField = field.getAnnotation(Excel.class);
                if (null == excelField) {
                    continue;
                }
                ExcelExportEntity excelExportEntity = new ExcelExportEntity(excelField.name(), item.getValue() + "_" + field.getName());
                excelExportEntity.setWidth(excelField.width());
                excelExportEntity.setOrderNum(beginOrderNum);
                excelExportEntity.setType(excelField.type());
                excelExportEntity.setGroupName("车辆油卡-" + item.getDesc());
                columnList.add(excelExportEntity);
            }
        }

        // 现金开支
        beginOrderNum++;
        ExcelExportEntity cashInitAmountCell = new ExcelExportEntity("初始现金", "initialAmount");
        cashInitAmountCell.setOrderNum(beginOrderNum);
        cashInitAmountCell.setGroupName("现金开支");
        columnList.add(cashInitAmountCell);

        beginOrderNum++;
        ExcelExportEntity cashReceiveAmountCell = new ExcelExportEntity("领取出车款", "rechargeAmount");
        cashReceiveAmountCell.setOrderNum(beginOrderNum);
        cashReceiveAmountCell.setGroupName("现金开支");

        columnList.add(cashReceiveAmountCell);

        List<CarCostCategoryVO> cashCarCostCategoryList = carCostCategoryDao.selectByCostType(CarCostCategoryCostTypeEnum.CASH_COST.getValue(), Boolean.FALSE);
        for (CarCostCategoryVO cashCategory : cashCarCostCategoryList) {
            beginOrderNum++;
            ExcelExportEntity cashCategoryCell = new ExcelExportEntity(cashCategory.getCategoryName(), cashCategory.getCategoryId());
            cashCategoryCell.setOrderNum(beginOrderNum);
            cashCategoryCell.setGroupName("现金开支");
            columnList.add(cashCategoryCell);
        }

        beginOrderNum++;
        ExcelExportEntity subtotalAmountCell = new ExcelExportEntity("开支小计", "subtotal");
        subtotalAmountCell.setOrderNum(beginOrderNum);
        subtotalAmountCell.setGroupName("现金开支");
        columnList.add(subtotalAmountCell);

        beginOrderNum++;
        ExcelExportEntity endAmountCell = new ExcelExportEntity("现金余额", "endAmount");
        endAmountCell.setOrderNum(beginOrderNum);
        endAmountCell.setGroupName("现金开支");
        columnList.add(endAmountCell);

        beginOrderNum++;
        ExcelExportEntity etcAmountCell = new ExcelExportEntity("ETC", "etcAmount");
        etcAmountCell.setOrderNum(beginOrderNum);

        columnList.add(etcAmountCell);

        beginOrderNum++;
        ExcelExportEntity ureaAmountCell = new ExcelExportEntity("尿素", "ureaAmount");
        ureaAmountCell.setOrderNum(beginOrderNum);
        columnList.add(ureaAmountCell);

        columnList.sort((Comparator.comparing(ExcelExportEntity::getOrderNum)));

        for (int i = 0; i < columnList.size(); i++) {
            ExcelExportEntity excelExportEntity = columnList.get(i);
            excelExportEntity.setOrderNum(i);
        }
        return columnList;
    }

    /**
     * 获取维修信息列表
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostDayReportVO> getRepairList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        List<RepairVO> repairVOList = repairService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        List<CarCostDayReportVO> repairCarCostList = repairVOList.stream().map(repair -> {
            CarCostDayReportVO carCostDayReportVO = new CarCostDayReportVO();
            List<RepairContentVO> repairContentVOList = repair.getContentVOList();

            BigDecimal repairTotalAmount = repairContentVOList.stream().map(RepairContentVO::getRepairAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            carCostDayReportVO.setRepairAmount(repairTotalAmount);

            StringBuilder sb = new StringBuilder();
            for (RepairContentVO repairContentVO : repairContentVOList) {
                sb.append(repairContentVO.getRepairContent()).append("：").append(repairContentVO.getRepairAmount()).append(DataTracerConstant.LINE);
            }
            carCostDayReportVO.setRemark(sb.toString());
            carCostDayReportVO.setVehicleId(repair.getModuleId());
            carCostDayReportVO.setCreateTime(SmartLocalDateUtil.toLocalDateTime(repair.getRepairDate()));
            return carCostDayReportVO;
        }).collect(Collectors.toList());
        return repairCarCostList;
    }

    /**
     * 获取保险信息列表
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostDayReportVO> getInsuranceList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        List<InsuranceVO> insuranceVOList = insuranceService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        List<CarCostDayReportVO> insuranceCarCostList = insuranceVOList.stream().map(insurance -> {
            CarCostDayReportVO carCostDayReportVO = new CarCostDayReportVO();
            carCostDayReportVO.setInsuranceAmount(insurance.getPolicyInsuranceAmount());
            carCostDayReportVO.setCreateTime(insurance.getCreateTime());

            StringBuilder sb = new StringBuilder();
            sb.append(insurance.getCreateUserName()).append("在").append(insurance.getInsuranceCompanyName())
                    .append("购买").append(SmartBaseEnumUtil.getEnumDescByValue(insurance.getInsuranceType(), InsuranceTypeEnum.class));
            carCostDayReportVO.setRemark(sb.toString());
            carCostDayReportVO.setVehicleId(insurance.getModuleId());
            return carCostDayReportVO;
        }).collect(Collectors.toList());
        return insuranceCarCostList;
    }

    /**
     * 获取审车信息列表
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostDayReportVO> getReviewList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        List<ReviewVO> reviewVOList = reviewService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        List<CarCostDayReportVO> reviewCarCostList = reviewVOList.stream().map(review -> {
            CarCostDayReportVO carCostDayReportVO = new CarCostDayReportVO();
            carCostDayReportVO.setReviewAmount(review.getReviewAmount());

            StringBuilder sb = new StringBuilder();
            sb.append(StringUtils.isBlank(review.getReviewPerson()) ? StringConst.EMPTY : review.getReviewPerson()).append("在")
                    .append(StringUtils.isBlank(review.getReviewPlant()) ? StringConst.EMPTY : review.getReviewPlant()).append("进行审车。");
            if (StringUtils.isNotBlank(review.getRemark())) {
                sb.append("审车备注为：").append(review.getRemark());
            }
            carCostDayReportVO.setRemark(sb.toString());
            carCostDayReportVO.setVehicleId(review.getVehicleId());
            carCostDayReportVO.setCreateTime(SmartLocalDateUtil.toLocalDateTime(review.getReviewDate()));
            return carCostDayReportVO;
        }).collect(Collectors.toList());
        return reviewCarCostList;
    }

    /**
     * 获取保养信息列表
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostDayReportVO> getMaintenanceList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        List<MaintenanceVO> maintenanceVOList = maintenanceService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        if (CollectionUtils.isEmpty(maintenanceVOList)) {
            return Lists.newArrayList();
        }

        List<Long> maintenanceIdList = maintenanceVOList.stream().map(MaintenanceVO::getMaintenanceId).collect(Collectors.toList());
        List<MaintenanceContentEntity> contentEntityList = maintenanceContentDao.selectByMaintenanceIds(maintenanceIdList);
        Map<Long, List<MaintenanceContentEntity>> contentMap = contentEntityList.stream().collect(groupingBy(MaintenanceContentEntity::getMaintenanceId));

        List<CarCostDayReportVO> maintenanceCarCostList = maintenanceVOList.stream().map(maintenance -> {
            CarCostDayReportVO carCostDayReportVO = new CarCostDayReportVO();
            List<MaintenanceContentEntity> contentList = contentMap.getOrDefault(maintenance.getMaintenanceId(), Lists.newArrayList());
            BigDecimal maintenanceAmount = contentList.stream().map(MaintenanceContentEntity::getMaintenanceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            carCostDayReportVO.setMaintenanceAmount(maintenanceAmount);

            StringBuilder sb = new StringBuilder();
            sb.append(StringUtils.isBlank(maintenance.getMaintenancePerson()) ? StringConst.EMPTY : maintenance.getMaintenancePerson()).append("在").
                    append(StringUtils.isBlank(maintenance.getMaintenancePlant()) ? StringConst.EMPTY : maintenance.getMaintenancePlant()).append("进行保养。");
            if (CollectionUtils.isNotEmpty(contentList)) {
                sb.append("保养内容为：");
                for (MaintenanceContentEntity maintenanceContentEntity : contentList) {
                    sb.append(maintenanceContentEntity.getMaintenanceContent()).append("，保养金额为：")
                            .append(maintenanceContentEntity.getMaintenanceAmount()).append(DataTracerConstant.LINE);
                }
            }
            if (StringUtils.isNotBlank(maintenance.getRemark())) {
                sb.append("保养备注为：").append(maintenance.getRemark());
            }
            carCostDayReportVO.setRemark(sb.toString());
            carCostDayReportVO.setVehicleId(maintenance.getVehicleId());
            carCostDayReportVO.setCreateTime(SmartLocalDateUtil.toLocalDateTime(maintenance.getMaintenanceDate()));
            return carCostDayReportVO;
        }).collect(Collectors.toList());
        return maintenanceCarCostList;
    }

    /**
     * 运单自有车费用查询
     *
     * @param queryForm
     * @return
     */
    private List<CarCostDayReportVO> queryWaybillCarCostList(CarCostDayStatisticQueryForm queryForm) {
        queryForm.setExcludeStatus(WaybillStatusEnum.CANCEL.getValue());
        List<WaybillEntity> waybillEntityList = waybillDao.selectByVehicleIdList(queryForm);
        if (CollectionUtils.isEmpty(waybillEntityList)) {
            return Lists.newArrayList();
        }
        List<Long> waybillIdList = waybillEntityList.stream().map(WaybillEntity::getWaybillId).collect(Collectors.toList());
        // 路线
        List<Long> orderIdList = waybillEntityList.stream().map(WaybillEntity::getOrderId).collect(Collectors.toList());
        List<OrderPathEntity> orderPathEntityList = orderPathDao.selectByOrderIdList(orderIdList);
        Map<Long, List<OrderPathEntity>> orderPathListMap = orderPathEntityList.stream().collect(groupingBy(OrderPathEntity::getOrderId));

        // 基本信息
        List<CarCostBasicInfoVO> basicInfoVOList = carCostBasicInfoDao.selectByWaybillIdList(waybillIdList);
        Map<Long, CarCostBasicInfoVO> basicInfoMap = basicInfoVOList.stream().collect(Collectors.toMap(CarCostBasicInfoVO::getWaybillId, Function.identity()));

        // 油卡收入和油费支出
        Map<Long, List<OilCardInfoVO>> oilCardMap = buildOilCardInfoVO(waybillIdList);

        // 现金收入和现金支出
        Map<Long, CashInfoVO> cashMap = buildCashInfoVO(waybillIdList);

        // ETC
        Map<Long, CarCostDayEtcVO> etcVOMap = buildEtcAmountMap(waybillIdList);

        // 尿素信息
        Map<Long, CarCostDayUreaVO> ureaVOMap = buildUreaAmountMap(waybillIdList);
        // 查询司机姓名
        Set<Long> driverIdSet = waybillEntityList.stream().map(WaybillEntity::getDriverId).collect(Collectors.toSet());
        List<DriverEntity> driverEntityList = driverManager.listByIds(driverIdSet);
        Map<Long, String> driverNameMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getDriverName));

        List<CarCostDayReportVO> resultList = Lists.newArrayList();
        List<CarCostDayReportVO> basicInfoStatisticsExportVOList = SmartBeanUtil.copyList(waybillEntityList, CarCostDayReportVO.class);
        for (CarCostDayReportVO exportVO : basicInfoStatisticsExportVOList) {
            exportVO.setDriverName(driverNameMap.getOrDefault(exportVO.getDriverId(), StringConst.EMPTY));
            Long orderId = exportVO.getOrderId();
            if (orderId != null) {
                List<OrderPathEntity> pathList = orderPathListMap.getOrDefault(orderId, Lists.newArrayList());
                String start = this.getSourceAddress(pathList);
                String end = this.getDestAddress(pathList);
                if (StringUtils.isBlank(exportVO.getRouteName())) {
                    exportVO.setRouteName(start + "-" + end);
                }
            }
            Long waybillId = exportVO.getWaybillId();
            // 设置基本信息
            CarCostBasicInfoVO basicInfoVO = basicInfoMap.get(waybillId);

            exportVO.setCarCostBasicInfoVO(basicInfoVO);
            // 设置现金信息
            CashInfoVO cashInfoVO = cashMap.get(waybillId);
            exportVO.setCashInfoVO(cashInfoVO);

            // 设置油卡信息
            List<OilCardInfoVO> oilCardInfoVOList = oilCardMap.getOrDefault(waybillId, Lists.newArrayList());
            exportVO.setOilCardInfoVOList(oilCardInfoVOList);
            // 设置ETC 金额
            exportVO.setEtcVO(etcVOMap.get(waybillId));
            // 设置尿素金额
            exportVO.setUreaVO(ureaVOMap.get(waybillId));
            resultList.add(exportVO);
        }

        return resultList;
    }

    private String getSourceAddress(List<OrderPathEntity> pathList) {
        if (CollectionUtils.isEmpty(pathList)) {
            return "";
        }
        pathList = pathList.stream().filter(e -> PathTypeEnum.CONTAINER_LOCATION.equalsValue(e.getType()) || PathTypeEnum.PLACING_LOADING.equalsValue(e.getType())).collect(Collectors.toList());
        OrderPathEntity startPath = pathList.get(0);
        String address = "";
        if (StringUtils.isNotBlank(startPath.getProvinceName())) {
            address = address + startPath.getProvinceName();
        }
        if (StringUtils.isNotBlank(startPath.getCityName())) {
            address = address + startPath.getCityName();
        }
        return address;
    }

    private String getDestAddress(List<OrderPathEntity> pathList) {
        if (CollectionUtils.isEmpty(pathList)) {
            return "";
        }
        pathList = pathList.stream().filter(e -> PathTypeEnum.RETURN_CONTAINER_LOCATION.equalsValue(e.getType()) || PathTypeEnum.UNLOADING_LOCATION.equalsValue(e.getType())).collect(Collectors.toList());
        OrderPathEntity endPath = pathList.get(pathList.size() - 1);
        String address = "";
        if (StringUtils.isNotBlank(endPath.getProvinceName())) {
            address = address + endPath.getProvinceName();
        }
        if (StringUtils.isNotBlank(endPath.getCityName())) {
            address = address + endPath.getCityName();
        }
        return address;
    }


    /**
     * 构建油卡收入和油费支出VO
     *
     * @param waybillIdList
     * @return
     */
    private Map<Long, List<OilCardInfoVO>> buildOilCardInfoVO(List<Long> waybillIdList) {
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Maps.newHashMap();
        }
        // 查询油卡充值
        List<CarCostOilCardReceiveEntity> oilCardReceiveEntityList = carCostOilCardReceiveDao.selectByWaybillIdListAndAuditStatus(waybillIdList, AuditStatusEnum.AUDIT_PASS.getValue());
        Map<Long, List<CarCostOilCardReceiveEntity>> oilCardReceiveMap = oilCardReceiveEntityList.stream().collect(groupingBy(CarCostOilCardReceiveEntity::getWaybillId));

        // 查询油卡支出
        List<CarCostOilPayEntity> oilCardPayEntityList = carCostOilPayDao.selectByWaybillIdListAndAuditStatus(waybillIdList, AuditStatusEnum.AUDIT_PASS.getValue());
        Map<Long, List<CarCostOilPayEntity>> oilCardPayMap = oilCardPayEntityList.stream().collect(groupingBy(CarCostOilPayEntity::getWaybillId));

        // 期初期末
        List<CarCostOilCardInitialEndVO> oilCardInitialEndVOList = carCostOilCardInitialEndDao.selectByWaybillIdList(waybillIdList);
        Map<Long, List<CarCostOilCardInitialEndVO>> oilCardInitialEndMap = oilCardInitialEndVOList.stream().collect(groupingBy(CarCostOilCardInitialEndVO::getWaybillId));

        Set<Long> oilCardIdSet = oilCardReceiveEntityList.stream().filter(e -> null != e.getOilCardId()).map(CarCostOilCardReceiveEntity::getOilCardId).collect(Collectors.toSet());
        oilCardIdSet.addAll(oilCardPayEntityList.stream().filter(e -> e.getOilCardId() != null).map(CarCostOilPayEntity::getOilCardId).collect(Collectors.toSet()));
        oilCardIdSet.addAll(oilCardInitialEndVOList.stream().filter(e -> e.getOilCardId() != null).map(CarCostOilCardInitialEndVO::getOilCardId).collect(Collectors.toSet()));

        Map<Long, List<OilCardInfoVO>> waybillOilCardInfoMap = Maps.newHashMap();
        for (Long waybillId : waybillIdList) {
            List<OilCardInfoVO> oilCardInfoVOList = Lists.newArrayList();

            // 期初期末根据油卡ID分组
            List<CarCostOilCardInitialEndVO> carCostOilCardInitialEndList = oilCardInitialEndMap.getOrDefault(waybillId, Lists.newArrayList());
            Map<Integer, Long> waybillOilCardFuelTypeMap = carCostOilCardInitialEndList.stream().collect(Collectors.toMap(CarCostOilCardInitialEndVO::getFuelType, CarCostOilCardInitialEndVO::getOilCardId, (o1, o2) -> o1));
            Map<Long, List<CarCostOilCardInitialEndVO>> waybillOilCardInitialEndMap = carCostOilCardInitialEndList.stream().collect(groupingBy(CarCostOilCardInitialEndVO::getOilCardId));

            // 收入分组
            List<CarCostOilCardReceiveEntity> waybillOilCardReceiveList = oilCardReceiveMap.getOrDefault(waybillId, Lists.newArrayList());
            Map<Long, List<CarCostOilCardReceiveEntity>> waybillOilCardReceiveMap = waybillOilCardReceiveList.stream().collect(groupingBy(CarCostOilCardReceiveEntity::getOilCardId));

            // 支出根据燃油类型分组
            List<CarCostOilPayEntity> waybillCarCostOilPayList = oilCardPayMap.getOrDefault(waybillId, Lists.newArrayList());
            Map<Integer, List<CarCostOilPayEntity>> waybillCarCostOilPayMap = waybillCarCostOilPayList.stream().collect(groupingBy(CarCostOilPayEntity::getFuelType));

            List<OilCardFuelTypeEnum> fuelTypeList = SmartBaseEnumUtil.enumList(OilCardFuelTypeEnum.class);
            for (OilCardFuelTypeEnum oilCardFuelTypeEnum : fuelTypeList) {
                Integer fuelType = oilCardFuelTypeEnum.getValue();

                OilCardInfoVO oilCardInfoVO = new OilCardInfoVO();
                // 设置默认值
                oilCardInfoVO.setFuelType(fuelType);
                oilCardInfoVO.setInitialAmount(BigDecimal.ZERO);
                oilCardInfoVO.setEndAmount(BigDecimal.ZERO);
                oilCardInfoVO.setOilCardReceiveAmount(BigDecimal.ZERO);
                oilCardInfoVO.setOilCardPayAmount(BigDecimal.ZERO);
                oilCardInfoVO.setCashPayAmount(BigDecimal.ZERO);
                oilCardInfoVO.setSubtotal(BigDecimal.ZERO);


                // 获取现金加油的金额 设置现金支出金额 ，支付方式为现金
                List<CarCostOilPayEntity> waybillPayList = waybillCarCostOilPayMap.getOrDefault(fuelType, Lists.newArrayList());
                BigDecimal cashPayAmount = waybillPayList.stream().filter(e -> CarCostCategoryPayModeEnum.CASH.equalsValue(e.getPayMode()))
                        .map(CarCostOilPayEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                oilCardInfoVO.setCashPayAmount(cashPayAmount);
                BigDecimal cashOilConsumption = waybillPayList.stream().filter(e -> CarCostCategoryPayModeEnum.CASH.equalsValue(e.getPayMode()))
                        .map(CarCostOilPayEntity::getOilConsumption).reduce(BigDecimal.ZERO, BigDecimal::add);
                oilCardInfoVO.setCashPayOilConsumption(cashOilConsumption);
                String cashPayRemark = waybillPayList.stream().filter(e -> CarCostCategoryPayModeEnum.CASH.equalsValue(e.getPayMode()) && StringUtils.isNotBlank(e.getRemark()))
                        .map(CarCostOilPayEntity::getRemark).collect(Collectors.joining(StringConst.SEPARATOR));
                oilCardInfoVO.setCashPayRemark(cashPayRemark);

                Long oilCardId = waybillOilCardFuelTypeMap.get(fuelType);
                if (null != oilCardId) {
                    // 设置期初期末
                    List<CarCostOilCardInitialEndVO> carCostOilCardInitialEndVOList = waybillOilCardInitialEndMap.get(oilCardId);
                    carCostOilCardInitialEndVOList = carCostOilCardInitialEndVOList.stream()
                            .sorted(Comparator.comparing(CarCostOilCardInitialEndVO::getCreateTime)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(carCostOilCardInitialEndVOList)) {
                        oilCardInfoVO.setInitialAmount(carCostOilCardInitialEndVOList.get(0).getInitialAmount());
                        oilCardInfoVO.setEndAmount(carCostOilCardInitialEndVOList.get(carCostOilCardInitialEndVOList.size() - 1).getEndAmount());
                    }

                    // 设置充值金额、支出金额、支出小计
                    List<CarCostOilCardReceiveEntity> carCostReceiveList = waybillOilCardReceiveMap.getOrDefault(oilCardId, Lists.newArrayList());
                    BigDecimal receiveAmount = carCostReceiveList.stream().map(CarCostOilCardReceiveEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    oilCardInfoVO.setOilCardReceiveAmount(receiveAmount);
                    String receiveRemark = carCostReceiveList.stream().filter(e -> StringUtils.isNotBlank(e.getRemark())).map(CarCostOilCardReceiveEntity::getRemark).collect(Collectors.joining(StringConst.SEPARATOR));
                    oilCardInfoVO.setOilCardReceiveRemark(receiveRemark);

                    // 设置油卡支出金额，类型为油卡支出且支付方式为油卡
                    BigDecimal payAmount = waybillPayList.stream().filter(e -> CarCostCategoryPayModeEnum.OIL_CARD.equalsValue(e.getPayMode()))
                            .map(CarCostOilPayEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    oilCardInfoVO.setOilCardPayAmount(payAmount);
                    BigDecimal oilCardPayOilConsumption = waybillPayList.stream().filter(e -> CarCostCategoryPayModeEnum.OIL_CARD.equalsValue(e.getPayMode()))
                            .map(CarCostOilPayEntity::getOilConsumption).reduce(BigDecimal.ZERO, BigDecimal::add);
                    oilCardInfoVO.setOilCardPayOilConsumption(oilCardPayOilConsumption);

                    String payRemark = waybillPayList.stream().filter(e -> CarCostCategoryPayModeEnum.OIL_CARD.equalsValue(e.getPayMode()) && StringUtils.isNotBlank(e.getRemark()))
                            .map(CarCostOilPayEntity::getRemark).collect(Collectors.joining(StringConst.SEPARATOR));
                    oilCardInfoVO.setOilCardPayRemark(payRemark);
                }
                BigDecimal oilConsumption = waybillPayList.stream().map(CarCostOilPayEntity::getOilConsumption).reduce(BigDecimal.ZERO, BigDecimal::add);
                oilCardInfoVO.setOilConsumption(oilConsumption);

                // 设置小计
                oilCardInfoVO.setSubtotal(SmartBigDecimalUtil.Amount.add(oilCardInfoVO.getCashPayAmount(), oilCardInfoVO.getOilCardPayAmount()));

                oilCardInfoVOList.add(oilCardInfoVO);
            }
            waybillOilCardInfoMap.put(waybillId, oilCardInfoVOList);
        }
        return waybillOilCardInfoMap;
    }

    /**
     * 根据运单ID构建现金收入和现金支出VO
     *
     * @param waybillIdList
     * @return
     */
    private Map<Long, CashInfoVO> buildCashInfoVO(List<Long> waybillIdList) {
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Maps.newHashMap();
        }
        // 现金收入
        List<CarCostCashReceiveEntity> cashReceiveEntityList = carCostCashReceiveDao.selectByWaybillIdListAndAuditStatus(waybillIdList, AuditStatusEnum.AUDIT_PASS.getValue());
        Map<Long, List<CarCostCashReceiveEntity>> cashReceiveMap = cashReceiveEntityList.stream().collect(groupingBy(CarCostCashReceiveEntity::getWaybillId));

        // 现金支出
        List<CarCostCashPayEntity> cashPayEntityList = carCostCashPayDao.selectByWaybillIdListAndAuditStatus(waybillIdList, AuditStatusEnum.AUDIT_PASS.getValue());
        Map<Long, List<CarCostCashPayEntity>> cashPayMap = cashPayEntityList.stream().collect(groupingBy(CarCostCashPayEntity::getWaybillId));

        List<CarCostCashInitialEndEntity> cashInitialEndEntityList = carCostCashInitialEndDao.selectByWaybillIdList(waybillIdList);
        Map<Long, List<CarCostCashInitialEndEntity>> cashInitialEndEntityMap = cashInitialEndEntityList.stream().collect(groupingBy(CarCostCashInitialEndEntity::getWaybillId));

        Map<Long, CashInfoVO> cashInfoVOMap = Maps.newHashMap();
        for (Long waybillId : waybillIdList) {
            CashInfoVO cashInfoVO = new CashInfoVO();

            // 设置期初期末
            cashInfoVO.setInitialAmount(BigDecimal.ZERO);
            cashInfoVO.setEndAmount(BigDecimal.ZERO);
            List<CarCostCashInitialEndEntity> cashInitialEndList = cashInitialEndEntityMap.get(waybillId);
            if (CollUtil.isNotEmpty(cashInitialEndList)) {
                cashInitialEndList = cashInitialEndList.stream().sorted(Comparator.comparing(CarCostCashInitialEndEntity::getCreateTime)).collect(Collectors.toList());
                cashInfoVO.setInitialAmount(cashInitialEndList.get(0).getInitialAmount());
                cashInfoVO.setEndAmount(cashInitialEndList.get(cashInitialEndList.size() - 1).getEndAmount());
            }

            // 小计默认为0
            cashInfoVO.setSubtotal(BigDecimal.ZERO);

            // 设置支出费用项、开支小计  支出根据分类计算每个分类的合计金额
            List<CarCostCashPayEntity> waybillCashItemList = cashPayMap.getOrDefault(waybillId, Lists.newArrayList());
            Map<Long, List<CarCostCashPayEntity>> payCashGroupMap = waybillCashItemList.stream().collect(groupingBy(CarCostCashPayEntity::getCategoryId));

            List<CarCostItemInfoVO> waybillPayCashGroupList = Lists.newArrayList();
            for (Map.Entry<Long, List<CarCostCashPayEntity>> entry : payCashGroupMap.entrySet()) {
                List<CarCostCashPayEntity> categoryCostItemList = entry.getValue();
                BigDecimal totalAmount = categoryCostItemList.stream().map(CarCostCashPayEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                String remark = categoryCostItemList.stream().map(CarCostCashPayEntity::getRemark).collect(Collectors.joining(StringConst.SEPARATOR));

                CarCostItemInfoVO carCostItemInfoVO = SmartBeanUtil.copy(categoryCostItemList.get(NumberConst.ZERO), CarCostItemInfoVO.class);
                carCostItemInfoVO.setAmount(totalAmount);
                carCostItemInfoVO.setRemark(remark);
                waybillPayCashGroupList.add(carCostItemInfoVO);
            }

            cashInfoVO.setItemInfoVOList(waybillPayCashGroupList);
            BigDecimal payTotalAmount = waybillPayCashGroupList.stream().map(CarCostItemInfoVO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            cashInfoVO.setSubtotal(payTotalAmount);

            // 设置领取的出车款金额、备注
            List<CarCostCashReceiveEntity> waybillCashReceiveEntityList = cashReceiveMap.getOrDefault(waybillId, Lists.newArrayList());
            BigDecimal rechargeAmount = waybillCashReceiveEntityList.stream().map(CarCostCashReceiveEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            cashInfoVO.setRechargeAmount(rechargeAmount);
            String rechargeRemark = waybillCashReceiveEntityList.stream().filter(e -> StringUtils.isNotBlank(e.getRemark())).map(CarCostCashReceiveEntity::getRemark).collect(Collectors.joining(StringConst.SEPARATOR));
            cashInfoVO.setRechargeRemark(rechargeRemark);
            cashInfoVOMap.put(waybillId, cashInfoVO);
        }
        return cashInfoVOMap;
    }

    /**
     * 计算ETC金额
     *
     * @param waybillIdList
     * @return
     */
    private Map<Long, CarCostDayEtcVO> buildEtcAmountMap(List<Long> waybillIdList) {
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Maps.newHashMap();
        }
        List<CarCostEtcPayEntity> etcPayEntityList = carCostEtcPayDao.selectByWaybillIdListAndAuditStatus(waybillIdList, AuditStatusEnum.AUDIT_PASS.getValue());

        Map<Long, CarCostDayEtcVO> etcVOMap = Maps.newHashMap();
        Map<Long, List<CarCostEtcPayEntity>> ectItemInfoMap = etcPayEntityList.stream().collect(groupingBy(CarCostEtcPayEntity::getWaybillId));
        for (Map.Entry<Long, List<CarCostEtcPayEntity>> entry : ectItemInfoMap.entrySet()) {
            Long waybillId = entry.getKey();
            List<CarCostEtcPayEntity> carcostItemList = entry.getValue();
            BigDecimal etcAmount = carcostItemList.stream().map(CarCostEtcPayEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            String remark = carcostItemList.stream().filter(e -> StringUtils.isNotBlank(e.getRemark())).map(CarCostEtcPayEntity::getRemark).collect(Collectors.joining(StringConst.SEPARATOR));

            CarCostDayEtcVO carCostDayEtcVO = new CarCostDayEtcVO();
            carCostDayEtcVO.setEtcAmount(etcAmount);
            carCostDayEtcVO.setRemark(remark);

            etcVOMap.put(waybillId, carCostDayEtcVO);
        }
        return etcVOMap;
    }

    /**
     * 计算尿素金额
     *
     * @param waybillIdList
     * @return
     */
    private Map<Long, CarCostDayUreaVO> buildUreaAmountMap(List<Long> waybillIdList) {
        if (CollectionUtils.isEmpty(waybillIdList)) {
            return Maps.newHashMap();
        }
        List<CarCostUreaPayEntity> ureaPayEntityList = carCostUreaPayDao.selectByWaybillIdListAndAuditStatus(waybillIdList, AuditStatusEnum.AUDIT_PASS.getValue());

        Map<Long, CarCostDayUreaVO> ureaPayEntityMap = Maps.newHashMap();
        Map<Long, List<CarCostUreaPayEntity>> ureaItemInfoMap = ureaPayEntityList.stream().collect(groupingBy(CarCostUreaPayEntity::getWaybillId));
        for (Map.Entry<Long, List<CarCostUreaPayEntity>> entry : ureaItemInfoMap.entrySet()) {
            Long waybillId = entry.getKey();
            List<CarCostUreaPayEntity> carcostItemList = entry.getValue();
            BigDecimal ureaAmount = carcostItemList.stream().map(CarCostUreaPayEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            String remark = carcostItemList.stream().filter(e -> StringUtils.isNotBlank(e.getRemark())).map(CarCostUreaPayEntity::getRemark).collect(Collectors.joining(StringConst.SEPARATOR));

            CarCostDayUreaVO carCostDayUreaVO = new CarCostDayUreaVO();
            carCostDayUreaVO.setUreaAmount(ureaAmount);
            carCostDayUreaVO.setRemark(remark);

            ureaPayEntityMap.put(waybillId, carCostDayUreaVO);
        }
        return ureaPayEntityMap;
    }

    /**
     * 车辆费用流水导出
     *
     * @param queryForm
     * @return
     */
    public List<CarCostFlowExportVO> costFlowExport(CarCostDayStatisticQueryForm queryForm) {
        List<Long> vehicleIdList = queryForm.getVehicleIdList();
        if (CollectionUtils.isEmpty(vehicleIdList)) {
            throw new BusinessException("车辆ID不能为空");
        }
        List<WaybillEntity> waybillEntityList = waybillDao.selectByVehicleIdList(queryForm);
        if (CollectionUtils.isEmpty(waybillEntityList)) {
            return Lists.newArrayList();
        }

        List<Long> waybillIdList = waybillEntityList.stream().map(WaybillEntity::getWaybillId).collect(Collectors.toList());
        Integer auditStatus = AuditStatusEnum.AUDIT_PASS.getValue();

        // 车牌号
        List<VehicleEntity> vehicleEntityList = vehicleDao.selectBatchIds(vehicleIdList);
        Map<Long, String> vehicleNumberMap = vehicleEntityList.stream().collect(Collectors.toMap(e -> e.getVehicleId(), e -> e.getVehicleNumber()));
        // 货主名称
        List<Long> shipperIdList = waybillEntityList.stream().map(WaybillEntity::getShipperId).collect(Collectors.toList());
        List<ShipperEntity> shipperEntityList = shipperManager.listByIds(shipperIdList);
        Map<Long, String> shipperMap = shipperEntityList.stream().collect(Collectors.toMap(ShipperEntity::getShipperId, ShipperEntity::getConsignor));
        // 司机名字
        List<Long> driverIdList = waybillEntityList.stream().map(WaybillEntity::getDriverId).distinct().collect(Collectors.toList());
        List<DriverEntity> driverEntityList = driverManager.listByIds(driverIdList);
        Map<Long, String> driverNameMap = driverEntityList.stream().collect(Collectors.toMap(DriverEntity::getDriverId, DriverEntity::getDriverName));

        LocalDate startTime = queryForm.getStartTime();
        LocalDate endTime = queryForm.getEndTime();
        // 应收运费
        List<CarCostFlowExportVO> receiveAmountList = waybillDao.selectCostFlowByWaybillIdList(waybillIdList);
        // 应收设置货主
        receiveAmountList.stream().forEach(e -> e.setCategoryName(shipperMap.getOrDefault(e.getShipperId(), StringConst.EMPTY)));
        // 现金支出
        List<CarCostFlowExportVO> cashPayList = carCostCashPayDao.selectCostFlowByWaybillIdList(waybillIdList, auditStatus);
        // 油费支出
        List<CarCostFlowExportVO> oilPayList = carCostOilPayDao.selectCostFlowByWaybillIdList(waybillIdList, auditStatus);
        // ETC支出
        List<CarCostFlowExportVO> etcPayList = carCostEtcPayDao.selectCostFlowByWaybillIdList(waybillIdList, auditStatus);
        // 尿素支出
        List<CarCostFlowExportVO> ureaPayList = carCostUreaPayDao.selectCostFlowByWaybillIdList(waybillIdList, auditStatus);
        // 维修支出
        List<CarCostFlowExportVO> repairPayList = this.buildRepairPayList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);
        // 审车支出
        List<CarCostFlowExportVO> reviewPayList = this.buildReviewPayList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);
        // 保险支出
        List<CarCostFlowExportVO> insurancePayList = this.buildInsurancePayList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);
        // 保养支出
        List<CarCostFlowExportVO> maintenancePayList = this.buildMaintenancePayList(queryForm.getEnterpriseId(), vehicleIdList, startTime, endTime);

        List<CarCostFlowExportVO> totalVOList = Lists.newArrayList();
        totalVOList.addAll(cashPayList);
        totalVOList.addAll(oilPayList);
        totalVOList.addAll(etcPayList);
        totalVOList.addAll(ureaPayList);
        totalVOList.addAll(receiveAmountList);
        totalVOList.addAll(repairPayList);
        totalVOList.addAll(reviewPayList);
        totalVOList.addAll(insurancePayList);
        totalVOList.addAll(maintenancePayList);
        totalVOList = totalVOList.stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getDate())))
                .collect(Collectors.toList());
        totalVOList.stream().forEach(e -> {
            e.setYear(e.getDate().substring(0, 4) + "年");
            e.setMonth(e.getDate().substring(4, 6) + "月");
            e.setVehicleNumber(vehicleNumberMap.get(e.getVehicleId()));
            if (ObjectUtil.isNotEmpty(e.getDriverId())) {
                e.setDriverName(driverNameMap.getOrDefault(e.getDriverId(), StringConst.EMPTY));
            }
        });
        // 序号
        Integer serialNo = 1;
        for (CarCostFlowExportVO exportVO : totalVOList) {
            exportVO.setSerialNo(serialNo++);
        }
        // 合计
        BigDecimal totalReceiveAmount = receiveAmountList.stream().map(CarCostFlowExportVO::getReceiveAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPayAmount = totalVOList.stream()
                .filter(e -> ObjectUtil.isNotEmpty(e.getPayAmount()))
                .map(CarCostFlowExportVO::getPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        CarCostFlowExportVO exportVO = new CarCostFlowExportVO();
        exportVO.setYear("合计");
        exportVO.setPayAmount(totalPayAmount);
        exportVO.setReceiveAmount(totalReceiveAmount);
        totalVOList.add(exportVO);

        return totalVOList;
    }

    /**
     * 获取维修表的导出内容
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostFlowExportVO> buildRepairPayList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        List<RepairVO> repairVOList = repairService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        List<CarCostFlowExportVO> repairPayList = repairVOList.stream().map(repairVO -> {
            CarCostFlowExportVO exportVO = new CarCostFlowExportVO();
            exportVO.setVehicleId(repairVO.getModuleId());
            exportVO.setDate(repairVO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            // 支出金额
            List<RepairContentVO> contentVOList = repairVO.getContentVOList();
            BigDecimal repairAmount = contentVOList.stream().map(RepairContentVO::getRepairAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            exportVO.setPayAmount(repairAmount);
            // 维修信息
            String repairContent = contentVOList.stream().map(RepairContentVO::getRepairContent).collect(Collectors.joining(","));
            exportVO.setSummary(repairContent);
            return exportVO;
        }).collect(Collectors.toList());
        return repairPayList;
    }

    /**
     * 审车支出
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostFlowExportVO> buildReviewPayList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        List<ReviewVO> reviewVOList = reviewService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        List<CarCostFlowExportVO> reviewPayList = reviewVOList.stream().map(reviewVO -> {
            CarCostFlowExportVO exportVO = new CarCostFlowExportVO();
            exportVO.setVehicleId(reviewVO.getVehicleId());
            exportVO.setDate(reviewVO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            exportVO.setSummary(reviewVO.getRemark());
            exportVO.setPayAmount(reviewVO.getReviewAmount());
            return exportVO;
        }).collect(Collectors.toList());
        return reviewPayList;
    }

    /**
     * 保险支出
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostFlowExportVO> buildInsurancePayList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate endTime) {
        List<InsuranceVO> insuranceVOList = insuranceService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        List<CarCostFlowExportVO> insurancePayList = insuranceVOList.stream().map(insuranceVO -> {
            CarCostFlowExportVO exportVO = new CarCostFlowExportVO();
            exportVO.setVehicleId(insuranceVO.getModuleId());
            exportVO.setDate(insuranceVO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            exportVO.setSummary("保险支出");
            exportVO.setPayAmount(insuranceVO.getInsuranceAmount());
            return exportVO;
        }).collect(Collectors.toList());
        return insurancePayList;
    }

    /**
     * 保养费用
     *
     * @param vehicleIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private List<CarCostFlowExportVO> buildMaintenancePayList(Long enterpriseId, List<Long> vehicleIdList, LocalDate startTime, LocalDate
            endTime) {
        List<MaintenanceVO> maintenanceVOList = maintenanceService.selectListByVehicleIdList(enterpriseId, vehicleIdList, startTime, endTime);
        if (CollectionUtils.isEmpty(maintenanceVOList)) {
            return Lists.newArrayList();
        }

        List<Long> maintenanceIdList = maintenanceVOList.stream().map(MaintenanceVO::getMaintenanceId).collect(Collectors.toList());
        List<MaintenanceContentEntity> contentEntityList = maintenanceContentDao.selectByMaintenanceIds(maintenanceIdList);
        Map<Long, List<MaintenanceContentEntity>> contentMap = contentEntityList.stream().collect(groupingBy(MaintenanceContentEntity::getMaintenanceId));

        List<CarCostFlowExportVO> maintenancePayList = maintenanceVOList.stream().map(maintenanceVO -> {
            CarCostFlowExportVO exportVO = new CarCostFlowExportVO();
            exportVO.setVehicleId(maintenanceVO.getVehicleId());
            exportVO.setDate(maintenanceVO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            exportVO.setSummary(maintenanceVO.getRemark());
            List<MaintenanceContentEntity> contentList = contentMap.getOrDefault(maintenanceVO.getMaintenanceId(), Lists.newArrayList());
            BigDecimal maintenanceAmount = contentList.stream().map(MaintenanceContentEntity::getMaintenanceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            exportVO.setPayAmount(maintenanceAmount);
            return exportVO;
        }).collect(Collectors.toList());
        return maintenancePayList;
    }

    /**
     * 统计自有车月报表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<List<CarCostMonthStatisticVO>> queryMonthStatisticsReport(CarCostMonthStatisticQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        List<VehicleSimpleVO> vehicleSimpleVOList = vehicleDao.selectByBusinessModeAndDisableFlag(queryForm.getEnterpriseId(), BusinessModeEnum.INNER_MANAGEMENT.getValue(), Boolean.FALSE, Boolean.FALSE);
        if (CollectionUtils.isEmpty(vehicleSimpleVOList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<Long> vehicleIdList = vehicleSimpleVOList.stream().map(VehicleSimpleVO::getVehicleId).collect(Collectors.toList());
        List<VehicleEntity> vehicleList = vehicleDao.selectVehicleList(vehicleIdList, null, Boolean.FALSE);

        List<Long> queryVehicleIdList = vehicleList.stream().map(VehicleEntity::getVehicleId).collect(Collectors.toList());
        // 过滤数据库查询ID列表以及用户检索数据
        if (CollectionUtils.isNotEmpty(queryForm.getVehicleIdList())) {
            vehicleList = vehicleList.stream().filter(e -> queryForm.getVehicleIdList().contains(e.getVehicleId())).collect(Collectors.toList());
            queryVehicleIdList = vehicleList.stream().map(VehicleEntity::getVehicleId).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(queryVehicleIdList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        queryForm.setVehicleIdList(queryVehicleIdList);

        // 保险
        Map<Long, BigDecimal> insuranceAmountMap = insuranceService.getCarCostInsuranceAmount(queryForm);
        // 保养
        Map<Long, BigDecimal> maintenanceAmountMap = maintenanceService.getCarCostMaintenanceAmount(queryForm);
        // 维修
        Map<Long, BigDecimal> repairAmountMap = repairService.getCarCostRepairAmount(queryForm);
        // 审车
        Map<Long, BigDecimal> reviewAmountMap = reviewService.getCarCostReviewAmount(queryForm);
        // 查询运单相关的费用
        // 应收
        List<CarCostWaybillMonthAmountDTO> vehicleWaybillList = waybillDao.sumAmountByVehicleId(queryForm);
        Map<Long, BigDecimal> freightAmountMap = vehicleWaybillList.stream().collect(Collectors.toMap(CarCostWaybillMonthAmountDTO::getVehicleId, CarCostWaybillMonthAmountDTO::getPayableAmount));
        // 基本信息
        List<CarCostVehicleMonthBasicInfoDTO> carCostVehicleMonthBasicInfoList = carCostBasicInfoDao.sumCarCostAmountByVehicle(queryForm);
        Map<Long, CarCostVehicleMonthBasicInfoDTO> carCostVehicleBasicInfoMap = carCostVehicleMonthBasicInfoList.stream().collect(Collectors.toMap(CarCostVehicleMonthBasicInfoDTO::getVehicleId, Function.identity()));

        // 油卡收入
        List<CarCostVehicleMonthAmountDTO> oilCardReceiveMonthAmountList = carCostOilCardReceiveDao.sumMonthAmountByVehicle(queryForm);
        Map<Long, BigDecimal> oilCardReceiveMonthAmountMap = oilCardReceiveMonthAmountList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));
        // 费用支出
        List<CarCostVehicleMonthAmountDTO> cashMonthAmountList = carCostCashPayDao.sumMonthAmountByVehicle(queryForm);
        Map<Long, BigDecimal> cashMonthAmountMap = cashMonthAmountList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));
        List<CarCostVehicleMonthOilAmountDTO> oilMonthAmountList = carCostOilPayDao.sumMonthAmountByVehicle(queryForm);
        Map<String, BigDecimal> oilPayMap = oilMonthAmountList.stream().collect(Collectors.toMap(e -> e.getVehicleId() + "_" + e.getFuelType(), CarCostVehicleMonthOilAmountDTO::getAmount));

        List<CarCostVehicleMonthOilConsumptionDTO> oilMonthOilConsumptionList = carCostOilPayDao.sumMonthOilConsumptionByVehicle(queryForm);
        Map<Long, BigDecimal> oilMonthOilConsumptionMap = oilMonthOilConsumptionList.stream().collect(Collectors.toMap(CarCostVehicleMonthOilConsumptionDTO::getVehicleId, CarCostVehicleMonthOilConsumptionDTO::getOilConsumption));


        List<CarCostVehicleMonthAmountDTO> etcMonthAmountList = carCostEtcPayDao.sumMonthAmountByVehicle(queryForm);
        Map<Long, BigDecimal> etcMonthAmountMap = etcMonthAmountList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));
        List<CarCostVehicleMonthAmountDTO> ureaMonthAmountList = carCostUreaPayDao.sumMonthAmountByVehicle(queryForm);
        Map<Long, BigDecimal> ureaMonthAmountMap = ureaMonthAmountList.stream().collect(Collectors.toMap(CarCostVehicleMonthAmountDTO::getVehicleId, CarCostVehicleMonthAmountDTO::getAmount));

        // 查询
        List<CarCostMonthStatisticVO> carCostMonthStatisticVOList = vehicleList.stream().map(e -> {
            Long vehicleId = e.getVehicleId();
            CarCostMonthStatisticVO statisticVO = new CarCostMonthStatisticVO();
            statisticVO.setVehicleId(vehicleId);
            statisticVO.setVehicleNumber(e.getVehicleNumber());

            // 设置运单的货重、运费
            BigDecimal freightAmount = freightAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO);
            statisticVO.setReceiveFrightAmount(freightAmount);
            // 设置基本信息
            CarCostVehicleMonthBasicInfoDTO basicInfoDTO = carCostVehicleBasicInfoMap.get(vehicleId);
            statisticVO.setHighSpeedMileage(BigDecimal.ZERO);
            statisticVO.setLowSpeedMileage(BigDecimal.ZERO);
            statisticVO.setGpsMileage(BigDecimal.ZERO);
            if (null != basicInfoDTO) {
                statisticVO.setHighSpeedMileage(basicInfoDTO.getHighSpeedMileage());
                statisticVO.setLowSpeedMileage(basicInfoDTO.getLowSpeedMileage());
                statisticVO.setGpsMileage(basicInfoDTO.getGpsMileage());
            }

            // 设置支出费用
            statisticVO.setCashPayAmount(cashMonthAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO));
            statisticVO.setDieselOilPayAmount(oilPayMap.getOrDefault(vehicleId + "_" + OilCardFuelTypeEnum.DIESEL_OIL_CARD.getValue(), BigDecimal.ZERO));
            statisticVO.setGasolineOilPayAmount(oilPayMap.getOrDefault(vehicleId + "_" + OilCardFuelTypeEnum.GASOLINE_CARD.getValue(), BigDecimal.ZERO));
            statisticVO.setUreaPayAmount(ureaMonthAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO));
            statisticVO.setEtcPayAmount(etcMonthAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO));

            // 设置保险维修
            statisticVO.setInsuranceAmount(insuranceAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO));
            statisticVO.setMaintenanceAmount(maintenanceAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO));
            statisticVO.setRepairAmount(repairAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO));
            statisticVO.setReviewAmount(reviewAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO));
            // 计算用油量
            BigDecimal oilConsumption = oilMonthOilConsumptionMap.getOrDefault(vehicleId, BigDecimal.ZERO);
            statisticVO.setOilConsumption(oilConsumption);
            // 计算支出总金额
            BigDecimal payAmount = calculatePayAmount(statisticVO);
            statisticVO.setPayAmount(payAmount);
            // 计算利润
            statisticVO.setProfitAmount(SmartBigDecimalUtil.Amount.subtract(freightAmount, statisticVO.getPayAmount()));
            // 计算油耗
            statisticVO.setOilConsumptionRate(SmartBigDecimalUtil.dividePercent(statisticVO.getOilConsumption(), statisticVO.getGpsMileage()));
            // 成本占比
            statisticVO.setPayRate(SmartBigDecimalUtil.dividePercent(payAmount, freightAmount));
            // 油耗
            BigDecimal oilRechargeAmount = oilCardReceiveMonthAmountMap.getOrDefault(vehicleId, BigDecimal.ZERO);
            statisticVO.setOilCardRate(SmartBigDecimalUtil.dividePercent(oilRechargeAmount, freightAmount));
            // 尿素占比
            statisticVO.setUreaRate(SmartBigDecimalUtil.dividePercent(statisticVO.getUreaPayAmount(), freightAmount));
            // ETC占比
            statisticVO.setRoadRate(SmartBigDecimalUtil.dividePercent(statisticVO.getEtcPayAmount(), freightAmount));
            // 工资占比
            statisticVO.setSalaryRate(SmartBigDecimalUtil.dividePercent(statisticVO.getCommissionSalary(), freightAmount));
            // 现金占比
            statisticVO.setOtherRate(SmartBigDecimalUtil.dividePercent(statisticVO.getCashPayAmount(), freightAmount));
            // 利润占比
            statisticVO.setProfitRate(SmartBigDecimalUtil.dividePercent(statisticVO.getProfitRate(), freightAmount));
            return statisticVO;
        }).collect(Collectors.toList());


        return ResponseDTO.ok(carCostMonthStatisticVOList);
    }

    private BigDecimal calculatePayAmount(CarCostMonthStatisticVO statisticVO) {
        List<BigDecimal> amountList = Lists.newArrayList(
                statisticVO.getCashPayAmount(),
                statisticVO.getDieselOilPayAmount(),
                statisticVO.getGasolineOilPayAmount(),
                statisticVO.getUreaPayAmount(),
                statisticVO.getEtcPayAmount(),
                statisticVO.getInsuranceAmount(),
                statisticVO.getMaintenanceAmount(),
                statisticVO.getRepairAmount(),
                statisticVO.getReviewAmount()
        );
        return amountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}