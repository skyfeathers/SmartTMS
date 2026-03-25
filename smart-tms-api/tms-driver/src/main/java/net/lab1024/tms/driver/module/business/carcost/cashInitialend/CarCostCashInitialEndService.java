package net.lab1024.tms.driver.module.business.carcost.cashInitialend;

import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashInitialEndEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCashInitialEndVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarCostCashInitialEndService {

    @Resource
    private CarCostCashInitialEndDao carCostCashInitialEndDao;

    /**
     * 自有车现金期初期末
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<CarCostCashInitialEndVO> info(Long waybillId) {
        CarCostCashInitialEndEntity cashInitialEndEntity = carCostCashInitialEndDao.selectByWaybillId(waybillId);
        CarCostCashInitialEndVO cashInitialEndVO = SmartBeanUtil.copy(cashInitialEndEntity, CarCostCashInitialEndVO.class);
        return ResponseDTO.ok(cashInitialEndVO);
    }

}
