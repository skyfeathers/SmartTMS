package net.lab1024.tms.common.module.business.driver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.time.LocalDateTime;

/**
 * 司机纳税登记人
 *
 * @author lidoudou
 * @date 2022/8/25 下午5:38
 */
@Data
@TableName("t_driver_tax_register")
public class DriverTaxRegisterEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 姓名
     */
    @DataTracerFieldDoc("姓名")
    private String name;

    /**
     * 纳税人身份证号
     */
    @DataTracerFieldDoc("纳税人身份证号")
    private String idCard;

    /**
     * 纳税人手机号
     */
    @DataTracerFieldDoc("纳税人手机号")
    private String phone;

    /**
     * 纳税人地址
     */
    @DataTracerFieldDoc("纳税人地址")
    private String address;

    /**
     * 纳税人身份证正面
     */
    private String idCardFrontAttachment;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
