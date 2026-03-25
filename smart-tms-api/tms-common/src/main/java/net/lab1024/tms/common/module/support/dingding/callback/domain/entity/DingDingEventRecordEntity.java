package net.lab1024.tms.common.module.support.dingding.callback.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 钉钉事件订阅记录
 *
 * @author lidoudou
 * @date 2023/4/21 下午2:51
 */
@Data
@TableName("t_ding_ding_event_record")
public class DingDingEventRecordEntity {

    /**
     * 订阅ID
     */
    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 签名
     */
    private String msgSignature;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonce;

    /**
     * 请求内容
     */
    private String requestBody;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
