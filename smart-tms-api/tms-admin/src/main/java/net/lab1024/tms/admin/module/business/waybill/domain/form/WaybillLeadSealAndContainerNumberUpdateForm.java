package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 更新运单箱号和铅封号
 *
 * @author zhaoxinyang
 * @date 2023/11/28 08:38
 */
@Data
public class WaybillLeadSealAndContainerNumberUpdateForm {

    @ApiModelProperty("运单号")
    @NotNull(message = "运单号不能为空")
    private Long waybillId;

    @ApiModelProperty("箱号")
    @NotBlank(message = "箱号不能为空")
    private String containerNumber;

    @ApiModelProperty("铅封号")
//    @NotBlank(message = "铅封号不能为空")
    private String leadSealNumber;

}
