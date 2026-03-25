package net.lab1024.tms.common.module.business.repair.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/***
 * 车辆维修子表
 *
 * @author zhaoxinyang
 * @date 2023/10/17 14:16
 */
@Data
@TableName("t_repair_content")
public class RepairContentEntity {

    /**
     * 维修子表ID
     */
    @TableId(type = IdType.AUTO)
    private Long repairContentId;

    /**
     * 维修表ID
     */
    private Long repairId;

    /**
     * 维修内容
     */
    private String repairContent;

    /**
     * 维修金额
     */
    private BigDecimal repairAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
