package net.lab1024.tms.admin.module.business.receive.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 确认对账接口
 *
 * @author lidoudou
 * @date 2022/7/20 下午9:13
 */
@Data
public class ReceiveCheckForm {

    @ApiModelProperty("收款单ID")
    @NotNull(message = "收款单ID不能为空")
    private Long receiveOrderId;

    @ApiModelProperty("核算时间")
    @NotNull(message = "核算时间不能为空")
    private LocalDate checkTime;

    @ApiModelProperty("核算凭证")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String checkAttachment;

    @ApiModelProperty("核算备注")
    private String checkRemark;

    @ApiModelProperty(value = "操作人", hidden = true)
    private Long checkUserId;

    @ApiModelProperty(value = "操作人", hidden = true)
    private String checkUserName;

}
