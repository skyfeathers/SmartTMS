package net.lab1024.tms.fixedasset.module.business.depreciation.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 保存
 *
 * @author lidoudou
 * @date 2023/4/10 下午5:3
 */
@Data
public class DepreciationAddForm {

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("计提日期")
    @NotNull(message = "计提日期不能为空")
    private LocalDate depreciationDate;

    @ApiModelProperty("备注")
    private String remark;
}
