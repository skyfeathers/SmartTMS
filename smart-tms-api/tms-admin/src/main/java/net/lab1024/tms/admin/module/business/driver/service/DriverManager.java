package net.lab1024.tms.admin.module.business.driver.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.driver.dao.DriverBankDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverTaxRegisterDao;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverAddForm;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverBankBaseForm;
import net.lab1024.tms.admin.module.business.driver.domain.form.DriverUpdateForm;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleManage;
import net.lab1024.tms.admin.module.business.expiredcertificate.ExpiredCertificateService;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateAddDTO;
import net.lab1024.tms.admin.module.business.expiredcertificate.domain.ExpiredCertificateDelDTO;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.enumeration.UserTypeEnum;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverTaxRegisterEntity;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateModuleTypeEnum;
import net.lab1024.tms.common.module.business.expiredcertificate.constant.ExpiredCertificateTypeEnum;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * 司机模块
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:42
 */
@Service
public class DriverManager extends ServiceImpl<DriverDao, DriverEntity> {

    @Autowired
    private DriverDao driverDao;
    @Autowired
    private DriverBankManager driverBankManager;
    @Autowired
    private DriverBankDao driverBankDao;
    @Autowired
    private DriverVehicleManage driverVehicleManage;
    @Autowired
    private DriverTaxRegisterDao driverTaxRegisterDao;
    @Autowired
    private DictCacheService dictCacheService;
    @Autowired
    private ExpiredCertificateService expiredCertificateService;

    /**
     * 新建
     *
     * @param addForm
     * @param requestUserId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long addHandle(DriverAddForm addForm, Long enterpriseId, Long requestUserId, String requestUserName) {
        DriverEntity driverEntity = SmartBeanUtil.copy(addForm, DriverEntity.class);
        driverEntity.setEnterpriseId(enterpriseId);
        driverEntity.setCreateUserId(requestUserId);
        driverEntity.setCreateUserName(requestUserName);
        driverEntity.setCreateUserType(UserTypeEnum.ADMIN.getValue());
        driverEntity.setCreateUserTypeDesc(UserTypeEnum.ADMIN.getDesc());
        driverDao.insert(driverEntity);
        Long driverId = driverEntity.getDriverId();

        this.handleBank(driverId, addForm.getBankList(), requestUserId, requestUserName);
        this.saveRelateInfo(addForm, driverId);
        this.updateExpiredCertificate(driverEntity);
        return driverEntity.getDriverId();
    }

    /**
     * 新建
     *
     * @param updateForm
     * @param requestUserId
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateHandle(DriverUpdateForm updateForm, Long enterpriseId, Long requestUserId, String requestUserName) {
        DriverEntity driverEntity = SmartBeanUtil.copy(updateForm, DriverEntity.class);
        // DriverUpdateForm 无 enterpriseId 字段；到期证件等处依赖 DriverEntity.enterpriseId，需从当前租户上下文注入
        driverEntity.setEnterpriseId(enterpriseId);
        driverEntity.setAuditStatus(AuditStatusEnum.WAIT_AUDIT.getValue());
        driverEntity.setUpdateUserId(requestUserId);
        driverEntity.setUpdateUserName(requestUserName);
        driverDao.updateById(driverEntity);

        Long driverId = driverEntity.getDriverId();
        this.handleBank(driverId, updateForm.getBankList(), requestUserId, requestUserName);
        this.deleteRelateInfo(driverId);
        this.saveRelateInfo(updateForm, driverId);
        this.updateExpiredCertificate(driverEntity);
    }


    /**
     * 根据条件 修改/新建/删除 司机银行卡信息
     *
     * @param driverId
     * @param bankList
     * @param requestUserId
     * @param requestUserName
     */
    private void handleBank(Long driverId, List<DriverBankBaseForm> bankList, Long requestUserId, String requestUserName) {
        bankList = bankList.stream().filter(e -> StrUtil.isNotBlank(e.getBankType())).collect(Collectors.toList());
        Boolean defaultFlag = Boolean.FALSE;
        for (DriverBankBaseForm e : bankList) {
            if (defaultFlag) {
                e.setDefaultFlag(Boolean.FALSE);
            } else {
                defaultFlag = e.getDefaultFlag();
            }
        }
        Map<Boolean, List<DriverBankBaseForm>> idExitsMap = bankList.stream().collect(Collectors.groupingBy(e -> Optional.ofNullable(e.getBankId()).isPresent()));
        List<DriverBankBaseForm> addBankList = idExitsMap.getOrDefault(Boolean.FALSE, Lists.newArrayList());
        List<DriverBankBaseForm> updateBankList = idExitsMap.getOrDefault(Boolean.TRUE, Lists.newArrayList());

        // 删除银行卡
        List<Long> updateBankIdList = updateBankList.stream().map(DriverBankBaseForm::getBankId).collect(Collectors.toList());
        driverBankDao.deleteByDriverIdExcludeIds(driverId, updateBankIdList);
        // 修改银行卡
        if (CollectionUtil.isNotEmpty(updateBankList)) {
            List<DriverBankEntity> updateBankEntityList = SmartBeanUtil.copyList(updateBankList, DriverBankEntity.class);
            updateBankEntityList.forEach(e -> {
                String bankName = StringConst.EMPTY;
                if (SmartStringUtil.isNotBlank(e.getBankType())) {
                    bankName = dictCacheService.selectValueNameByValueCode(e.getBankType());
                }
                e.setBankName(bankName);
            });
            driverBankManager.updateBatchById(updateBankEntityList);
        }

        // 添加银行卡
        if (CollectionUtil.isNotEmpty(addBankList)) {
            List<DriverBankEntity> addBankEntityList = SmartBeanUtil.copyList(addBankList, DriverBankEntity.class);
            for (DriverBankEntity e : addBankEntityList) {
                String bankName = StringConst.EMPTY;
                if (SmartStringUtil.isNotBlank(e.getBankType())) {
                    bankName = dictCacheService.selectValueNameByValueCode(e.getBankType());
                }
                e.setBankName(bankName);
                e.setDriverId(driverId);
                e.setCreateUserId(requestUserId);
                e.setCreateUserName(requestUserName);
                e.setCreateUserType(UserTypeEnum.ADMIN.getValue());
                e.setCreateUserTypeDesc(UserTypeEnum.ADMIN.getDesc());
            }
            driverBankManager.saveBatch(addBankEntityList);
        }
    }


