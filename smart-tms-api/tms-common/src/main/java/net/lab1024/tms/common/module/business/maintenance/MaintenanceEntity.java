package net.lab1024.tms.common.module.business.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_maintenance")
public class MaintenanceEntity {

    /**
     * 保养表ID
     */
    @TableId(type = IdType.AUTO)
    private Long maintenanceId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 所属企业ID
     */
    private Long enterpriseId;

    /**
     * 保养人
     */
    private String maintenancePerson;

    /**
     * 保养地点
     */
    private String maintenancePlant;

    /**
     * 备注
     */
    private String remark;

    /**
     * 里程
     */
    private BigDecimal mileage;

    /**
     * 保养时间
     */
    private LocalDate maintenanceDate;

    /**
     * 下次保养时间
     */
    private LocalDate nextMaintenanceDate;

    /**
     * 下次保养里程
     */
    private BigDecimal nextMaintenanceMileage;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名字
     */
    private String createUserName;

    /**
     * 修改人ID
     */
    private Long updateUserId;

    /**
     * 修改人名字
     */
    private String updateUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
}