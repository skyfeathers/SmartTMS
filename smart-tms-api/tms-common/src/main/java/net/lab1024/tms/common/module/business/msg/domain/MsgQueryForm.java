package net.lab1024.tms.common.module.business.msg.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgTypeEnum;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 通知消息 分页查询
 * 包含后管 前台用户
 *
 * @author: listen
 * @date: 2022/7/24 20:34
 */
@Data
public class MsgQueryForm extends PageParam {

    @ApiModelProperty("搜索词")
    @Length(max = 50, message = "搜索词最多50字符")
    private String searchWord;

    @ApiModelPropertyEnum(value = MsgTypeEnum.class)
    @CheckEnum(value = MsgTypeEnum.class, message = "消息类型错误")
    private Integer msgType;

    @ApiModelPropertyEnum(value = MsgSubTypeEnum.class)
    @CheckEnum(value = MsgSubTypeEnum.class, message = "消息子类型错误")
    private Integer msgSubType;

    @ApiModelPropertyEnum(value = MsgReceiverTypeEnum.class, hidden = true)
    @CheckEnum(value = MsgReceiverTypeEnum.class, message = "接收者类型错误")
    private Integer receiverType;

    @ApiModelProperty(value = "接收者id", hidden = true)
    private Long receiverId;

    @ApiModelProperty("相关业务id")
    private String dataId;

    @ApiModelProperty("是否已读")
    private Boolean readFlag;

    @ApiModelProperty("查询开始时间")
    private LocalDate startDate;

    @ApiModelProperty("查询结束时间")
    private LocalDate endDate;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
