package net.lab1024.tms.admin.module.business.carcost.ureapay;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndDao;
import net.lab1024.tms.admin.module.business.carcost.cashInitialend.CarCostCashInitialEndService;
import net.lab1024.tms.admin.module.business.carcost.oilcardinitialend.CarCostOilCardInitialEndDao;
import net.lab1024.tms.admin.module.business.carcost.oilcardinitialend.CarCostOilCardInitialEndService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardBalanceChangeForm;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardService;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillService;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostUreaPayEntity;
import net.lab1024.tms.common.module.business.driver.CommonDriverBalanceService;
import net.lab1024.tms.common.module.business.driver.constants.DriverBalanceTradeTypeEnum;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceChangeForm;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceRecordForm;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class CarCostUreaPayManager extends ServiceImpl<CarCostUreaPayDao, CarCostUreaPayEntity> {

    @Resource
    private DriverDao driverDao;
    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private WaybillService waybillService;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private CarCostUreaPayDao carCostUreaPayDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostCashInitialEndDao carCostCashInitialEndDao;
    @Resource
    private CarCostOilCardInitialEndDao carCostOilCardInitialEndDao;
    @Resource
    private CarCostCashInitialEndService carCostCashInitialEndService;
    @Resource
    private CarCostOilCardInitialEndService carCostOilCardInitialEndService;
    @Autowired
    private OilCardService oilCardService;
    @Resource
    private CommonDriverBalanceService commonDriverBalanceService;
    /**
     * 新建尿素支出
     *
     * @param moduleId
     * @param ureaPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(Long moduleId, CarCostUreaPayEntity ureaPayEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostUreaPayDao.insert(ureaPayEntity);

        Long ureaPayId = ureaPayEntity.getUreaPayId();
        tabulationEntity.setModuleId(ureaPayId);
        carCostTabulationDao.insert(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(ureaPayEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = ureaPayEntity.getEnterpriseId();
        Long waybillId = ureaPayEntity.getWaybillId();
        BigDecimal amount = ureaPayEntity.getAmount().negate();
        Integer payMode = ureaPayEntity.getPayMode();
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, moduleId, amount);
            this.driverBalanceUpdate(ureaPayEntity.getUreaPayId(), moduleId, enterpriseId, amount, false, "新增费用，司机："+moduleId);
        } else {
            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, moduleId, amount);
            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(ureaPayEntity.getOilCardId());
            changeForm.setChangeAmount(amount);
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_UREA_PAY.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_UREA_PAY, dataTracerRequestForm);
        }
    }

    /**
     * 更新尿素支出
     *
     * @param ureaPayEntity
     * @param beforeUreaPayEntity
     * @param tabulationEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostUreaPayEntity ureaPayEntity, CarCostUreaPayEntity beforeUreaPayEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostUreaPayDao.updateById(ureaPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(ureaPayEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = beforeUreaPayEntity.getEnterpriseId();
        Long lastWaybillId = beforeUreaPayEntity.getWaybillId();
        if (lastWaybillId != null) {
            BigDecimal lastAmount = beforeUreaPayEntity.getAmount();
            Integer lastPayMode = beforeUreaPayEntity.getPayMode();
            Long lastDriverId = beforeUreaPayEntity.getDriverId();
            if (CarCostCategoryPayModeEnum.CASH.equalsValue(lastPayMode)) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, lastWaybillId, lastDriverId, lastAmount);
                this.driverBalanceUpdate(beforeUreaPayEntity.getUreaPayId(), lastDriverId, enterpriseId, lastAmount, true, "编辑费用，司机："+ lastDriverId);
            } else {
                Long lastOilCardId = beforeUreaPayEntity.getOilCardId();
                // 退回油卡收入
                OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(lastOilCardId);
                if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
                    throw new BusinessException("更改之前的油卡不可用，余额不可退回");
                }
                Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
                OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
                if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
                    throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
                }

                carCostOilCardInitialEndService.updateOilCardInitialEnd(lastWaybillId, lastOilCardId, lastAmount);

                OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
                changeForm.setOilCardId(lastOilCardId);
                changeForm.setChangeAmount(lastAmount);
                changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY_RETREAT.getValue());
                oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY_RETREAT, dataTracerRequestForm);
            }
        }

        Long waybillId = ureaPayEntity.getWaybillId();
        if (waybillId != null) {
            Integer payMode = ureaPayEntity.getPayMode();
            BigDecimal amountNegate = ureaPayEntity.getAmount().negate();
            Long driverId = ureaPayEntity.getDriverId();
            Long oilCardId = ureaPayEntity.getOilCardId();
            if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amountNegate);
                this.driverBalanceUpdate(beforeUreaPayEntity.getUreaPayId(), driverId, enterpriseId, amountNegate, false, "编辑费用，司机："+ driverId);
            } else {
                carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amountNegate);
                OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
                changeForm.setOilCardId(oilCardId);
                changeForm.setChangeAmount(amountNegate);
                changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY.getValue());
                oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY, dataTracerRequestForm);
            }

        }

    }

    /**
     * 审核尿素支出
     *
     * @param beforeUreaPayEntity
     * @param ureaPayEntity
     * @param tabulationEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAudit(CarCostUreaPayEntity beforeUreaPayEntity, CarCostUreaPayEntity ureaPayEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostUreaPayDao.updateById(ureaPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        Long waybillId = beforeUreaPayEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        Long enterpriseId = beforeUreaPayEntity.getEnterpriseId();
        Long oilCardId = beforeUreaPayEntity.getOilCardId();
        BigDecimal amount = beforeUreaPayEntity.getAmount();
        Integer payMode = beforeUreaPayEntity.getPayMode();
        Long driverId = beforeUreaPayEntity.getDriverId();
        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforeUreaPayEntity.getAuditStatus()) && AuditStatusEnum.REJECT.equalsValue(ureaPayEntity.getAuditStatus())) {
            // 回退费用
            if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
                this.driverBalanceUpdate(beforeUreaPayEntity.getUreaPayId(), driverId, enterpriseId, amount, true, "审核通过数据二次驳回");
            } else {

                // 退回油卡收入
                OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(oilCardId);
                if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
                    throw new BusinessException("更改之前的油卡不可用，余额不可退回");
                }
                Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
                OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
                if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
                    throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
                }

                carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount);

                OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
                changeForm.setOilCardId(oilCardId);
                changeForm.setChangeAmount(amount);
                changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY_RETREAT.getValue());
                oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY_RETREAT, dataTracerRequestForm);
            }

        }
        if ((AuditStatusEnum.REJECT.equalsValue(beforeUreaPayEntity.getAuditStatus())  || AuditStatusEnum.WAIT_AUDIT.equalsValue(beforeUreaPayEntity.getAuditStatus())) && AuditStatusEnum.AUDIT_PASS.equalsValue(ureaPayEntity.getAuditStatus())) {
            // 更新费用
            if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
                this.driverBalanceUpdate(beforeUreaPayEntity.getUreaPayId(), driverId, enterpriseId, amount, false, "审核驳回数据二次通过");
            } else {
                carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount.negate());
                OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
                changeForm.setOilCardId(oilCardId);
                changeForm.setChangeAmount(amount.negate());
                changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY.getValue());
                oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY, dataTracerRequestForm);
            }
        }

    }

    /**
     * 删除费用类型
     *
     * @param tabulationEntity
     * @param beforeUreaPayEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleDel(CarCostTabulationEntity tabulationEntity, CarCostUreaPayEntity beforeUreaPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        Long ureaPayId = beforeUreaPayEntity.getUreaPayId();
        carCostUreaPayDao.deleteById(ureaPayId);
        carCostTabulationDao.deleteById(tabulationEntity.getTabulationId());

        Long waybillId = tabulationEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeUreaPayEntity.getAuditStatus())) {
            return;
        }
        // 回退费用
        Long enterpriseId = beforeUreaPayEntity.getEnterpriseId();
        Long oilCardId = beforeUreaPayEntity.getOilCardId();
        BigDecimal amount = beforeUreaPayEntity.getAmount();
        Integer payMode = beforeUreaPayEntity.getPayMode();
        Long driverId = beforeUreaPayEntity.getDriverId();
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(beforeUreaPayEntity.getUreaPayId(), driverId, enterpriseId, amount, true, "删除已审核通过费用");
        } else {
            // 退回油卡收入
            OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(oilCardId);
            if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
                throw new BusinessException("更改之前的油卡不可用，余额不可退回");
            }
            Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
            OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
            if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
                throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
            }

            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount);

            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(oilCardId);
            changeForm.setChangeAmount(amount);
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY_RETREAT.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY_RETREAT, dataTracerRequestForm);
        }
    }

    /**
     * 关联运单
     *
     * @param ureaPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleRelateWaybill(CarCostUreaPayEntity ureaPayEntity, CarCostTabulationEntity tabulationEntity, CarCostUreaPayEntity beforeUreaPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostUreaPayDao.updateById(ureaPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeUreaPayEntity.getAuditStatus())){
            return;
        }
        Long waybillId = ureaPayEntity.getWaybillId();
        Long enterpriseId = beforeUreaPayEntity.getEnterpriseId();
        Long oilCardId = beforeUreaPayEntity.getOilCardId();
        BigDecimal amount = beforeUreaPayEntity.getAmount();
        Integer payMode = beforeUreaPayEntity.getPayMode();
        Long driverId = beforeUreaPayEntity.getDriverId();
        // 更新费用
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
            this.driverBalanceUpdate(beforeUreaPayEntity.getUreaPayId(), driverId, enterpriseId, amount, false, "关联运单：" + waybillId);
        } else {
            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount.negate());
            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(oilCardId);
            changeForm.setChangeAmount(amount.negate());
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY, dataTracerRequestForm);
        }
    }

    /**
     * 取消关联运单
     *
     * @param ureaPayId
     * @param tabulationId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleCancelRelateWaybill(Long ureaPayId, Long tabulationId, CarCostUreaPayEntity beforeUreaPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostUreaPayDao.updateWaybillId(ureaPayId, null);
        carCostTabulationDao.updateWaybillId(tabulationId, null);


        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeUreaPayEntity.getAuditStatus())){
            return;
        }
        Long enterpriseId = beforeUreaPayEntity.getEnterpriseId();
        Long waybillId = beforeUreaPayEntity.getWaybillId();
        Long oilCardId = beforeUreaPayEntity.getOilCardId();
        BigDecimal amount = beforeUreaPayEntity.getAmount();
        Integer payMode = beforeUreaPayEntity.getPayMode();
        Long driverId = beforeUreaPayEntity.getDriverId();
        // 回退费用
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(beforeUreaPayEntity.getUreaPayId(), driverId, enterpriseId, amount, true, "取消关联运单："+ waybillId);
        } else {

            // 退回油卡收入
            OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(oilCardId);
            if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
                throw new BusinessException("更改之前的油卡不可用，余额不可退回");
            }
            Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
            OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
            if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
                throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
            }

            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount);

            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(oilCardId);
            changeForm.setChangeAmount(amount);
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY_RETREAT.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY_RETREAT, dataTracerRequestForm);
        }
    }

    private void driverBalanceUpdate(Long relateOrderId, Long driverId, Long enterpriseId, BigDecimal changeAmount, Boolean incomeFlag, String content){
        DriverBalanceRecordForm recordForm =new DriverBalanceRecordForm();
        recordForm.setTradeType(DriverBalanceTradeTypeEnum.UREA_PAY.getValue());
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
