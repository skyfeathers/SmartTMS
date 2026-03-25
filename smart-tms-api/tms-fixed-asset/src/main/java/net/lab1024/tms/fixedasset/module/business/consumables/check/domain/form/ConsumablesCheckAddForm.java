package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 新增盘点
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:05
 */
@Data
public class ConsumablesCheckAddForm {

    @ApiModelProperty(value = "所属公司", hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("盘点名称")
    @NotBlank(message = "盘点名称不能为空")
    private String checkName;

    @ApiModelProperty("盘点位置")
    @NotNull(message = "盘点位置不能为空")
    private Long locationId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("盘点人")
    @Size(min = 1, message = "盘点人不能为空")
    @Valid
    private List<Long> employeeIdList;
}
