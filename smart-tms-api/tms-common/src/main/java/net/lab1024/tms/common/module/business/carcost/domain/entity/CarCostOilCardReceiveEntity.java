package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车-油卡收入 Entity
 *
 * @author zhaoxinyang
 * @date 2023/10/24 08:38
 */
@Data
@TableName("t_car_cost_oil_card_receive")
public class CarCostOilCardReceiveEntity {

    /**
     * 油卡收入表ID
     */
    @TableId(type = IdType.AUTO)
    private Long oilCardReceiveId;

    private Long enterpriseId;
    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 油卡ID
     */
    private Long oilCardId;

    /**
     * 收入金额
     */
    private BigDecimal amount;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核状态
     * @see AuditStatusEnum
     */
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名字
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

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}