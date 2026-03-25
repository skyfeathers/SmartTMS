package net.lab1024.tms.fixedasset.module.business.check.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;
import net.lab1024.tms.fixedasset.module.business.check.constants.CheckStatusEnum;

import java.time.LocalDateTime;

/**
 * 盘点资产
 *
 * @author lidoudou
 * @date 2023/3/24 上午11:21
 */
@Data
public class CheckAssetVO extends AssetVO {

    private Long itemId;

    @ApiModelPropertyEnum(desc = "盘点状态", value = CheckStatusEnum.class)
    private Integer status;

    @ApiModelProperty("盘点人")
    private String employeeName;

    @ApiModelProperty("盘点时间")
    private LocalDateTime checkTime;
}
