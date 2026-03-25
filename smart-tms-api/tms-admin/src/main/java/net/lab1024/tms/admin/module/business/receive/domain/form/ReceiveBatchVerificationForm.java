package net.lab1024.tms.admin.module.business.receive.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 批量核销接口
 *
 * @author lidoudou
 * @date 2022/10/21 下午8:25
 */
@Data
public class ReceiveBatchVerificationForm {

    @ApiModelProperty("收款单ID")
    @NotNull(message = "收款单不能为空")
    private List<Long> receiveOrderIdList;

    @ApiModelProperty(value = "收款企业ID", hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("收款银行ID")
    @NotNull(message = "收款银行不能为空")
    private Long bankId;

    @ApiModelProperty("核销时间")
    @NotNull(message = "核销时间不能为空")
    private LocalDate verificationTime;

    @ApiModelProperty("凭证")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("流水单号")
    @NotBlank(message = "流水单号不能为空")
    private String sequenceCode;

    @ApiModelProperty(value = "创建人ID", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人名称", hidden = true)
    private String createUserName;

}
