package net.lab1024.tms.admin.module.business.oilcard.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.oilcard.dao.OilCardDao;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEnterpriseEntity;
import net.lab1024.tms.common.module.business.oilcard.domain.OilCardEntity;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OilCardManager extends ServiceImpl<OilCardDao, OilCardEntity> {
    @Autowired
    private OilCardDao oilCardDao;
    @Autowired
    private OilCardDataTracerService oilCardDataTracerService;

    @Transactional(rollbackFor = Throwable.class)
    public void addOilCard(OilCardEntity oilCardEntity) {
        oilCardDao.insert(oilCardEntity);
    }

    /**
     * 油卡更新
     *
     * @param oilCardEntity
     * @param dataTracerRequestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateOilCard(OilCardEntity oilCardEntity, Boolean disabledFlagChange, DataTracerRequestForm dataTracerRequestForm) {
        oilCardDao.updateById(oilCardEntity);

        if (OilCardTypeEnum.SLAVE.equalsValue(oilCardEntity.getType())) {
            return;
        }
        if (disabledFlagChange) {
            return;
        }
        oilCardDao.updateDisabledFlagByMasterOilId(oilCardEntity.getOilCardId(), Boolean.TRUE, Boolean.FALSE);
        oilCardDataTracerService.batchDisabledFlagLog(oilCardEntity.getOilCardId(), dataTracerRequestForm);
    }

    /**
     * 构建油卡公司列表
     *
     * @param oilCardId
     * @param enterpriseIdList
     * @return
     */
    private List<OilCardEnterpriseEntity> buildOilCardEnterprise(Long oilCardId, List<Long> enterpriseIdList) {
        List<OilCardEnterpriseEntity> oilCardEnterpriseList = enterpriseIdList.stream().map(e -> {
            OilCardEnterpriseEntity oilCardEnterpriseEntity = new OilCardEnterpriseEntity();
            oilCardEnterpriseEntity.setOilCardId(oilCardId);
            oilCardEnterpriseEntity.setEnterpriseId(e);
            return oilCardEnterpriseEntity;
        }).collect(Collectors.toList());
        return oilCardEnterpriseList;
    }
}
