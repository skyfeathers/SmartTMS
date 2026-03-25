package net.lab1024.tms.admin.module.business.carcost.cashreceive;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashReceiveEntity;
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
public class CarCostCashReceiveManager extends ServiceImpl<CarCostCashReceiveDao, CarCostCashReceiveEntity> {

    @Resource
    private DriverDao driverDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCashReceiveDao carCostCashReceiveDao;
    @Resource
    private CarCostCashInitialEndService carCostCashInitialEndService;
    @Resource
    private CommonDriverBalanceService commonDriverBalanceService;
    /**
     * 新建现金收入
     *
     * @param cashReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostCashReceiveEntity cashReceiveEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashReceiveDao.insert(cashReceiveEntity);

        Long cashReceiveId = cashReceiveEntity.getCashReceiveId();
        tabulationEntity.setModuleId(cashReceiveId);
        carCostTabulationDao.insert(tabulationEntity);

        // 更新期初期末，需要放在更新余额之前
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(cashReceiveEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = cashReceiveEntity.getEnterpriseId();
        Long waybillId = cashReceiveEntity.getWaybillId();
        BigDecimal amount = cashReceiveEntity.getAmount();
        Long driverId = cashReceiveEntity.getDriverId();
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
        this.driverBalanceUpdate(cashReceiveEntity.getCashReceiveId(), driverId, enterpriseId, amount, true, "新增费用，司机："+driverId);

    }

    /**
     * 编辑现金收入
     *
     * @param beforeReceiveEntity
     * @param cashReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostCashReceiveEntity beforeReceiveEntity, CarCostCashReceiveEntity cashReceiveEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashReceiveDao.updateById(cashReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);


        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeReceiveEntity.getAuditStatus())) {
            return;
        }
        // 回退上次信息
        Long enterpriseId = beforeReceiveEntity.getEnterpriseId();
        Long lastWaybillId = cashReceiveEntity.getWaybillId();
        if(lastWaybillId != null) {
            Long lastDriverId = beforeReceiveEntity.getDriverId();
            BigDecimal lastAmount = beforeReceiveEntity.getAmount();
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, lastWaybillId, lastDriverId, lastAmount.negate());
            this.driverBalanceUpdate(beforeReceiveEntity.getCashReceiveId(), lastDriverId, enterpriseId, lastAmount, false, "编辑费用，司机："+ lastDriverId);
        }

        // 新增本次信息
        Long waybillId = cashReceiveEntity.getWaybillId();
        if(waybillId != null) {
            Long driverId = beforeReceiveEntity.getDriverId();
            BigDecimal amount = cashReceiveEntity.getAmount();
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(beforeReceiveEntity.getCashReceiveId(), driverId, enterpriseId, amount, true, "编辑费用，司机：" + driverId);
        }

    }

    /**
     * 审核现金收入
     *
     * @param beforeReceiveEntity
     * @param cashReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAudit(CarCostCashReceiveEntity beforeReceiveEntity, CarCostCashReceiveEntity cashReceiveEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashReceiveDao.updateById(cashReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        Long waybillId = beforeReceiveEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        Long enterpriseId = beforeReceiveEntity.getEnterpriseId();
        Long driverId = beforeReceiveEntity.getDriverId();

        BigDecimal amount = beforeReceiveEntity.getAmount();
        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforeReceiveEntity.getAuditStatus()) && AuditStatusEnum.REJECT.equalsValue(cashReceiveEntity.getAuditStatus())) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
            this.driverBalanceUpdate(beforeReceiveEntity.getCashReceiveId(), driverId, enterpriseId, amount, false, "审核通过数据二次驳回");
        }
        if ((AuditStatusEnum.REJECT.equalsValue(beforeReceiveEntity.getAuditStatus()) || AuditStatusEnum.WAIT_AUDIT.equalsValue(beforeReceiveEntity.getAuditStatus())) && AuditStatusEnum.AUDIT_PASS.equalsValue(cashReceiveEntity.getAuditStatus())) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(beforeReceiveEntity.getCashReceiveId(), driverId, enterpriseId, amount, true, "审核驳回数据二次通过");
        }
    }

    /**
     * 删除自有车费用记录
     *
     * @param tabulationEntity
     * @param beforeCashReceiveEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleDel(CarCostTabulationEntity tabulationEntity, CarCostCashReceiveEntity beforeCashReceiveEntity) {
        Long cashReceiveId = beforeCashReceiveEntity.getCashReceiveId();
        carCostCashReceiveDao.deleteById(cashReceiveId);
        carCostTabulationDao.deleteById(tabulationEntity.getTabulationId());

        Long waybillId = beforeCashReceiveEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        Long enterpriseId = beforeCashReceiveEntity.getEnterpriseId();
        // 如果绑定了运单 进行回退余额
        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforeCashReceiveEntity.getAuditStatus())) {
            BigDecimal lastAmount = beforeCashReceiveEntity.getAmount();
            Long driverId = beforeCashReceiveEntity.getDriverId();
            this.driverBalanceUpdate(beforeCashReceiveEntity.getCashReceiveId(), driverId, enterpriseId, lastAmount, false, "删除已审核通过费用");
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, lastAmount.negate());
        }
    }

    /**
     * 关联运单
     *
     * @param cashReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleRelateWaybill(CarCostCashReceiveEntity cashReceiveEntity, CarCostTabulationEntity tabulationEntity, CarCostCashReceiveEntity beforeCashReceiveEntity) {
        carCostCashReceiveDao.updateById(cashReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeCashReceiveEntity.getAuditStatus())){
            return;
        }
        Long enterpriseId = beforeCashReceiveEntity.getEnterpriseId();
        Long waybillId = cashReceiveEntity.getWaybillId();
        Long driverId = cashReceiveEntity.getDriverId();

        BigDecimal amount = beforeCashReceiveEntity.getAmount();
        // 更新费用
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
        this.driverBalanceUpdate(beforeCashReceiveEntity.getCashReceiveId(), driverId, enterpriseId, amount, true, "关联运单：" + waybillId);
    }

    /**
     * 取消关联运单
     *
     * @param cashReceiveId
     * @param tabulationId
     * @param beforeCashReceiveEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleCancelRelateWaybill(Long cashReceiveId, Long tabulationId, CarCostCashReceiveEntity beforeCashReceiveEntity) {
        carCostCashReceiveDao.updateWaybillId(cashReceiveId, null);
        carCostTabulationDao.updateWaybillId(tabulationId, null);

        // 回退费用
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeCashReceiveEntity.getAuditStatus())){
           return;
        }
        Long enterpriseId = beforeCashReceiveEntity.getEnterpriseId();
        Long waybillId = beforeCashReceiveEntity.getWaybillId();
        Long driverId = beforeCashReceiveEntity.getDriverId();
        BigDecimal amount = beforeCashReceiveEntity.getAmount();
        carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
        this.driverBalanceUpdate(beforeCashReceiveEntity.getCashReceiveId(), driverId, enterpriseId, amount, false, "取消关联运单："+waybillId);

    }


    private void driverBalanceUpdate(Long relateOrderId, Long driverId, Long enterpriseId, BigDecimal changeAmount, Boolean incomeFlag, String content){
        DriverBalanceRecordForm recordForm =new DriverBalanceRecordForm();
        recordForm.setTradeType(DriverBalanceTradeTypeEnum.CASH_RECEIVE.getValue());
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