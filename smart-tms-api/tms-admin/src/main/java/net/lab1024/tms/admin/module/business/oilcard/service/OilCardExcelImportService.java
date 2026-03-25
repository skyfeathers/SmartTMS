package net.lab1024.tms.admin.module.business.oilcard.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oilcard.domain.dto.OilCardExcelImportDTO;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yandy
 * @description:
 * @date 2022/9/14 2:22 下午
 */
@Service
public class OilCardExcelImportService {

    @Autowired
    private OilCardManager oilCardManager;

    public void importOilCard(Long enterpriseId, File excelFile) {
        List<OilCardExcelImportDTO> list = this.excelParse(excelFile);
        OilCardExcelImportService excelImportService = (OilCardExcelImportService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list, enterpriseId);
    }

    private List<OilCardExcelImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<OilCardExcelImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, OilCardExcelImportDTO.class,
                params);
        List<OilCardExcelImportDTO> importList = excelInfo.getList();
        return importList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<OilCardExcelImportDTO> list, Long enterpriseId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<String, List<String>> listMap = list.stream().collect(Collectors.groupingBy(OilCardExcelImportDTO::getMasterOilCardNo,
                Collectors.mapping(OilCardExcelImportDTO::getSalveOilCardNo, Collectors.toList())));
        //中石化
        String brand = "SINOPEC-GROUP";
        for (Map.Entry<String, List<String>> entry : listMap.entrySet()) {
            //主卡
            String masterOilCardNo = entry.getKey();
            OilCardEntity masterOilCardEntity = this.buildOilCardEntity(masterOilCardNo, OilCardTypeEnum.MASTER.getValue(), brand, 0L, enterpriseId);
            oilCardManager.getBaseMapper().insert(masterOilCardEntity);
            //副卡
            Long masterOilCardId = masterOilCardEntity.getOilCardId();
            List<String> salveOilCardNoList = entry.getValue();
            List<OilCardEntity> salveOilCardEntityList = Lists.newArrayList();
            for (String salveOilCardNo : salveOilCardNoList) {
                OilCardEntity salveOilCard = this.buildOilCardEntity(salveOilCardNo, OilCardTypeEnum.SLAVE.getValue(), brand, masterOilCardId, enterpriseId);
                salveOilCardEntityList.add(salveOilCard);
            }
            if(CollectionUtils.isNotEmpty(salveOilCardEntityList)){
                oilCardManager.saveBatch(salveOilCardEntityList);
            }
        }
    }

    private OilCardEntity buildOilCardEntity(String cardNo, Integer cardType, String brand, Long masterOilCardId, Long enterpriseId) {
        OilCardEntity oilCardEntity = new OilCardEntity();
        oilCardEntity.setOilCardNo(cardNo);
//        oilCardEntity.setEnterpriseId(enterpriseId);
        oilCardEntity.setType(cardType);
        oilCardEntity.setMasterOilCardId(masterOilCardId);
        oilCardEntity.setBrand(brand);
        oilCardEntity.setCreateUserId(0L);
        oilCardEntity.setCreateUserName("系统导入");
        return oilCardEntity;
    }

}