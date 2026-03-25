package net.lab1024.tms.fixedasset.module.business.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.fixedasset.module.business.check.constants.CheckTypeEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 新建盘点
 *
 * @author lidoudou
 * @date 2023/3/24 上午10:23
 */
@Data
public class CheckAddForm {

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("盘点名称")
    @NotBlank(message = "盘点名称不能为空")
    private String checkName;

    @ApiModelProperty("盘点类型")
    @NotNull(message = "盘点类型不能为空")
    @CheckEnum(value = CheckTypeEnum.class, message = "盘点错误", required = true)
    private Integer checkType;

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
