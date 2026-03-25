package net.lab1024.tms.common.module.business.msg.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgTypeEnum;

import java.time.LocalDateTime;

/**
 * 通知消息 VO
 * 包含后管 前台用户
 *
 * @author: listen
 * @date: 2022/7/22 20:34
 */
@Data
public class MsgVO {

    private Long msgId;

    @ApiModelPropertyEnum(value = MsgTypeEnum.class)
    private Integer msgType;

    @ApiModelPropertyEnum(value = MsgSubTypeEnum.class)
    private Integer msgSubType;

    @ApiModelPropertyEnum(value = MsgReceiverTypeEnum.class)
    private Integer receiverType;

    @ApiModelProperty("接收者id")
    private Long receiverId;

    @ApiModelProperty("相关业务id")
    private String dataId;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("是否已读")
    private Boolean readFlag;

    @ApiModelProperty("已读时间")
    private LocalDateTime readTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}