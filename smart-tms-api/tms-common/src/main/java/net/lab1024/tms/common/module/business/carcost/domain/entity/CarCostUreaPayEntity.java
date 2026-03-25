package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 尿素支出 Entity
 *
 * @author zhaoxinyang
 * @date 2023/10/26 14:25
 */
@Data
@TableName("t_car_cost_urea_pay")
public class CarCostUreaPayEntity {

    /**
     * 油费支出表ID
     */
    @TableId(type = IdType.AUTO)
    private Long ureaPayId;

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
     * 油卡ID
     */
    private Long oilCardId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 费用类型
     * @see CarCostCategoryCostTypeEnum
     */
    private Integer costType;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 支付方式
     * @see CarCostCategoryPayModeEnum
     */
    private Integer payMode;

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