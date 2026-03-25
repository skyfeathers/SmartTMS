package net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领用明细
 *
 * @author lidoudou
 * @date 2023/4/14 上午9:44
 */
@Data
@TableName("t_consumables_requisition_item")
public class ConsumablesRequisitionItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 领用ID
     */
    private Long requisitionId;

    /**
     * 易耗品ID
     */
    private Long consumablesId;

    /**
     * 领取数量
     */
    private Integer count;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
