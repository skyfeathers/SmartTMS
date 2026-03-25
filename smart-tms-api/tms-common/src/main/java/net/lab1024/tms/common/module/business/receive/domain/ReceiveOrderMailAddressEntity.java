package net.lab1024.tms.common.module.business.receive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应收帐款的邮寄信息
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:07
 */
@Data
@TableName("t_receive_order_mail_address")
public class ReceiveOrderMailAddressEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long receiveOrderId;

    /**
     * 收件人姓名
     */
    private String receivePerson;
    /**
     * 收件人手机号
     */
    private String receivePersonPhone;
    /**
     * 省
     */
    private Integer receiveProvinceCode;

    /**
     * 省
     */
    private String receiveProvinceName;

    private Integer receiveCityCode;
    /**
     * 市
     */
    private String receiveCityName;

    private Integer receiveDistrictCode;
    /**
     * 区
     */
    private String receiveDistrictName;

    /**
     * 详细地址
     */
    private String receiveAddress;

    /**
     * 邮箱
     */
    private String email;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
