package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.oilcard.dao.BalanceRecordDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardEnterpriseDao;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEnterpriseEntity;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/29 下午5:54
 */
@Service
public class OilCardClearService {
    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardEnterpriseDao oilCardEnterpriseDao;
    @Autowired
    private BalanceRecordDao balanceRecordDao;
    @Autowired
    private DataTracerClearService dataTracerClearService;

    public void clear(List<Long> notInEnterpriseIdList) {
        QueryWrapper qw = new QueryWrapper();
        qw.notIn("enterprise_id", notInEnterpriseIdList);
        List<OilCardEnterpriseEntity> oilCardEnterpriseEntityList = oilCardEnterpriseDao.selectList(qw);
        Set<Long> oilCardIdSet = oilCardEnterpriseEntityList.stream().map(OilCardEnterpriseEntity::getOilCardId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(oilCardIdSet)) {
            return;
        }

        QueryWrapper deleteQw = new QueryWrapper();
        deleteQw.in("oil_card_id", oilCardIdSet);
        balanceRecordDao.delete(deleteQw);
        oilCardDao.delete(deleteQw);
        oilCardEnterpriseDao.delete(deleteQw);
        // 删除操作记录
        dataTracerClearService.deleteByBusinessIdAndType(DataTracerBusinessTypeEnum.OIL_CARD, oilCardIdSet);
    }
}