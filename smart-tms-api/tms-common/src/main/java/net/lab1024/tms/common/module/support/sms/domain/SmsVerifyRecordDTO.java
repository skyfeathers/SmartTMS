package net.lab1024.tms.common.module.support.sms.domain;

import lombok.Data;
import net.lab1024.tms.common.module.support.sms.constant.SmsTypeEnum;

/**
 * 短信发送记录DTO
 *
 * @author listen
 * @date 2019/09/27 08:43
 */
@Data
public class SmsVerifyRecordDTO {

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 发送时间 秒
     */
    private Long sendSecond;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 短信类型
     *
     * @see SmsTypeEnum
     */
    private Integer smsType;

    /**
     * 是否已使用
     */
    private Boolean useFlag;

}




