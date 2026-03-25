package net.lab1024.tms.driver.module.system.login;

import cn.hutool.core.util.ObjectUtil;
import net.lab1024.tms.common.common.code.DriverErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartRandomUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.support.sms.constant.SmsVerifyTypeEnum;
import net.lab1024.tms.common.module.support.sms.service.SmsService;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigService;
import net.lab1024.tms.common.module.support.token.LoginDeviceEnum;
import net.lab1024.tms.common.module.support.token.TokenService;
import net.lab1024.tms.common.module.support.token.UserTerminalEnum;
import net.lab1024.tms.driver.constant.DriverCommonConst;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.system.login.domain.DriverSmsLoginForm;
import net.lab1024.tms.driver.module.system.login.domain.DriverLoginVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class DriverLoginService {

    @Resource
    private SmsService smsService;

    @Resource
    private DriverDao driverDao;

    @Resource
    private SystemConfigService systemConfigService;

    @Autowired
    private TokenService tokenService;

    public ResponseDTO<DriverLoginVO> smsVerifyLogin(DriverSmsLoginForm loginForm) {
        String phone = loginForm.getPhone();
        ResponseDTO<String> checkDto = smsService.checkVerifyCode(phone, loginForm.getSmsCode(), SmsVerifyTypeEnum.VERIFY_CODE_LOGIN);
        if (!checkDto.getOk()) {
            return ResponseDTO.error(DriverErrorCode.VERIFY_CODE_ERROR);
        }
        
        DriverEntity driverEntity = driverDao.selectByPhone(loginForm.getEnterpriseId(), phone, false);
        if (driverEntity == null) {
            return ResponseDTO.error(DriverErrorCode.PHONE_NOT_REGISTER);
        }
        if (driverEntity.getDeletedFlag() || DriverStatusEnum.DISABLED.equalsValue(driverEntity.getStatus())) {
            return ResponseDTO.error(DriverErrorCode.STATUS_ERROR);
        }
        DriverEntity updateDriver = new DriverEntity();
        updateDriver.setDriverId(driverEntity.getDriverId());
        updateDriver.setLastLoginTime(LocalDateTime.now());
        driverDao.updateById(updateDriver);

        String superPassword = systemConfigService.getConfigValue(SystemConfigKeyEnum.SUPER_PASSWORD);
        Boolean superPasswordFlag = superPassword.equals(loginForm.getSmsCode());
        LoginDeviceEnum deviceEnum = SmartBaseEnumUtil.getEnumByValue(loginForm.getDeviceType(), LoginDeviceEnum.class);
        String token = tokenService.generateToken(driverEntity.getDriverId(), driverEntity.getDriverName(), UserTypeEnum.DRIVER, deviceEnum, UserTerminalEnum.DRIVER, superPasswordFlag);

        DriverLoginVO loginVO = SmartBeanUtil.copy(driverEntity, DriverLoginVO.class);
        loginVO.setToken(token);
        return ResponseDTO.ok(loginVO);
    }

    /**
     * 生成默认名称
     * @return
     */
    public String generateDriverName() {
        String defaultDriverName = systemConfigService.getConfigValue(SystemConfigKeyEnum.DEFAULT_DRIVER_NAME);
        return defaultDriverName + SmartRandomUtil.generateRandomNum(DriverCommonConst.NICKNAME_RANDOM_NUM_LEN);
    }

    /**
     * 获取司机基础信息
     *
     * @param driverId
     * @return
     */
    public ResponseDTO<DriverLoginVO> getLoginInfo(Long driverId) {
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (ObjectUtil.isEmpty(driverEntity) || driverEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("司机不存在");
        }
        DriverLoginVO driverBaseInfoVO = SmartBeanUtil.copy(driverEntity, DriverLoginVO.class);
        return ResponseDTO.ok(driverBaseInfoVO);
    }
}
