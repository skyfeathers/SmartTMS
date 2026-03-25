package net.lab1024.tms.fixedasset.module.business.depreciation.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;

import java.math.BigDecimal;

/**
 * 折旧表
 *
 * @author lidoudou
 * @date 2023/4/10 下午4
 */
@Data
public class DepreciationItemVO extends AssetVO {

    @ApiModelProperty("购买价格")
    private BigDecimal price;

    @ApiModelProperty("期初、累计折旧金额")
    private BigDecimal initialDepreciationAmount;

    @ApiModelProperty("本次折旧金额")
    private BigDecimal depreciationAmount;

    @ApiModelProperty("月折旧率")
    private BigDecimal monthResidualValueRate;

    @ApiModelProperty("期末折旧金额")
    private BigDecimal endingDepreciationAmount;
}
