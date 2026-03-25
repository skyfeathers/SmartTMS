package net.lab1024.tms.admin.module.business.oilcard;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.domain.MasterOilCardImportDTO;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardManager;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
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
import java.util.stream.Collectors;

@Service
public class MasterOilCardImportService {

    @Autowired
    private OilCardDao oilCardDao;

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private OilCardManager oilCardManager;
    @Autowired
    private DictCacheService dictCacheService;

    public void importData(File excelFile) {
        List<MasterOilCardImportDTO> list = this.excelParse(excelFile);
        MasterOilCardImportService excelImportService = (MasterOilCardImportService) AopContext.currentProxy();
        excelImportService.excelDataHandle(list);
    }

    private List<MasterOilCardImportDTO> excelParse(File file) {
        ImportParams params = new ImportParams();
        //表格标题行数
        params.setTitleRows(0);
        //表头行数
        params.setHeadRows(1);
        params.setSheetNum(1);
        params.setStartSheetIndex(0);
        ExcelImportResult<MasterOilCardImportDTO> excelInfo = ExcelImportUtil.importExcelMore(file, MasterOilCardImportDTO.class,
                params);
        List<MasterOilCardImportDTO> importList = excelInfo.getList();
        return importList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void excelDataHandle(List<MasterOilCardImportDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 所属公司
        List<EnterpriseEntity> enterpriseList = enterpriseDao.selectList(null);
        Map<String, Long> enterpriseNameMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseEntity::getEnterpriseName, EnterpriseEntity::getEnterpriseId));

        List<DictValueVO> dictList = dictCacheService.selectByKeyCode("OIL-CARD-BRAND");
        Map<String, String> dictValueCodeMap = dictList.stream().collect(Collectors.toMap(DictValueVO::getValueName, DictValueVO::getValueCode));


        List<OilCardEntity> insertList = Lists.newArrayList();

        for (MasterOilCardImportDTO item : list) {
            OilCardEntity oilCardEntity = SmartBeanUtil.copy(item, OilCardEntity.class);

            String enterpriseName = item.getEnterpriseName();
            Long enterpriseId = null;
            if (!enterpriseNameMap.containsKey(enterpriseName)) {
                throw new BusinessException(oilCardEntity.getOilCardNo() + "的企业名称不存在：" + enterpriseName);
            }

            if (!dictValueCodeMap.containsKey(item.getModel())) {
                throw new BusinessException(oilCardEntity.getOilCardNo() + "的品牌未找到：" + item.getModel());
            }

            enterpriseId = enterpriseNameMap.get(enterpriseName);
//            oilCardEntity.setEnterpriseId(enterpriseId);
            oilCardEntity.setBeginBalance(new BigDecimal(item.getBalance()));
            oilCardEntity.setBalance(new BigDecimal(item.getBalance()));
            oilCardEntity.setType(OilCardTypeEnum.MASTER.getValue());
            oilCardEntity.setBrand(dictValueCodeMap.get(item.getModel()));

            oilCardEntity.setCreateUserId(0L);
            oilCardEntity.setCreateUserName("系统导入");
            insertList.add(oilCardEntity);
        }
        oilCardManager.saveBatch(insertList);
    }
}
