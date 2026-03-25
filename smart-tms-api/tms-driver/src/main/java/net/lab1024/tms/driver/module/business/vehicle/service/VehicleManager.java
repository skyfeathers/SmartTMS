package net.lab1024.tms.driver.module.business.vehicle.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.business.expiredcertificate.CommonExpiredCertificateService;
import net.lab1024.tms.common.module.business.expiredcertificate.domain.ExpiredCertificateAddDTO;
import net.lab1024.tms.driver.module.business.vehicle.dao.DriverVehicleDao;
import net.lab1024.tms.driver.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.driver.module.business.vehicle.domain.form.VehicleAddForm;
import net.lab1024.tms.driver.module.business.vehicle.domain.form.VehicleUpdateForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class VehicleManager extends ServiceImpl<VehicleDao, VehicleEntity> {

    @Resource
    private VehicleDao vehicleDao;

    @Resource
    private DriverVehicleDao driverVehicleDao;

    @Resource
    private CommonExpiredCertificateService commonExpiredCertificateService;

    /**
     * 更新车辆信息
     *
     * @param updateForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateVehicle(VehicleUpdateForm updateForm) {
        VehicleEntity vehicleEntity = SmartBeanUtil.copy(updateForm, VehicleEntity.class);
        vehicleEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        vehicleEntity.setUpdateUserId(updateForm.getOperatorId());
        vehicleEntity.setUpdateUserName(updateForm.getOperatorName());
        vehicleDao.updateById(vehicleEntity);
        updateExpiredCertificate(vehicleEntity);
    }

    /**
     * 更新车辆的到期证件
     *
     * @param vehicleEntity
     */
    private void updateExpiredCertificate(VehicleEntity vehicleEntity) {
        ExpiredCertificateAddDTO baseDTO = new ExpiredCertificateAddDTO();
        baseDTO.setModuleType(ExpiredCertificateModuleTypeEnum.VEHICLE);
        baseDTO.setModuleId(vehicleEntity.getVehicleId());
        baseDTO.setModuleName(vehicleEntity.getVehicleNumber());

        if (null != vehicleEntity.getExpireDate()) {
            ExpiredCertificateAddDTO xingShiZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            xingShiZheng.setType(ExpiredCertificateTypeEnum.XING_SHI_ZHENG);
            xingShiZheng.setExpiredTime(vehicleEntity.getExpireDate());
            commonExpiredCertificateService.save(xingShiZheng);
        }

        if (null != vehicleEntity.getRoadTransportCertificateExpireDate()) {
            ExpiredCertificateAddDTO daoLuYunShuXuKeZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            daoLuYunShuXuKeZheng.setType(ExpiredCertificateTypeEnum.DAO_LU_YUN_SHU_XU_KE_ZHENG);
            daoLuYunShuXuKeZheng.setExpiredTime(vehicleEntity.getRoadTransportCertificateExpireDate());
            commonExpiredCertificateService.save(daoLuYunShuXuKeZheng);
        }

        if (vehicleEntity.getRelyEnterpriseRoadTransportBusinessLicenseExpireDate() != null) {
            ExpiredCertificateAddDTO guaKaoZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            guaKaoZheng.setType(ExpiredCertificateTypeEnum.GUA_KAO_ZHENG);
            guaKaoZheng.setExpiredTime(vehicleEntity.getRelyEnterpriseRoadTransportBusinessLicenseExpireDate());
            commonExpiredCertificateService.save(guaKaoZheng);
        }
    }

    /**
     * 保存车辆
     *
     * @param addForm
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long saveVehicle(VehicleAddForm addForm) {
        VehicleEntity vehicleEntity = SmartBeanUtil.copy(addForm, VehicleEntity.class);
        vehicleEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        vehicleEntity.setCreateUserId(addForm.getOperatorId());
        vehicleEntity.setCreateUserName(addForm.getOperatorName());
        vehicleEntity.setCreateUserType(addForm.getOperatorType());
        vehicleEntity.setCreateUserTypeDesc(addForm.getOperatorTypeDesc());
        vehicleDao.insert(vehicleEntity);
        this.saveRelateInfo(addForm, vehicleEntity.getVehicleId());
        this.updateExpiredCertificate(vehicleEntity);
        return vehicleEntity.getVehicleId();
    }

    /**
     * 设置司机车辆关联
     *
     * @param addForm
     * @param vehicleId
     */
    private void saveRelateInfo(VehicleAddForm addForm, Long vehicleId) {
        DriverVehicleEntity driverVehicleEntity = new DriverVehicleEntity();
        driverVehicleEntity.setVehicleId(vehicleId);
        driverVehicleEntity.setDriverId(addForm.getDriverId());
        driverVehicleDao.insert(driverVehicleEntity);
    }
}
