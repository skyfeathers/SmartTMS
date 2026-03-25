package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;

import java.time.LocalDateTime;

/**
 * 自有车-支出分类
 *
 * @author zhaoxinyang
 * @date 2023/10/24 17:19
 */
@Data
@TableName("t_car_cost_category")
public class CarCostCategoryEntity {

    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long categoryId;

    /**
     * 费用类型
     * @see CarCostCategoryCostTypeEnum
     */
    private Integer costType;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 支付方式
     * @see CarCostCategoryPayModeEnum
     */
    private Integer payMode;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}