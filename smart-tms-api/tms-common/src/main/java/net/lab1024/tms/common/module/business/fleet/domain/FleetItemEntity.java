package net.lab1024.tms.common.module.business.fleet.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.fleet.constants.FleetItemTypeEnum;

import java.time.LocalDateTime;

/**
 * 车队关联信息
 *
 * @author lidoudou
 * @date 2022/6/28 上午11:15
 */
@Data
@TableName("t_fleet_item")
public class FleetItemEntity {

    @TableId(type = IdType.AUTO)
    private Long fleetItemId;

    /**
     * 车队ID
     */
    private Long fleetId;

    /**
     * 关联ID
     */
    private Long itemId;

    /**
     * 类型
     * @see FleetItemTypeEnum
     */
    private Integer itemType;

    private Long createUserId;

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
