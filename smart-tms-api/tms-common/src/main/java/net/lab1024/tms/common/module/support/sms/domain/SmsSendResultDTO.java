package net.lab1024.tms.common.module.support.sms.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短信发送结果 实体类
 *
 * @author Turbolisten
 * @date 2020/3/16 16:44
 */
@Data
public class SmsSendResultDTO {

    /**
     * 短信内容
     */
    private String content;

    /**
     * 接收人手机号
     */
    private String phone;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 是否发送成功
     */
    private Boolean successFlag;

    /**
     * 错误信息
     */
    private String errorMsg;

}
