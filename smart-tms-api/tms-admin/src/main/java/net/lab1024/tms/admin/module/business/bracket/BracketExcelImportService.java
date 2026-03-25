package net.lab1024.tms.admin.module.business.bracket;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.bracket.domain.dto.BracketExcelImportDTO;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.bracket.domain.BracketEntity;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.vehicle.constants.VehiclePlateColorEnum;
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
 * 挂车导入
 *
 * @author lidoudou
 * @date 2022/10/31 上午11:56
 */
@Slf4j
@Service
public class BracketExcelImportService {

    @Autowired
    private BracketDao bracketDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EmployeeDao employeeDao;

    public void importData(File excelFile) {
        List<BracketExcelImportDTO> list = this.excelParse(excelFile);
        BracketExcelImportService excelImportService = (BracketExcelImportService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    private List<BracketExcelImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<BracketExcelImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, BracketExcelImportDTO.class,
                params);
        List<BracketExcelImportDTO> importList = excelInfo.getList();
        return importList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<BracketExcelImportDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<BracketEntity> bracketList = bracketDao.selectList(null);
        List<String> bracketNoList = bracketList.stream().map(BracketEntity::getBracketNo).collect(Collectors.toList());
        List<String> excelNoList = Lists.newArrayList();

        // 所属公司
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectList(null);
        Map<String, Long> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseName, EnterpriseEntity::getEnterpriseId));

        // 负责人
        List<EmployeeEntity> employeeList = employeeDao.selectList(null);
        Map<String, Long> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getActualName, EmployeeEntity::getEmployeeId));

        for (BracketExcelImportDTO item : list) {
            String bracketNo = item.getBracketNo();
            item.setBracketNo(bracketNo.trim().toUpperCase());
            if (bracketNoList.contains(item.getBracketNo()) || excelNoList.contains(item.getBracketNo())) {
                if (bracketNoList.contains(bracketNo)) {
                    log.error("该挂车在系统中已存在：" + bracketNo);
                }
                if (excelNoList.contains(bracketNo)) {
                    log.error("该挂车在excel已存在：" + bracketNo);
                }
                continue;
            }

            BracketEntity bracketEntity = SmartBeanUtil.copy(item, BracketEntity.class);

            String enterpriseName = item.getEnterpriseName();
            Long enterpriseId = null;
            if (!enterpriseNameMap.containsKey(enterpriseName)) {
                throw new BusinessException(bracketEntity.getBracketNo() + "的企业名称不存在：" + enterpriseName);
            }
            enterpriseId = enterpriseNameMap.get(enterpriseName);

            String managerName = item.getManagerName();
            Long managerId = null;
            if (StringUtils.isNotEmpty(managerName)) {
                if (!employeeNameMap.containsKey(managerName)) {
                    throw new BusinessException(bracketEntity.getBracketNo() + "的负责人不存在：" + managerName);
                }
                managerId = employeeNameMap.get(managerName);
            }


            String businessMode = item.getBusinessMode().split("（")[0];
            BusinessModeEnum businessModeEnum = SmartBaseEnumUtil.getEnumByDesc(businessMode, BusinessModeEnum.class);
            bracketEntity.setManagerId(managerId);

            /**
             * 车牌颜色
             */
            if (StringUtils.isNotEmpty(item.getPlateColorCode())) {
                VehiclePlateColorEnum vehiclePlateColorEnum = SmartBaseEnumUtil.getEnumByDesc(item.getPlateColorCode(), VehiclePlateColorEnum.class);
                bracketEntity.setPlateColorCode(vehiclePlateColorEnum.getValue());
            }

            if (StringUtils.isNotEmpty(item.getRegisterTime())) {
                String registerTimeStr = item.getRegisterTime();
                LocalDate registerTime = SmartLocalDateUtil.parseChineseYMD(registerTimeStr);
                bracketEntity.setRegisterTime(registerTime);
            }

            if (StringUtils.isNotEmpty(item.getIssueTime())) {
                String issueTimeStr = item.getIssueTime();
                LocalDate issueTime = SmartLocalDateUtil.parseChineseYMD(issueTimeStr);
                bracketEntity.setIssueTime(issueTime);
            }

            if (StringUtils.isNotEmpty(item.getWeight())) {
                bracketEntity.setWeight(new BigDecimal(item.getWeight()));
            }

            if (StringUtils.isNotEmpty(item.getTonnage())) {
                bracketEntity.setTonnage(new BigDecimal(item.getTonnage()));
            }

            bracketEntity.setCreateUserId(0L);
            bracketEntity.setCreateUserName("系统导入");
            bracketDao.insert(bracketEntity);
            excelNoList.add(bracketEntity.getBracketNo());
        }
    }

}
