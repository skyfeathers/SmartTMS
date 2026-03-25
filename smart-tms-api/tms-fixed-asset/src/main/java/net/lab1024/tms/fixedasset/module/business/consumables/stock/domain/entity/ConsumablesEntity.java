package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 易耗品
 *
 * @author lidoudou
 * @date 2023/4/11 下午4:52
 */
@Data
@TableName("t_consumables")
public class ConsumablesEntity {

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long consumablesId;

    /**
     * 所属公司
     */
    private Long enterpriseId;

    /**
     * 易耗材编码
     */
    private String consumablesNo;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 易耗材名称
     */
    private String consumablesName;

    /**
     * 库存预警值
     */
    private Integer stockWarningValue;

    /**
     * 图片
     */
    private String attachment;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

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
