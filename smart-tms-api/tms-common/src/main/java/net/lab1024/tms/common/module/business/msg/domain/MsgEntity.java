package net.lab1024.tms.common.module.business.msg.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgTypeEnum;

import java.time.LocalDateTime;

/**
 * 通知消息 实体类
 * 包含后管 前台用户
 *
 * @author: listen
 * @date: 2022/7/22 20:34
 */
@Data
@TableName("t_msg")
public class MsgEntity {

    @TableId(type = IdType.AUTO)
    private Long msgId;

    private Long enterpriseId;

    /**
     * 消息类型
     *
     * @see MsgTypeEnum
     */
    private Integer msgType;

    /**
     * 消息子类型
     *
     * @see MsgSubTypeEnum
     */
    private Integer msgSubType;

    /**
     * 接收者类型
     *
     * @see MsgReceiverTypeEnum
     */
    private Integer receiverType;

    /**
     * 接收者id
     */
    private Long receiverId;

    /**
     * 相关业务id
     */
    private String dataId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Boolean readFlag;

    /**
     * 已读时间
     */
    private LocalDateTime readTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
