package net.lab1024.tms.fixedasset.module.business.allocation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 新建 - 调拨
 *
 * @author lidoudou
 * @date 2023/3/20 下午5:46
 */
@Data
public class AllocationAddForm {

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("调出位置")
    @NotNull(message = "调出位置不能为空")
    private Long fromLocationId;

    @ApiModelProperty("掉入位置")
    @NotNull(message = "调入位置不能为空")
    private Long toLocationId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("资产ID列表")
    @Size(min = 1, message = "至少选择一个资产")
    private List<Long> assetIdList;

}