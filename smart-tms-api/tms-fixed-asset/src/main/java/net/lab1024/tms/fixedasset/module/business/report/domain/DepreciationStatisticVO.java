package net.lab1024.tms.fixedasset.module.business.report.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 折扣明细
 *
 * @author lidoudou
 * @date 2023/3/29 上午11:44
 */
@Data
public class DepreciationStatisticVO {

    @ApiModelProperty("资产ID")
    private Long assetId;

    @ApiModelProperty("资产编号")
    private String assetNo;

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("所属公司 - ID")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("所属分类 - ID")
    private Long categoryId;

    @ApiModelProperty("所属分类")
    private String categoryName;

    @ApiModelProperty("预计使用期限")
    private Integer monthCount;

    @ApiModelProperty("已使用期限")
    private Integer usedMonthCount;

    @ApiModelProperty("剩余使用期限")
    private Integer surplusMonthCount;

    @ApiModelProperty("购买价格")
    private BigDecimal price;

    @ApiModelProperty("累计折旧")
    private BigDecimal totalDepreciation;

    @ApiModelProperty("剩余净值")
    private BigDecimal residualValue;

    @ApiModelProperty("残值率")
    private BigDecimal residualValueRate;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
