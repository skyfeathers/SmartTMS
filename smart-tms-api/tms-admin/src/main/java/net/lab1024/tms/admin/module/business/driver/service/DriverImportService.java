package net.lab1024.tms.admin.module.business.driver.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.driver.dao.DriverBankDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.driver.domain.dto.DriverExcelImportDTO;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.module.business.driver.constants.VehicleClassEnum;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DriverImportService {

    @Autowired
    private DriverDao driverDao;
    @Autowired
    private DriverBankDao driverBankDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EmployeeDao employeeDao;

    public void importData(File excelFile) {
        List<DriverExcelImportDTO> list = this.excelParse(excelFile);
        DriverImportService excelImportService = (DriverImportService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    private List<DriverExcelImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<DriverExcelImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, DriverExcelImportDTO.class,
                params);
        List<DriverExcelImportDTO> importList = excelInfo.getList();
        return importList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<DriverExcelImportDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<DriverEntity> driverList = driverDao.selectList(null);
        List<String> driverNameList = driverList.stream().map(e -> e.getDriverName() + e.getTelephone()).collect(Collectors.toList());
        List<String> excelNoList = Lists.newArrayList();

        // 所属公司
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectList(null);
        Map<String, Long> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseName, EnterpriseEntity::getEnterpriseId));

        // 负责人
        List<EmployeeEntity> employeeList = employeeDao.selectList(null);
        Map<String, Long> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getActualName, EmployeeEntity::getEmployeeId));

        for (DriverExcelImportDTO item : list) {
            item.setDriverName(item.getDriverName().trim());
            item.setTelephone(item.getTelephone().trim());
            String key = item.getDriverName() + item.getTelephone();

            if (driverNameList.contains(key) || excelNoList.contains(key)) {
                if (driverNameList.contains(key)) {
                    log.error("该司机在系统中已存在：" + key);
                }
                if (excelNoList.contains(key)) {
                    log.error("该司机在excel已存在：" + key);
                }
                continue;
            }

            DriverEntity driverEntity = SmartBeanUtil.copy(item, DriverEntity.class);

            String enterpriseName = item.getEnterpriseName();
            Long enterpriseId = null;
            if (!enterpriseNameMap.containsKey(enterpriseName)) {
                throw new BusinessException(driverEntity.getDriverName() + "的企业名称不存在：" + enterpriseName);
            }
            enterpriseId = enterpriseNameMap.get(enterpriseName);

            String managerName = item.getManagerName();
            Long managerId = null;
            if (StringUtils.isNotEmpty(managerName)) {
                if (!employeeNameMap.containsKey(managerName)) {
                    throw new BusinessException(driverEntity.getDriverName() + "的负责人不存在：" + managerName);
                }
                managerId = employeeNameMap.get(managerName);
            }
            driverEntity.setManagerId(managerId);

            /**
             * 准驾车型
             */
            if (StringUtils.isNotEmpty(item.getVehicleClass())) {
                VehicleClassEnum vehicleClassEnum = SmartBaseEnumUtil.getEnumByDesc(item.getVehicleClass(), VehicleClassEnum.class);
                driverEntity.setVehicleClass(vehicleClassEnum.getValue());
            }


            if (StringUtils.isNotEmpty(item.getLicenseFirstGetDate())) {
                String licenseFirstGetDateStr = item.getLicenseFirstGetDate();
                LocalDate licenseFirstGetDate = SmartLocalDateUtil.parseChineseYMD(licenseFirstGetDateStr);
                driverEntity.setLicenseFirstGetDate(licenseFirstGetDate);
            }

            if (StringUtils.isNotEmpty(item.getValidPeriodFrom())) {
                String validPeriodFromStr = item.getValidPeriodFrom();
                LocalDate validPeriodFrom = SmartLocalDateUtil.parseChineseYMD(validPeriodFromStr);
                driverEntity.setValidPeriodFrom(validPeriodFrom);
            }

            if (StringUtils.isNotEmpty(item.getValidPeriodTo())) {
                String validPeriodToStr = item.getValidPeriodTo();
                LocalDate validPeriodTo = SmartLocalDateUtil.parseChineseYMD(validPeriodToStr);
                driverEntity.setValidPeriodTo(validPeriodTo);
            }

            if (StringUtils.isNotEmpty(item.getQualificationCertificateStartDate())) {
                String qualificationCertificateStartDateStr = item.getQualificationCertificateStartDate();
                LocalDate qualificationCertificateStartDate = SmartLocalDateUtil.parseChineseYMD(qualificationCertificateStartDateStr);
                driverEntity.setQualificationCertificateStartDate(qualificationCertificateStartDate);
            }

            if (StringUtils.isNotEmpty(item.getQualificationCertificateEndDate())) {
                String qualificationCertificateEndDateStr = item.getQualificationCertificateEndDate();
                LocalDate qualificationCertificateEndDate = SmartLocalDateUtil.parseChineseYMD(qualificationCertificateEndDateStr);
                driverEntity.setQualificationCertificateEndDate(qualificationCertificateEndDate);
            }

            driverEntity.setCreateUserId(0L);
            driverEntity.setCreateUserName("系统导入");
            driverDao.insert(driverEntity);
            excelNoList.add(key);

            // 设置银行信息
            if (StringUtils.isNotEmpty(item.getAccountName())) {
                DriverBankEntity driverBankEntity = new DriverBankEntity();
                driverBankEntity.setDriverId(driverEntity.getDriverId());
                driverBankEntity.setAccountName(item.getAccountName());
                driverBankEntity.setBankAccount(item.getBankAccount());
                driverBankEntity.setBankName(item.getBankName());
                driverBankEntity.setBankBranchName(item.getBankBranchName());
                driverBankEntity.setDefaultFlag(Boolean.FALSE);
                if (StringUtils.isNotEmpty(item.getDefaultFlag()) && item.getDefaultFlag().equals("是")) {
                    driverBankEntity.setDefaultFlag(Boolean.TRUE);
                }
                driverBankDao.insert(driverBankEntity);
            }
        }
    }

}
