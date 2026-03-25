package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 易耗品采购主表
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:02
 */
@Data
@TableName("t_consumables_purchase")
public class ConsumablesPurchaseEntity {

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long purchaseId;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 所属位置
     */
    private Long locationId;

    /**
     * 采购编码
     */
    private String purchaseNo;

    /**
     * 来源
     */
    private String sourceId;

    /**
     * 状态，暂时没用到
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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
