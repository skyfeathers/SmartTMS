package net.lab1024.tms.common.module.support.reload.core.domain;

import lombok.Data;

/**
 * ReloadItem 类
 *
 * @author zhuoda
 */
@Data
public class SmartReloadItem {

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

}
