package net.lab1024.tms.common.module.support.dingding.callback.service;

import net.lab1024.tms.common.module.support.dingding.callback.domain.dto.DingDingEventBaseDTO;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2023/5/8 4:54 下午
 */
public interface IDingCallBackHandleService <T extends DingDingEventBaseDTO> {

    /**
     * 注册事件
     * @return
     */
    List<String> registerEvent();

    /**
     * 事件处理
     * @param enterpriseId
     * @param t
     */
    void handle(Long enterpriseId, T t);
}