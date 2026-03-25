package net.lab1024.tms.fixedasset.module.business.allocation.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidoudou
 * @date 2023/3/20 下午5:31
 */
@Data
@TableName("t_asset_allocation")
public class AllocationEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long allocationId;

    /**
     * 调拨编号
     */
    private String allocationNo;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 调出位置
     */
    private Long fromLocationId;

    /**
     * 掉入位置
     */
    private Long toLocationId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人
     */
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
