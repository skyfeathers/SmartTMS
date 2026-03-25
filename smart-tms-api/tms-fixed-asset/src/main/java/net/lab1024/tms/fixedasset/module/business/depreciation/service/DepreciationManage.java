package net.lab1024.tms.fixedasset.module.business.depreciation.service;

import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.fixedasset.module.business.depreciation.dao.DepreciationDao;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity.DepreciationEntity;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity.DepreciationItemEntity;
import net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo.DepreciationItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 折旧
 *
 * @author lidoudou
 * @date 2023/4/11 上午8:59
 */
@Service
public class DepreciationManage {

    @Autowired
    private DepreciationDao depreciationDao;
    @Autowired
    private DepreciationItemManager depreciationItemManager;

    @Transactional(rollbackFor = Throwable.class)
    public void save(DepreciationEntity depreciationEntity, List<DepreciationItemVO> assetList) {
        depreciationDao.insert(depreciationEntity);
        List<DepreciationItemEntity> depreciationItemEntityList = assetList.stream().map(asset -> {
            DepreciationItemEntity depreciationItemEntity = SmartBeanUtil.copy(asset, DepreciationItemEntity.class);
            depreciationItemEntity.setDepreciationId(depreciationEntity.getDepreciationId());
            return depreciationItemEntity;
        }).collect(Collectors.toList());
        depreciationItemManager.saveBatch(depreciationItemEntityList);
    }
}