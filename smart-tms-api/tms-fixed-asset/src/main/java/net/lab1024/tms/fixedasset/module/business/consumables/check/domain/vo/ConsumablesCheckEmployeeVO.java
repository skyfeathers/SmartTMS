package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo;

import lombok.Data;


/**
 * 员工列表
 *
 * @author lidoudou
 * @date 2023/3/27 上午9:31
 */
@Data
public class ConsumablesCheckEmployeeVO {
    /**
     * 员工
     */
    private Long employeeId;

    /**
     * 员工信命
     */
    private String actualName;
}
