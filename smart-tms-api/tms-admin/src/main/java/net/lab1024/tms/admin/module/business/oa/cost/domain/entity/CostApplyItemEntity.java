package net.lab1024.tms.admin.module.business.oa.cost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费用申请 - 申请项目
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:52
 */
@Data
@TableName("t_oa_cost_apply_item")
public class CostApplyItemEntity {

    @TableId(type = IdType.AUTO)
    private Long applyItemId;

    /**
     * 主表ID
     */
    private Long applyId;

    /**
     * 报销项目
     */
    private String applyItemName;

    /**
     * 报销金额
     */
    private BigDecimal applyAmount;

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