package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 根据员工查询运单
 *
 * @author zhaoxinyang
 * @date 2023/12/11
 */
@Data
public class EmployeeCustomerServiceWaybillDTO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("所属公司ID")
    private Long enterpriseId;

    @ApiModelProperty("应收金额")
    @Excel(name = "业务应收", orderNum = "2", width = 20)
    private BigDecimal receiveAmount;

    @ApiModelProperty("应付金额")
    @Excel(name = "业务应付", orderNum = "3", width = 20)
    private BigDecimal payableAmount;

    @ApiModelProperty("客服ID")
    private Long customerServiceId;

}
