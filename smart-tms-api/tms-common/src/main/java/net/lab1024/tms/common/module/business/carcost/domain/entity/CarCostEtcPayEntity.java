package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBigDecimal;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ETC支出 Entity
 *
 * @author zhaoxinyang
 * @date 2023/10/26 09:10
 */
@Data
@TableName("t_car_cost_etc_pay")
public class CarCostEtcPayEntity {

    /**
     * ETC支出表ID
     */
    @TableId(type = IdType.AUTO)
    private Long etcPayId;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 费用类型
     * @see CarCostCategoryCostTypeEnum
     */
//    @DataTracerFieldDoc("费用类型")
//    @DataTracerFieldEnum(enumClass = CarCostCategoryCostTypeEnum.class)
    private Integer costType;

    /**
     * 分类名称
     */
    @DataTracerFieldDoc("分类名字")
    private String categoryName;

    /**
     * 支付方式
     * @see CarCostCategoryPayModeEnum
     */
    @DataTracerFieldDoc("支付方式")
    @DataTracerFieldEnum(enumClass = CarCostCategoryPayModeEnum.class)
    private Integer payMode;

    /**
     * 收入金额
     */
    @DataTracerFieldDoc("收入金额")
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