package net.lab1024.tms.admin.module.business.driver.service;

import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 司机提醒通知
 *
 * @author lidoudou
 * @date 2022/7/1 下午2:53
 */
@Service
public class DriverNoticeService {

    @Autowired
    private DriverDao driverDao;


}
