package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 本次签署流程的任务信息配置 - 通知配置项
 *
 * @author lihaifan
 * @date 2022/9/16 10:08
 */
@Data
public class ESignFlowNotifyConfigForm {

    /**
     * 通知开发者地址。
     *
     * （e签宝服务器主动通过POST方式通知开发者指定服务器的页面路径（http/https））
     *
     */
    private String noticeDeveloperUrl;

    /**
     * 签署通知方式， 默认方式：1。
     *
     * 同时支持多种通知方式，用逗号分割。
     *
     * 1-短信，2-邮件。
     *
     * 注：短信或者邮件获取到的签署链接，
     *
     * 有效期默认30天；如果客户需要不通知，
     *
     * 可以设置noticeType=""
     */
    private String noticeType;
}
