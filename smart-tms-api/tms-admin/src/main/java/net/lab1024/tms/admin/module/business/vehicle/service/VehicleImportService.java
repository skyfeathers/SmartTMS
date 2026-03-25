package net.lab1024.tms.admin.module.business.vehicle.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.bracket.BracketDao;
import net.lab1024.tms.admin.module.business.driver.dao.DriverDao;
import net.lab1024.tms.admin.module.business.drivervehicle.DriverVehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.domain.dto.VehicleImportDTO;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.driver.domain.DriverEntity;
import net.lab1024.tms.common.module.business.drivervehicle.DriverVehicleEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 车辆导入
 *
 * @author lidoudou
 * @date 2022/10/31 上午9:09
 */
@Slf4j
@Service
public class VehicleImportService {

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private BracketDao bracketDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DriverVehicleDao driverVehicleDao;
    @Autowired
    private DictCacheService dictCacheService;

    public void importData(File excelFile) {
        List<VehicleImportDTO> list = this.excelParse(excelFile);
        VehicleImportService excelImportService = (VehicleImportService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    private List<VehicleImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<VehicleImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, VehicleImportDTO.class,
                params);
        List<VehicleImportDTO> importList = excelInfo.getList();
        return importList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<VehicleImportDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<VehicleEntity> vehicleList = vehicleDao.selectList(null);
        List<String> vehicleNumberList = vehicleList.stream().map(VehicleEntity::getVehicleNumber).collect(Collectors.toList());
        List<String> excelNoList = Lists.newArrayList();

        // 绑定司机
        QueryWrapper qw = new QueryWrapper();
        qw.groupBy("driver_name");
        List<DriverEntity> driverList = driverDao.selectList(qw);
        Map<String, Long> driverNameMap = driverList.stream().collect(Collectors.toMap(DriverEntity::getDriverName, DriverEntity::getDriverId));

        // 绑定挂车
        List<BracketEntity> bracketList = bracketDao.selectList(null);
        Map<String, Long> bracketNoMap = bracketList.stream().collect(Collectors.toMap(BracketEntity::getBracketNo, BracketEntity::getBracketId));

        // 负责人
        List<EmployeeEntity> employeeList = employeeDao.selectList(null);
        Map<String, Long> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getActualName, EmployeeEntity::getEmployeeId));

        List<DictValueVO> dictValueList = dictCacheService.selectByKeyCode("vehicleType");
        Map<String, String> dictValueMap = dictValueList.stream().collect(Collectors.toMap(DictValueVO::getValueName, DictValueVO::getValueCode));

        for (VehicleImportDTO item : list) {
            String vehicleNumber = item.getVehicleNumber();
            item.setVehicleNumber(vehicleNumber.trim().toUpperCase());
            if (vehicleNumberList.contains(item.getVehicleNumber()) || excelNoList.contains(item.getVehicleNumber())) {
                if (vehicleNumberList.contains(item.getVehicleNumber())) {
                    log.error("该车辆在系统中已存在：" + item.getVehicleNumber());
                }
                if (excelNoList.contains(item.getVehicleNumber())) {
                    log.error("该车辆在excel已存在：" + item.getVehicleNumber());
                }
                continue;
            }


            VehicleEntity vehicleEntity = SmartBeanUtil.copy(item, VehicleEntity.class);

            String driverName = item.getDriverName();
            Long driverId = null;
            if (StringUtils.isNotEmpty(driverName)) {
                if (driverNameMap.containsKey(driverName)) {
                    driverId = driverNameMap.get(driverName);
                }
            }

            String bracketNo = item.getBracketNo();
            Long bracketId = null;
            if (StringUtils.isNotEmpty(bracketNo)) {
                if (!bracketNoMap.containsKey(bracketNo)) {
                    throw new BusinessException(vehicleEntity.getVehicleNumber() + "的绑定挂车不存在：" + bracketNo);
                }
                bracketId = bracketNoMap.get(bracketNo);
            }

            String managerName = item.getManagerName();
            Long managerId = null;
            if (StringUtils.isNotEmpty(managerName)) {
                if (!employeeNameMap.containsKey(managerName)) {
                    throw new BusinessException(vehicleEntity.getVehicleNumber() + "的负责人不存在：" + managerName);
                }
                managerId = employeeNameMap.get(managerName);
            }

            String vehicleTypeDesc = item.getVehicleType();
            String vehicleType = null;
            if (StringUtils.isNotEmpty(vehicleTypeDesc)) {
                if (!dictValueMap.containsKey(vehicleTypeDesc)) {
                    throw new BusinessException(vehicleEntity.getVehicleNumber() + "的车辆类型不存在：" + vehicleTypeDesc);
                }
                vehicleType = dictValueMap.get(vehicleTypeDesc);
            }
            vehicleEntity.setManagerId(managerId);
            vehicleEntity.setBracketId(bracketId);
            vehicleEntity.setVehicleRegistrationCertificateNo(item.getVehicleRegistrationCertificateNo());
            if (StringUtils.isNotEmpty(item.getVehicleAuditExpireDate())) {
                String vehicleAuditExpireDateStr = item.getVehicleAuditExpireDate();
                LocalDate vehicleAuditExpireDate = SmartLocalDateUtil.parseChineseYMD(vehicleAuditExpireDateStr);
                vehicleEntity.setVehicleAuditExpireDate(vehicleAuditExpireDate);
            }

            if (StringUtils.isNotEmpty(item.getVehiclePlateColorCode())) {
                VehiclePlateColorEnum colorEnum = SmartBaseEnumUtil.getEnumByDesc(item.getVehiclePlateColorCode(), VehiclePlateColorEnum.class);
                if (null != colorEnum) {
                    vehicleEntity.setVehiclePlateColorCode(colorEnum.getValue());
                }
            }

            vehicleEntity.setVehicleType(vehicleType);
            if (StringUtils.isNotEmpty(item.getRegisterDate())) {
                String registerDateStr = item.getRegisterDate();
                LocalDate registerDate = SmartLocalDateUtil.parseChineseYMD(registerDateStr);
                vehicleEntity.setRegisterDate(registerDate);
            }

            if (StringUtils.isNotEmpty(item.getIssueDate())) {
                String issueDateStr = item.getIssueDate();
                LocalDate issueDate = SmartLocalDateUtil.parseChineseYMD(issueDateStr);
                vehicleEntity.setIssueDate(issueDate);
            }

            if (StringUtils.isNotEmpty(item.getVehicleTonnage())) {
                vehicleEntity.setVehicleTonnage(new BigDecimal(item.getVehicleTonnage()));
            }

            if (StringUtils.isNotEmpty(item.getRoadTransportCertificateStartDate())) {
                String roadTransportCertificateStartDateStr = item.getIssueDate();
                LocalDate roadTransportCertificateStartDate = SmartLocalDateUtil.parseChineseYMD(roadTransportCertificateStartDateStr);
                vehicleEntity.setRoadTransportCertificateStartDate(roadTransportCertificateStartDate);
            }

            if (StringUtils.isNotEmpty(item.getRoadTransportCertificateExpireDate())) {
                String roadTransportCertificateExpireDateStr = item.getIssueDate();
                LocalDate roadTransportCertificateExpireDate = SmartLocalDateUtil.parseChineseYMD(roadTransportCertificateExpireDateStr);
                vehicleEntity.setRoadTransportCertificateExpireDate(roadTransportCertificateExpireDate);
            }

            vehicleEntity.setCreateUserId(0L);
            vehicleEntity.setCreateUserName("系统导入");
            vehicleDao.insert(vehicleEntity);
            excelNoList.add(vehicleEntity.getVehicleNumber());

            if (driverId != null) {
                DriverVehicleEntity driverVehicleEntity = new DriverVehicleEntity();
                driverVehicleEntity.setVehicleId(vehicleEntity.getVehicleId());
                driverVehicleEntity.setDriverId(driverId);
                driverVehicleDao.insert(driverVehicleEntity);
            }
        }
    }

}
