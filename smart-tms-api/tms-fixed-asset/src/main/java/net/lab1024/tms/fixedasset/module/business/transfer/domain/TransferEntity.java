package net.lab1024.tms.fixedasset.module.business.transfer.domain;

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
@TableName("t_asset_transfer")
public class TransferEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long transferId;

    /**
     * 转移编号
     */
    private String transferNo;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 调出位置
     */
    private Long fromLocationId;

    /**
     * 转入位置
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
