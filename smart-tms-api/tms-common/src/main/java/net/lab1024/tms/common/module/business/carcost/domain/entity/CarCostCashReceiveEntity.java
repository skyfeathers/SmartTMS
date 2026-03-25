package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车-现金收入 Entity
 *
 * @author zhaoxinyang
 * @date 2023/10/24 08:38
 */
@Data
@TableName("t_car_cost_cash_receive")
public class CarCostCashReceiveEntity {

    /**
     * 现金收入表ID
     */
    @TableId(type = IdType.AUTO)
    private Long cashReceiveId;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 收入金额
     */
    @DataTracerFieldDoc("申请金额")
    @DataTracerFieldBigDecimal
    private BigDecimal amount;

    /**
     * 附件
     */
    @DataTracerFieldDoc("附件")
    private String attachment;

    /**
     * 备注
     */
    @DataTracerFieldDoc("备注")
    private String remark;

    /**
     * 审核状态
     * @see AuditStatusEnum
     */
    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    private Integer auditStatus;

    /**
     * 审核备注
     */
    @DataTracerFieldDoc("审核备注")
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