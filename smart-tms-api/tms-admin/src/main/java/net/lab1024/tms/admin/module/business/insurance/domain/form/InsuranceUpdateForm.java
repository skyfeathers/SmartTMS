package net.lab1024.tms.admin.module.business.insurance.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 * 保险基本信息
 *
 * @author lidoudou
 * @date 2022/6/21 下午4:08
 */
@Data
public class InsuranceUpdateForm extends InsuranceAddForm {

    @ApiModelProperty("保险ID")
    @NotNull(message = "保险ID不能为空")
    private Long insuranceId;

}
