package net.lab1024.tms.common.module.business.msg.domain;

import lombok.Data;
import net.lab1024.tms.common.module.business.msg.constant.MsgReceiverTypeEnum;
import net.lab1024.tms.common.module.business.msg.constant.MsgSubTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 通知消息 发送
 *
 * @author: listen
 * @date: 2022/7/22 21:00
 */
@Data
public class MsgSendDTO {

    /**
     * 消息子类型
     *
     * @see MsgSubTypeEnum
     */
    @NotNull(message = "消息子类型不能为空")
    private MsgSubTypeEnum msgSubType;

    /**
     * 接收者类型
     *
     * @see MsgReceiverTypeEnum
     */
    @NotNull(message = "接收者类型不能为空")
    private MsgReceiverTypeEnum receiverType;

    /**
     * 接收者id
     */
    @NotNull(message = "接收者id不能为空")
    private Long receiverId;

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;

    /**
     * 相关业务id | 可选
     * 用于跳转具体业务
     */
    private Object dataId;

    /**
     * 消息参数 | 可选
     * 例：订单号：【{orderId}】{time}所提交的对账单被作废，请核实信息重新提交~
     * {orderId} {time} 就是消息的参数变量
     * 发送消息时 需要在map中放入k->orderId k->time
     */
    private Map<String, Object> contentParam;
}
