package net.lab1024.tms.common.module.business.oilcard.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_oil_card_balance_record")
public class OilCardBalanceRecordEntity {

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
     * 油卡ID
     */
    private Long oilCardId;

    /**
     * 油卡类型
     *
     * @see OilCardTypeEnum
     */
    private Integer oilCardType;

    /**
     * 交易记录类型
     *
     * @see OilCardBalanceRecordTypeEnum
     */
    private Integer recordType;

    /**
     * 交易记录类型描述
     *
     * @see OilCardBalanceRecordTypeEnum
     */
    private String recordTypeDesc;

    /**
     * 变动前金额
     */
    private BigDecimal beforeBalance;

    /**
     * 变动金额
     */
    private BigDecimal changeBalance;

    /**
     * 变动后金额
     */
    private BigDecimal afterBalance;

    /**
     * 关联记录id
     */
    private Long relatedRecordId;

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

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;
}
