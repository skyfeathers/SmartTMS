package net.lab1024.tms.driver.module.business.driver.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverTaxRegisterEntity;
import net.lab1024.tms.common.module.business.expiredcertificate.CommonExpiredCertificateService;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateAddDTO;
import net.lab1024.tms.driver.module.business.driver.dao.DriverDao;
import net.lab1024.tms.driver.module.business.driver.dao.DriverTaxRegisterDao;
import net.lab1024.tms.driver.module.business.driver.domain.form.DriverUpdateForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DriverManager extends ServiceImpl<DriverDao, DriverEntity> {

    @Resource
    private DriverDao driverDao;

    @Resource
    private DriverTaxRegisterDao driverTaxRegisterDao;

    @Resource
    private CommonExpiredCertificateService commonExpiredCertificateService;

    /**
     * 更新司机信息
     *
     * @param updateForm
     * @param driverId
     * @param driverName
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateHandle(DriverUpdateForm updateForm, Long driverId, String driverName) {
        DriverEntity driverEntity = SmartBeanUtil.copy(updateForm, DriverEntity.class);
        driverEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        driverEntity.setUpdateUserId(driverId);
        driverEntity.setUpdateUserName(driverName);
        driverDao.updateById(driverEntity);
        this.updateTaxRegister(updateForm);
        this.updateExpiredCertificate(driverEntity);
    }

    /**
     * 更新税务登记人信息
     *
     * @param updateForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateTaxRegister(DriverUpdateForm updateForm) {
        driverTaxRegisterDao.deleteByDriverId(updateForm.getDriverId());
        if (ObjectUtil.isNotEmpty(updateForm.getTaxRegister())) {
            DriverTaxRegisterEntity taxRegisterEntity = SmartBeanUtil.copy(updateForm.getTaxRegister(), DriverTaxRegisterEntity.class);
            taxRegisterEntity.setDriverId(updateForm.getDriverId());
            driverTaxRegisterDao.insert(taxRegisterEntity);
        }
    }

    /**
     * 更新司机的到期证件
     *
     * @param driverEntity
     */
    private void updateExpiredCertificate(DriverEntity driverEntity) {
        ExpiredCertificateAddDTO baseDTO = new ExpiredCertificateAddDTO();
        baseDTO.setModuleType(ExpiredCertificateModuleTypeEnum.DRIVER);
        baseDTO.setModuleId(driverEntity.getDriverId());
        baseDTO.setModuleName(driverEntity.getDriverName());

        if (null != driverEntity.getIdCardEffectiveEndDate()) {
            ExpiredCertificateAddDTO shenfenZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            shenfenZheng.setType(ExpiredCertificateTypeEnum.SHEN_FEN_ZHENG);
            shenfenZheng.setExpiredTime(driverEntity.getIdCardEffectiveEndDate());
            commonExpiredCertificateService.save(shenfenZheng);
        }

        if (null != driverEntity.getValidPeriodTo()) {
            ExpiredCertificateAddDTO jiashiZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            jiashiZheng.setType(ExpiredCertificateTypeEnum.JIA_SHI_ZHENG);
            jiashiZheng.setExpiredTime(driverEntity.getValidPeriodTo());
            commonExpiredCertificateService.save(jiashiZheng);
        }

        if (null != driverEntity.getQualificationCertificateEndDate()) {
            ExpiredCertificateAddDTO congyezigeZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            congyezigeZheng.setType(ExpiredCertificateTypeEnum.CONG_YE_ZI_GE_ZHENG);
            congyezigeZheng.setExpiredTime(driverEntity.getQualificationCertificateEndDate());
            commonExpiredCertificateService.save(congyezigeZheng);
        }
    }

}
