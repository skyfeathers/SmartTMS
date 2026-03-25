package net.lab1024.tms.fixedasset.module.business.purchase.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidoudou
 * @date 2023/3/18 下午4:58
 */
@Data
@TableName("t_asset_purchase_item")
public class PurchaseItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 采购ID
     */
    private Long purchaseId;

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
