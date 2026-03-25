package net.lab1024.tms.common.module.business.waybill.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */

@TableName("t_waybill_voucher")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaybillVoucherEntity {

    /**
     * 凭证id
     */
    @TableId(type = IdType.AUTO)
    private Long waybillVoucherId;

    /**
     * 运单Id
     */
    private Long waybillId;

    /**
     * 运单类型
     */
    private Integer type;

    /**
     * 附件
     */
    private String attachment;


    /**
     * 人车合影
     */
    private String sceneAttachment;

    /**
     * 磅单凭证
     */
    private String poundListAttachment;

    /**
     * 位置
     */
    private String location;

    /**
     * 纬度
     */
    private BigDecimal longitude;

    /**
     * 经度
     */
    private BigDecimal latitude;

    /**
     * 地区code
     */
    private String adcode;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人类型
     */
    private Integer createUserType;

    /**
     * 创建人类型描述
     */
    private String createUserTypeDesc;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
