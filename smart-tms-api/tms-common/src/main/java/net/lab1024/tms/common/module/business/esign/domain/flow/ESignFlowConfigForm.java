package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 本次签署流程的任务信息配置
 *
 * @author lihaifan
 * @date 2022/7/18 17:50
 */
@Data
public class ESignFlowConfigForm {

    /**
     * 认证配置项
     */
    private ESignFlowIdentificationConfigForm identificationConfig;

    /**
     * 通知配置项
     */
    private ESignFlowNotifyConfigForm notifyConfig;

    /**
     * 签署配置项
     */
    private ESignFlowSignConfigForm signConfig;
}
