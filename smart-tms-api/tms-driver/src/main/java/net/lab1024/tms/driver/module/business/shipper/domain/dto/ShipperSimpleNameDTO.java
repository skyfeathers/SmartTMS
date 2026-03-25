package net.lab1024.tms.driver.module.business.shipper.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/29 18:11
 */
@Data
public class ShipperSimpleNameDTO {
    
    @ApiModelProperty("货主id")
    private Long shipperId;
    
    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("简称")
    private String shortName;


}
