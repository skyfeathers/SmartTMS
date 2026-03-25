package net.lab1024.tms.admin.module.business.material.transportroute.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.material.transportroute.domain.dto.TransportRoutePathDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 运输路线展示
 *
 * @author lidoudou
 * @date 2022/7/12 下午3:51
 */
@Data
public class TransportRouteVO {

    @ApiModelProperty("运输路线ID")
    private Long transportRouteId;

    @ApiModelProperty("运输路线名称")
    private String transportRouteName;

    @ApiModelPropertyEnum(value = TransportationTypeEnum.class, desc = "运输类型")
    private Integer transportRouteType;

    @ApiModelProperty("里程")
    private BigDecimal mileage;

    @ApiModelProperty("地点列表")
    private List<TransportRoutePathDTO> pathList;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
