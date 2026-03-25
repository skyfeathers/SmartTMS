package net.lab1024.tms.admin.module.business.carcost.etcpay;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndDao;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillService;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
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
public class CarCostEtcPayManager extends ServiceImpl<CarCostEtcPayDao, CarCostEtcPayEntity> {

    @Resource
    private CarCostEtcPayDao carCostEtcPayDao;
    @Resource
    private WaybillService waybillService;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private DriverDao driverDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCashInitialEndDao carCostCashInitialEndDao;
    @Resource
    private CarCostCashInitialEndService carCostCashInitialEndService;
    @Resource
    private CommonDriverBalanceService commonDriverBalanceService;
    /**
     * 新建ETC支出记录
     *
     * @param etcPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostEtcPayEntity etcPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostEtcPayDao.insert(etcPayEntity);

        Long etcPayId = etcPayEntity.getEtcPayId();
        tabulationEntity.setModuleId(etcPayId);
        carCostTabulationDao.insert(tabulationEntity);
        // 更新期初期末，需要放在更新余额之前
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(etcPayEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = etcPayEntity.getEnterpriseId();
        // 如果是现金支付，更新司机余额
        BigDecimal amountNegate = etcPayEntity.getAmount().negate();
        Integer payMode = etcPayEntity.getPayMode();
        Long driverId = etcPayEntity.getDriverId();
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            Long waybillId = etcPayEntity.getWaybillId();
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amountNegate);
            this.driverBalanceUpdate(etcPayEntity.getEtcPayId(), driverId, enterpriseId, amountNegate, false, "新增费用，司机："+driverId);
        }
    }

    /**
     * 编辑ETC支出记录
     *
     * @param etcPayEntity
     * @param beforeEtcPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostEtcPayEntity etcPayEntity, CarCostEtcPayEntity beforeEtcPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostEtcPayDao.updateById(etcPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        Long enterpriseId = beforeEtcPayEntity.getEnterpriseId();
        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforeEtcPayEntity.getAuditStatus())
            && CarCostCategoryPayModeEnum.CASH.equalsValue(beforeEtcPayEntity.getPayMode())) {
            BigDecimal lastAmount = beforeEtcPayEntity.getAmount();
            Long lastWaybillId = beforeEtcPayEntity.getWaybillId();
            Long lastDriverId = beforeEtcPayEntity.getDriverId();
            if(lastWaybillId != null) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, lastWaybillId, lastDriverId, lastAmount);
                this.driverBalanceUpdate(beforeEtcPayEntity.getEtcPayId(), lastDriverId, enterpriseId, lastAmount, true, "编辑费用，司机："+ lastDriverId);
            }

        }

        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforeEtcPayEntity.getAuditStatus())
            && CarCostCategoryPayModeEnum.CASH.equalsValue(etcPayEntity.getPayMode())) {
            Long waybillId = etcPayEntity.getWaybillId();
            Long driverId = beforeEtcPayEntity.getDriverId();
            BigDecimal amount = etcPayEntity.getAmount();
            if(waybillId != null) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
                this.driverBalanceUpdate(beforeEtcPayEntity.getEtcPayId(), driverId, enterpriseId, amount, false, "编辑费用，司机："+ driverId);
            }

        }
    }

    /**
     * ETC支出审核
     *
     * @param beforeEtcPayEntity
     * @param etcPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAudit(CarCostEtcPayEntity beforeEtcPayEntity, CarCostEtcPayEntity etcPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostEtcPayDao.updateById(etcPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        Long waybillId = beforeEtcPayEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        Long enterpriseId = beforeEtcPayEntity.getEnterpriseId();
        BigDecimal amount = beforeEtcPayEntity.getAmount();
        Long driverId = beforeEtcPayEntity.getDriverId();
        Integer payMode = beforeEtcPayEntity.getPayMode();
        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforeEtcPayEntity.getAuditStatus())
                && AuditStatusEnum.REJECT.equalsValue(etcPayEntity.getAuditStatus())
                && CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(beforeEtcPayEntity.getEtcPayId(), driverId, enterpriseId, amount, true, "审核通过数据二次驳回");
        }

        if ((AuditStatusEnum.REJECT.equalsValue(beforeEtcPayEntity.getAuditStatus()) || AuditStatusEnum.WAIT_AUDIT.equalsValue(beforeEtcPayEntity.getAuditStatus()))
                && AuditStatusEnum.AUDIT_PASS.equalsValue(etcPayEntity.getAuditStatus())
                && CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
            this.driverBalanceUpdate(beforeEtcPayEntity.getEtcPayId(), driverId, enterpriseId, amount, false, "审核驳回数据二次通过");
        }
    }

    /**
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param beforeEtcPayEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void del(CarCostTabulationEntity tabulationEntity, CarCostEtcPayEntity beforeEtcPayEntity) {
        Long etcPayId = beforeEtcPayEntity.getEtcPayId();
        carCostEtcPayDao.deleteById(etcPayId);
        carCostTabulationDao.deleteById(tabulationEntity.getTabulationId());

        Long waybillId = beforeEtcPayEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeEtcPayEntity.getAuditStatus())) {
            return;
        }
        if (!CarCostCategoryPayModeEnum.CASH.equalsValue(beforeEtcPayEntity.getPayMode())) {
            return;
        }
        // 如果原数据审核通过 进行回退余额
        Long enterpriseId = beforeEtcPayEntity.getEnterpriseId();
        BigDecimal lastAmount = beforeEtcPayEntity.getAmount();
        Long driverId = beforeEtcPayEntity.getDriverId();
        this.driverBalanceUpdate(beforeEtcPayEntity.getEtcPayId(), driverId, enterpriseId, lastAmount, true, "删除已审核通过费用");
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, lastAmount);
    }

    /**
     * 关联运单
     *
     * @param etcPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleRelateWaybill(CarCostEtcPayEntity etcPayEntity, CarCostTabulationEntity tabulationEntity, CarCostEtcPayEntity beforeEtcPayEntity) {
        carCostEtcPayDao.updateById(etcPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeEtcPayEntity.getAuditStatus())) {
            return;
        }
        if (!CarCostCategoryPayModeEnum.CASH.equalsValue(etcPayEntity.getPayMode())) {
            return;
        }
        Long enterpriseId = beforeEtcPayEntity.getEnterpriseId();
        Long waybillId = etcPayEntity.getWaybillId();
        Long driverId = etcPayEntity.getDriverId();

        BigDecimal amount = beforeEtcPayEntity.getAmount();
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
        this.driverBalanceUpdate(beforeEtcPayEntity.getEtcPayId(), driverId, enterpriseId, amount, false, "关联运单：" + waybillId);
    }

    /**
     * 取消关联运单
     *
     * @param etcPayId
     * @param tabulationId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleCancelRelateWaybill(Long etcPayId, Long tabulationId, CarCostEtcPayEntity beforeEtcPayEntity) {
        carCostEtcPayDao.updateWaybillId(etcPayId, null);
        carCostTabulationDao.updateWaybillId(tabulationId, null);
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeEtcPayEntity.getAuditStatus())) {
            return;
        }
        if (!CarCostCategoryPayModeEnum.CASH.equalsValue(beforeEtcPayEntity.getPayMode())) {
            return;
        }
        // 回退费用
        Long enterpriseId = beforeEtcPayEntity.getEnterpriseId();
        Long waybillId = beforeEtcPayEntity.getWaybillId();
        Long driverId = beforeEtcPayEntity.getDriverId();
        BigDecimal amount = beforeEtcPayEntity.getAmount();
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
        this.driverBalanceUpdate(beforeEtcPayEntity.getEtcPayId(), driverId, enterpriseId, amount, true, "取消关联运单："+ waybillId);

    }

    private void driverBalanceUpdate(Long relateOrderId, Long driverId, Long enterpriseId, BigDecimal changeAmount, Boolean incomeFlag, String content){
        DriverBalanceRecordForm recordForm =new DriverBalanceRecordForm();
        recordForm.setTradeType(DriverBalanceTradeTypeEnum.ETC_PAY.getValue());
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
