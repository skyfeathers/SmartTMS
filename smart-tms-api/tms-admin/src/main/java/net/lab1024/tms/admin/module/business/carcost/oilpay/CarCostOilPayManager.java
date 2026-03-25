package net.lab1024.tms.admin.module.business.carcost.oilpay;

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
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.driver.CommonDriverBalanceService;
import net.lab1024.tms.common.module.business.driver.constants.DriverBalanceTradeTypeEnum;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceChangeForm;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceRecordForm;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class CarCostOilPayManager extends ServiceImpl<CarCostOilPayDao, CarCostOilPayEntity> {

    @Resource
    private DriverDao driverDao;
    @Resource
    private WaybillService waybillService;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private OilCardService oilCardService;
    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private CarCostOilPayDao carCostOilPayDao;
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
    @Resource
    private CommonDriverBalanceService commonDriverBalanceService;
    /**
     * 新建油费支出记录
     *
     * @param moduleId
     * @param payMode
     * @param oilPayEntity
     * @param tabulationEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(Long moduleId, Integer payMode, CarCostOilPayEntity oilPayEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilPayDao.insert(oilPayEntity);

        Long oilPayId = oilPayEntity.getOilPayId();
        tabulationEntity.setModuleId(oilPayId);
        carCostTabulationDao.insert(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(oilPayEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = oilPayEntity.getEnterpriseId();
        Long waybillId = oilPayEntity.getWaybillId();
        BigDecimal amount = oilPayEntity.getAmount().negate();
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, moduleId, amount);
            this.driverBalanceUpdate(oilPayId, moduleId, enterpriseId, amount, false, "新增费用，司机："+moduleId);
        } else {
            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, moduleId, amount);
            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(moduleId);
            changeForm.setChangeAmount(amount);
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY, dataTracerRequestForm);
        }
    }

    /**
     * 更新油费支出记录
     *
     * @param oilPayEntity
     * @param beforeOilPayEntity
     * @param tabulationEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostOilPayEntity oilPayEntity, CarCostOilPayEntity beforeOilPayEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilPayDao.updateById(oilPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(oilPayEntity.getAuditStatus())) {
            return;
        }
        Long enterpriseId = beforeOilPayEntity.getEnterpriseId();
        Long lastWaybillId = beforeOilPayEntity.getWaybillId();
        BigDecimal lastAmount = beforeOilPayEntity.getAmount();
        Integer lastPayMode = beforeOilPayEntity.getPayMode();
        Long lastDriverId = beforeOilPayEntity.getDriverId();
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(lastPayMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, lastWaybillId, lastDriverId, lastAmount);
            this.driverBalanceUpdate(beforeOilPayEntity.getOilPayId(), lastDriverId, enterpriseId, lastAmount, true, "编辑费用，司机："+ lastDriverId);
        } else {
            Long lastOilCardId = beforeOilPayEntity.getOilCardId();
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

        Long waybillId = beforeOilPayEntity.getWaybillId();
        Integer payMode = oilPayEntity.getPayMode();
        BigDecimal amountNegate = oilPayEntity.getAmount().negate();
        Long driverId = oilPayEntity.getDriverId();
        Long oilCardId = oilPayEntity.getOilCardId();
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amountNegate);
            this.driverBalanceUpdate(beforeOilPayEntity.getOilPayId(), driverId, enterpriseId, amountNegate, false, "编辑费用，司机："+ driverId);
        } else {
            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amountNegate);
            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(oilCardId);
            changeForm.setChangeAmount(amountNegate);
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_PAY.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_PAY, dataTracerRequestForm);
        }
    }

    /**
     * 油费支付审核
     *
     * @param beforePayEntity
     * @param oilPayEntity
     * @param tabulationEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAudit(CarCostOilPayEntity beforePayEntity, CarCostOilPayEntity oilPayEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilPayDao.updateById(oilPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        Long waybillId = beforePayEntity.getWaybillId();
        if(waybillId == null) {
            return;
        }
        Long enterpriseId = beforePayEntity.getEnterpriseId();
        Long oilCardId = beforePayEntity.getOilCardId();
        BigDecimal amount = beforePayEntity.getAmount();
        Integer payMode = beforePayEntity.getPayMode();
        Long driverId = beforePayEntity.getDriverId();
        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforePayEntity.getAuditStatus()) && AuditStatusEnum.REJECT.equalsValue(oilPayEntity.getAuditStatus())) {
            // 回退费用
            if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
                this.driverBalanceUpdate(beforePayEntity.getOilPayId(), driverId, enterpriseId, amount, true, "审核通过数据二次驳回");
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
        if ((AuditStatusEnum.REJECT.equalsValue(beforePayEntity.getAuditStatus()) || AuditStatusEnum.WAIT_AUDIT.equalsValue(beforePayEntity.getAuditStatus())) && AuditStatusEnum.AUDIT_PASS.equalsValue(oilPayEntity.getAuditStatus())) {
            // 更新费用
            if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
                carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
                this.driverBalanceUpdate(beforePayEntity.getOilPayId(), driverId, enterpriseId, amount, false, "审核驳回数据二次通过");
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
     * 删除费用记录
     *
     * @param tabulationEntity
     * @param beforePayEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleDel(CarCostTabulationEntity tabulationEntity, CarCostOilPayEntity beforePayEntity, DataTracerRequestForm dataTracerRequestForm) {
        Long oilPayId = beforePayEntity.getOilPayId();
        carCostOilPayDao.deleteById(oilPayId);
        carCostTabulationDao.deleteById(tabulationEntity.getTabulationId());

        Long waybillId = tabulationEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforePayEntity.getAuditStatus())) {
            return;
        }
        // 回退费用
        Long enterpriseId = beforePayEntity.getEnterpriseId();
        Long oilCardId = beforePayEntity.getOilCardId();
        BigDecimal amount = beforePayEntity.getAmount();
        Integer payMode = beforePayEntity.getPayMode();
        Long driverId = beforePayEntity.getDriverId();
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(beforePayEntity.getOilPayId(), driverId, enterpriseId, amount, true, "删除已审核通过费用");
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
     * @param oilPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleRelateWaybill(CarCostOilPayEntity oilPayEntity, CarCostTabulationEntity tabulationEntity, CarCostOilPayEntity beforeOilPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilPayDao.updateById(oilPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeOilPayEntity.getAuditStatus())){
            return;
        }
        Long enterpriseId = beforeOilPayEntity.getEnterpriseId();
        Long waybillId = oilPayEntity.getWaybillId();

        Long oilCardId = beforeOilPayEntity.getOilCardId();
        BigDecimal amount = beforeOilPayEntity.getAmount();
        Integer payMode = beforeOilPayEntity.getPayMode();
        Long driverId = beforeOilPayEntity.getDriverId();
        // 更新费用
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount.negate());
            this.driverBalanceUpdate(beforeOilPayEntity.getOilPayId(), driverId, enterpriseId, amount, false, "关联运单：" + waybillId);
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
     * @param oilPayId
     * @param tabulationId
     * @param beforeOilPayEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleCancelRelateWaybill(Long oilPayId, Long tabulationId, CarCostOilPayEntity beforeOilPayEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilPayDao.updateWaybillId(oilPayId, null);
        carCostTabulationDao.updateWaybillId(tabulationId, null);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeOilPayEntity.getAuditStatus())){
            return;
        }
        Long enterpriseId = beforeOilPayEntity.getEnterpriseId();
        Long waybillId = beforeOilPayEntity.getWaybillId();
        Long oilCardId = beforeOilPayEntity.getOilCardId();
        BigDecimal amount = beforeOilPayEntity.getAmount();
        Integer payMode = beforeOilPayEntity.getPayMode();
        Long driverId = beforeOilPayEntity.getDriverId();
        // 回退费用
        if (CarCostCategoryPayModeEnum.CASH.equalsValue(payMode)) {
            carCostCashInitialEndService.updateInitialEnd(enterpriseId, waybillId, driverId, amount);
            this.driverBalanceUpdate(beforeOilPayEntity.getOilPayId(), driverId, enterpriseId, amount, true, "取消关联运单："+ waybillId);
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
        recordForm.setTradeType(DriverBalanceTradeTypeEnum.OIL_CARD_PAY.getValue());
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
