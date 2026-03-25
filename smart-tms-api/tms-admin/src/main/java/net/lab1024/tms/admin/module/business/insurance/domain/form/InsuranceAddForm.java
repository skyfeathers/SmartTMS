package net.lab1024.tms.admin.module.business.insurance.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.insurance.domain.dto.InsuranceBaseDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.insurance.constants.InsuranceModuleTypeEnum;

import javax.validation.constraints.NotNull;

/***
 * 保险基本信息
 *
 * @author lidoudou
 * @date 2022/6/21 下午4:08
 */
@Data
public class InsuranceAddForm extends InsuranceBaseDTO {

    @ApiModelProperty("保险对象ID")
    @NotNull(message = "保险对象不能为空")
    private Long moduleId;

    @ApiModelPropertyEnum(value = InsuranceModuleTypeEnum.class, desc = "保险类型")
    @NotNull(message = "保险类型不能为空")
    @CheckEnum(value = InsuranceModuleTypeEnum.class, message = "保险类型不正确")
    private Integer moduleType;
}
