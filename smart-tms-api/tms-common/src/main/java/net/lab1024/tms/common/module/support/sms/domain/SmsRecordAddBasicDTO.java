package net.lab1024.tms.common.module.support.sms.domain;

import lombok.Data;
import net.lab1024.tms.common.module.support.sms.constant.SmsTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 新增 短信记录 实体类
 *
 * @author Turbolisten
 * @date 2020/3/16 16:44
 */
@Data
public class SmsRecordAddBasicDTO {

    /**
     * 短信类型
     *
     * @see SmsTypeEnum
     */
    @NotNull(message = "短信类型不能为空")
    private SmsTypeEnum smsType;

    /**
     * 子类型
     */
    @NotNull(message = "短信子类型不能为空")
    private Integer smsSubType;

    /**
     * 短信内容
     */
    @Length(max = 250, message = "短信内容最多250字符")
    private String content;

    /**
     * 额外数据
     */
    @Length(max = 250, message = "额外数据最多250字符")
    private String extraData;
    /**
     * 短信服务商的发送记录id
     */
    private String serviceRecordId;

    /**
     * 是否发送成功
     */
    @NotNull(message = "是否发送成功不能为空")
    private Boolean successFlag;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 错误信息
     */
    @Length(max = 250, message = "错误信息最多250字符")
    private String errorMsg;
}
