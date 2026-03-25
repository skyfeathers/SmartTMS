package net.lab1024.tms.common.module.business.carcost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车费用-审核列表
 *
 * @author zhaoxinyang
 * @date 2023/11/01 16:35
 */
@Data
@TableName("t_car_cost_tabulation")
public class CarCostTabulationEntity {

    /**
     * 自有车费用列表ID
     */
    @TableId(type = IdType.AUTO)
    private Long tabulationId;

    /**
     * 企业id
     */
    private Long enterpriseId;


    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 模块ID
     */
    private Long moduleId;

    /**
     * 模块类型
     *
     * @see CarCostModuleTypeEnum
     */
    private Integer moduleType;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 审核状态
     *
     * @see AuditStatusEnum
     */
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 油卡ID
     */
    private Long oilCardId;

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
