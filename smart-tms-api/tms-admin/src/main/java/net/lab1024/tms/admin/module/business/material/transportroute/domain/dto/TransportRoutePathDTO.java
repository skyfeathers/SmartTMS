package net.lab1024.tms.admin.module.business.material.transportroute.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;

/**
 * 运输路线的地点详情
 *
 * @author lidoudou
 * @date 2022/7/12 下午2:49
 */
@Data
public class TransportRoutePathDTO {

    @ApiModelProperty(value = "运输路线地点ID", hidden = true)
    private Long transportRoutePathId;

    @ApiModelProperty(value = "运输路线ID", hidden = true)
    private Long transportRouteId;

    @ApiModelPropertyEnum(desc = "运输路线类型", value = PathTypeEnum.class)
    private Integer type;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("市")
    private Integer city;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区县")
    private Integer district;

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("联系人")
    private String contactName;

    @ApiModelProperty("联系电话")
    private String contactPhone;
}
