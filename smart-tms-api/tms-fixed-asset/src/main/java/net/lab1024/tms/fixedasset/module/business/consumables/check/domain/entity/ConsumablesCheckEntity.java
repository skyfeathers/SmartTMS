package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 易耗品盘点
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:03
 */
@Data
@TableName("t_consumables_check")
public class ConsumablesCheckEntity {

    @TableId(type = IdType.AUTO)
    private Long checkId;

    /**
     * 盘点编号
     */
    private String checkNo;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 盘点名称
     */
    private String checkName;

    /**
     * 盘点位置
     */
    private Long locationId;

    private String remark;

    /**
     * 完成状态
     */
    private Boolean completeFlag;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

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
