package net.lab1024.tms.fixedasset.module.business.check.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 盘点详情
 *
 * @author lidoudou
 * @date 2023/3/24 上36
 */
@Data
public class CheckDetailVO extends CheckVO {

    @ApiModelProperty("完成状态")
    private Boolean completeFlag;

    @ApiModelProperty("未盘点数量")
    private Integer notCheckCount;

    @ApiModelProperty("盘盈数量")
    private Integer profitCount;

    @ApiModelProperty("盘亏数量")
    private Integer lossCount;

    @ApiModelProperty("资产清单明细")
    private List<CheckAssetVO> assetList;

    @ApiModelProperty("盘点员工")
    private List<CheckEmployeeVO> employeeList;

}
