package net.lab1024.tms.common.module.business.waybill.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @Date 2022-08-13
 */
@TableName("t_waybill_oil_card_recharge_apply")
@Data
public class WaybillOilCardRechargeApplyEntity {

    @TableId(type = IdType.AUTO)
    private Long rechargeApplyId;

    /**
     * 运单id
     */
    private Long waybillId;

    /**
     * 企业ID
     */
    private Long enterpriseId;
    /**
     * 油卡id
     */
    private Long oilCardId;

    /**
     * 油卡主卡的所属公司
     */
    private Long masterOilCardEnterpriseId;

    /**
     * 司机id
     */
    private Long driverId;

    /**
     * 车辆id
     */
    private Long vehicleId;

    private BigDecimal amount;

    /**
     * 是否充值
     */
    private Boolean rechargeFlag;

    /**
     * 充值附件
     */
    @DataTracerFieldDoc("充值附件")
    private String rechargeAttachment;

    /**
     * 充值日期
     */
    private LocalDateTime rechargeTime;

    /**
     * 充值人
     */
    private Long rechargeUserId;

    /**
     * 充值人
     */
    private String rechargeUserName;

    /**
     * 是否核销
     */
    private Boolean verificationFlag;

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

    /**
     * 核销日期
     */
    private LocalDateTime verificationTime;

    /**
     * 核销人
     */
    private Long verificationUserId;

    /**
     * 核销人
     */
    private String verificationUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * @see FlowAuditStatusEnum
     */
    private Integer auditStatus;

    /**
     * 作废备注
     */
    private String disabledRemark;

    /**
     * 是否已增加预充值金额
     */
    private Boolean addPreDistributionBalanceFlag;

    /**
     * 是否已减少预充值金额
     */
    private Boolean reducePreDistributionBalanceFlag;
    /**
     * 作废状态
     */
    private Boolean disabledFlag;

    /**
     * 流程实例id
     */
    private Long flowInstanceId;

    private Long createUserId;

    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
