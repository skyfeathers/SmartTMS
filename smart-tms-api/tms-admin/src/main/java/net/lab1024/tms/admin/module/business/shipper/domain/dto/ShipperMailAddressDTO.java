package net.lab1024.tms.admin.module.business.shipper.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * [ 货主邮寄地址 ]
 *
 * @author yandanyang
 * @date 2020/7/30 9:27
 */
@Data
public class ShipperMailAddressDTO {

    @ApiModelProperty("mailAddressId")
    private Long mailAddressId;

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("收货省编码")
    private Integer receiveProvinceCode;

    @ApiModelProperty("收货省名称")
    private String receiveProvinceName;

    @ApiModelProperty("收货市编码")
    private Integer receiveCityCode;

    @ApiModelProperty("收货市名称")
    private String receiveCityName;

    @ApiModelProperty("收货区编码")
    private Integer receiveDistrictCode;

    @ApiModelProperty("收货区名称")
    private String receiveDistrictName;

    @ApiModelProperty("收货详细地址")
    private String receiveAddress;

    @ApiModelProperty("收货人")
    @NotNull(message = "收货人不能为空")
    @Length(max = 50, message = "收货人不能超过500字符")
    private String receivePerson;

    @ApiModelProperty("收货人电话")
    @NotNull(message = "收货人电话不能为空")
    @Length(max = 50, message = "收货人电话不能超过500字符")
    private String receivePersonPhone;

    @ApiModelProperty("默认标识 1默认")
    @NotNull(message = "邮寄地址默认标识不能为空")
    private Boolean defaultFlag;

}
