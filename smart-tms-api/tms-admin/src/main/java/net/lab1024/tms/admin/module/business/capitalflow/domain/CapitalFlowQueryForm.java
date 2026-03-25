package net.lab1024.tms.admin.module.business.capitalflow.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;

import java.time.LocalDate;

/**
 * 资金流水查询
 *
 * @author lidoudou
 * @date 2022/8/20 下午2:57
 */
@Data
public class CapitalFlowQueryForm extends PageParam {

    @ApiModelProperty("核销-开始时间")
    private LocalDate verificationStartTime;

    @ApiModelProperty("核销-结束时间")
    private LocalDate verificationEndTime;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelPropertyEnum(value = CheckStatusEnum.class, desc = "应收核算状态")
    private Integer receiveOrderExcludeCheckStatus;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class, desc = "付款单审核状态")
    private Integer payOrderExcludeAuditStatus;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
