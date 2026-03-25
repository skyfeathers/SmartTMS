package net.lab1024.tms.driver.module.business.driver.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/***
 * 银行卡信息
 *
 * @author zhaoxinyang
 * @date 2023/8/24 14:15
 */
@Data
public class DriverBankUpdateForm {

    @ApiModelProperty("银行ID")
    @NotNull(message = "银行ID不能为空")
    private Long bankId;

    @ApiModelProperty("银行类型")
    private String bankType;

    @ApiModelProperty("支行名称")
    @Length(max = 100, message = "支行名称不能超过100字符")
    private String bankBranchName;

    @ApiModelProperty("开户名")
    @Length(max = 100, message = "开户名不能超过100字符")
    private String accountName;

    @ApiModelProperty("银行账号")
    @Length(max = 100, message = "银行账号不能超过100字符")
    private String bankAccount;

    @ApiModelProperty("银行卡正面")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String bankCardFrontAttachment;

    @ApiModelProperty("是否为纳税人信息")
    private Boolean taxpayerFlag;

    @ApiModelProperty("是否默认")
    private Boolean defaultFlag;

}
