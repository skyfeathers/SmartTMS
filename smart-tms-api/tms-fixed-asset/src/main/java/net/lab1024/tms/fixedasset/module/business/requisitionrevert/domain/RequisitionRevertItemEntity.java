package net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领用退款
 *
 * @author lidoudou
 * @date 2023/3/21 上午9:50
 */
@Data
@TableName("t_asset_requisition_revert_item")
public class RequisitionRevertItemEntity {

    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 领用
     */
    private Long requisitionRevertId;

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
