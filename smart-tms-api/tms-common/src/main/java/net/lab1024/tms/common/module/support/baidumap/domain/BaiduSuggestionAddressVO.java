package net.lab1024.tms.common.module.support.baidumap.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2024/12/4 17:42
 */
@Data
public class BaiduSuggestionAddressVO {
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("省")
    private String provinceName;

    @ApiModelProperty("市")
    private String cityName;

    @ApiModelProperty("区")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("经度")
    private BigDecimal latitude;

    @ApiModelProperty("纬度")
    private BigDecimal longitude;

    @ApiModelProperty("地区码")
    private Integer adcode;
}