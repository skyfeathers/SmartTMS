package net.lab1024.tms.driver.module.business.carcost.ureapay;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostUreaPayEntity;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CarCostUreaPayManager extends ServiceImpl<CarCostUreaPayDao, CarCostUreaPayEntity> {

    @Resource
    private WaybillDao waybillDao;

    @Resource
    private CarCostUreaPayDao carCostUreaPayDao;

    @Resource
    private CarCostTabulationDao carCostTabulationDao;

    /**
     * 新建尿素支出
     *
     * @param ureaPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostUreaPayEntity ureaPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostUreaPayDao.insert(ureaPayEntity);

        tabulationEntity.setModuleId(ureaPayEntity.getUreaPayId());
        carCostTabulationDao.insert(tabulationEntity);

    }

    /**
     * 编辑尿素支出
     *
     * @param ureaPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostUreaPayEntity ureaPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostUreaPayDao.updateById(ureaPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);
    }
}
