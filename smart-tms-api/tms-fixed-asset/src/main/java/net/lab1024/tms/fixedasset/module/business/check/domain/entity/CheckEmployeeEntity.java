package net.lab1024.tms.fixedasset.module.business.check.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidoudou
 * @date 2023/3/24 上午08:45
 */
@Data
@TableName("t_asset_check_employee")
public class CheckEmployeeEntity {

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
