package net.lab1024.tms.admin.module.business.fleet.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidoudou
 * @date 2022/6/27 下午5:09
 */
@Data
public class FleetSimpleVO {

    @ApiModelProperty("车队ID")
    private Long fleetId;

    @ApiModelProperty("速记码")
    private String shorthandCode;

    @ApiModelProperty("车队名称")
    private String fleetName;

    @ApiModelProperty("车队长名称")
    private String captainName;
}
