package net.lab1024.tms.admin.module.business.vehicle.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.vehicle.dao.VehicleDao;
import net.lab1024.tms.admin.module.business.vehicle.domain.dto.VehicleImportDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartLocalDateUtil;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.business.vehicle.domain.VehicleEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class VehicleBusinessModeUpdateTest extends TmsAdminApplicationTest {

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private VehicleManager vehicleManager;

    @Test
    public void updateByExcel() {
        File excelFile = new File("/Users/lidoudou/Desktop/DDD1/副本车辆信息模板确认版.xlsx");
        List<VehicleImportDTO> list = this.excelParse(excelFile);
        excelDataHandle(list);
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

        QueryWrapper qw = new QueryWrapper();
        qw.eq("deleted_flag", 0);
        List<VehicleEntity> vehicleList = vehicleDao.selectList(qw);
        List<String> vehicleNumberList = vehicleList.stream().map(VehicleEntity::getVehicleNumber).collect(Collectors.toList());
        Map<String, VehicleEntity> vehicleMap = vehicleList.stream().collect(Collectors.toMap(VehicleEntity::getVehicleNumber, Function.identity()));


        List<VehicleEntity> updateList = Lists.newArrayList();
        for (VehicleImportDTO item : list) {
            String vehicleNumber = item.getVehicleNumber().trim().toUpperCase();
            if (!vehicleNumberList.contains(vehicleNumber)) {
                throw new BusinessException("车辆不存在：" + vehicleNumber);
            }
            String businessModeStr = item.getBusinessMode().split("（")[0];
            BusinessModeEnum businessModeEnum = SmartBaseEnumUtil.getEnumByDesc(businessModeStr, BusinessModeEnum.class);
            if (null == businessModeEnum) {
                throw new BusinessException("车辆经营泪目不存在：" + vehicleNumber);
            }

            VehicleEntity dbVehicleEntity = vehicleMap.get(vehicleNumber);
            VehicleEntity vehicleEntity = new VehicleEntity();
            vehicleEntity.setVehicleId(dbVehicleEntity.getVehicleId());
//            vehicleEntity.setModel(item.getModel());
//            vehicleEntity.setEngineNumber(item.getEngineNumber());
//            vehicleEntity.setVin(item.getVin());
//            vehicleEntity.setGabarite(item.getGabarite());
//            vehicleEntity.setRoadTransportCertificateNumber(item.getRoadTransportCertificateNumber());
//            if (StringUtils.isNotEmpty(item.getRegisterDate())) {
//                String registerDateStr = item.getRegisterDate();
//                LocalDate registerDate = SmartLocalDateUtil.parseChineseYMD(registerDateStr);
//                vehicleEntity.setRegisterDate(registerDate);
//            }
            updateList.add(vehicleEntity);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            vehicleManager.updateBatchById(updateList);
        }

    }

    public static void main(String[] args) {
        List<String> dateList = Lists.newArrayList(
                "2015年11月23日",
                "2017年2月22日",
                "2016年6月24日",
                "2016年6月24日",
                "2015年7月1日",
                "2015年8月19日",
                "2015年8月4日",
                "2017年2月23日",
                "2017年8月15日"
        );
        dateList.forEach(item -> {
            LocalDate registerDate = SmartLocalDateUtil.parseChineseYMD(item);
            System.out.println(registerDate);
        });

    }
}
