package net.lab1024.tms.admin.module.business.fleet.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 银行卡信息
 *
 * @author lidoudou
 * @date 2022/6/27 下午4:00
 */
@Data
public class FleetBankDTO {

    private Long bankId;

    @NotBlank(message = "银行类型不能为空")
    @ApiModelProperty("银行类型")
    private String bankType;

    @ApiModelProperty("银行名称")
    private String bankName;

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

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @Length(max = 255, message = "附件URL超出最大长度200")
    private String attachment;

    @ApiModelProperty("是否默认")
    @NotNull(message = "是否默认不能为空")
    private Boolean defaultFlag;
}
