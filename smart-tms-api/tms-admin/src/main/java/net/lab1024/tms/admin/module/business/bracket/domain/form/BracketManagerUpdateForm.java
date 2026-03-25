package net.lab1024.tms.admin.module.business.bracket.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量修改负责人
 *
 * @author lidoudou
 * @date 2022/8/12 下午4:56
 */
@Data
public class BracketManagerUpdateForm {

    @ApiModelProperty("挂车ID列表")
    @NotNull(message = "挂车D不能为空")
    @Size(min = 1, message = "挂车ID不能为空")
    private List<Long> bracketIdList;

    @ApiModelProperty("负责人ID")
    private Long managerId;
}
