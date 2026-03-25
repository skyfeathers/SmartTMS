package net.lab1024.tms.driver.module.business.driver.service;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.driver.CommonDriverBalanceService;
import net.lab1024.tms.common.module.business.driver.domain.DriverBalanceEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverTaxRegisterEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.business.driver.dao.DriverTaxRegisterDao;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverTaxRegisterForm;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverUpdateForm;
import net.lab1024.tms.driver.module.business.driver.domain.vo.DriverVO;
import net.lab1024.tms.driver.module.business.driver.manager.DriverManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DriverService {

    @Resource
    private DriverDao driverDao;

    @Resource
    private DriverManager driverManager;

    @Resource
    private CommonDriverBalanceService commonDriverBalanceService;

    @Resource
    private DriverTaxRegisterDao driverTaxRegisterDao;

    @Resource
    private DriverDataTracerService driverDataTracerService;

    /**
     * 获取司机信息
     *
     * @param driverId
     * @return
     */
    public ResponseDTO<DriverVO> getInfo(Long driverId) {
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (ObjectUtil.isEmpty(driverEntity) || driverEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "司机不存在");
        }
        DriverVO driverVO = SmartBeanUtil.copy(driverEntity, DriverVO.class);
        DriverTaxRegisterEntity driverTaxRegisterEntity = driverTaxRegisterDao.selectByDriverId(driverId);
        if (ObjectUtil.isNotEmpty(driverTaxRegisterEntity)) {
            driverVO.setTaxRegister(SmartBeanUtil.copy(driverTaxRegisterEntity, DriverTaxRegisterForm.class));
        }
        return ResponseDTO.ok(driverVO);
    }

    /**
     * 更新司机信息
     *
     * @param updateForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> update(DriverUpdateForm updateForm, DataTracerRequestForm dataTracerRequestForm) {
        DriverEntity driverEntity = driverDao.selectById(updateForm.getDriverId());
        if (ObjectUtil.isEmpty(driverEntity) || driverEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "司机不存在");
        }
        List<DriverEntity> driverList = driverDao.selectByPhoneExcludeIds(updateForm.getEnterpriseId(), updateForm.getTelephone(), Lists.newArrayList(updateForm.getDriverId()), Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(driverList)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "手机号已存在");
        }
        DriverVO beforeData = this.getInfo(updateForm.getDriverId()).getData();
        driverManager.updateHandle(updateForm, dataTracerRequestForm.getOperatorId(), dataTracerRequestForm.getOperatorName());
        DriverVO afterData = this.getInfo(updateForm.getDriverId()).getData();
        driverDataTracerService.updateLog(beforeData, afterData, dataTracerRequestForm);
        return ResponseDTO.ok();
    }

    public ResponseDTO<BigDecimal> getBalance(Long enterpriseId, Long driverId) {
        DriverEntity driverEntity = driverDao.selectById(driverId);
        if (ObjectUtil.isEmpty(driverEntity) || driverEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "司机不存在");
        }
        DriverBalanceEntity driverBalanceEntity = commonDriverBalanceService.getBalanceEntity(driverId, enterpriseId);
        return ResponseDTO.ok(driverBalanceEntity.getBalance());
    }
}
