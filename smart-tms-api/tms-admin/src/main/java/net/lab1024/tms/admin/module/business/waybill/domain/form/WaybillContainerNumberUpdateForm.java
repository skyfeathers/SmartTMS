package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */
@Data
public class WaybillContainerNumberUpdateForm {

    @ApiModelProperty("箱号")
    @NotBlank(message = "箱号不能为空")
    private String containerNumber;

    @ApiModelProperty("运单号")
    @NotNull(message = "运单号不能为空")
    private Long waybillId;



}
