package net.lab1024.tms.common.module.business.driver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/5/19 10:27
 */
@Data
@TableName("t_driver_balance_record")
public class DriverBalanceRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long driverBalanceRecordId;

    private Long balanceId;

    /**
     * 司机 id
     */
    private Long driverId;

    /**
     * 企业 id
     */
    private Long enterpriseId;


    /**
     * 交易类型
     * @see net.lab1024.tms.common.module.business.driver.constants.DriverBalanceTradeTypeEnum
     */
    private Integer tradeType;

    /**
     * 交易类型描述
     */
    private String tradeTypeDesc;
    /**
     * 交易内容
     */
    private String tradeContent;

    /**
     * 关联订单号
     */
    private Long relateOrderId;

    /**
     * 收入标识
     */
    private Boolean incomeFlag;
    /**
     * 变动前余额
     */
    private BigDecimal beforeBalance;
    /**
     * 变动金额
     */
    private BigDecimal changeBalance;
    /**
     * 变动后余额
     */
    private BigDecimal afterBalance;
    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime update_time;
}
