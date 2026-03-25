package net.lab1024.tms.admin.module.business.maintenance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 保养列表VO
 *
 * @author zhaoxinyang
 * @date 2023/10/18 14:01
 */
@Data
public class MaintenanceVO {
    
    @ApiModelProperty("保养ID")
    private Long maintenanceId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车辆ID")
    private String vehicleNumber;

    @ApiModelProperty("公司名字")
    private String enterpriseName;

    @ApiModelProperty("保养人")
    private String maintenancePerson;

    @ApiModelProperty("保养地点")
    private String maintenancePlant;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("保养里程")
    private BigDecimal mileage;

    @ApiModelProperty("保养时间")
    private LocalDate maintenanceDate;

    @ApiModelProperty("下次保养时间")
    private LocalDate nextMaintenanceDate;

    @ApiModelProperty("下次保养里程")
    private BigDecimal nextMaintenanceMileage;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("保养内容")
    private List<MaintenanceContentVO> contentVOList;
    
}