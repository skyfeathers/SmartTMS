package net.lab1024.tms.common.module.support.baidumap.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yandy
 * @description:
 * @date 2022/12/16 4:05 下午
 */
@Data
public class BaiduAddressDistanceQueryForm {

    @ApiModelProperty("源地址")
    @NotBlank(message = "源地址不能为空")
    private String sourceAddress;

    @ApiModelProperty("目的地址")
    @NotBlank(message = "目的地址不能为空")
    private String destAddress;

    @ApiModelProperty("源地址经纬度")
    private String sourceLatLng;

    @ApiModelProperty("目的地址经纬度")
    private String destLatLng;
}