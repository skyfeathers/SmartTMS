package net.lab1024.tms.admin.module.business.shipper.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/29 18:11
 */
@Data
public class ShipperBasicDTO {

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("简称")
    private String shortName;

    @ApiModelProperty("所在地区")
    private String area;

    @ApiModelProperty("税点")
    private BigDecimal taxPoint;
}
