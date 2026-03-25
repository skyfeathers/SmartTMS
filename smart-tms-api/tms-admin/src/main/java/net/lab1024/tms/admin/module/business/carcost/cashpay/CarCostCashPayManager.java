package net.lab1024.tms.admin.module.business.carcost.cashpay;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndDao;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillService;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.driver.CommonDriverBalanceService;
import net.lab1024.tms.common.module.business.driver.constants.DriverBalanceTradeTypeEnum;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceChangeForm;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceRecordForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class CarCostCashPayManager extends ServiceImpl<CarCostCashPayDao, CarCostCashPayEntity> {

    @Resource
    private DriverDao driverDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCashPayDao carCostCashPayDao;
    @Resource
    private CarCostCashInitialEndService carCostCashInitialEndService;
    @Resource
    private CommonDriverBalanceService commonDriverBalanceService;
    /**
     * 新建现金支出记录
     *
     * @param cashPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostCashPayEntity cashPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashPayDao.insert(cashPayEntity);

        Long cashPayId = cashPayEntity.getCashPayId();
        tabulationEntity.setModuleId(cashPayId);
        carCostTabulationDao.insert(tabulationEntity);
        // 更新期初期末，需要放在更新余额之前
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(cashPayEntity.getAuditStatus())) {
            return;
        }
        // 更新本次费用
        Long enterpriseId = cashPayEntity.getEnterpriseId();
        Long waybillId = cashPayEntity.getWaybillId();
        BigDecimal amount = cashPayEntity.getAmount().negate();
        Long driverId = cashPayEntity.getDriverId();
        // 减期初
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
        // 减司机余额
        this.driverBalanceUpdate(cashPayId, driverId, enterpriseId, amount, false, "新增费用，司机："+driverId);
    }



    /**
     * 编辑现金支出记录
     *
     * @param beforeCashPayEntity
     * @param cashPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostCashPayEntity beforeCashPayEntity, CarCostCashPayEntity cashPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashPayDao.updateById(cashPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeCashPayEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = beforeCashPayEntity.getEnterpriseId();
        // 上次司机余额回退
        Long lastWaybillId = beforeCashPayEntity.getWaybillId();
        if(lastWaybillId != null) {
            BigDecimal lastAmount = beforeCashPayEntity.getAmount();
            Long lastDriverId = beforeCashPayEntity.getDriverId();
            this.driverBalanceUpdate(cashPayEntity.getCashPayId(), lastDriverId, enterpriseId, lastAmount, true, "编辑费用，司机："+ lastDriverId);
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, lastWaybillId, lastDriverId, lastAmount);
        }

        // 本次费用更新
        Long waybillId = cashPayEntity.getWaybillId();
        if(waybillId != null) {
            Long driverId = beforeCashPayEntity.getDriverId();
            BigDecimal amount = cashPayEntity.getAmount().negate();
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(cashPayEntity.getCashPayId(), driverId, enterpriseId, amount, false, "编辑费用，司机："+ driverId);
        }

    }

    /**
     * 现金支出审核
     *
     * @param beforePayEntity
     * @param cashPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAudit(CarCostCashPayEntity beforePayEntity, CarCostCashPayEntity cashPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashPayDao.updateById(cashPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        Long waybillId = beforePayEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        Long enterpriseId = beforePayEntity.getEnterpriseId();
        BigDecimal amount = beforePayEntity.getAmount();
        Long driverId = beforePayEntity.getDriverId();
        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforePayEntity.getAuditStatus()) && AuditStatusEnum.REJECT.equalsValue(cashPayEntity.getAuditStatus())) {
            // 回退费用
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(cashPayEntity.getCashPayId(), driverId, enterpriseId, amount, true, "审核通过数据二次驳回");
        }
        if ((AuditStatusEnum.REJECT.equalsValue(beforePayEntity.getAuditStatus()) || AuditStatusEnum.WAIT_AUDIT.equalsValue(beforePayEntity.getAuditStatus())) && AuditStatusEnum.AUDIT_PASS.equalsValue(cashPayEntity.getAuditStatus())) {
            // 更新费用
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
            this.driverBalanceUpdate(cashPayEntity.getCashPayId(), driverId, enterpriseId, amount, false, "审核驳回数据二次通过");
        }
    }

    /**
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param beforeCashPayEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleDel(CarCostTabulationEntity tabulationEntity, CarCostCashPayEntity beforeCashPayEntity) {
        Long cashPayId = beforeCashPayEntity.getCashPayId();

        carCostCashPayDao.deleteById(cashPayId);
        carCostTabulationDao.deleteById(tabulationEntity.getTabulationId());

        Long waybillId = beforeCashPayEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeCashPayEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = beforeCashPayEntity.getEnterpriseId();
        // 如果审核通过进行回退余额
        BigDecimal lastAmount = beforeCashPayEntity.getAmount();
        Long driverId = beforeCashPayEntity.getDriverId();
        this.driverBalanceUpdate(beforeCashPayEntity.getCashPayId(), driverId, enterpriseId, lastAmount, true, "删除已审核通过费用");
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, lastAmount);
    }

    /**
     * 关联运单
     *
     * @param cashPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleRelateWaybill(CarCostCashPayEntity cashPayEntity, CarCostTabulationEntity tabulationEntity, CarCostCashPayEntity beforeCashPayEntity) {
        carCostCashPayDao.updateById(cashPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeCashPayEntity.getAuditStatus())){
            return;
        }
        Long enterpriseId = beforeCashPayEntity.getEnterpriseId();
        Long waybillId = cashPayEntity.getWaybillId();
        Long driverId = cashPayEntity.getDriverId();

        BigDecimal amount = beforeCashPayEntity.getAmount();
        // 更新费用
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
        this.driverBalanceUpdate(cashPayEntity.getCashPayId(), driverId, enterpriseId, amount, false, "关联运单：" + waybillId);
    }

    /**
     * 取消关联运单
     *
     * @param cashPayId
     * @param tabulationId
     * @param beforeCashPayEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleCancelRelateWaybill(Long cashPayId, Long tabulationId,  CarCostCashPayEntity beforeCashPayEntity) {
        carCostCashPayDao.updateWaybillId(cashPayId, null);
        carCostTabulationDao.updateWaybillId(tabulationId, null);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeCashPayEntity.getAuditStatus())){
            return;
        }
        Long enterpriseId = beforeCashPayEntity.getEnterpriseId();
        Long waybillId = beforeCashPayEntity.getWaybillId();
        Long driverId = beforeCashPayEntity.getDriverId();
        BigDecimal amount = beforeCashPayEntity.getAmount();
        // 回退费用
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
        this.driverBalanceUpdate(beforeCashPayEntity.getCashPayId(), driverId, enterpriseId, amount, true, "取消关联运单："+ waybillId);
    }

    private void driverBalanceUpdate(Long relateOrderId, Long driverId, Long enterpriseId, BigDecimal changeAmount, Boolean incomeFlag, String content){
        DriverBalanceRecordForm recordForm =new DriverBalanceRecordForm();
        recordForm.setTradeType(DriverBalanceTradeTypeEnum.CASH_PAY.getValue());
        recordForm.setTradeContent(content);
        recordForm.setRelateOrderId(relateOrderId);

        DriverBalanceChangeForm driverBalanceChangeForm = new DriverBalanceChangeForm();
        driverBalanceChangeForm.setDriverId(driverId);
        driverBalanceChangeForm.setEnterpriseId(enterpriseId);
        driverBalanceChangeForm.setChangeAmount(changeAmount.abs());
        driverBalanceChangeForm.setIncomeFlag(incomeFlag);
        driverBalanceChangeForm.setRecordForm(recordForm);
        commonDriverBalanceService.balanceChange(driverBalanceChangeForm);
    }
}