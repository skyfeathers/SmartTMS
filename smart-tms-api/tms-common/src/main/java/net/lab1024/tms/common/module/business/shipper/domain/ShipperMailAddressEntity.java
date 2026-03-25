package net.lab1024.tms.common.module.business.shipper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDateTime;

/**
 * [ 来往机构邮寄地址 ]
 *
 * @author yandanyang
 * @date 2020/7/30 14:29
 */
@Data
@TableName("t_shipper_mail_address")
public class ShipperMailAddressEntity {

    @TableId(type = IdType.AUTO)
    private Long mailAddressId;

    /**
     * 货主id
     */
    private Long shipperId;

    /**
     * 收件人名称
     */
    @DataTracerFieldDoc("收件人名称")
    private String receivePerson;

    /**
     * 收件人电话
     */
    @DataTracerFieldDoc("收件人电话")
    private String receivePersonPhone;

    private Integer receiveProvinceCode;
    @DataTracerFieldDoc("省")
    private String receiveProvinceName;

    private Integer receiveCityCode;
    @DataTracerFieldDoc("市")
    private String receiveCityName;

    private Integer receiveDistrictCode;
    @DataTracerFieldDoc("区")
    private String receiveDistrictName;

    @DataTracerFieldDoc("详细地址")
    private String receiveAddress;
    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    /**
     * 默认标识
     */
    private Boolean defaultFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
