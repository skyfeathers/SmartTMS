package net.lab1024.tms.common.module.business.material.transportroute.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 业务资料-运输路线
 *
 * @author lidoudou
 * @date 2022/7/12 下午2:55
 */
@Data
@TableName("t_transport_route")
public class TransportRouteEntity {

    /**
     * 运输路线ID
     */
    @TableId(type = IdType.AUTO)
    private Long transportRouteId;

    /**
     * 运输路线名称
     */
    private String transportRouteName;

    private Long enterpriseId;

    /**
     * 运输类型
     *
     * @see TransportationTypeEnum
     */
    private Integer transportRouteType;

    /**
     * 里程
     */
    private BigDecimal mileage;

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
