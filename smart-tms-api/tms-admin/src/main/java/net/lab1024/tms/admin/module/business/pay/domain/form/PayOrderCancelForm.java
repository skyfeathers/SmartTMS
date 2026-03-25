package net.lab1024.tms.admin.module.business.pay.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2022-07-20
 */
@Data
public class PayOrderCancelForm {

    @ApiModelProperty("付款单id")
    @NotNull(message = "付款单id 不能为空")
    private Long payOrderId;

    @ApiModelProperty("付款时间")
    private String remark;

}
