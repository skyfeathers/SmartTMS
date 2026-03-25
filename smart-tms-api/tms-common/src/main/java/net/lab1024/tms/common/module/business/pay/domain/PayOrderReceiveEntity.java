package net.lab1024.tms.common.module.business.pay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收款信息
 * @author yandy
 * @Date 2022-08-13
 */
@Data
@TableName("t_pay_order_receive")
public class PayOrderReceiveEntity {


    @TableId(type = IdType.AUTO)
    private Long payOrderReceiveId;

    /**
     * 付款单id
     */
    private Long payOrderId;

    /**
     * 银行类型 数据字典
     */
    private String receiveBankType;

    /**
     * 银行名称
     */
    private String receiveBankName;

    /**
     * 支行名称
     */
    private String receiveBankBranchName;

    /**
     * 开户名
     */
    private String receiveAccountName;

    /**
     * 银行账号
     */
    private String receiveBankAccount;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
