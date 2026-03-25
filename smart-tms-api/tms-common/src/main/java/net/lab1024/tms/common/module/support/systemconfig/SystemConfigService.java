package net.lab1024.tms.common.module.support.systemconfig;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.constant.ReloadConst;
import net.lab1024.tms.common.module.support.reload.core.annoation.SmartReload;
import net.lab1024.tms.common.module.support.systemconfig.domain.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置业务类
 *
 * @author huke
 * @date 2021年9月23日 20:49:13
 */
@Slf4j
@Service
public class SystemConfigService {

    /**
     * 一个简单的系统配置缓存
     */
    private final ConcurrentHashMap<String, SystemConfigEntity> configCache = new ConcurrentHashMap<>();

    @Autowired
    private SystemConfigDao systemConfigDao;

    @SmartReload(ReloadConst.SYSTEM_CONFIG_RELOAD)
    public void configReload(String param) {
        this.loadConfigCache();
    }

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void loadConfigCache() {
        configCache.clear();
        List<SystemConfigEntity> entityList = systemConfigDao.selectList(null);
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        entityList.forEach(entity -> this.configCache.put(entity.getConfigKey().toLowerCase(), entity));
        log.info("################# 系统配置缓存初始化完毕:{} ###################", configCache.size());
    }

    /**
     * 刷新系统设置缓存
     */
    private void refreshConfigCache(Long configId) {
        // 重新查询 加入缓存
        SystemConfigEntity configEntity = systemConfigDao.selectById(configId);
        if (null == configEntity) {
            return;
        }
        this.configCache.put(configEntity.getConfigKey().toLowerCase(), configEntity);
    }

    /**
     * 分页查询系统配置
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<SystemConfigVO>> queryConfigPage(SystemConfigQueryForm queryDTO) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<SystemConfigEntity> entityList = systemConfigDao.queryByPage(page, queryDTO);
        PageResult<SystemConfigVO> pageResult = SmartPageUtil.convert2PageResult(page, entityList, SystemConfigVO.class);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询配置缓存
     *
     * @param configKey
     * @return
     */
    public SystemConfigVO getConfig(SystemConfigKeyEnum configKey) {
        return this.getConfig(configKey.getValue());
    }

    /**
     * 查询配置缓存
     *
     * @param configKey
     * @return
     */
    public SystemConfigVO getConfig(String configKey) {
//        boolean check = SmartBaseEnumUtil.checkEnum(configKey, SystemConfigKeyEnum.class);
//        Assert.isTrue(check, "config key error");
//
        SystemConfigEntity entity = this.configCache.get(configKey);
        Assert.notNull(entity, "缺少系统配置[" + configKey + "]");

        return SmartBeanUtil.copy(entity, SystemConfigVO.class);
    }

    /**
     * 查询配置缓存参数
     *
     * @param configKey
     * @return
     */
    public String getConfigValue(SystemConfigKeyEnum configKey) {
        return this.getConfig(configKey).getConfigValue();
    }

    /**
     * 根据参数key查询 并转换为对象
     *
     * @param configKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getConfigValue2Obj(SystemConfigKeyEnum configKey, Class<T> clazz) {
        String configValue = this.getConfigValue(configKey);
        return JSON.parseObject(configValue, clazz);
    }

    /**
     * 添加系统配置
     *
     * @param configAddDTO
     * @return
     */
    public ResponseDTO<String> add(SystemConfigAddForm configAddDTO) {
        SystemConfigEntity entity = systemConfigDao.selectByKey(configAddDTO.getConfigKey());
        if (null != entity) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        entity = SmartBeanUtil.copy(configAddDTO, SystemConfigEntity.class);
        systemConfigDao.insert(entity);

        // 刷新缓存
        this.refreshConfigCache(entity.getSystemConfigId());
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateSystemConfig(SystemConfigUpdateForm updateDTO) {
        Long configId = updateDTO.getSystemConfigId();
        SystemConfigEntity entity = systemConfigDao.selectById(configId);
        if (null == entity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        SystemConfigEntity alreadyEntity = systemConfigDao.selectByKey(updateDTO.getConfigKey());
        if (null != alreadyEntity && !Objects.equals(configId, alreadyEntity.getSystemConfigId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "config key 已存在");
        }

        // 更新数据
        entity = SmartBeanUtil.copy(updateDTO, SystemConfigEntity.class);
        systemConfigDao.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     *
     * @param key
     * @param value
     * @return
     */
    public ResponseDTO<String> updateValueByKey(SystemConfigKeyEnum key, String value) {
        SystemConfigVO config = this.getConfig(key);
        if (null == config) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 更新数据
        Long configId = config.getSystemConfigId();
        SystemConfigEntity entity = new SystemConfigEntity();
        entity.setSystemConfigId(configId);
        entity.setConfigValue(value);
        systemConfigDao.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }
}
