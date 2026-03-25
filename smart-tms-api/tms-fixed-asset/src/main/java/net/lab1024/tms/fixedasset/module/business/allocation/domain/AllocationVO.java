package net.lab1024.tms.fixedasset.module.business.allocation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.fixedasset.module.business.allocation.constants.AllocationStatusEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 详情
 *
 * @author lidoudou
 * @date 2023/3/21 上午9:08
 */
@Data
public class AllocationVO {

    @ApiModelProperty("ID")
    private Long allocationId;

    @ApiModelProperty("编号")
    private String allocationNo;

    @ApiModelProperty("所属公司 - ID")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("调出位置 - ID")
    private Long fromLocationId;

    @ApiModelProperty("调出位置")
    private String fromLocationName;

    @ApiModelProperty("调入位置 - ID")
    private Long toLocationId;

    @ApiModelProperty("调入位置")
    private String toLocationName;

    @ApiModelPropertyEnum(value = AllocationStatusEnum.class, desc = "调拨状态")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("资产列表")
    private List<AssetVO> assetList;
}
