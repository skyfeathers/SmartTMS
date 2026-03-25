package net.lab1024.tms.fixedasset.module.business.check.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidoudou
 * @date 2023/3/24 上午08:45
 */
@Data
@TableName("t_asset_check_item")
public class CheckItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 盘点单ID
     */
    private Long checkId;

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 盘点人
     */
    private Long employeeId;

    /**
     * 盘点状态
     */
    private Integer status;

    private String remark;

    /**
     * 盘点时间
     */
    private LocalDateTime checkTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