    /**
     * 更新司机的到期证件
     *
     * @param driverEntity
     */
    private void updateExpiredCertificate(DriverEntity driverEntity) {
        ExpiredCertificateAddDTO baseDTO = new ExpiredCertificateAddDTO();
        baseDTO.setEnterpriseId(driverEntity.getEnterpriseId());
        baseDTO.setModuleType(ExpiredCertificateModuleTypeEnum.DRIVER);
        baseDTO.setModuleId(driverEntity.getDriverId());
        baseDTO.setModuleName(driverEntity.getDriverName());

        if (null != driverEntity.getIdCardEffectiveEndDate()) {
            ExpiredCertificateAddDTO shenfenZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            shenfenZheng.setType(ExpiredCertificateTypeEnum.SHEN_FEN_ZHENG);
            shenfenZheng.setExpiredTime(driverEntity.getIdCardEffectiveEndDate());
            expiredCertificateService.save(shenfenZheng);
        }

        if (null != driverEntity.getValidPeriodTo()) {
            ExpiredCertificateAddDTO jiashiZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            jiashiZheng.setType(ExpiredCertificateTypeEnum.JIA_SHI_ZHENG);
            jiashiZheng.setExpiredTime(driverEntity.getValidPeriodTo());
            expiredCertificateService.save(jiashiZheng);
        }

        if (null != driverEntity.getQualificationCertificateEndDate()) {
            ExpiredCertificateAddDTO congyezigeZheng = SmartBeanUtil.copy(baseDTO, ExpiredCertificateAddDTO.class);
            congyezigeZheng.setType(ExpiredCertificateTypeEnum.CONG_YE_ZI_GE_ZHENG);
            congyezigeZheng.setExpiredTime(driverEntity.getQualificationCertificateEndDate());
            expiredCertificateService.save(congyezigeZheng);
        }
    }


    /**
     * 删除司机相关的信息
     *
     * @param driverIdList
     * @param requestUserId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteDriver(List<Long> driverIdList, Long requestUserId) {
        driverDao.updateDeletedFlagByDriverId(driverIdList, Boolean.TRUE, requestUserId);
        driverBankManager.getBaseMapper().updateDeletedFlag(driverIdList, Boolean.TRUE);
        driverVehicleManage.getBaseMapper().deleteByDriverId(driverIdList);

        driverIdList.forEach(e -> {
            ExpiredCertificateDelDTO delDTO = new ExpiredCertificateDelDTO();
            delDTO.setModuleType(ExpiredCertificateModuleTypeEnum.DRIVER);
            delDTO.setModuleId(e);
            expiredCertificateService.del(delDTO);
        });
    }

    /**
     * 删除关联信息
     *
     * @param driverId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteRelateInfo(Long driverId) {
        driverVehicleManage.getBaseMapper().deleteByDriverId(Lists.newArrayList(driverId));
        driverTaxRegisterDao.deleteByDriverId(driverId);
    }

    /**
     * 保存关联信息
     *
     * @param addForm
     * @param driverId
     */
    private void saveRelateInfo(DriverAddForm addForm, Long driverId) {
        // 保存车辆关联信息
        if (CollectionUtils.isNotEmpty(addForm.getVehicleIdList())) {
            List<DriverVehicleEntity> driverVehicleEntities = addForm.getVehicleIdList().stream().map(e -> {
                DriverVehicleEntity driverVehicleEntity = new DriverVehicleEntity();
                driverVehicleEntity.setDriverId(driverId);
                driverVehicleEntity.setVehicleId(e);
                return driverVehicleEntity;
            }).collect(Collectors.toList());
            driverVehicleManage.saveBatch(driverVehicleEntities);
        }

        // 保存税务登记人信息
        if (null != addForm.getTaxRegister()) {
            DriverTaxRegisterEntity taxRegisterEntity = SmartBeanUtil.copy(addForm.getTaxRegister(), DriverTaxRegisterEntity.class);
            taxRegisterEntity.setDriverId(driverId);
            driverTaxRegisterDao.insert(taxRegisterEntity);
        }
    }


    /**
     * 添加银行信息
     *
     * @param driverBankEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void addBank(DriverBankEntity driverBankEntity) {
        Long driverId = driverBankEntity.getDriverId();
        DriverBankEntity defaultBank = driverBankDao.selectDefaultByDriverId(driverId, true);
        if (defaultBank == null) {
            driverBankDao.insert(driverBankEntity);
            return;
        }
        DriverBankEntity updateDefaultBank = new DriverBankEntity();
        updateDefaultBank.setDefaultFlag(false);
        updateDefaultBank.setBankId(defaultBank.getBankId());
        driverBankDao.updateById(updateDefaultBank);

        driverBankDao.insert(driverBankEntity);
    }
}
