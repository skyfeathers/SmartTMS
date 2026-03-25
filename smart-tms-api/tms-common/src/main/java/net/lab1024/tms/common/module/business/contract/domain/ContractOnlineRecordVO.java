package net.lab1024.tms.common.module.business.contract.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 线上合同操作记录
 *
 * @author lihaifan
 * @date 2022/9/17 18:07
 */
@Data
public class ContractOnlineRecordVO {

    /**
     * 记录ID
     */
    @ApiModelProperty("记录ID")
    private Long recordId;

    /**
     * 事件名称
     */
    @ApiModelProperty("事件名称")
    private String action;

    /**
     * 事件名称描述
     */
    @ApiModelProperty("事件名称描述")
    private String actionDesc;

    /**
     * 流程ID
     */
    @ApiModelProperty("流程ID")
    private String flowId;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private String requestBody;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
