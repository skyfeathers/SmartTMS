package net.lab1024.tms.admin.module.business.repair.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.repair.dao.RepairContentDao;
import net.lab1024.tms.admin.module.business.repair.dao.RepairDao;
import net.lab1024.tms.admin.module.business.repair.domain.RepairContentForm;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.repair.entity.RepairContentEntity;
import net.lab1024.tms.common.module.business.repair.entity.RepairEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RepairManager extends ServiceImpl<RepairDao, RepairEntity> {

    @Resource
    private RepairDao repairDao;

    @Resource
    private RepairContentDao repairContentDao;

    @Resource
    private RepairContentManager repairContentManager;

    /**
     * 新建维修信息
     *
     * @param repairEntity
     * @param contentFormList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleAdd(RepairEntity repairEntity, List<RepairContentForm> contentFormList) {
        repairDao.insert(repairEntity);

        Long repairId = repairEntity.getRepairId();
        List<RepairContentEntity> contentEntityList = this.getContentEntityList(repairId, contentFormList);
        repairContentManager.saveBatch(contentEntityList);
    }

    /**
     * 修改维修信息
     *
     * @param afterRepairEntity
     * @param contentFormList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdate(RepairEntity afterRepairEntity, List<RepairContentForm> contentFormList) {
        Long repairId = afterRepairEntity.getRepairId();
        List<RepairContentEntity> afterContentEntityList = this.getContentEntityList(repairId, contentFormList);
        repairDao.updateById(afterRepairEntity);
        repairContentDao.deleteByRepairId(repairId);
        repairContentManager.saveBatch(afterContentEntityList);
    }

    /**
     * 获取维修内容表 Entity
     *
     * @param repairId
     * @param contentFormList
     * @return
     */
    private List<RepairContentEntity> getContentEntityList(Long repairId, List<RepairContentForm> contentFormList) {
        if (CollectionUtil.isEmpty(contentFormList)) {
            return Lists.newArrayList();
        }

        List<RepairContentEntity> contentEntityList = SmartBeanUtil.copyList(contentFormList, RepairContentEntity.class);
        contentEntityList.stream().forEach(e -> e.setRepairId(repairId));
        return contentEntityList;
    }

}
