package net.lab1024.tms.common.module.business.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单的收货人信息
 *
 * @author lidoudou
 * @date 2022/8/25 下午2:42
 */
@Data
@TableName("t_order_mail_address")
public class OrderMailAddressEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    /**
     * 收货人姓名
     */
    private String consignee;

    /**
     * 收货人联系电话
     */
    private String telephone;

    /**
     * 收货单位
     */
    private String customerName;

    /**
     * 收获人识别号
     */
    private String identificationNumberOfTheTaxpayer;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
