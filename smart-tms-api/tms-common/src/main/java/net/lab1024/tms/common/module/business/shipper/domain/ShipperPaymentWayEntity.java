package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.shipper.constant.PaymentTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.time.LocalDateTime;

/**
 * [ 货主付款方式 ]
 *
 * @author yandanyang
 * @date 2020/7/30 9:10
 */
@Data
@TableName("t_shipper_payment_way")
public class ShipperPaymentWayEntity {

    @TableId(type = IdType.AUTO)
    private Long paymentWayId;

    /**
     * 货主id
     */
    private Long shipperId;
    /**
     * 付款方式
     */
    @DataTracerFieldDoc("付款方式")
    @DataTracerFieldEnum(enumClass = PaymentTypeEnum.class)
    private Integer paymentType;
    /**
     * 银行名称
     */
    @DataTracerFieldDoc("银行名称")
    private String bankName;
    /**
     * 支行名称
     */
    @DataTracerFieldDoc("支行名称")
    private String bankBranchName;
    /**
     * 账户名
     */
    @DataTracerFieldDoc("账户名")
    private String accountName;
    /**
     * 账号
     */
    @DataTracerFieldDoc("账号")
    private String accountNumber;

    /**
     * 付款二维码，银行卡等信息（单张）
     */
    private String payImage;
    /**
     * 收款二维码，银行卡等信息（单张）
     */
    private String receiveImage;
    /**
     * 附件信息
     */
    private String attachment;

    /**
     * 默认标识
     */
    private Boolean defaultFlag;

    /**
     * 是否为公户，如果类型为个人，则一定为否
     */
    @DataTracerFieldDoc("是否为公户")
    private Boolean publicAccountFlag;

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
