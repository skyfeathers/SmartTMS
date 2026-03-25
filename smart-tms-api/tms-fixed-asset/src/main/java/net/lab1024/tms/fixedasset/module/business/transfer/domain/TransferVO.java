package net.lab1024.tms.fixedasset.module.business.transfer.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.transfer.constants.TransferStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 详情
 *
 * @author lidoudou
 * @date 2023/3/21 上午9:08
 */
@Data
public class TransferVO {

    @ApiModelProperty("ID")
    private Long transferId;

    @ApiModelProperty("编号")
    private String transferNo;

    @ApiModelProperty("公司 - ID")
    private Long enterpriseId;

    @ApiModelProperty("公司")
    private String enterpriseName;

    @ApiModelProperty("调出位置 - ID")
    private Long fromLocationId;

    @ApiModelProperty("调出位置")
    private String fromLocationName;

    @ApiModelProperty("转入位置 - ID")
    private Long toLocationId;

    @ApiModelProperty("转入位置")
    private String toLocationName;

    @ApiModelPropertyEnum(value = TransferStatusEnum.class, desc = "转移状态")
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
