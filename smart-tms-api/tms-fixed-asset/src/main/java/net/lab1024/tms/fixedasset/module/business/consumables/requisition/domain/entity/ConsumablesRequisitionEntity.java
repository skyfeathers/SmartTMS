package net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 易耗品领用
 *
 * @author lidoudou
 * @date 2023/4/14 上午9:42
 */
@Data
@TableName("t_consumables_requisition")
public class ConsumablesRequisitionEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long requisitionId;

    /**
     * 领用编号
     */
    private String requisitionNo;

    /**
     * 使用日期
     */
    private LocalDate useTime;

    /**
     * 使用公司
     */
    private Long enterpriseId;

    /**
     * 所属位置
     */
    private Long locationId;

    /**
     * 使用部门
     */
    private Long departmentId;

    /**
     * 使用人
     */
    private Long userId;

    /**
     * 申请说明
     */
    private String remark;

    /**
     * 创建人ID
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
