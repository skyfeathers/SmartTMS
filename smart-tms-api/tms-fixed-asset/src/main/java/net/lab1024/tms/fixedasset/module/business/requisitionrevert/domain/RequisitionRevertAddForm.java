package net.lab1024.tms.fixedasset.module.business.requisitionrevert.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 新建 - 领用
 *
 * @author lidoudou
 * @date 2023/3/20 下午5:46
 */
@Data
public class RequisitionRevertAddForm {

    @ApiModelProperty("日期")
    @NotNull(message = "日期不能为空")
    private LocalDate useTime;

    @ApiModelProperty(value = "公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("领用部门")
    @NotNull(message = "领用部门不能为空")
    private Long departmentId;

    @ApiModelProperty("领用人")
    @NotNull(message = "领用人不能为空")
    private Long userId;

    @ApiModelProperty("存放位置")
    private Long locationId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("资产ID列表")
    @Size(min = 1, message = "至少选择一个资产")
    private List<Long> assetIdList;

    @ApiModelProperty(hidden = true)
    private Integer type;

}