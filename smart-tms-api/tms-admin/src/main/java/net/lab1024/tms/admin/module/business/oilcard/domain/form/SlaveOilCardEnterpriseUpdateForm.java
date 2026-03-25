package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量更新油卡公司
 *
 * @author lidoudou
 * @date 2022/11/10 上午10:09
 */
@Data
public class SlaveOilCardEnterpriseUpdateForm {

    @ApiModelProperty("油卡ID")
    @NotNull(message = "油卡ID不能为空")
    private List<Long> oilCardIdList;

    @ApiModelProperty("公司ID")
    @NotNull(message = "公司ID不能为空")
    private List<Long> enterpriseIdList;

    @ApiModelProperty(value = "操作人", hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "操作人", hidden = true)
    private String operatorName;
}