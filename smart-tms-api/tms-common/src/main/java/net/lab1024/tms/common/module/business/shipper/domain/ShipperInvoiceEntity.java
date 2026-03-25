package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDateTime;

/**
 * [ 来往机构开票信息 ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:29
 */
@Data
@TableName("t_shipper_invoice")
public class ShipperInvoiceEntity {

    @TableId(type = IdType.AUTO)
    private Long invoiceId;

    /**
     * 货主id
     */
    private Long shipperId;

    /**
     * 纳税识别号
     */
    @DataTracerFieldDoc("纳税识别号")
    private String invoiceNo;

    /**
     * 开票地址
     */
    @DataTracerFieldDoc("开票地址")
    private String invoiceAddress;
    /**
     * 开票抬头
     */
    @DataTracerFieldDoc("开票抬头")
    private String invoiceName;
    /**
     * 开票电话
     */
    @DataTracerFieldDoc("开票电话")
    private String invoicePhone;
    /**
     * 开票银行
     */
    @DataTracerFieldDoc("开票银行")
    private String invoiceBankName;
    /**
     * 银行账号
     */
    @DataTracerFieldDoc("银行账号")
    private String invoiceBankAccount;
    /**
     * 开户行号
     */
    @DataTracerFieldDoc("开户行号")
    private String invoiceBankNo;
    /**
     * 开户行地址
     */
    @DataTracerFieldDoc("开户行地址")
    private String invoiceBankAddress;

    /**
     * 中征码
     */
    @DataTracerFieldDoc("中征码")
    private String loanCardNo;

    /**
     * 附件信息
     */
    private String attachment;

    /**
     * 状态
     */
    @DataTracerFieldDoc("状态")
    private Boolean disableFlag;

    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
