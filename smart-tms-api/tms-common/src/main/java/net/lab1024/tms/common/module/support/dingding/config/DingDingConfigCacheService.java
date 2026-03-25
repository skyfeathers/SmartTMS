package net.lab1024.tms.common.module.support.dingding.config;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.constant.ReloadConst;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import net.lab1024.tms.common.module.support.reload.core.annoation.SmartReload;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 钉钉配置缓存
 *
 * @author lidoudou
 * @date 2023/4/21 下午5:17
 */
@Slf4j
@Service
public class DingDingConfigCacheService {

    /**
     * 一个简单的系统配置缓存
     */
    private final ConcurrentHashMap<Long, DingDingConfigEntity> dingDingConfigCache = new ConcurrentHashMap<>();

    @Autowired
    private DingDingConfigDao dingDingConfigDao;

    @SmartReload(ReloadConst.ENTERPRISE_DING_DING_CONFIG_RELOAD)
    public void configReload() {
        this.loadConfigCache();
    }

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void loadConfigCache() {
        dingDingConfigCache.clear();
        List<DingDingConfigEntity> entityList = dingDingConfigDao.selectList(null);
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        entityList.forEach(entity -> this.dingDingConfigCache.put(entity.getEnterpriseId(), entity));
    }


    /**
     * 查询配置缓存
     *
     * @param enterpriseId
     * @return
     */
    public DingDingConfigEntity getConfig(Long enterpriseId) {
        DingDingConfigEntity entity = this.dingDingConfigCache.get(enterpriseId);
        return entity;
    }

}
