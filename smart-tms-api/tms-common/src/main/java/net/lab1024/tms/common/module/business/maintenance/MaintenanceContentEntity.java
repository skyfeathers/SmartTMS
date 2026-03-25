package net.lab1024.tms.common.module.business.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/***
 * 车辆保养子表
 *
 * @author zhaoxinyang
 * @date 2023/11/14 14:16
 */
@Data
@TableName("t_maintenance_content")
public class MaintenanceContentEntity {

    /**
     * 保养子表ID
     */
    @TableId(type = IdType.AUTO)
    private Long maintenanceContentId;

    /**
     * 保养表ID
     */
    private Long maintenanceId;

    /**
     * 保养内容
     */
    private String maintenanceContent;

    /**
     * 保养金额
     */
    private BigDecimal maintenanceAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
