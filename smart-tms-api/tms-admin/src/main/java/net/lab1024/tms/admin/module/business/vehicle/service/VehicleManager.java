package net.lab1024.tms.admin.module.business.vehicle.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleManage;
import net.lab1024.tms.admin.module.business.expiredcertificate.ExpiredCertificateService;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateAddDTO;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateDelDTO;
import net.lab1024.tms.admin.module.business.insurance.InsuranceManager;
import net.lab1024.tms.admin.module.business.repair.dao.RepairDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.domain.form.VehicleAddForm;
import net.lab1024.tms.admin.module.business.vehicle.domain.form.VehicleUpdateForm;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/***
 * 车辆管理
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:28
 */
@Service
public class VehicleManager extends ServiceImpl<VehicleDao, VehicleEntity> {

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private RepairDao repairDao;

    @Autowired
    private InsuranceManager insuranceManager;
    @Autowired
    private DriverVehicleManage driverVehicleManage;
    @Autowired
    private ExpiredCertificateService expiredCertificateService;


    /**
     * 更新
     *
     * @param updateDTO
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateHandle(VehicleUpdateForm updateDTO) {
        VehicleEntity vehicleEntity = SmartBeanUtil.copy(updateDTO, VehicleEntity.class);
        vehicleEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        vehicleEntity.setUpdateUserId(updateDTO.getOperatorId());
        vehicleEntity.setUpdateUserName(updateDTO.getOperatorName());
        vehicleDao.updateById(vehicleEntity);
        vehicleDao.updateBracketId(vehicleEntity.getVehicleId(), vehicleEntity.getBracketId());

        Long vehicleId = updateDTO.getVehicleId();
        this.deleteRelateInfo(vehicleId);
        this.saveRelateInfo(updateDTO, vehicleId, updateDTO.getOperatorId());
        updateExpiredCertificate(vehicleEntity);
    }

    /**
     * 新建
     *
     * @param addDTO
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long addHandle(VehicleAddForm addDTO, Long enterpriseId) {
        VehicleEntity vehicleEntity = SmartBeanUtil.copy(addDTO, VehicleEntity.class);
        vehicleEntity.setEnterpriseId(enterpriseId);
        vehicleEntity.setCreateUserId(addDTO.getOperatorId());
        vehicleEntity.setCreateUserName(addDTO.getOperatorName());
        vehicleEntity.setCreateUserType(addDTO.getOperatorType());
        vehicleEntity.setCreateUserTypeDesc(addDTO.getOperatorTypeDesc());
        vehicleDao.insert(vehicleEntity);

        this.saveRelateInfo(addDTO, vehicleEntity.getVehicleId(), addDTO.getOperatorId());
        this.updateExpiredCertificate(vehicleEntity);
        return vehicleEntity.getVehicleId();
    }


    /**
     * 更新车辆的到期证件
     *
     * @param vehicleEntity
     */
    private void updateExpiredCertificate(VehicleEntity vehicleEntity) {
        ExpiredCertificateAddDTO baseDTO = new ExpiredCertificateAddDTO();
        baseDTO.setEnterpriseId(vehicleEntity.getEnterpriseId());
        baseDTO.setModuleType(ExpiredCertificateModuleTypeEnum.VEHICLE);
        baseDTO.setModuleId(vehicleEntity.getVehicleId());
        baseDTO.setModuleName(vehicleEntity.getVehicleNumber());

        if (null != vehicleEntity.getExpireDate()) {
            ExpiredCertificateAddDTO xingShiZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            xingShiZheng.setType(ExpiredCertificateTypeEnum.XING_SHI_ZHENG);
            xingShiZheng.setExpiredTime(vehicleEntity.getExpireDate());
            expiredCertificateService.save(xingShiZheng);
        }

        if (null != vehicleEntity.getRoadTransportCertificateExpireDate()) {
            ExpiredCertificateAddDTO daoLuYunShuXuKeZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            daoLuYunShuXuKeZheng.setType(ExpiredCertificateTypeEnum.DAO_LU_YUN_SHU_XU_KE_ZHENG);
            daoLuYunShuXuKeZheng.setExpiredTime(vehicleEntity.getRoadTransportCertificateExpireDate());
            expiredCertificateService.save(daoLuYunShuXuKeZheng);
        }

        if (vehicleEntity.getRelyEnterpriseRoadTransportBusinessLicenseExpireDate() != null) {
            ExpiredCertificateAddDTO guaKaoZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            guaKaoZheng.setType(ExpiredCertificateTypeEnum.GUA_KAO_ZHENG);
            guaKaoZheng.setExpiredTime(vehicleEntity.getRelyEnterpriseRoadTransportBusinessLicenseExpireDate());
            expiredCertificateService.save(guaKaoZheng);
        }
    }

    /**
     * 删除车辆相关的信息
     *
     * @param vehicleIdList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void batchDelete(List<Long> vehicleIdList) {
        vehicleDao.updateDeletedFlagByVehicleId(vehicleIdList, Boolean.TRUE);
        driverVehicleManage.getBaseMapper().deleteByVehicleId(vehicleIdList);

        vehicleIdList.forEach(e -> {
            ExpiredCertificateDelDTO delDTO = new ExpiredCertificateDelDTO();
            delDTO.setModuleType(ExpiredCertificateModuleTypeEnum.VEHICLE);
            delDTO.setModuleId(e);
            expiredCertificateService.del(delDTO);
        });
    }

    /**
     * 删除关联信息
     *
     * @param vehicleId
     */
    private void deleteRelateInfo(Long vehicleId) {
        repairDao.deleteByModuleIdAndType(vehicleId, RepairModuleTypeEnum.VEHICLE.getValue());
        insuranceManager.getBaseMapper().deleteByModuleIdAndType(vehicleId, InsuranceModuleTypeEnum.VEHICLE.getValue());
        driverVehicleManage.getBaseMapper().deleteByVehicleId(Lists.newArrayList(vehicleId));
    }

    /**
     * 保存关联信息
     *
     * @param addForm
     * @param vehicleId
     * @param requestUserId
     */
    private void saveRelateInfo(VehicleAddForm addForm, Long vehicleId, Long requestUserId) {
        if (CollectionUtils.isEmpty(addForm.getDriverIdList())) {
            return;
        }
        List<DriverVehicleEntity> driverVehicleEntities = addForm.getDriverIdList().stream().map(e -> {
            DriverVehicleEntity driverVehicleEntity = new DriverVehicleEntity();
            driverVehicleEntity.setDriverId(e);
            driverVehicleEntity.setVehicleId(vehicleId);
            return driverVehicleEntity;
        }).collect(Collectors.toList());
        driverVehicleManage.saveBatch(driverVehicleEntities);
    }

    public void batchUpdateDisabled(List<Long> vehicleIdList, boolean disabledFlag) {
        vehicleDao.batchUpdateDisabled(vehicleIdList, disabledFlag);
    }
}
