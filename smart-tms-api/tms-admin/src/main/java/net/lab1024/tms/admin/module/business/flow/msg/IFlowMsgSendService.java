package net.lab1024.tms.admin.module.business.flow.msg;

import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;

import java.util.List;

/**
 * [ 消息发送 ]
 *
 * @author yandanyang
 * @date 2021/10/15 14:14
 */
public interface IFlowMsgSendService <T> {

    /**
     * 发送消息
     * @param employeeIdList
     * @param data
     * @param flowInstanceEntity
     */
    void sendMsg(List<Long> employeeIdList, FlowInstanceEntity flowInstanceEntity, T data);


}
