package net.lab1024.tms.admin.module.business.pay.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-20
 */
@Data
public class PayOrderBatchPaymentForm {

    @ApiModelProperty("付款单id")
    @NotEmpty(message = "付款单id 不能为空")
    private List<Long> payOrderIdList;

    @ApiModelProperty("银行账户id")
    @NotNull(message = "银行账户id 不能为空")
    private Long bankId;

    @ApiModelProperty("资金流水号")
    private String sequenceCode;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("付款时间")
    @NotNull(message = "付款时间不能为空")
    private LocalDateTime payTime;

}
