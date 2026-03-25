package net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存记录
 *
 * @author lidoudou
 * @date 2023/4/18 下午5:01
 */
@Data
@TableName("t_consumables_stock_record")
public class ConsumablesStockRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 耗材ID
     */
    private Long consumablesId;

    /**
     * 所属位置
     */
    private Long locationId;

    /**
     * 变动类型
     */
    private Integer recordType;

    /**
     * 单据ID
     */
    private Long orderId;

    /**
     * 单据编号
     */
    private String orderNo;

    /**
     * 变动前库存数量
     */
    private Integer beforeCount;

    /**
     * 变动数量
     */
    private Integer changeCount;

    /**
     * 变动后数量
     */
    private Integer afterCount;

    /**
     * 变动前平均采购价
     */
    private BigDecimal beforeAveragePrice;

    /**
     * 本次采购价
     */
    private BigDecimal price;

    /**
     * 变动后采购价格
     */
    private BigDecimal afterAveragePrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
