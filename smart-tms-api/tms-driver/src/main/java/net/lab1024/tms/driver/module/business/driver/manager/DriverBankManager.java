package net.lab1024.tms.driver.module.business.driver.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.driver.module.business.driver.dao.DriverBankDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DriverBankManager extends ServiceImpl<DriverBankDao, DriverBankEntity> {

    @Resource
    private DriverBankDao driverBankDao;

    /**
     * 添加司机银行信息
     *
     * @param driverBankEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveDriverBank(DriverBankEntity driverBankEntity) {
        if (driverBankEntity.getDefaultFlag().equals(Boolean.TRUE)) {
            this.updateDefaultBankByDriverId(driverBankEntity.getDriverId());
        }
        driverBankDao.insert(driverBankEntity);
    }

    /**
     * 更新司机银行信息
     *
     * @param driverBankEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateDriverBank(DriverBankEntity driverBankEntity) {
        if (ObjectUtil.isNotEmpty(driverBankEntity.getDefaultFlag()) && driverBankEntity.getDefaultFlag().equals(Boolean.TRUE)) {
            updateDefaultBankByDriverId(driverBankEntity.getDriverId());
        }
        driverBankDao.updateById(driverBankEntity);
    }

    /**
     * 更改司机默认银行卡默认状态
     *
     * @param driverId
     */
    private void updateDefaultBankByDriverId(Long driverId) {
        DriverBankEntity defaultDriverBank = driverBankDao.selectDefaultBankByDriverId(driverId, Boolean.TRUE);
        if (ObjectUtil.isNotEmpty(defaultDriverBank)) {
            DriverBankEntity updateDriverBank = new DriverBankEntity();
            updateDriverBank.setBankId(defaultDriverBank.getBankId());
            updateDriverBank.setDefaultFlag(Boolean.FALSE);
            driverBankDao.updateById(updateDriverBank);
        }
    }
}
