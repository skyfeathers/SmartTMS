package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.PathTypeEnum;

import java.math.BigDecimal;

/**
 * @author yandy
 */
@Data
public class WaybillPathVO {

    @ApiModelProperty(value = "运单ID", hidden = true)
    private Long waybillId;

    @ApiModelProperty(value = "运输路线ID")
    private Long waybillPathId;

    @ApiModelPropertyEnum(value = PathTypeEnum.class, desc = "类型")
    private Integer type;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("市")
    private Integer city;

    @ApiModelProperty("城市名称")
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

    @ApiModelProperty("地址的纬度")
    private BigDecimal latitude;

    @ApiModelProperty("地址的经度")
    private BigDecimal longitude;

}
