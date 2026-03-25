package net.lab1024.tms.common.module.business.waybill.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @Date 2022-08-13
 */
@TableName("t_waybill_cost")
@Data
public class WaybillCostEntity {

    @TableId(type = IdType.AUTO)
    private Long waybillCostId;

    /**
     * 运单id
     */
    private Long waybillId;

    /**
     * 费用项id
     */
    private Long costItemId;

    /**
     * 分类
     * @see CostItemCategoryEnum
     */
    private Integer costItemCategory;

    /**
     * 费用项类型
     * @see CostItemTypeEnum
     */
    private Integer costItemType;

    /**
     * 费用项名称
     */
    private String costItemName;

    /**
     * 费用
     */
    private BigDecimal costAmount;

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
