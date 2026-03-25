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
 * @author lidoudou
 * @date 2022/6/22 下午2:36
 */
@Data
public class DriverBankBaseForm {

    @NotBlank(message = "银行类型不能为空")
    @ApiModelProperty("银行类型")
    private String bankType;

    @ApiModelProperty("支行名称")
    @NotBlank(message = "支行名称不能为空")
    @Length(max = 100, message = "支行名称不能超过100字符")
    private String bankBranchName;

    @ApiModelProperty("开户名")
    @NotBlank(message = "开户名不能为空")
    @Length(max = 100, message = "开户名不能超过100字符")
    private String accountName;

    @ApiModelProperty("银行账号")
    @NotBlank(message = "银行账号不能为空")
    @Length(max = 100, message = "银行账号不能超过100字符")
    private String bankAccount;

    @ApiModelProperty("银行卡正面")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String bankCardFrontAttachment;

    @ApiModelProperty("是否为纳税人信息")
    private Boolean taxpayerFlag;

    @ApiModelProperty("是否默认")
    @NotNull(message = "是否默认不能为空")
    private Boolean defaultFlag;
}
