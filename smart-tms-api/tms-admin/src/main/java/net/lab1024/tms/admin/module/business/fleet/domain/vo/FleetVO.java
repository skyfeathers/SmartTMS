package net.lab1024.tms.admin.module.business.fleet.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.fleet.domain.dto.FleetBaseDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import java.time.LocalDateTime;

/**
 * @author lidoudou
 * @date 2022/6/27 下午5:09
 */
@Data
public class FleetVO extends FleetBaseDTO {

    @ApiModelProperty("车队ID")
    private Long fleetId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("负责人名称")
    private String managerName;

    @ApiModelPropertyEnum(desc = "审核状态", value = AuditStatusEnum.class)
    private Integer auditStatus;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    private Boolean deletedFlag;
}
