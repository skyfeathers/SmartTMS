package net.lab1024.tms.admin.module.business.waybill.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 上传回单
 *
 * @author lidoudou
 * @date 2023/2/13 上午11:05
 */
@Data
public class WaybillReceiptUpdateForm {

    @ApiModelProperty("运单ID")
    @NotNull(message = "运单不能为空")
    private Long waybillId;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @NotEmpty(message = "回单凭证不能为空")
    private String receiptAttachment;

}
