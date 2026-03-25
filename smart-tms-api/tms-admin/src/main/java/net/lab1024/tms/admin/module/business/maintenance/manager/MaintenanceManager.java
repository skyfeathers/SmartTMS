package net.lab1024.tms.admin.module.business.maintenance.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.maintenance.dao.MaintenanceContentDao;
import net.lab1024.tms.admin.module.business.maintenance.dao.MaintenanceDao;
import net.lab1024.tms.admin.module.business.maintenance.domain.MaintenanceContentForm;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceContentEntity;
import net.lab1024.tms.common.module.business.maintenance.MaintenanceEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MaintenanceManager extends ServiceImpl<MaintenanceDao, MaintenanceEntity> {

    @Resource
    private MaintenanceDao maintenanceDao;

    @Resource
    private MaintenanceContentDao maintenanceContentDao;

    @Resource
    private MaintenanceContentManager maintenanceContentManager;

    /**
     * 添加保养记录
     *
     * @param maintenanceEntity
     * @param contentFormList
     */
    public void addHandle(MaintenanceEntity maintenanceEntity, List<MaintenanceContentForm> contentFormList) {
        maintenanceDao.insert(maintenanceEntity);

        List<MaintenanceContentEntity> contentEntityList = this.getContentEntityList(maintenanceEntity.getMaintenanceId(), contentFormList);
        maintenanceContentManager.saveBatch(contentEntityList);
    }

    /**
     * 修改保养记录
     *
     * @param updateEntity
     * @param contentFormList
     */
    public void updateHandel(MaintenanceEntity updateEntity, List<MaintenanceContentForm> contentFormList) {
        maintenanceDao.updateById(updateEntity);

        Long maintenanceId = updateEntity.getMaintenanceId();
        maintenanceContentDao.deleteByMaintenanceId(maintenanceId);

        List<MaintenanceContentEntity> contentEntityList = this.getContentEntityList(maintenanceId, contentFormList);
        maintenanceContentManager.saveBatch(contentEntityList);
    }

    /**
     * 获取内容Entity
     *
     * @param maintenanceId
     * @param contentFormList
     * @return
     */
    private List<MaintenanceContentEntity> getContentEntityList(Long maintenanceId, List<MaintenanceContentForm> contentFormList) {
        if (CollectionUtil.isEmpty(contentFormList)) {
            return Lists.newArrayList();
        }

        List<MaintenanceContentEntity> contentEntityList = SmartBeanUtil.copyList(contentFormList, MaintenanceContentEntity.class);
        contentEntityList.stream().forEach(e -> e.setMaintenanceId(maintenanceId));
        return contentEntityList;
    }
}
