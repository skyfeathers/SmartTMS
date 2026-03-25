package net.lab1024.tms.common.module.business.contract.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignTypeEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractSignerTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 创建合同
 *
 * @author lihaifan
 * @date 2022/7/15 16:34
 */
@Data
public class ContractCreateForm {


    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("合同名称")
    @NotBlank(message = "合同名称不能为空")
    @Length(max = 100, message = "合同名称不能超过100字符")
    private String contractName;

    @ApiModelProperty("合同类型")
    private Integer contractType;

    @ApiModelProperty("到期日期")
    @NotNull(message = "到期日期不能为空")
    private LocalDate expirationDate;

    @ApiModelProperty("合同负责人")
    private Long responsibleUserId;

    @ApiModelPropertyEnum(value = ContractSignTypeEnum.class,desc = "合同签署类型")
    @CheckEnum(value = ContractSignTypeEnum.class,message = "合同签署类型错误",required = true)
    private Integer signType;

    @ApiModelProperty("合同文件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String contractFile;

    @ApiModelPropertyEnum(value = ContractSignerTypeEnum.class,desc = "签署人类型")
    @CheckEnum(value = ContractSignerTypeEnum.class,message = "签署人类型错误")
    private Integer signerType;

    @ApiModelProperty("签署人归属ID（货主/车队/司机）")
    private Long signerBelongId;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "企业ID",hidden = true)
    private Long enterpriseId;
}
