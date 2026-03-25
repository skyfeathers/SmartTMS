package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 易耗品盘点人
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:02
 */
@Data
@TableName("t_consumables_check_employee")
public class ConsumablesCheckEmployeeEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 盘点单ID
     */
    private Long checkId;

    /**
     * 盘点人
     */
    private Long employeeId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
