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

/**
 * 上传充值附件
 *
 * @author lidoudou
 * @date 2022/10/28 下午3:19
 */
@Data
public class OilCardRechargeForm {

    @ApiModelProperty("付款单ID")
    @NotNull(message = "付款单不能为空")
    private Long payOrderId;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @NotEmpty(message = "充值凭证不能为空")
    private String attachment;

    @ApiModelProperty("充值时间")
    @NotNull(message = "充值时间不能为空")
    private LocalDateTime rechargeTime;
}
