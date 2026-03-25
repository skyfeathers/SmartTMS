package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 提交订单对账
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:41
 */
@Data
public class ReceiveOrderMailForm {

    @ApiModelProperty("收货人")
    @NotNull(message = "收货人不能为空")
    @Length(max = 50, message = "收货人不能超过500字符")
    private String receivePerson;

    @ApiModelProperty("收货人电话")
    @NotNull(message = "收货人电话不能为空")
    @Length(max = 50, message = "收货人电话不能超过500字符")
    private String receivePersonPhone;

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

    @ApiModelProperty("邮箱")
    private String email;

}
