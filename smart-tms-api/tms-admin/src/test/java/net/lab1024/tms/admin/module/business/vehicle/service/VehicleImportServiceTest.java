package net.lab1024.tms.admin.module.business.vehicle.service;

import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.bracket.BracketExcelImportService;
import net.lab1024.tms.admin.module.business.driver.service.DriverImportService;
import net.lab1024.tms.admin.module.business.oilcard.MasterOilCardImportService;
import net.lab1024.tms.admin.module.business.oilcard.SlaveOilCardImportService;
import net.lab1024.tms.admin.module.business.oilcard.SlaveOilCardUpdateService;
import net.lab1024.tms.admin.module.business.shipper.ShipperExcelImportService;
import net.lab1024.tms.admin.module.business.shipper.ShipperExcelUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

class VehicleImportServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private VehicleImportService vehicleImportService;
    @Autowired
    private ShipperExcelImportService shipperExcelImportService;
    @Autowired
    private DriverImportService driverImportService;
    @Autowired
    private BracketExcelImportService bracketExcelImportService;

    @Test
    void importShipperData() {
        shipperExcelImportService.importData(new File("/Users/lidoudou/Desktop/DDD1/货主（客户）信息导入确认版2.xlsx"));
    }

    @Test
    void importDriverData() {
        driverImportService.importData(new File("/Users/lidoudou/Desktop/DDD1/司机信息模板确认版.xlsx"));
    }

    @Test
    void importBracketData() {
        bracketExcelImportService.importData(new File("/Users/lidoudou/Desktop/DDD1/副本挂车信息模板确认版.xlsx"));
    }

    @Test
    void importData() {
        vehicleImportService.importData(new File("/Users/lidoudou/Desktop/DDD1/副本车辆信息模板确认版.xlsx"));
    }

    @Autowired
    private MasterOilCardImportService masterOilCardImportService;
    @Autowired
    private SlaveOilCardImportService slaveOilCardImportService;

    /**
     * 导入油卡主卡
     */
    @Test
    void importMasterData() {
        masterOilCardImportService.importData(new File("/Users/lidoudou/Desktop/DDD1/油卡信息导入模板-主卡.xlsx"));
    }

    /**
     * 导入油卡副卡
     */
    @Test
    void importSlaveData() {
        slaveOilCardImportService.importData(new File("/Users/lidoudou/Desktop/DDD1/油卡信息导入模板 - 副卡.xlsx"));
    }

    @Autowired
    private ShipperExcelUpdateService shipperExcelUpdateService;
    @Test
    void updateShipperData() {
        shipperExcelUpdateService.importData(new File("/Users/lidoudou/Desktop/DDD1/货主（客户）信息导入确认版.xlsx"));
    }
    @Autowired
    private SlaveOilCardUpdateService slaveOilCardUpdateService;
    @Test
    void updateSlaveOilCard() {
        slaveOilCardUpdateService.importData(new File("/Users/lidoudou/Desktop/DDD1/油卡信息导入模板 - 副卡.xlsx"));
    }
}