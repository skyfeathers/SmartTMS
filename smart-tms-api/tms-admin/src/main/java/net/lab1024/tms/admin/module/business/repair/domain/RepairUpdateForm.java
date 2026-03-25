package net.lab1024.tms.admin.module.business.repair.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/***
 * 更新维修信息
 *
 * @author lidoudou
 * @date 2022/6/27 上午10:20
 */
@Data
public class RepairUpdateForm extends RepairBaseDTO {

    @ApiModelProperty("维修表ID")
    @NotNull(message = "维修表ID不能为空")
    private Long repairId;

    @Valid
    @Size(min = 1, message = "维修内容和维修金额不能为空")
    @NotNull(message = "维修内容和维修金额不能为空")
    @ApiModelProperty("维修内容")
    private List<RepairContentForm> contentFormList;

}
