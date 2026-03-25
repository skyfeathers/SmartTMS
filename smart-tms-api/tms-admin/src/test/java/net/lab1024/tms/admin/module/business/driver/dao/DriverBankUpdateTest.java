package net.lab1024.tms.admin.module.business.driver.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.driver.service.DriverBankManager;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.driver.domain.DriverBankEntity;
import net.lab1024.tms.common.module.support.dict.DictCacheService;
import net.lab1024.tms.common.module.support.dict.domain.vo.DictValueVO;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DriverBankUpdateTest extends TmsAdminApplicationTest {

    @Autowired
    private DriverBankDao driverBankDao;
    @Autowired
    private DriverBankManager driverBankManager;
    @Autowired
    private DictCacheService dictCacheService;

    @Test
    void updateBankType() {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("bank_type", "");
        qw.ne("bank_branch_name", "");
        List<DriverBankEntity> driverBankList = driverBankDao.selectList(qw);
        if (CollectionUtils.isEmpty(driverBankList)) {
            return;
        }

        List<DictValueVO> dictValueList = dictCacheService.selectByKeyCode("BANK-TYPE");
        if (CollectionUtils.isEmpty(dictValueList)) {
            return;
        }
        Map<String, String> bankTypeNameMap = dictValueList.stream().collect(Collectors.toMap(DictValueVO::getValueName, DictValueVO::getValueCode));

        List<DriverBankEntity> updateBankList = Lists.newArrayList();
        driverBankList.forEach(item -> {
            String bankTypeCode = bankTypeNameMap.get(item.getBankName());
            if (SmartStringUtil.isNotBlank(bankTypeCode)) {
                DriverBankEntity updateEntity = new DriverBankEntity();
                updateEntity.setBankId(item.getBankId());
                updateEntity.setBankType(bankTypeCode);
                updateBankList.add(updateEntity);
            } else {
                System.err.println(String.format("%s    %s  ", item.getBankId(), item.getBankName()));
            }
        });
        if(CollectionUtils.isNotEmpty(updateBankList)){
            driverBankManager.updateBatchById(updateBankList);
        }
    }
}
