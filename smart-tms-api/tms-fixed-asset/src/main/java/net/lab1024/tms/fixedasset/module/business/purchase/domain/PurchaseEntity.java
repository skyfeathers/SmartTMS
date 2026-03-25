package net.lab1024.tms.fixedasset.module.business.purchase.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.fixedasset.module.business.asset.constants.PurchaseStatusEnum;

import java.time.LocalDateTime;

/**
 * 资产采购
 *
 * @author lidoudou
 * @date 2023/3/18 下午5:30
 */
@Data
@TableName("t_asset_purchase")
public class PurchaseEntity {

    /**
     * 采购ID
     */
    @TableId(type = IdType.AUTO)
    private Long purchaseId;

    /**
     * 采购编号
     */
    private String purchaseNo;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 状态暂时不限制不显示
     * {@link PurchaseStatusEnum}
     */
    private Integer status;

    /**
     * 来源
     */
    private String sourceId;

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
