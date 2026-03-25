package net.lab1024.tms.common.module.system.employee.domain;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2025/2/10 10:43 上午
 */
@Data
public class EmployeeBO {

    private Long employeeId;

    /**
     * 员工名称
     */
    private String actualName;


    private Long enterpriseId;

}