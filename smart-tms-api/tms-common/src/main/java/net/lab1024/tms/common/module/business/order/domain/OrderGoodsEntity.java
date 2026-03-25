package net.lab1024.tms.common.module.business.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单货物信息
 *
 * @author lidoudou
 * @date 2022/7/12 上午10:03
 */
@Data
@TableName("t_order_goods")
public class OrderGoodsEntity {

    @TableId(type = IdType.AUTO)
    private Long orderGoodsId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 货物类型
     */
    private String goodsType;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 毛重
     */
    private BigDecimal goodsQuantity;


    /**
     * 单位
     * @see GoodsUnitTypeEnum
     */
    private Integer goodsUnit;


    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
