package net.lab1024.tms.admin.module.business.oa.cost.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:52
 */
@Data
@TableName("t_oa_cost_apply")
public class CostApplyEntity {

    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long applyId;

    /**
     * 申请单号
     */
    private String applyNo;

    /**
     * 申请时间
     */
    private LocalDate applyDate;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 所属企业
     */
    private Long enterpriseId;

    /**
     * 所属部门
     */
    private Long departmentId;

    /**
     * 申请人
     */
    private Long applyUserId;

    /**
     * 申请总金额
     */
    private BigDecimal totalAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 创建人
     */
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