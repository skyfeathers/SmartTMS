package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盘点耗材
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:05
 */
@Data
@TableName("t_consumables_check_item")
public class ConsumablesCheckItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 盘点单ID
     */
    private Long checkId;

    /**
     * 易耗品ID
     */
    private Long consumablesId;

    /**
     * 盘点人
     */
    private Long employeeId;

    /**
     * 盘点状态
     */
    private Integer status;

    /**
     * 盘点数量
     */
    private Integer count;

    private String remark;

    /**
     * 盘点时间
     */
    private LocalDateTime checkTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
