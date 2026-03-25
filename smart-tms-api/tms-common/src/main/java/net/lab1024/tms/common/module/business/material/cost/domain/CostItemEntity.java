package net.lab1024.tms.common.module.business.material.cost.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
@TableName("t_cost_item")
public class CostItemEntity {

    @TableId(type = IdType.AUTO)
    private Long costItemId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     * @see CostItemTypeEnum
     */
    private Integer type;

    /**
     * 分类
     * @see CostItemCategoryEnum
     */
    private Integer category;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 备注
     */
    private String remark;

    private Integer deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
     */
    private String createUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
