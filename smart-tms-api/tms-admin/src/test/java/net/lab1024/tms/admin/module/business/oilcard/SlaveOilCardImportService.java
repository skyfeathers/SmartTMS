package net.lab1024.tms.admin.module.business.oilcard;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.SlaveOilCardImportDTO;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardManager;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
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
public class SlaveOilCardImportService {

    @Autowired
    private OilCardDao oilCardDao;

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private OilCardManager oilCardManager;
    @Autowired
    private DictCacheService dictCacheService;

    public void importData(File excelFile) {
        List<SlaveOilCardImportDTO> list = this.excelParse(excelFile);
        SlaveOilCardImportService excelImportService = (SlaveOilCardImportService) AopContext.currentProxy();
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
        QueryWrapper qw = new QueryWrapper();
        qw.eq("type", OilCardTypeEnum.MASTER.getValue());
        List<OilCardEntity> masterOilCardList = oilCardDao.selectList(qw);
        Map<String, OilCardEntity> masterOilCardNoMap = masterOilCardList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardNo, Function.identity()));


        QueryWrapper slaveQw = new QueryWrapper();
        slaveQw.eq("type", OilCardTypeEnum.SLAVE.getValue());
        List<OilCardEntity> slaveOilCardList = oilCardDao.selectList(slaveQw);
        Map<String, OilCardEntity> slaveOilCardNoMap = slaveOilCardList.stream().collect(Collectors.toMap(OilCardEntity::getOilCardNo, Function.identity()));


        List<DictValueVO> dictList = dictCacheService.selectByKeyCode("OIL-CARD-BRAND");
        Map<String, String> dictValueCodeMap = dictList.stream().collect(Collectors.toMap(DictValueVO::getValueName, DictValueVO::getValueCode));

        List<OilCardEntity> insertList = Lists.newArrayList();

        for (SlaveOilCardImportDTO item : list) {
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
//            oilCardEntity.setEnterpriseId(masterOilCardEntity.getEnterpriseId());
            oilCardEntity.setBeginBalance(new BigDecimal(item.getBalance()));
            oilCardEntity.setBalance(new BigDecimal(item.getBalance()));

            oilCardEntity.setType(OilCardTypeEnum.SLAVE.getValue());
            oilCardEntity.setBrand(dictValueCodeMap.get(item.getModel()));

            oilCardEntity.setCreateUserId(0L);
            oilCardEntity.setCreateUserName("系统导入");
            insertList.add(oilCardEntity);
        }
        oilCardManager.saveBatch(insertList);
    }
}
