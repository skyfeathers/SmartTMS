package net.lab1024.tms.common.module.support.reload;

import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.support.reload.core.AbstractSmartReloadCommand;
import net.lab1024.tms.common.module.support.reload.core.domain.SmartReloadItem;
import net.lab1024.tms.common.module.support.reload.core.domain.SmartReloadResult;
import net.lab1024.tms.common.module.support.reload.dao.ReloadItemDao;
import net.lab1024.tms.common.module.support.reload.dao.ReloadResultDao;
import net.lab1024.tms.common.module.support.reload.domain.ReloadItemEntity;
import net.lab1024.tms.common.module.support.reload.domain.ReloadResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Smart Reload 业务
 *
 * @author 胡克
 * @date 2018/02/10 09:18
 */
@Component
public class ReloadCommand extends AbstractSmartReloadCommand {

    @Autowired
    private ReloadItemDao reloadItemDao;

    @Autowired
    private ReloadResultDao reloadResultDao;

    /**
     * 读取数据库中SmartReload项
     *
     * @return List<ReloadItem>
     */
    @Override
    public List<SmartReloadItem> readReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemDao.selectList(null);
        return SmartBeanUtil.copyList(reloadItemEntityList, SmartReloadItem.class);
    }


    /**
     * 保存reload结果
     *
     * @param smartReloadResult
     */
    @Override
    public void handleReloadResult(SmartReloadResult smartReloadResult) {
        ReloadResultEntity reloadResultEntity = SmartBeanUtil.copy(smartReloadResult, ReloadResultEntity.class);
        reloadResultDao.insert(reloadResultEntity);
    }
}
