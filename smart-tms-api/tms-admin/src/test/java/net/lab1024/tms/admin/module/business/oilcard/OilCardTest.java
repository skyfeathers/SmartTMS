package net.lab1024.tms.admin.module.business.oilcard;

import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardExcelImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @author yandy
 * @description:
 * @date 2022/9/14 2:52 下午
 */
public class OilCardTest extends TmsAdminApplicationTest {

    @Autowired
    private OilCardExcelImportService oilCardExcelImportService;
    @Autowired
    private SlaveOilCardNewImportService slaveOilCardNewImportService;

    @Test
    public void excelImportTest() {
        oilCardExcelImportService.importOilCard(1L, new File("/Users/yandy/Documents/1024lab/nft/油卡信息2.xlsx"));
    }

    @Test
    public void newExcelImportTest() {
        slaveOilCardNewImportService.importData(new File("/Users/lidoudou/Desktop/油卡导入.xlsx"));
    }
}