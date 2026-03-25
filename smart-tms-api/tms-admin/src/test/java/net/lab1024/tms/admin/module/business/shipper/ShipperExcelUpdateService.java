package net.lab1024.tms.admin.module.business.shipper;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperDao;
import net.lab1024.tms.admin.module.business.shipper.dao.ShipperPrincipalDao;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperExcelImportDTO;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperEntity;
import net.lab1024.tms.common.module.business.shipper.domain.ShipperPrincipalEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShipperExcelUpdateService {

    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;


    public void importData(File excelFile) {
        List<ShipperExcelImportDTO> list = this.excelParse(excelFile);
        ShipperExcelUpdateService excelImportService = (ShipperExcelUpdateService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<ShipperExcelImportDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("deleted_flag", 0);
        List<ShipperEntity> shipperList = shipperDao.selectList(qw);
        Map<String, Long> shipperNameMap = shipperList.stream().collect(Collectors.toMap(ShipperEntity::getConsignor, ShipperEntity::getShipperId));

        // 负责人
        List<EmployeeEntity> employeeList = employeeDao.selectList(null);
        Map<String, Long> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getActualName, EmployeeEntity::getEmployeeId));

        for (ShipperExcelImportDTO item : list) {
            String consignor = item.getConsignor().trim();
            if (!shipperNameMap.containsKey(consignor)) {
                throw new BusinessException(consignor + "货主不存在");
            }

            ShipperEntity shipperEntity = new ShipperEntity();
            shipperEntity.setShipperId(shipperNameMap.get(consignor));

            String managerName = item.getManagerName();
            Long managerId = null;
            if (StringUtils.isNotEmpty(managerName)) {
                if (!employeeNameMap.containsKey(managerName)) {
                    throw new BusinessException(shipperEntity.getShortName() + "的负责人不存在：" + managerName);
                }
                managerId = employeeNameMap.get(managerName);
            }

            String customerName = item.getCustomerName();
            Long customerId = null;
            if (StringUtils.isNotEmpty(customerName)) {
                if (!employeeNameMap.containsKey(customerName)) {
                    throw new BusinessException(shipperEntity.getShortName() + "的客服负责人不存在：" + customerName);
                }
                customerId = employeeNameMap.get(customerName);
            }

            shipperDao.updateById(shipperEntity);

            if (null != customerId) {
                ShipperPrincipalEntity shipperPrincipalEntity = new ShipperPrincipalEntity();
                shipperPrincipalEntity.setShipperId(shipperEntity.getShipperId());
                shipperPrincipalEntity.setEmployeeId(customerId);
                shipperPrincipalEntity.setType(PrincipalTypeEnum.CUSTOMER_SERVICE.getValue());
                shipperPrincipalDao.insert(shipperPrincipalEntity);
            }
        }
    }


    private List<ShipperExcelImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<ShipperExcelImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, ShipperExcelImportDTO.class,
                params);
        List<ShipperExcelImportDTO> importList = excelInfo.getList();
        return importList;
    }

    public static void main(String[] args) {
        System.out.println("aldjiu哦i啊u的2392839%##¥".replaceAll("[^(0-9)]", ""));
    }
}