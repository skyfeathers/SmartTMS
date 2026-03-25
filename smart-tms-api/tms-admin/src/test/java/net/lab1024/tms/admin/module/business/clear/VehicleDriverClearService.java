package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.bracket.BracketDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverBankDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverTaxRegisterDao;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.waybill.dao.WaybillDao;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.waybill.domain.entity.WaybillEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2023/5/30 9:29 上午
 */
@Service
public class VehicleDriverClearService {

    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private DriverBankDao driverBankDao;
    @Autowired
    private DriverTaxRegisterDao driverTaxRegisterDao;
    @Autowired
    private DriverVehicleDao driverVehicleDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private BracketDao bracketDao;

    public void vehicleDriverClear(List<Long> enterpriseIdList) {

    }
}