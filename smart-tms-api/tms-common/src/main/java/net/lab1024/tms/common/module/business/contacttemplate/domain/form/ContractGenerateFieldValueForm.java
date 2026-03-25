package net.lab1024.tms.common.module.business.contacttemplate.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * [ 合同模板字段值 ]
 *
 * @author yandanyang
 * @date 2021/8/12 15:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractGenerateFieldValueForm {

    @ApiModelProperty("字段key")
    @NotNull(message = "字段key不能为空")
    private String fieldKey;


    @ApiModelProperty("字段Value")
    @NotNull(message = "字段Value不能为空")
    private String fieldValue;

}
