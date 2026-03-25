package net.lab1024.tms.driver.module.business.carcost.cashpay;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CarCostCashPayManager extends ServiceImpl<CarCostCashPayDao, CarCostCashPayEntity> {

    @Resource
    private WaybillDao waybillDao;

    @Resource
    private CarCostCashPayDao carCostCashPayDao;

    @Resource
    private CarCostTabulationDao carCostTabulationDao;

    /**
     * 新建现金收入
     *
     * @param cashPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostCashPayEntity cashPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashPayDao.insert(cashPayEntity);

        tabulationEntity.setModuleId(cashPayEntity.getCashPayId());
        carCostTabulationDao.insert(tabulationEntity);
    }

    /**
     * 编辑现金收入
     *
     * @param cashPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostCashPayEntity cashPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashPayDao.updateById(cashPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);
    }

}
