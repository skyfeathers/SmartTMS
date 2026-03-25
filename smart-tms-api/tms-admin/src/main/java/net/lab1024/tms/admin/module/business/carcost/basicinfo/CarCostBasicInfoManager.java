package net.lab1024.tms.admin.module.business.carcost.basicinfo;

import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.admin.module.business.waybill.service.WaybillCommonService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.business.carcost.domain.entity.CarCostBasicInfoEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/2/26 下午5:01
 */
@Service
public class CarCostBasicInfoManager {

    @Resource
    private CarCostBasicInfoDao carCostBasicInfoDao;

    @Resource
    private WaybillDao waybillDao;
    @Resource
    private WaybillCommonService waybillCommonService;

    @Transactional(rollbackFor = Throwable.class)
    public void updateConfirm(Long basicInfoId, Long waybillId, Boolean confirmFlag) {
        carCostBasicInfoDao.updateConfirmFlag(basicInfoId, confirmFlag);
        if (waybillId != null) {
            waybillCommonService.updateProfitAmount(waybillId);
        }
    }

    /**
     * 删除自有车基本信息
     *
     * @param basicInfoId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public void del(Long basicInfoId, Long waybillId) {
        if (waybillId != null) {
            waybillCommonService.updateProfitAmount(waybillId);
        }
        carCostBasicInfoDao.deleteById(basicInfoId);
    }
}