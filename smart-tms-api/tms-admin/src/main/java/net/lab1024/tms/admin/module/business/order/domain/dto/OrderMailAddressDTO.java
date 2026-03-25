package net.lab1024.tms.admin.module.business.order.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import javax.validation.constraints.NotBlank;

/**
 * 订单的收货人信息
 *
 * @author lidoudou
 * @date 2022/8/25 下午2:42
 */
@Data
public class OrderMailAddressDTO {

    @ApiModelProperty(value = "订单ID", hidden = true)
    private Long orderId;

    @NotBlank(message = "收货人姓名不能为空")
    @ApiModelProperty("收货人姓名")
    @DataTracerFieldDoc("收货人姓名")
    private String consignee;

    @ApiModelProperty("收货人联系电话")
    @DataTracerFieldDoc("收货人联系电话")
    private String telephone;

    @ApiModelProperty("收货单位")
    @DataTracerFieldDoc("收货单位")
    private String customerName;

    @ApiModelProperty("收货人识别号")
    @DataTracerFieldDoc("收货人识别号")
    private String identificationNumberOfTheTaxpayer;

}
