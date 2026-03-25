package net.lab1024.tms.admin.module.business.flow.define.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:23
 */
@Data
@TableName("t_flow")
public class FlowEntity {

    @TableId(type = IdType.AUTO)
    private Long flowId;

    /**
     * 流程类型
     * {@link FlowTypeEnum}
     */
    private Integer flowType;

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 流程配置
     * FlowHandlerConfigBO - 》 流程发起人配置对象
     */
    private String flowConfig;

    /**
     * 流程说明
     */
    private String flowDesc;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
