package net.lab1024.tms.admin.module.business.carcost.cashInitialend;

import net.lab1024.tms.admin.module.business.carcost.cashpay.CarCostCashPayDao;
import net.lab1024.tms.admin.module.business.carcost.cashreceive.CarCostCashReceiveDao;
import net.lab1024.tms.admin.module.business.carcost.etcpay.CarCostEtcPayDao;
import net.lab1024.tms.admin.module.business.carcost.oilpay.CarCostOilPayDao;
import net.lab1024.tms.admin.module.business.carcost.ureapay.CarCostUreaPayDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartBigDecimalUtil;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostCashInitialEndEntity;
import net.lab1024.tms.common.module.business.carcost.domain.vo.CarCostCashInitialEndVO;
import net.lab1024.tms.common.module.business.driver.CommonDriverBalanceService;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverBalanceEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CarCostCashInitialEndService {

    @Resource
    private DriverDao driverDao;

    @Resource
    private CarCostCashPayDao carCostCashPayDao;

    @Resource
    private CarCostOilPayDao carCostOilPayDao;

    @Resource
    private CarCostEtcPayDao carCostEtcPayDao;

    @Resource
    private CarCostUreaPayDao carCostUreaPayDao;

    @Resource
    private CarCostCashReceiveDao carCostCashReceiveDao;

    @Resource
    private CommonDriverBalanceService commonDriverBalanceService;

    @Resource
    private CarCostCashInitialEndDao carCostCashInitialEndDao;

    @Resource
    private CarCostCashInitialEndManager carCostCashInitialEndManager;

    /**
     * 更新司机余额期初期末
     *
     * @param waybillId
     * @param driverId
     * @param amount
     */
    public void updateInitialEnd(Long enterpriseId, Long waybillId, Long driverId, BigDecimal amount) {
        CarCostCashInitialEndEntity cashInitialEndEntity =
                carCostCashInitialEndDao.selectByWaybillId(waybillId);

        if (cashInitialEndEntity == null) {
            DriverEntity driverEntity = driverDao.selectById(driverId);
            if (driverEntity == null
                    || driverEntity.getDeletedFlag()
                    || DriverStatusEnum.DISABLED.equalsValue(driverEntity.getStatus())) {
                throw new BusinessException("更新期初期末时司机不存在或已禁用");
            }
            DriverBalanceEntity driverBalanceEntity = commonDriverBalanceService.getBalanceEntity(driverId, enterpriseId);
            BigDecimal balance = driverBalanceEntity.getBalance();
            BigDecimal endAmount = SmartBigDecimalUtil.add(balance, amount, 4);

            cashInitialEndEntity = new CarCostCashInitialEndEntity();
            cashInitialEndEntity.setWaybillId(waybillId);
            cashInitialEndEntity.setDriverId(driverId);
            cashInitialEndEntity.setInitialAmount(balance);
            cashInitialEndEntity.setEndAmount(endAmount);
            carCostCashInitialEndDao.insert(cashInitialEndEntity);
        } else {
            Long cashInitialEndId = cashInitialEndEntity.getCashInitialEndId();
            BigDecimal endAmount = SmartBigDecimalUtil.add(cashInitialEndEntity.getEndAmount(), amount, 4);

            cashInitialEndEntity = new CarCostCashInitialEndEntity();
            cashInitialEndEntity.setCashInitialEndId(cashInitialEndId);
            cashInitialEndEntity.setWaybillId(waybillId);
            cashInitialEndEntity.setDriverId(driverId);
            cashInitialEndEntity.setEndAmount(endAmount);
            carCostCashInitialEndDao.updateById(cashInitialEndEntity);

            this.updateAfterInitialEnd(cashInitialEndId, driverId, amount);
        }
    }



    /**
     * 更新之后的期初期末
     *
     * @param cashInitialEndId
     * @param driverId
     * @param changeAmount
     */
    private void updateAfterInitialEnd(Long cashInitialEndId, Long driverId, BigDecimal changeAmount) {
        List<CarCostCashInitialEndEntity> cashInitialEndEntityList = carCostCashInitialEndDao.selectByIdAfter(cashInitialEndId, driverId);
        if (CollectionUtils.isEmpty(cashInitialEndEntityList)) {
            return;
        }

        for (CarCostCashInitialEndEntity initialEndEntity : cashInitialEndEntityList) {
            initialEndEntity.setInitialAmount(SmartBigDecimalUtil.add(initialEndEntity.getInitialAmount(), changeAmount, 4));
            initialEndEntity.setEndAmount(SmartBigDecimalUtil.add(initialEndEntity.getEndAmount(), changeAmount, 4));
        }
        carCostCashInitialEndManager.updateBatchById(cashInitialEndEntityList);
    }

    /**
     * 自有车现金期初期
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
