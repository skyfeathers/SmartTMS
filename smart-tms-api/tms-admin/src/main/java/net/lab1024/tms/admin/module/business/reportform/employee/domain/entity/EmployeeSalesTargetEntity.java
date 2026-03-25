package net.lab1024.tms.admin.module.business.reportform.employee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 员工 业绩目标 实体类
 *
 * @author Turbolisten
 * @date 2021/8/13 17:55
 */
@Data
@TableName("t_employee_sales_target")
public class EmployeeSalesTargetEntity {

    @TableId(type = IdType.AUTO)
    private Long targetId;

    /**
     * 员工id
     */
    private Long employeeId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 目标
     */
    private BigDecimal target;

    private String remark;

    private String updateName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
