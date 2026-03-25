package net.lab1024.tms.admin.module.business.flow.instance.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/16 16:48
 */
@Data
@TableName("t_flow_instance")
public class FlowInstanceEntity {

    @TableId(type = IdType.AUTO)
    private Long instanceId;

    private Long enterpriseId;

    /**
     * 审批流id
     */
    private Long flowId;

    /**
     * 流程类型
     * {@link FlowTypeEnum}
     */
    private Integer flowType;

    /**
     * 审核状态
     * {@link FlowAuditStatusEnum}
     */
    private Integer auditStatus;

    /**
     * 发起人id
     */
    private Long initiatorId;

    /**
     * 当前任务节点id
     */
    private Long currentTaskId;

    private String currentTaskName;
    /**
     * 当前任务处理人id
     */
    private String currentHandlers;
    /**
     * 当前任务处理人名称
     */
    private String currentHandlerNames;
    /**
     * 当前可进行的操作代码，可根据此字段判断进行什么样的操作
     */
    private String currentOperateCode;
    /**
     * 业务id
     */
    private Long businessId;

    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 业务数据
     */
    private String businessData;

    /**
     * 扩展字段
     */
    private String extendField1;
    /**
     * 扩展字段
     */
    private String extendFieldName1;
    /**
     * 扩展字段
     */
    private String extendField2;
    /**
     * 扩展字段
     */
    private String extendFieldName2;
    /**
     * 扩展字段
     */
    private String extendField3;
    /**
     * 扩展字段
     */
    private String extendFieldName3;
    /**
     * 扩展字段
     */
    private String extendField4;
    /**
     * 扩展字段
     */
    private String extendFieldName4;
    /**
     * 扩展字段
     */
    private String extendField5;
    /**
     * 扩展字段
     */
    private String extendFieldName5;

    /**
     * 扩展字段
     */
    private String extendField6;
    /**
     * 扩展字段
     */
    private String extendFieldName6;

    /**
     * 扩展字段
     */
    private String extendField7;
    /**
     * 扩展字段
     */
    private String extendFieldName7;

    /**
     * 扩展字段
     */
    private String extendField8;
    /**
     * 扩展字段
     */
    private String extendFieldName8;

    /**
     * 扩展字段
     */
    private String extendField9;
    /**
     * 扩展字段
     */
    private String extendFieldName9;

    /**
     * 扩展字段
     */
    private String extendField10;
    /**
     * 扩展字段
     */
    private String extendFieldName10;
    /**
     * 备注
     */
    private String remark;

    private LocalDateTime finishTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
