package net.lab1024.tms.admin.module.business.oilcard;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.SlaveOilCardNewImportDTO;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardEnterpriseManager;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardManager;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 副卡导入
 *
 * @author lidoudou
 * @date 2023/3/30 下午3:32
 */
@Slf4j
@Service
public class SlaveOilCardNewImportService {

    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardManager oilCardManager;
    @Autowired
    private OilCardEnterpriseManager oilCardEnterpriseManager;

    public void importData(File excelFile) {
        List<SlaveOilCardNewImportDTO> list = this.excelParse(excelFile);
        SlaveOilCardNewImportService excelImportService = (SlaveOilCardNewImportService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    private List<SlaveOilCardNewImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<SlaveOilCardNewImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, SlaveOilCardNewImportDTO.class,
                params);
        List<SlaveOilCardNewImportDTO> importList = excelInfo.getList();
        return importList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<SlaveOilCardNewImportDTO> list) {
        list = list.stream().filter(e-> SmartStringUtil.isNotBlank(e.getOilCardNo())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        QueryWrapper qw = new QueryWrapper();
        qw.eq("type", OilCardTypeEnum.MASTER.getValue());
        List<OilCardEntity> masterOilCardList = oilCardDao.selectList(qw);
        Map<String, OilCardEntity> masterOilCardNoMap = masterOilCardList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardNo, Function.identity()));


        QueryWrapper slaveQw = new QueryWrapper();
        slaveQw.eq("type", OilCardTypeEnum.SLAVE.getValue());
        List<OilCardEntity> slaveOilCardList = oilCardDao.selectList(slaveQw);
        Map<String, OilCardEntity> slaveOilCardNoMap = slaveOilCardList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardNo, Function.identity()));

        List<OilCardEntity> insertList = Lists.newArrayList();

        for (SlaveOilCardNewImportDTO item : list) {
            if (slaveOilCardNoMap.containsKey(item.getOilCardNo())) {
                log.error("副卡号已经存在" + item.getOilCardNo());
                continue;
            }

            if (!masterOilCardNoMap.containsKey(item.getMasterCardNo())) {
                throw new BusinessException(item.getOilCardNo() + "的主卡未找到");
            }
            OilCardEntity masterOilCardEntity = masterOilCardNoMap.get(item.getMasterCardNo());
            OilCardEntity oilCardEntity = SmartBeanUtil.copy(item, OilCardEntity.class);
            oilCardEntity.setMasterOilCardId(masterOilCardEntity.getOilCardId());

            oilCardEntity.setType(OilCardTypeEnum.SLAVE.getValue());
            oilCardEntity.setBrand("SINOPEC-GROUP");
            oilCardEntity.setPassword("000111");

            oilCardEntity.setCreateUserId(24L);
            oilCardEntity.setCreateUserName("尚蓉蓉");
            insertList.add(oilCardEntity);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            oilCardManager.saveBatch(insertList);
            List<Long> enterpriseIdList = Arrays.asList(2L, 3L, 5L, 6L, 7L, 8L, 10L, 11L);
            List<OilCardEnterpriseEntity> oilCardEnterpriseList = Lists.newArrayList();
            insertList.forEach(oilCard -> {
                enterpriseIdList.forEach(enterpriseId->{
                    OilCardEnterpriseEntity oilCardEnterpriseEntity = new OilCardEnterpriseEntity();
                    oilCardEnterpriseEntity.setEnterpriseId(enterpriseId);
                    oilCardEnterpriseEntity.setOilCardId(oilCard.getOilCardId());
                    oilCardEnterpriseList.add(oilCardEnterpriseEntity);
                });
            });
            if(CollectionUtils.isNotEmpty(oilCardEnterpriseList)){
                oilCardEnterpriseManager.saveBatch(oilCardEnterpriseList);
            }
        }
    }
}
