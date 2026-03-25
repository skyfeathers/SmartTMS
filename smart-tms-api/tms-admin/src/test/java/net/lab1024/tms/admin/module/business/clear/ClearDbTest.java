package net.lab1024.tms.admin.module.business.clear;

import net.lab1024.tms.admin.TmsAdminApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/29 下午4:06
 */
public class ClearDbTest extends TmsAdminApplicationTest {

    @Autowired
    private ClearService clearService;


    @Test
    void clear() {
        clearService.clear();
    }
}