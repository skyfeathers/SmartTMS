package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 根据货主ID查询
 *
 * @author lidoudou
 * @date 2022/8/9 下午3:17
 */
@Data
public class ShipperReceiveQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("账期-开始时间")
    private LocalDate accountPeriodStartTime;

    @ApiModelProperty("账期-结束时间")
    private LocalDate accountPeriodEndTime;

    @ApiModelPropertyEnum(value = CheckStatusEnum.class, desc = "核算状态")
    private Integer checkStatus;

    @ApiModelProperty("是否开票")
    private Integer invoiceStatus;

    @ApiModelProperty("货主ID")
    @NotNull(message = "货主不能为空")
    private Long shipperId;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty("订单所属公司")
    private Long enterpriseId;
}
