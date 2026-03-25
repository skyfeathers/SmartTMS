package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/8/25 9:29
 */
@Data
public class ShipperContactVO {

    @ApiModelProperty("联系人id")
    private Long contactId;

    @ApiModelProperty("联系人名称")
    private String contactName;

    @ApiModelProperty("联系人电话")
    private String contactPhone;

    @ApiModelProperty("联系人岗位")
    private String position;

    @ApiModelProperty("省编码")
    private Integer provinceCode;

    private String provinceName;
    @ApiModelProperty("市编码")
    private Integer cityCode;

    private String cityName;

    @ApiModelProperty("区编码")
    private Integer districtCode;

    private String districtName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("默认标识")
    private Boolean defaultFlag;
}
