package net.lab1024.tms.common.module.business.waybill.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @Date 2022-08-13
 */
@TableName("t_waybill_goods")
@Data
public class WaybillGoodsEntity {

    @TableId(type = IdType.AUTO)
    private Long waybillGoodsId;

    /**
     * 运单id
     */
    private Long waybillId;
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单商品id
     */
    private Long orderGoodsId;

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
