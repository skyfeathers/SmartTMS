package net.lab1024.tms.common.module.business.pay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @Date 2022-08-13
 */
@Data
@TableName("t_pay_order_cost")
public class PayOrderCostEntity {


    @TableId(type = IdType.AUTO)
    private Long payOrderCostId;
    /**
     * 付款单id
     */
    private Long payOrderId;
    /**
     * 运单id
     */
    private Long waybillId;

    /**
     * 运单费用项id
     */
    private Long waybillCostId;

    /**
     * 费用项id
     */
    private Long costItemId;

    /**
     * 费用项类型
     */
    private Integer costItemType;

    /**
     * 分类
     * @see CostItemCategoryEnum
     */
    private Integer costItemCategory;

    /**
     * 费用项名称
     */
    private String costItemName;

    private String remark;
    /**
     * 应付金额
     */
    private BigDecimal costAmount;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
