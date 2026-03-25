package net.lab1024.tms.common.module.business.material.cabinet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 业务资料-柜型管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
@TableName("t_material_cabinet")
public class CabinetEntity {

    /**
     * 柜型ID
     */
    @TableId(type = IdType.AUTO)
    private Long cabinetId;

    private Long enterpriseId;

    /**
     * 柜型名称
     */
    private String cabinetName;

    /**
     * 柜型皮重
     */
    private BigDecimal tare;

    /**
     * 默认状态
     */
    private Boolean defaultFlag;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
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
