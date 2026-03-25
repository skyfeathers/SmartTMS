package net.lab1024.tms.fixedasset.module.business.depreciation.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 折旧资产表
 *
 * @author lidoudou
 * @date 2023/4/10 下午4:52
 */
@Data
public class DepreciationItemForm {

    @ApiModelProperty("资产ID")
    @NotNull(message = "资产ID不能为空")
    private Long assetId;

    /**
     * 购买价格
     */
    private BigDecimal price;

    /**
     * 期初、累计折旧金额
     */
    private BigDecimal initialDepreciationAmount;

    /**
     * 本次折旧金额
     */
    private BigDecimal depreciationAmount;

    /**
     * 月折旧率
     */
    private BigDecimal residualValueRate;

    /**
     * 期末折旧金额
     */
    private BigDecimal endingDepreciationAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
