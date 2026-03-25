package net.lab1024.tms.fixedasset.module.business.depreciation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 折旧资产表
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:52
 */
@Data
@TableName("t_asset_depreciation_item")
public class DepreciationItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 折旧表ID
     */
    private Long depreciationId;

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 购买价格
     */
    private BigDecimal price;

    /**
     * 期初、累计折旧金额
     */
    private BigDecimal initialDepreciationAmount;

    /**
     * 本次折旧金额
     */
    private BigDecimal depreciationAmount;

    /**
     * 月折旧率
     */
    private BigDecimal monthResidualValueRate;

    /**
     * 期末折旧金额
     */
    private BigDecimal endingDepreciationAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
