package net.lab1024.tms.admin.module.business.receive.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 申请开票
 *
 * @author lidoudou
 * @date 2022/7/25 下午17:21
 */
@Data
public class ApplyInvoiceForm {

    @ApiModelProperty("申请开票ID")
    @NotNull(message = "申请开票ID不能为空")
    private Long receiveOrderInvoiceId;

    @ApiModelProperty("开票时间")
    @NotNull(message = "开票时间不能为空")
    private LocalDate invoiceTime;

    @ApiModelProperty("发票号")
    private List<String> invoiceNumberList;

    @ApiModelProperty("快递单号")
    private String courierNumber;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String invoiceAttachment;

    @ApiModelProperty("对账单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String billAttachment;

    @ApiModelProperty(value = "开票人", hidden = true)
    private Long invoiceUserId;

    @ApiModelProperty(value = "开票人", hidden = true)
    private String invoiceUserName;
}
