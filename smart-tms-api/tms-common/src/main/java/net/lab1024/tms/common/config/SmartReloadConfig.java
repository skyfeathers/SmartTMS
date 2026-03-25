package net.lab1024.tms.common.config;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.common.module.support.reload.ReloadCommand;
import net.lab1024.tms.common.module.support.reload.core.SmartReloadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [  ]
 *
 * @author zhuoda
 * @date 2021/9/1 21:40
 */
@Slf4j
@Configuration
public class SmartReloadConfig {

    /**
     * 间隔时间
     */
    @Value("${reload.interval-seconds}")
    private Integer intervalSeconds;

    @Autowired
    private ReloadCommand reloadCommand;

    @Bean
    public SmartReloadManager initSmartReloadManager() {
        // 创建 Reload Manager 调度器
        return new SmartReloadManager(reloadCommand,intervalSeconds);
    }
}
