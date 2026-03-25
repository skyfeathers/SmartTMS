package net.lab1024.tms.admin.module.business.shipper;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.shipper.dao.*;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperExcelImportDTO;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.constant.NumberConst;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.business.shipper.constant.PaymentTypeEnum;
import net.lab1024.tms.common.module.business.shipper.constant.PrincipalTypeEnum;
import net.lab1024.tms.common.module.business.shipper.constant.ShipperNatureEnum;
import net.lab1024.tms.common.module.business.shipper.domain.*;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShipperExcelImportService {

    @Autowired
    private ShipperDao shipperDao;
    @Autowired
    private ShipperInvoiceDao shipperInvoiceDao;
    @Autowired
    private ShipperContactDao shipperContactDao;
    @Autowired
    private ShipperPaymentWayDao shipperPaymentWayDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EmployeeDao employeeDao;
//    @Autowired
//    private ShipperEnterpriseDao shipperEnterpriseDao;
    @Autowired
    private ShipperMailAddressDao shipperMailAddressDao;
    @Autowired
    private ShipperPrincipalDao shipperPrincipalDao;


    public void importData(File excelFile) {
        List<ShipperExcelImportDTO> list = this.excelParse(excelFile);
        ShipperExcelImportService excelImportService = (ShipperExcelImportService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<ShipperExcelImportDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<ShipperEntity> shipperList = shipperDao.selectList(null);
        List<String> shipperNameList = shipperList.stream().map(ShipperEntity::getConsignor).collect(Collectors.toList());
        List<String> excelNameList = Lists.newArrayList();

        // 所属公司
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectList(null);
        Map<String, Long> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseName, EnterpriseEntity::getEnterpriseId));

        // 负责人
        List<EmployeeEntity> employeeList = employeeDao.selectList(null);
        Map<String, Long> employeeNameMap = employeeList.stream().collect(Collectors.toMap(EmployeeEntity::getActualName, EmployeeEntity::getEmployeeId));

        for (ShipperExcelImportDTO item : list) {
            item.setConsignor(item.getConsignor().trim());
            if (shipperNameList.contains(item.getConsignor()) || excelNameList.contains(item.getConsignor())) {
                if (shipperNameList.contains(item.getConsignor())) {
                    log.error("该货主在系统中已存在：" + item.getConsignor());
                }
                if (excelNameList.contains(item.getConsignor())) {
                    log.error("该货主在excel已存在：" + item.getConsignor());
                }
                continue;
            }

            ShipperEntity shipperEntity = SmartBeanUtil.copy(item, ShipperEntity.class);

            String enterpriseName = item.getEnterpriseName();
            Long enterpriseId = null;
            if (!enterpriseNameMap.containsKey(enterpriseName)) {
                throw new BusinessException(shipperEntity.getShortName() + "的企业名称不存在：" + enterpriseName);
            }
            enterpriseId = enterpriseNameMap.get(enterpriseName);

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

            shipperEntity.setShipperNature(ShipperNatureEnum.ENTERPRISE.getValue());

            String makeInvoiceStr = item.getMakeInvoiceFlag();
            Boolean makeInvoiceFlag = Boolean.FALSE;
            if (makeInvoiceStr.equals("需要") || makeInvoiceStr.equals("是")) {
                makeInvoiceFlag = Boolean.TRUE;
            }
            shipperEntity.setMakeInvoiceFlag(makeInvoiceFlag);

            shipperEntity.setTaxPoint(BigDecimal.ZERO);
            if (makeInvoiceFlag) {
                shipperEntity.setTaxPoint(new BigDecimal(9));
            }

            String accountPeriod = item.getAccountPeriod();
            if (StringUtils.isNotEmpty(accountPeriod)) {
                String filter = accountPeriod.replaceAll("[^(0-9)]", "");
                shipperEntity.setAccountPeriod(StringUtils.isNotEmpty(filter) ? Integer.valueOf(filter) : NumberConst.ZERO);
            }
            shipperEntity.setCreateUserId(0L);
            shipperEntity.setCreateUserName("系统导入");
            shipperEntity.setAuditStatus(AuditStatusEnum.AUDIT_PASS.getValue());
            shipperDao.insert(shipperEntity);
            excelNameList.add(shipperEntity.getConsignor());

            // 插入货主-公司关系表
//            ShipperEnterpriseEntity shipperEnterpriseEntity = new ShipperEnterpriseEntity();
//            shipperEnterpriseEntity.setShipperId(shipperEntity.getShipperId());
//            shipperEnterpriseEntity.setEnterpriseId(enterpriseId);
//            shipperEnterpriseDao.insert(shipperEnterpriseEntity);

            // 添加联系人信息
            if (StringUtils.isNotEmpty(item.getContactName()) && StringUtils.isNotEmpty(item.getContactPhone())) {
                ShipperContactEntity shipperContactEntity = new ShipperContactEntity();
                shipperContactEntity.setShipperId(shipperEntity.getShipperId());
                shipperContactEntity.setContactName(item.getContactName());
                shipperContactEntity.setContactPhone(item.getContactPhone());
                shipperContactDao.insert(shipperContactEntity);
            }

            // 添加开票信息
            if (StringUtils.isNotEmpty(item.getInvoiceNo())) {
                ShipperInvoiceEntity shipperInvoiceEntity = SmartBeanUtil.copy(item, ShipperInvoiceEntity.class);
                shipperInvoiceEntity.setShipperId(shipperEntity.getShipperId());
                shipperInvoiceDao.insert(shipperInvoiceEntity);
            }

            // 添加付款方式
            if (StringUtils.isNotEmpty(item.getAccountName())) {
                ShipperPaymentWayEntity shipperPaymentWayEntity = SmartBeanUtil.copy(item, ShipperPaymentWayEntity.class);
                shipperPaymentWayEntity.setPaymentType(PaymentTypeEnum.BANK.getValue());
                shipperPaymentWayEntity.setPublicAccountFlag(Boolean.TRUE);
                shipperPaymentWayEntity.setShipperId(shipperEntity.getShipperId());
                shipperPaymentWayDao.insert(shipperPaymentWayEntity);
            }

            if (StringUtils.isNotEmpty(item.getReceivePerson())) {
                ShipperMailAddressEntity shipperMailAddressEntity = SmartBeanUtil.copy(item, ShipperMailAddressEntity.class);
                shipperMailAddressEntity.setShipperId(shipperEntity.getShipperId());
                shipperMailAddressDao.insert(shipperMailAddressEntity);
            }

            if(null != customerId){
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