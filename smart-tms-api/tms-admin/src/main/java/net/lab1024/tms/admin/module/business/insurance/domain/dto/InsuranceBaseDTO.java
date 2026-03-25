package net.lab1024.tms.admin.module.business.insurance.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceTypeEnum;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncrypt;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/***
 * 保险基本信息
 *
 * @author lidoudou
 * @date 2022/6/21 下午3:35
 */
@Data
public class InsuranceBaseDTO {

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("保单号")
    @NotBlank(message = "保单号不能为空")
    private String policyNumber;

    @ApiModelProperty("保险公司代码")
    @NotBlank(message = "保险公司代码不能为空")
    private String insuranceCompanyCode;

    @ApiModelPropertyEnum(value = InsuranceTypeEnum.class, desc = "保险类型")
    @NotNull(message = "保险类型不能为空")
    @CheckEnum(value = InsuranceTypeEnum.class, message = "保险类型错误")
    private Integer insuranceType;

    @ApiModelProperty("保险金额")
    @NotNull(message = "保险金额不能为空")
    private BigDecimal insuranceAmount;

    @ApiModelProperty("保险单费用")
    @NotNull(message = "保险单费用不能为空")
    private BigDecimal policyInsuranceAmount;

    @ApiModelProperty(value = "投保时间", example = "2022-01-01")
    @NotNull(message = "投保时间不能为空")
    private LocalDate effectDate;

    @ApiModelProperty(value = "有效期至", example = "2022-01-01")
    @NotNull(message = "有效期至不能为空")
    private LocalDate expireDate;

    @ApiModelProperty("附件")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty(value = "登记日期", example = "2022-01-01")
    private LocalDate enrollDate;

    @ApiModelProperty("备注")
    private String remark;
}
