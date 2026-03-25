package net.lab1024.tms.admin.module.business.waybill.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.CarCostBasicInfoDao;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostBasicInfoEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/2/26 下午4:28
 */
@Service
public class WaybillCommonService {

    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private CarCostBasicInfoDao carCostBasicInfoDao;
    @Autowired
    private CarCostTabulationDao carCostTabulationDao;
    /**
     * 计算利润信息 应收金额 - 应付金额 - 工资 - 车辆在途费用 - 税金
     *
     * @param receiveAmount    运单信息
     * @param taxPoint 税点
     * @return
     */
    public BigDecimal calculateProfitAmount(BigDecimal receiveAmount, BigDecimal payableAmount, BigDecimal salaryAmount, BigDecimal carCostAmount, BigDecimal taxPoint) {
        // 税金  = 应收 / (1+ 税点) * 税点
        BigDecimal taxAmount = receiveAmount.divide(new BigDecimal("100").add(taxPoint), 10, RoundingMode.HALF_UP);
        taxAmount = SmartBigDecimalUtil.Amount.multiply(taxAmount, taxPoint);

        BigDecimal profitAmount = receiveAmount.subtract(payableAmount).subtract(taxAmount).setScale(SmartBigDecimalUtil.PRICE_DECIMAL_POINT, RoundingMode.HALF_UP);
        return profitAmount;
    }

    /**
     * 更新运单利润
     * @param waybillId
     */
    public void updateProfitAmount(Long waybillId) {
        WaybillEntity waybillEntity = waybillDao.selectById(waybillId);
        BigDecimal taxPoint = waybillEntity.getTaxPoint();
        BigDecimal receiveAmount = waybillEntity.getReceiveAmount();
        BigDecimal payableAmount = waybillEntity.getPayableAmount();
        BigDecimal salaryAmount = BigDecimal.ONE;
        CarCostBasicInfoEntity carCostBasicInfoEntity = carCostBasicInfoDao.selectByWaybillIdAndConfirmFlag(waybillId, true);
        if (carCostBasicInfoEntity != null) {
            salaryAmount =  carCostBasicInfoEntity.getSalaryTotal();
        }
        BigDecimal carCostAmount = BigDecimal.ONE;
        List<CarCostTabulationEntity> carCostTabulationEntityList = carCostTabulationDao.selectByWaybillIdAndAuditStatus(waybillId, AuditStatusEnum.AUDIT_PASS.getValue());
        if (CollectionUtils.isNotEmpty(carCostTabulationEntityList)) {
            List<Integer> moduleTypeList = Lists.newArrayList(CarCostModuleTypeEnum.CASH_PAY.getValue(), CarCostModuleTypeEnum.OIL_PAY.getValue(), CarCostModuleTypeEnum.ETC_PAY.getValue(), CarCostModuleTypeEnum.UREA_PAY.getValue());
            carCostAmount = carCostTabulationEntityList.stream().filter(e->moduleTypeList.contains(e.getModuleType())).map(e -> e.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        BigDecimal profitAmount = this.calculateProfitAmount(receiveAmount, payableAmount, salaryAmount, carCostAmount, taxPoint);
        WaybillEntity updateWaybillEntity = new WaybillEntity();
        updateWaybillEntity.setWaybillId(waybillId);
        updateWaybillEntity.setSalaryAmount(salaryAmount);
        updateWaybillEntity.setCarCostAmount(carCostAmount);
        updateWaybillEntity.setProfitAmount(profitAmount);
        waybillDao.updateById(updateWaybillEntity);
    }
}