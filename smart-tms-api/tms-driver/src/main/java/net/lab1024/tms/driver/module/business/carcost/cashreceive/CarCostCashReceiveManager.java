package net.lab1024.tms.driver.module.business.carcost.cashreceive;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashReceiveEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CarCostCashReceiveManager extends ServiceImpl<CarCostCashReceiveDao, CarCostCashReceiveEntity> {

    @Resource
    private CarCostCashReceiveDao carCostCashReceiveDao;

    @Resource
    private WaybillDao waybillDao;

    @Resource
    private CarCostTabulationDao carCostTabulationDao;

    /**
     * 添加现金收入
     *
     * @param cashReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostCashReceiveEntity cashReceiveEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashReceiveDao.insert(cashReceiveEntity);

        tabulationEntity.setModuleId(cashReceiveEntity.getCashReceiveId());
        carCostTabulationDao.insert(tabulationEntity);
    }

    /**
     * 编辑现金收入
     *
     * @param cashReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostCashReceiveEntity cashReceiveEntity, CarCostTabulationEntity tabulationEntity) {
        carCostCashReceiveDao.updateById(cashReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);
    }
}
