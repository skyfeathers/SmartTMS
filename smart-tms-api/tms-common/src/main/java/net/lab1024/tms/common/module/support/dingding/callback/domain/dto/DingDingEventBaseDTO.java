package net.lab1024.tms.common.module.support.dingding.callback.domain.dto;

import lombok.Data;

/**
 * 钉钉事件订阅
 *
 * @author lidoudou
 * @date 2023/4/22 上午11:10
 */
@Data
public class DingDingEventBaseDTO {

    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 操作人的userId
     */
    private String optStaffId;

    /**
     * 时间戳
     */
    private String timeStamp;
}
