package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2024/5/14 6:19 下午
 */
@Data
public class WaybillCreateSplitTransportForm {

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
}