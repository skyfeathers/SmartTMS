package net.lab1024.tms.admin.module.business.oilcard;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.admin.module.business.oilcard.service.OilCardEnterpriseManager;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据客户提供的内容，共用油卡副卡
 * <p>
 * <p>
 * 油卡独立企业：
 * 河南城达通供应链管理有限责任公司
 * 河南省顺瑞物流有限公司
 * <p>
 * 油卡共用企业
 * 平顶山市豫鑫
 * 君安达
 * 金乐源
 * 安顺达
 * 瑞云
 * 三合A、B
 * 三合国际
 *
 * @author lidoudou
 * @date 2022/11/9 下午2:39
 */
public class SlaveOilCardEnterpriseUpdateServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardEnterpriseManager oilCardEnterpriseManager;


    @Test
    void updateEnterprise() {
        List<Long> excludeEnterpriseIdList = Lists.newArrayList(
                1L,// 河南省顺瑞物流有限公司
                9L // 河南城达通供应链管理有限责任公司
        );
        List<OilCardEntity> oilCardList = oilCardDao.selectByTypeAndEnterprise(OilCardTypeEnum.SLAVE.getValue(), excludeEnterpriseIdList, Boolean.FALSE);
        if (CollectionUtils.isEmpty(oilCardList)) {
            return;
        }
        List<Long> oilCardIdList = oilCardList.stream().map(OilCardEntity::getOilCardId).collect(Collectors.toList());
        oilCardEnterpriseManager.getBaseMapper().deleteByOilCardIdList(oilCardIdList);

        List<Long> enterpriseIdList = Lists.newArrayList(
                2L,
                3L,
                4L,
                5L,
                6L,
                7L,
                8L,
                10L
        );

        List<OilCardEnterpriseEntity> insertList = Lists.newArrayList();
        for (Long oilCardId : oilCardIdList) {
            for (Long enterpriseId : enterpriseIdList) {
                OilCardEnterpriseEntity insertEntity = new OilCardEnterpriseEntity();
                insertEntity.setEnterpriseId(enterpriseId);
                insertEntity.setOilCardId(oilCardId);
                insertList.add(insertEntity);
            }
        }
        oilCardEnterpriseManager.saveBatch(insertList);
    }
}
