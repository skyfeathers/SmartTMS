package net.lab1024.tms.driver.module.business.carcost.oilcardinitialend;

import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostOilCardInitialEndVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarCostOilCardInitialEndService {

    @Resource
    private CarCostOilCardInitialEndDao carCostOilCardInitialEndDao;

    /**
     * 油卡期初期末基本信息
     *
     * @param waybillId
     * @return
     */
    public ResponseDTO<List<CarCostOilCardInitialEndVO>> info(Long waybillId) {
        List<CarCostOilCardInitialEndVO> oilCardInitialEndVOList = carCostOilCardInitialEndDao.selectByWaybillId(waybillId);
        return ResponseDTO.ok(oilCardInitialEndVOList);
    }
}
