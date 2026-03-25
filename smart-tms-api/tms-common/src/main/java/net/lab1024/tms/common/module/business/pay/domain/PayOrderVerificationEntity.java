package net.lab1024.tms.common.module.business.pay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDateTime;

/**
 * 核销
 * @author yandy
 * @Date 2022-08-13
 */
@Data
@TableName("t_pay_order_verification")
public class PayOrderVerificationEntity {


    @TableId(type = IdType.AUTO)
    private Long payOrderVerificationId;

    /**
     * 付款单id
     */
    private Long payOrderId;

    /**
     * 核销附件
     */
    @DataTracerFieldDoc("核销附件")
    private String verificationAttachment;

    /**
     * 核销备注
     */
    @DataTracerFieldDoc("核销备注")
    private String verificationRemark;

    private Long createUserId;

    private String createUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
