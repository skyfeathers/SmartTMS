package net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2025/2/13 12:33 下午
 */
@Data
public class EnterpriseEmployeeVO {
    /**
     * 企业ID
     */
    private Long enterpriseId;
    /**
     * 企业名称
     */
    private String enterpriseName;
    /**
     * 员工
     */
    private Long employeeId;

    /**
     * 员工
     */
    private String actualName;
}
