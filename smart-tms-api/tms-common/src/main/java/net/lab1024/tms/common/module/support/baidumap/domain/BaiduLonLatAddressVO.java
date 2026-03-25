package net.lab1024.tms.common.module.support.baidumap.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 经纬度转换后的位置
 * @author lidoudou
 * @description:
 * @date 2024/7/4 17:03
 */
@Data
public class BaiduLonLatAddressVO {

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String district;

    @ApiModelProperty("街道")
    private String town;

    @ApiModelProperty("街道编码")
    private Integer townCode;

    @ApiModelProperty("行政区划代码")
    private Integer adcode;

    @ApiModelProperty("街道")
    private String street;

    @ApiModelProperty("街道号")
    private String streetNumber;

    @ApiModelProperty("结构化地址")
    private String address;

}