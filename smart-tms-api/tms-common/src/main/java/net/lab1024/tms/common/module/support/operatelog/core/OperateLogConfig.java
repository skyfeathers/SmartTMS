package net.lab1024.tms.common.module.support.operatelog.core;

import lombok.Builder;
import lombok.Data;
import net.lab1024.tms.common.module.support.operatelog.domain.OperateLogEntity;

import java.util.function.Function;

/**
* @Description:    配置
* @Author:         卓大
* @CreateDate:     2021/8/3 21:54
* @Version:        1.0
*/
@Data
@Builder
public class OperateLogConfig {

    /**
     * 操作日志存储方法
     */
    private Function<OperateLogEntity, Boolean> saveFunction;

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 队列大小
     */
    private Integer queueCapacity;


}
