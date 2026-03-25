package net.lab1024.tms.admin.module.business.reportform.home;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.expiredcertificate.ExpiredCertificateDao;
import net.lab1024.tms.admin.module.business.order.dao.OrderDao;
import net.lab1024.tms.admin.module.business.reportform.home.domain.HomeExpireCertVO;
import net.lab1024.tms.admin.module.business.reportform.home.domain.HomeNumDTO;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateStatusEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/3/17 上午10:40
 */
@Service
public class HomeService {

    @Autowired
    private DriverDao driverDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private ExpiredCertificateDao expiredCertificateDao;

    public ResponseDTO<HomeNumDTO> homeNum(Long enterpriseId) {
        Integer driverNum = driverDao.countDriver(enterpriseId, false);
        Integer vehicleNum = vehicleDao.countVehicle(enterpriseId, false);
        Integer orderNum = orderDao.countOrder(enterpriseId, OrderStatusEnum.CANCEL.getValue());
        Integer waybillNum = waybillDao.countWaybillByExcludeStatus(enterpriseId, WaybillStatusEnum.CANCEL.getValue());

        HomeNumDTO homeNumDTO = new HomeNumDTO();
        homeNumDTO.setDriverNum(driverNum);
        homeNumDTO.setVehicleNum(vehicleNum);
        homeNumDTO.setOrderNum(orderNum);
        homeNumDTO.setWaybillNum(waybillNum);
        return ResponseDTO.ok(homeNumDTO);
    }

    public ResponseDTO<List<HomeExpireCertVO>> expireCert(Long enterpriseId) {
        List<ExpiredCertificateStatusEnum> statusList = SmartBaseEnumUtil.enumList(ExpiredCertificateStatusEnum.class);
        List<HomeExpireCertVO> statusNumList = expiredCertificateDao.moduleStatusNum(enterpriseId);
        Map<Integer,Integer> statusNumMap = statusNumList.stream().collect(Collectors.toMap(HomeExpireCertVO::getStatus, HomeExpireCertVO::getValue));

        List<HomeExpireCertVO> expireCertVOList = Lists.newArrayList();
        for (ExpiredCertificateStatusEnum expiredCertificateStatusEnum : statusList) {
            HomeExpireCertVO expireCertVO = new HomeExpireCertVO();
            expireCertVO.setStatus(expiredCertificateStatusEnum.getValue());
            expireCertVO.setValue(statusNumMap.getOrDefault(expiredCertificateStatusEnum.getValue(), 0));
            expireCertVO.setName(expiredCertificateStatusEnum.getDesc());
            expireCertVOList.add(expireCertVO);
        }
        return ResponseDTO.ok(expireCertVOList);
    }
}