package net.lab1024.tms.fixedasset.module.business.check.domain.vo;

import lombok.Data;


/**
 * 员工列表
 *
 * @author lidoudou
 * @date 2023/3/27 上午9:31
 */
@Data
public class CheckEmployeeVO {
    /**
     * 员工
     */
    private Long employeeId;

    /**
     * 员工
     */
    private String actualName;
}
