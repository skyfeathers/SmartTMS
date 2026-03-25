package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 查询员工销售情况
 *
 * @author lidoudou
 * @date 2023/3/7 上午11:13
 */
@Data
public class CustomerServiceDayStatisticQueryForm {

    @ApiModelProperty("开始日期")
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @ApiModelProperty("结束日期")
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @ApiModelProperty("客服ID列表")
    private List<Long> customerServiceIdList;

    @ApiModelProperty("部门ID | 可选")
    private Long departmentId;

    @ApiModelProperty(value = "员工ID列表", hidden = true)
    private List<Long> employeeIdList;

    @ApiModelPropertyEnum(value = WaybillStatusEnum.class, desc = "排除的运单状态", hidden = true)
    private Integer excludeWaybillStatus;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "排除的付款单状态", hidden = true)
    private Integer excludePayOrderStatus;


    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
