package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/8/29 17:54
 */
@Data
public class ShipperSimpleVO {

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("货主简称")
    private String shortName;

    @ApiModelProperty("禁用状态")
    private Boolean disableFlag;

}
