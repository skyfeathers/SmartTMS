package net.lab1024.tms.common.module.support.sms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.sms.constant.SmsTypeEnum;

import java.time.LocalDateTime;

/**
 * 短信记录 实体类
 *
 * @author Turbolisten
 * @date 2020/3/16 16:44
 */
@Data
@TableName("t_sms_record")
public class SmsRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 短信类型
     *
     * @see SmsTypeEnum
     */
    private Integer smsType;

    /**
     * 短信类型 说明
     */
    private String smsTypeDesc;

    /**
     * 子类型 验证码类型的 或 营销类的
     */
    private Integer smsSubType;

    /**
     * 子类说明
     */
    private String smsSubTypeDesc;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 额外数据
     */
    private String extraData;

    /**
     * 接收人手机号
     */
    private String phone;

    /**
     * 短信服务商的发送记录id
     */
    private String serviceRecordId;

    /**
     * 是否发送成功
     */
    private Boolean successFlag;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 错误信息
     */
    private String errorMsg;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
