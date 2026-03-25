package net.lab1024.tms.common.module.business.pay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款信息
 * @author yandy
 * @Date 2022-08-13
 */
@Data
@TableName("t_pay_order_payment")
public class PayOrderPaymentEntity {

    @TableId(type = IdType.AUTO)
    private Long payOrderPaymentId;
    /**
     * 付款单id
     */
    private Long payOrderId;

    /**
     * 银行名称
     */
    @DataTracerFieldDoc("付款银行")
    private String payBankName;

    /**
     * 开户名
     */
    @DataTracerFieldDoc("付款银行开户名")
    private String payAccountName;

    /**
     * 银行账号
     */
    @DataTracerFieldDoc("付款银行账号")
    private String payBankAccount;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
