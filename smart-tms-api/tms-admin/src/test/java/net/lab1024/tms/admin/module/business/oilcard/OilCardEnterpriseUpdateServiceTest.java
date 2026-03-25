package net.lab1024.tms.admin.module.business.oilcard;

import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardEnterpriseManager;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class OilCardEnterpriseUpdateServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardEnterpriseManager oilCardEnterpriseManager;


    @Test
    void updateEnterprise() {
        List<OilCardEntity> oilCardEntityList = oilCardDao.selectList(null);
        if (CollectionUtils.isEmpty(oilCardEntityList)) {
            return;
        }
        List<OilCardEnterpriseEntity> oilCardEnterpriseEntityList = oilCardEntityList.stream().map(oilCard -> {
            OilCardEnterpriseEntity oilCardEnterpriseEntity = new OilCardEnterpriseEntity();
            oilCardEnterpriseEntity.setOilCardId(oilCard.getOilCardId());
//            oilCardEnterpriseEntity.setEnterpriseId(oilCard.getEnterpriseId());
            return oilCardEnterpriseEntity;
        }).collect(Collectors.toList());
        oilCardEnterpriseManager.saveBatch(oilCardEnterpriseEntityList);
    }
}
