package net.lab1024.tms.common.module.business.driver;

import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.driver.constants.DriverBalanceTradeTypeEnum;
import net.lab1024.tms.common.module.business.driver.dao.CommonDriverBalanceDao;
import net.lab1024.tms.common.module.business.driver.dao.CommonDriverBalanceRecordDao;
import net.lab1024.tms.common.module.business.driver.domain.DriverBalanceEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverBalanceRecordEntity;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceChangeForm;
import net.lab1024.tms.common.module.business.driver.domain.form.DriverBalanceRecordForm;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author renjipeng
 * @date 2022年09月02日 9:34
 */
@Service
public class CommonDriverBalanceService {

    @Autowired
    private CommonDriverBalanceDao commonDriverBalanceDao;

    @Autowired
    private CommonDriverBalanceRecordDao commonDriverBalanceRecordDao;

    /**
     * 获取余额对象
     *
     * @param driverId
     */
    public DriverBalanceEntity getBalanceEntity(Long driverId, Long enterpriseId) {
        DriverBalanceEntity driverBalanceEntity = commonDriverBalanceDao.selectByEnterpriseIdAndDriverId(enterpriseId, driverId);

        if (driverBalanceEntity == null) {
            String lock = driverId + "_" + enterpriseId;
            synchronized (StringConst.STRING_POOL.intern(lock)) {
                driverBalanceEntity = commonDriverBalanceDao.selectByEnterpriseIdAndDriverId(enterpriseId, driverId);
                if (driverBalanceEntity == null) {
                    driverBalanceEntity = new DriverBalanceEntity();
                    driverBalanceEntity.setDriverId(driverId);
                    driverBalanceEntity.setBalance(BigDecimal.ZERO);
                    driverBalanceEntity.setEnterpriseId(enterpriseId);
                    driverBalanceEntity.setUpdateVersion(0);
                    commonDriverBalanceDao.insert(driverBalanceEntity);
                }
            }
        }
        return driverBalanceEntity;
    }

    /**
     * 余额变动
     *
     * @param driverBalanceChangeForm
     */
    public void balanceChange(DriverBalanceChangeForm driverBalanceChangeForm) {
        Long driverId = driverBalanceChangeForm.getDriverId();
        Long enterpriseId = driverBalanceChangeForm.getEnterpriseId();

        DriverBalanceEntity driverBalanceEntity = this.getBalanceEntity(driverId, enterpriseId);
        CommonDriverBalanceService commonDriverBalanceService = (CommonDriverBalanceService) AopContext.currentProxy();
        commonDriverBalanceService.balanceChange(driverBalanceEntity, driverBalanceChangeForm);
    }

    /**
     * 余额变动
     *
     * @param driverBalanceEntity
     * @param driverBalanceChangeForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void balanceChange(DriverBalanceEntity driverBalanceEntity, DriverBalanceChangeForm driverBalanceChangeForm) {
        BigDecimal changeAmount = driverBalanceChangeForm.getChangeAmount();
        if (!driverBalanceChangeForm.getIncomeFlag()) {
            changeAmount = changeAmount.negate();
        }
        Integer updateRows = commonDriverBalanceDao.updateBalanceAmount(driverBalanceEntity.getDriverBalanceId(), changeAmount, driverBalanceEntity.getUpdateVersion());
        if (updateRows == 0) {
            throw new RuntimeException("余额更新失败");
        }
        this.saveBalanceRecord(driverBalanceEntity, driverBalanceChangeForm);
    }


    /**
     * 保存交易流水
     *
     * @param originBalanceEntity
     * @param driverBalanceChangeForm
     */
    private void saveBalanceRecord(DriverBalanceEntity originBalanceEntity, DriverBalanceChangeForm driverBalanceChangeForm) {
        Boolean incomeFlag = driverBalanceChangeForm.getIncomeFlag();
        BigDecimal changeAmount = driverBalanceChangeForm.getChangeAmount();
        BigDecimal afterAmount = BigDecimal.ZERO;
        if (incomeFlag) {
            afterAmount = originBalanceEntity.getBalance().add(changeAmount);
        } else {
            afterAmount = originBalanceEntity.getBalance().subtract(changeAmount);
        }
        DriverBalanceRecordForm recordForm = driverBalanceChangeForm.getRecordForm();

        DriverBalanceRecordEntity recordEntity = new DriverBalanceRecordEntity();
        recordEntity.setBalanceId(originBalanceEntity.getDriverBalanceId());
        recordEntity.setDriverId(originBalanceEntity.getDriverId());
        recordEntity.setEnterpriseId(originBalanceEntity.getEnterpriseId());
        recordEntity.setTradeType(recordForm.getTradeType());
        recordEntity.setTradeTypeDesc(SmartBaseEnumUtil.getEnumDescByValue(recordForm.getTradeType(), DriverBalanceTradeTypeEnum.class));
        recordEntity.setTradeContent(recordForm.getTradeContent());
        recordEntity.setRelateOrderId(recordForm.getRelateOrderId());
        recordEntity.setIncomeFlag(incomeFlag);
        recordEntity.setBeforeBalance(originBalanceEntity.getBalance());
        recordEntity.setChangeBalance(changeAmount);
        recordEntity.setAfterBalance(afterAmount);
        recordEntity.setRemark(recordForm.getRemark());
        commonDriverBalanceRecordDao.insert(recordEntity);
    }
}
