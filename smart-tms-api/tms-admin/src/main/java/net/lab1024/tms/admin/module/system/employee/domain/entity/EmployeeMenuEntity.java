package net.lab1024.tms.admin.module.system.employee.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author yandy
 */
@Data
@TableName("t_employee_menu")
public class EmployeeMenuEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long employeeMenuId;

    /**
     * 员工 id
     */
    private Long employeeId;

    /**
     * 菜单 id
     */
    private Long menuId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
