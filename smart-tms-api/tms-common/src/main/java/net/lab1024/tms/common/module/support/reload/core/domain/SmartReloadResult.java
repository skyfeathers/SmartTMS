package net.lab1024.tms.common.module.support.reload.core.domain;

import lombok.Data;

/**
 * t_reload_result 表 实体类
 *
 * @author zhuoda
 */
@Data
public class SmartReloadResult {

    /**
     * 项名称
     */
    private String tag;

    /**
     * 参数
     */
    private String args;

    /**
     * 标识
     */
    private String identification;

    /**
     * 处理结果
     */
    private boolean result;

    /**
     * 异常说明
     */
    private String exception;


}
