package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/1/22 上午10:49
 */
@Data
public class ShipperContactAddForm {

    @ApiModelProperty("货主id")
    @NotNull(message = "货主id不能为空")
    private Long shipperId;

    @ApiModelProperty("联系人名称")
    @NotNull(message = "联系人名称不能为空")
    @Length(max = 50,message = "联系人名称不能超过50字符")
    private String contactName;

    @ApiModelProperty("联系人电话")
    @NotNull(message = "联系人电话不能为空")
    @Length(max = 50,message = "联系人电话不能超过50字符")
    private String contactPhone;

    @ApiModelProperty("岗位")
    private String position;

    @ApiModelProperty("省编码")
    private Integer provinceCode;

    @ApiModelProperty("省名称")
    @Length(max = 50, message = "省不能超过50字符")
    private String provinceName;

    @ApiModelProperty("市编码")
    private Integer cityCode;

    @ApiModelProperty("市名称")
    @Length(max = 50, message = "市不能超过50字符")
    private String cityName;

    @ApiModelProperty("区编码")
    private Integer districtCode;

    @ApiModelProperty("区名称")
    @Length(max = 50, message = "区不能超过100字符")
    private String districtName;

    @ApiModelProperty("详细地址")
    @Length(max = 500, message = "详细地址不能超过500字符")
    private String address;

    @ApiModelProperty("备注")
    @Length(max = 5000,message = "备注信息不能超过5000字符")
    private String remark;

}