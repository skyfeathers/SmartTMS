package net.lab1024.tms.driver.module.business.carcost.etcpay;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostEtcPayEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CarCostEtcPayManager extends ServiceImpl<CarCostEtcPayDao, CarCostEtcPayEntity> {

    @Resource
    private CarCostEtcPayDao carCostEtcPayDao;

    @Resource
    private WaybillDao waybillDao;

    @Resource
    private CarCostTabulationDao carCostTabulationDao;

    /**
     * 新建ETC支出记录
     *
     * @param etcPayEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostEtcPayEntity etcPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostEtcPayDao.insert(etcPayEntity);

        tabulationEntity.setModuleId(etcPayEntity.getEtcPayId());
        carCostTabulationDao.insert(tabulationEntity);

    }

    /**
     * 编辑ETC支出记录
     *
     * @param etcPayEntity
     * @#param tabulationEntity
     */
    public void handleUpdate(CarCostEtcPayEntity etcPayEntity, CarCostTabulationEntity tabulationEntity) {
        carCostEtcPayDao.updateById(etcPayEntity);
        carCostTabulationDao.updateById(tabulationEntity);
    }

}
