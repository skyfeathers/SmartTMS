package net.lab1024.tms.driver.module.business.carcost.oilcardreceive;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardReceiveEntity;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostTabulationEntity;
import net.lab1024.tms.driver.module.business.carcost.tabulation.CarCostTabulationDao;
import net.lab1024.tms.driver.module.business.waybill.dao.WaybillDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CarCostOilCardReceiveManager extends ServiceImpl<CarCostOilCardReceiveDao, CarCostOilCardReceiveEntity> {

    @Resource
    private WaybillDao waybillDao;

    @Resource
    private CarCostOilCardReceiveDao carCostOilCardReceiveDao;

    @Resource
    private CarCostTabulationDao carCostTabulationDao;

    /**
     * 新建油卡收入
     *
     * @param oilCardReceiveEntity
     * @param tabulationEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(CarCostOilCardReceiveEntity oilCardReceiveEntity, CarCostTabulationEntity tabulationEntity) {
        carCostOilCardReceiveDao.insert(oilCardReceiveEntity);

        tabulationEntity.setModuleId(oilCardReceiveEntity.getOilCardReceiveId());
        carCostTabulationDao.insert(tabulationEntity);

    }

    /**
     * 编辑油卡收入
     *
     * @param oilCardReceiveEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(CarCostOilCardReceiveEntity oilCardReceiveEntity, CarCostTabulationEntity tabulationEntity) {
        carCostOilCardReceiveDao.updateById(oilCardReceiveEntity);
        carCostTabulationDao.updateById(tabulationEntity);
    }
}
