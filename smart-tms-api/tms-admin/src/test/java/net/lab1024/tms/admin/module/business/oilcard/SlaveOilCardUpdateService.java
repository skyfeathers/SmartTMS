package net.lab1024.tms.admin.module.business.oilcard;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.SlaveOilCardImportDTO;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardManager;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 副卡导入
 *
 * @author lidoudou
 * @date 2022/10/31 下午8:53
 */
@Slf4j
@Service
public class SlaveOilCardUpdateService {

    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardManager oilCardManager;
    @Autowired
    private DictCacheService dictCacheService;

    public void importData(File excelFile) {
        List<SlaveOilCardImportDTO> list = this.excelParse(excelFile);
        SlaveOilCardUpdateService excelImportService = (SlaveOilCardUpdateService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    private List<SlaveOilCardImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<SlaveOilCardImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, SlaveOilCardImportDTO.class,
                params);
        List<SlaveOilCardImportDTO> importList = excelInfo.getList();
        return importList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<SlaveOilCardImportDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        QueryWrapper slaveQw = new QueryWrapper();
        slaveQw.eq("type", OilCardTypeEnum.SLAVE.getValue());
        List<OilCardEntity> slaveOilCardList = oilCardDao.selectList(slaveQw);
        Map<String, OilCardEntity> slaveOilCardNoMap = slaveOilCardList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardNo, Function.identity()));

        List<OilCardEntity> updateList = Lists.newArrayList();

        for (SlaveOilCardImportDTO item : list) {
            if (!slaveOilCardNoMap.containsKey(item.getOilCardNo())) {
                throw new BusinessException(item.getOilCardNo() + "油卡好不存在");
            }

            OilCardEntity oilCardEntity = slaveOilCardNoMap.get(item.getOilCardNo());
            Boolean disabledFlag = Boolean.FALSE;
            if (item.getDisabledFlag().equals("禁用")) {
                disabledFlag = Boolean.TRUE;
            }
            oilCardEntity.setDisabledFlag(disabledFlag);
            updateList.add(oilCardEntity);
        }
        oilCardManager.updateBatchById(updateList);
    }
}
