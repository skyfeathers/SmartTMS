package net.lab1024.tms.admin.module.business.flow.instance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowInstanceQueryTypeEnum;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 15:14
 */
@Data
public class FlowInstanceQueryForm extends PageParam {

    @ApiModelPropertyEnum(value = FlowInstanceQueryTypeEnum.class,desc = "查询类型")
    @CheckEnum(value = FlowInstanceQueryTypeEnum.class,required = true,message = "查询方式错误")
    private Integer queryType;

    @ApiModelProperty("查询关键字")
    private String searchWord;

    @ApiModelPropertyEnum(value = FlowTypeEnum.class,desc = "审批类型")
    private Integer flowType;

    @ApiModelProperty("审批状态列表")
    private List<Integer> auditStatusList;

    @ApiModelProperty("开始日期")
    private LocalDate submitStartDate;

    @ApiModelProperty("结束日期")
    private LocalDate submitEndDate;

    @ApiModelProperty("开始日期")
    private LocalDate finishStartDate;

    @ApiModelProperty("结束日期")
    private LocalDate finishEndDate;

    @ApiModelProperty(value = "当前查询人id",hidden = true)
    private Long handlerId;

    @ApiModelProperty(value = "发起人id",hidden = true)
    private Long initiatorId;

    @ApiModelProperty(value = "企业id",hidden = true)
    private Long enterpriseId;
}
