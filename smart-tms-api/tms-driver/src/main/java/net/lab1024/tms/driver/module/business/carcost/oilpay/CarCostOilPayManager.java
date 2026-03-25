package net.lab1024.tms.driver.module.business.carcost.oilpay;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CarCostOilPayManager extends ServiceImpl<CarCostOilPayDao, CarCostOilPayEntity> {

    @Resource
    private WaybillDao waybillDao;

    @Resource
    private CarCostOilPayDao carCostOilPayDao;

    @Resource
    private CarCostTabulationDao carCostTabulationDao;

    /**
     * 新建油费支付
     *
     * @param oilPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostOilPayEntity oilPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostOilPayDao.insert(oilPayEntity);

        tabulationEntity.setModuleId(oilPayEntity.getOilPayId());
        carCostTabulationDao.insert(tabulationEntity);

    }

    /**
     * 编辑油费支付
     *
     * @param oilPayEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostOilPayEntity oilPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostOilPayDao.updateById(oilPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);
    }
}
