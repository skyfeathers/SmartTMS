package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 易耗品
 *
 * @author lidoudou
 * @date 2023/4/11 下午4:52
 */
@Data
@TableName("t_consumables_stock")
public class ConsumablesStockEntity {

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 易耗品ID
     */
    private Long consumablesId;

    /**
     * 所属位置
     */
    private Long locationId;

    /**
     * 单价
     */
    private BigDecimal averagePrice;

    /**
     * 数量
     */
    private Integer stockCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
