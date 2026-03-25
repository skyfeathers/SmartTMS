package net.lab1024.tms.admin.module.business.receive.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotNull;

/**
 * 上传对账单
 *
 * @author lidoudou
 * @date 2022/10/22 下午2:43
 */
@Data
public class InvoiceUploadBillForm {

    @ApiModelProperty("应收账款ID")
    @NotNull(message = "应收账款ID不能为空")
    private Long receiveOrderId;

    @ApiModelProperty("对账单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String billAttachment;

    @ApiModelProperty(value = "开票人", hidden = true)
    private Long invoiceUserId;

    @ApiModelProperty(value = "开票人", hidden = true)
    private String invoiceUserName;
}
