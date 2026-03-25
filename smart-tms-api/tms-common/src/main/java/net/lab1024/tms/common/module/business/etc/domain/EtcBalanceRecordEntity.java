package net.lab1024.tms.common.module.business.etc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_etc_balance_record")
public class EtcBalanceRecordEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long balanceRecordId;

    /**
     * 编号
     */
    private String balanceRecordNo;

    /**
     * ETC ID
     */
    private Long etcId;

    /**
     * 收入标识
     */
    private Boolean incomeFlag;

    /**
     * 变动前金额
     */
    private BigDecimal beforeBalance;

    /**
     * 变动金额
     */
    private BigDecimal changeBalance;

    /**
     * 变动金额
     */
    private BigDecimal afterBalance;

    /**
     * 充值时间
     */
    private LocalDateTime rechargeTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
