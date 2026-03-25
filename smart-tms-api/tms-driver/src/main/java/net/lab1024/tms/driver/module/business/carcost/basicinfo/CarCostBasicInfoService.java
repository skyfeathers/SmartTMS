package net.lab1024.tms.driver.module.business.carcost.basicinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lidoudou
 * @description:
 * @date 2023/11/9 15:54
 */
@Service
public class CarCostBasicInfoService {

    @Autowired
    private CarCostBasicInfoDao carCostBasicInfoDao;

    /**
     * 查询自有车费用确认状态
     *
     * @param waybillId
     * @return
     */
    public Boolean selectConfirmFlagByWaybillId(Long waybillId) {
        Boolean confirmFlag = carCostBasicInfoDao.selectConfirmFlagByWaybillId(waybillId);
        if (null == confirmFlag) {
            return Boolean.FALSE;
        }

        return confirmFlag;
    }

}