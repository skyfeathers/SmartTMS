package net.lab1024.tms.admin.module.business.flow.listener;

import lombok.Data;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/3/16 2:20 下午
 */
@Data
public class TaskAuditListenerBO {

    /**
     * 处理人
     */
    private List<Long> handlerIdIdList;
    /**
     * 任务监听器
     */
    private String taskListener;
    /**
     * 业务信息
     */
    private Long businessId;

    private String businessCode;

    /**
     * 请求参数
     */
    private String extData;
}