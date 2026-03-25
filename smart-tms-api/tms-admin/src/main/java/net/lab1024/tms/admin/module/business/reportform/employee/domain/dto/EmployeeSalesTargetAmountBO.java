package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工目标金额BO
 *
 * @author lihaifan
 * @date 2022/5/17 16:00
 */
@Data
public class EmployeeSalesTargetAmountBO {

    /**
     * 员工
     */
    private Long employeeId;

    /**
     * 目标
     */
    private BigDecimal target;
}
