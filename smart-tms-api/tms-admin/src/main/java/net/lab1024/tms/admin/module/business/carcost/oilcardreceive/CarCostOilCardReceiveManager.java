package net.lab1024.tms.admin.module.business.carcost.oilcardreceive;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.carcost.oilcardinitialend.CarCostOilCardInitialEndDao;
import net.lab1024.tms.admin.module.business.carcost.oilcardinitialend.CarCostOilCardInitialEndService;
import net.lab1024.tms.admin.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.form.OilCardBalanceChangeForm;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardService;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillService;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardReceiveEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardDataTracerOperateTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class CarCostOilCardReceiveManager extends ServiceImpl<CarCostOilCardReceiveDao, CarCostOilCardReceiveEntity> {

    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private WaybillService waybillService;
    @Resource
    private WaybillDao waybillDao;
    @Resource
    private OilCardService oilCardService;
    @Resource
    private CarCostOilCardReceiveDao carCostOilCardReceiveDao;
    @Resource
    private CarCostOilCardInitialEndDao carCostOilCardInitialEndDao;
    @Resource
    private CarCostTabulationDao carCostTabulationDao;
    @Resource
    private CarCostOilCardInitialEndService carCostOilCardInitialEndService;

    /**
     * 新建油卡收入
     *
     * @param oilCardReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostOilCardReceiveEntity oilCardReceiveEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilCardReceiveDao.insert(oilCardReceiveEntity);

        tabulationEntity.setModuleId(oilCardReceiveEntity.getOilCardReceiveId());
        carCostTabulationDao.insert(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(oilCardReceiveEntity.getAuditStatus())) {
            return;
        }
        Long waybillId = oilCardReceiveEntity.getWaybillId();
        BigDecimal amount = oilCardReceiveEntity.getAmount();
        Long oilCardId = oilCardReceiveEntity.getOilCardId();
        carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount);

        OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
        changeForm.setOilCardId(oilCardId);
        changeForm.setChangeAmount(amount);
        changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE.getValue());
        oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE, dataTracerRequestForm);
    }

    /**
     * 编辑油卡收入
     *
     * @param oilCardReceiveEntity
     * @param beforeOilCardReceiveEntity
     * @param tabulationEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostOilCardReceiveEntity oilCardReceiveEntity, CarCostOilCardReceiveEntity beforeOilCardReceiveEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        // 更新收入记录
        carCostOilCardReceiveDao.updateById(oilCardReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeOilCardReceiveEntity.getAuditStatus())) {
            return;
        }
        // 回退上次油卡收入
        Long lastOilCardId = beforeOilCardReceiveEntity.getOilCardId();
        OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(lastOilCardId);
        if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
            throw new BusinessException("更改之前的油卡不可用，余额不可退回");
        }
        Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
        OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
        if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
            throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
        }
        Long lastWaybillId = beforeOilCardReceiveEntity.getWaybillId();
        if (lastWaybillId != null) {
            BigDecimal lastAmount = beforeOilCardReceiveEntity.getAmount();
            carCostOilCardInitialEndService.updateOilCardInitialEnd(lastWaybillId, lastOilCardId, lastAmount.negate());
            // 退回油卡收入
            OilCardBalanceChangeForm lastChangeForm = new OilCardBalanceChangeForm();
            lastChangeForm.setOilCardId(lastOilCardId);
            lastChangeForm.setChangeAmount(lastAmount.negate());
            lastChangeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT.getValue());
            oilCardService.carCostChangeBalance(lastChangeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT, dataTracerRequestForm);

        }
        Long waybillId = oilCardReceiveEntity.getWaybillId();
        if (waybillId != null) {
            // 本次油卡充值
            BigDecimal amount = oilCardReceiveEntity.getAmount();
            Long oilCardId = oilCardReceiveEntity.getOilCardId();
            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount);

            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(oilCardId);
            changeForm.setChangeAmount(amount);
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE, dataTracerRequestForm);
        }

    }

    /**
     * 油卡收入审核
     *
     * @param beforeOilCardReceiveEntity
     * @param oilCardReceiveEntity
     * @param tabulationEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAudit(CarCostOilCardReceiveEntity beforeOilCardReceiveEntity, CarCostOilCardReceiveEntity oilCardReceiveEntity, CarCostTabulationEntity tabulationEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilCardReceiveDao.updateById(oilCardReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        Long waybillId = beforeOilCardReceiveEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }

        Long  oilCardId = beforeOilCardReceiveEntity.getOilCardId();
        BigDecimal amount = beforeOilCardReceiveEntity.getAmount();

        if (AuditStatusEnum.AUDIT_PASS.equalsValue(beforeOilCardReceiveEntity.getAuditStatus()) && AuditStatusEnum.REJECT.equalsValue(oilCardReceiveEntity.getAuditStatus())) {
            // 回退上次油卡收入
            OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(oilCardId);
            if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
                throw new BusinessException("更改之前的油卡不可用，余额不可退回");
            }
            Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
            OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
            if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
                throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
            }
            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount.negate());
            // 退回油卡收入
            OilCardBalanceChangeForm lastChangeForm = new OilCardBalanceChangeForm();
            lastChangeForm.setOilCardId(oilCardId);
            lastChangeForm.setChangeAmount(amount.negate());
            lastChangeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT.getValue());
            oilCardService.carCostChangeBalance(lastChangeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT, dataTracerRequestForm);

        }
        if ((AuditStatusEnum.REJECT.equalsValue(beforeOilCardReceiveEntity.getAuditStatus()) || AuditStatusEnum.WAIT_AUDIT.equalsValue(beforeOilCardReceiveEntity.getAuditStatus())) && AuditStatusEnum.AUDIT_PASS.equalsValue(oilCardReceiveEntity.getAuditStatus())) {
            carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount);

            OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
            changeForm.setOilCardId(oilCardId);
            changeForm.setChangeAmount(amount);
            changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE.getValue());
            oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE, dataTracerRequestForm);
        }

    }

    /**
     * 删除油卡收入
     *
     * @param tabulationEntity
     * @param beforeOilCardReceiveEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleDel(CarCostTabulationEntity tabulationEntity, CarCostOilCardReceiveEntity beforeOilCardReceiveEntity, DataTracerRequestForm dataTracerRequestForm) {
        Long oilCardReceiveId = beforeOilCardReceiveEntity.getOilCardReceiveId();
        carCostOilCardReceiveDao.deleteById(oilCardReceiveId);
        carCostTabulationDao.deleteById(tabulationEntity.getTabulationId());

        Long waybillId = tabulationEntity.getWaybillId();
        if (waybillId == null) {
            return;
        }
        // 如果绑定了运单 进行回退余额
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeOilCardReceiveEntity.getAuditStatus())) {
           return;
        }
        Long  oilCardId = beforeOilCardReceiveEntity.getOilCardId();
        BigDecimal amount = beforeOilCardReceiveEntity.getAmount();
        // 回退上次油卡收入
        OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(oilCardId);
        if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
            throw new BusinessException("更改之前的油卡不可用，余额不可退回");
        }
        Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
        OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
        if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
            throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
        }
        carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount.negate());
        // 退回油卡收入
        OilCardBalanceChangeForm lastChangeForm = new OilCardBalanceChangeForm();
        lastChangeForm.setOilCardId(oilCardId);
        lastChangeForm.setChangeAmount(amount.negate());
        lastChangeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT.getValue());
        oilCardService.carCostChangeBalance(lastChangeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT, dataTracerRequestForm);
    }

    /**
     * 关联运单
     *
     * @param oilCardReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleRelateWaybill(CarCostOilCardReceiveEntity oilCardReceiveEntity, CarCostTabulationEntity tabulationEntity, CarCostOilCardReceiveEntity beforeOilCardReceiveEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilCardReceiveDao.updateById(oilCardReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);

        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeOilCardReceiveEntity.getAuditStatus())){
            return;
        }
        Long waybillId = oilCardReceiveEntity.getWaybillId();

        Long oilCardId = beforeOilCardReceiveEntity.getOilCardId();
        BigDecimal amount = beforeOilCardReceiveEntity.getAmount();
        carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount);

        OilCardBalanceChangeForm changeForm = new OilCardBalanceChangeForm();
        changeForm.setOilCardId(oilCardId);
        changeForm.setChangeAmount(amount);
        changeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE.getValue());
        oilCardService.carCostChangeBalance(changeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE, dataTracerRequestForm);
    }

    /**
     * 取消关联运单
     *
     * @param oilCardReceiveId
     * @param tabulationId
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleCancelRelateWaybill(Long oilCardReceiveId, Long tabulationId, CarCostOilCardReceiveEntity beforeOilCardReceiveEntity, DataTracerRequestForm dataTracerRequestForm) {
        carCostOilCardReceiveDao.updateWaybillId(oilCardReceiveId, null);
        carCostTabulationDao.updateWaybillId(tabulationId, null);
        // 回退费用
        if (!AuditStatusEnum.AUDIT_PASS.equalsValue(beforeOilCardReceiveEntity.getAuditStatus())){
            return;
        }
        Long waybillId = beforeOilCardReceiveEntity.getWaybillId();
        Long  oilCardId = beforeOilCardReceiveEntity.getOilCardId();
        BigDecimal amount = beforeOilCardReceiveEntity.getAmount();
        // 回退上次油卡收入
        OilCardEntity beforeSlaveOilCardEntity = oilCardDao.selectById(oilCardId);
        if (ObjectUtil.isEmpty(beforeSlaveOilCardEntity) || beforeSlaveOilCardEntity.getDeletedFlag() || beforeSlaveOilCardEntity.getDisabledFlag()) {
            throw new BusinessException("更改之前的油卡不可用，余额不可退回");
        }
        Long beforeMasterOilCardId = beforeSlaveOilCardEntity.getMasterOilCardId();
        OilCardEntity beforeMasterOilCardEntity = oilCardDao.selectById(beforeMasterOilCardId);
        if (ObjectUtil.isEmpty(beforeMasterOilCardEntity) || beforeMasterOilCardEntity.getDeletedFlag() || beforeMasterOilCardEntity.getDisabledFlag()) {
            throw new BusinessException("更改之前的油卡所属主卡不可用，余额不可退回");
        }
        carCostOilCardInitialEndService.updateOilCardInitialEnd(waybillId, oilCardId, amount.negate());
        // 退回油卡收入
        OilCardBalanceChangeForm lastChangeForm = new OilCardBalanceChangeForm();
        lastChangeForm.setOilCardId(oilCardId);
        lastChangeForm.setChangeAmount(amount.negate());
        lastChangeForm.setRecordType(OilCardBalanceRecordTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT.getValue());
        oilCardService.carCostChangeBalance(lastChangeForm, OilCardDataTracerOperateTypeEnum.CAR_COST_OIL_CARD_RECEIVE_RETREAT, dataTracerRequestForm);


    }
}