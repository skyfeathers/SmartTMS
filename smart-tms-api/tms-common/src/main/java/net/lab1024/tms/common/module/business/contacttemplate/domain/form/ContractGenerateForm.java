package net.lab1024.tms.common.module.business.contacttemplate.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/12 15:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractGenerateForm {

    @ApiModelProperty("模板id")
    @NotNull(message = "模板id不能为空")
    private Long templateId;

    @ApiModelProperty("单号")
    @NotNull(message = "单号不能为空")
    private String serialNumber;

    @ApiModelProperty("模板字段值")
    @NotEmpty(message = "模板字段值不能为空")
    private List<ContractGenerateFieldValueForm> fieldValueList;

}
