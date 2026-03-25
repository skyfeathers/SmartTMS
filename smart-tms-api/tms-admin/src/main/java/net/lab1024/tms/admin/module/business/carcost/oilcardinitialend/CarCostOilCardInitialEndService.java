package net.lab1024.tms.admin.module.business.carcost.oilcardinitialend;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostOilCardInitialEndEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostOilCardInitialEndVO;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CarCostOilCardInitialEndService {

    @Resource
    private OilCardDao oilCardDao;
    @Resource
    private CarCostOilCardInitialEndDao carCostOilCardInitialEndDao;
    @Resource
    private CarCostOilCardInitialEndManager carCostOilCardInitialEndManager;

    /**
     * 更新油卡期初期末
     *
     * @param waybillId
     * @param oilCardId
     */
    public void updateOilCardInitialEnd(Long waybillId, Long oilCardId, BigDecimal amount) {
        CarCostOilCardInitialEndEntity oilCardInitialEndEntity =
                carCostOilCardInitialEndDao.selectByWaybillIdAndOilCardId(waybillId, oilCardId);

        if (ObjectUtil.isEmpty(oilCardInitialEndEntity)) {
            OilCardEntity oilCardEntity = oilCardDao.selectById(oilCardId);
            if (ObjectUtil.isEmpty(oilCardEntity)
                    || oilCardEntity.getDeletedFlag()
                    || oilCardEntity.getDisabledFlag()) {
                throw new BusinessException("保存期初期末时油卡不存在或被禁用");
            }

            BigDecimal balance = oilCardEntity.getBalance();
            BigDecimal endAmount = SmartBigDecimalUtil.add(balance, amount, 4);

            oilCardInitialEndEntity = new CarCostOilCardInitialEndEntity();
            oilCardInitialEndEntity.setWaybillId(waybillId);
            oilCardInitialEndEntity.setOilCardId(oilCardId);
            oilCardInitialEndEntity.setInitialAmount(balance);
            oilCardInitialEndEntity.setEndAmount(endAmount);
            carCostOilCardInitialEndDao.insert(oilCardInitialEndEntity);
        } else {
            Long oilCardInitialEndId = oilCardInitialEndEntity.getOilCardInitialEndId();
            BigDecimal endAmount = SmartBigDecimalUtil.add(oilCardInitialEndEntity.getEndAmount(), amount, 4);

            oilCardInitialEndEntity = new CarCostOilCardInitialEndEntity();
            oilCardInitialEndEntity.setOilCardInitialEndId(oilCardInitialEndId);
            oilCardInitialEndEntity.setWaybillId(waybillId);
            oilCardInitialEndEntity.setOilCardId(oilCardId);
            oilCardInitialEndEntity.setEndAmount(endAmount);
            carCostOilCardInitialEndDao.updateById(oilCardInitialEndEntity);

            this.updateAfterInitialEnd(oilCardInitialEndId, oilCardId, amount);
        }
    }


    /**
     * 更新之后的期初期末
     *
     * @param oilCardInitialEndId
     * @param oilCardId
     * @param changeAmount
     */
    private void updateAfterInitialEnd(Long oilCardInitialEndId, Long oilCardId, BigDecimal changeAmount) {
        List<CarCostOilCardInitialEndEntity> initialEndEntityList = carCostOilCardInitialEndDao.selectByIdAfter(oilCardInitialEndId, oilCardId);
        if (CollectionUtils.isEmpty(initialEndEntityList)) {
            return;
        }

        for (CarCostOilCardInitialEndEntity initialEndEntity : initialEndEntityList) {
            initialEndEntity.setInitialAmount(SmartBigDecimalUtil.add(initialEndEntity.getInitialAmount(), changeAmount, 4));
            initialEndEntity.setEndAmount(SmartBigDecimalUtil.add(initialEndEntity.getEndAmount(), changeAmount, 4));
        }
        carCostOilCardInitialEndManager.updateBatchById(initialEndEntityList);
    }

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
