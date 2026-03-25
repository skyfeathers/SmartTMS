package net.lab1024.tms.admin.module.business.expiredcertificate;

import lombok.SneakyThrows;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateAddDTO;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

class ExpiredCertificateServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private ExpiredCertificateService expiredCertificateService;

    @SneakyThrows
    @Test
    void save() {
        ExpiredCertificateAddDTO addDTO = new ExpiredCertificateAddDTO();
        addDTO.setEnterpriseId(1L);
        addDTO.setModuleType(ExpiredCertificateModuleTypeEnum.DRIVER);
        addDTO.setModuleId(1L);
        addDTO.setType(ExpiredCertificateTypeEnum.SHEN_FEN_ZHENG);
        addDTO.setExpiredTime(SmartLocalDateUtil.parseYMD("2022-07-20"));
        addDTO.setModuleName("是你冬哥啊");
        addDTO.setRemark("hhh");
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(() -> {
                expiredCertificateService.save(addDTO);
                countDownLatch.countDown();
            });
            thread.start();
        }
        countDownLatch.await();
    }
}