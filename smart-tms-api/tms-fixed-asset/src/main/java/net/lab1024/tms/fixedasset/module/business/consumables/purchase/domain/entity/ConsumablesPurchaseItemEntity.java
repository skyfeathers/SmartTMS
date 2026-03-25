package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购子表
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:09
 */
@Data
@TableName("t_consumables_purchase_item")
public class ConsumablesPurchaseItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 采购ID
     */
    private Long purchaseId;

    /**
     * 资产ID
     */
    private Long consumablesId;

    /**
     * 采购价格
     */
    private BigDecimal price;

    /**
     * 采购数量
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
