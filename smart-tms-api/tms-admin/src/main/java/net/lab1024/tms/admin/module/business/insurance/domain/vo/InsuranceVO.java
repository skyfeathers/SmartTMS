package net.lab1024.tms.admin.module.business.insurance.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.insurance.domain.dto.InsuranceBaseDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncrypt;

import java.time.LocalDateTime;

/***
 * 保险基本信息
 *
 * @author lidoudou
 * @date 2022/6/21 下午3:35
 */
@Data
public class InsuranceVO extends InsuranceBaseDTO {

    @FieldEncrypt
    @ApiModelProperty("保单号")
    private String encryptPolicyNumber;

    @ApiModelProperty("保险ID")
    private Long insuranceId;

    @ApiModelProperty("保险公司名称")
    private String insuranceCompanyName;

    @ApiModelPropertyEnum(value = InsuranceModuleTypeEnum.class, desc = "保险类型")
    private Integer moduleType;

    @ApiModelProperty("保险对象ID")
    private Long moduleId;

    @FieldEncrypt
    @ApiModelProperty("保险对象名称")
    private String moduleName;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
