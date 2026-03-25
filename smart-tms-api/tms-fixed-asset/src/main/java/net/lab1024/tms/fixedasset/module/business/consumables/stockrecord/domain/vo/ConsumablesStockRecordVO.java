package net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存变动记录
 *
 * @author lidoudou
 * @date 2023/4/19 上午9:55
 */
@Data
public class ConsumablesStockRecordVO {

    @ApiModelProperty("耗材ID")
    private Long consumablesId;

    @ApiModelProperty("所属位置")
    private Long locationId;

    @ApiModelProperty("所属位置")
    private String locationName;

    @ApiModelProperty("变动类型")
    private Integer recordType;

    @ApiModelProperty("变动类型")
    private String recordTypeDesc;

    @ApiModelProperty("单据ID")
    private Long orderId;

    @ApiModelProperty("单据编号")
    private String orderNo;

    @ApiModelProperty("变动前库存数量")
    private Integer beforeCount;

    @ApiModelProperty("变动数量")
    private Integer changeCount;

    @ApiModelProperty("变动后数量")
    private Integer afterCount;

    @ApiModelProperty("变动前平均采购价")
    private BigDecimal beforeAveragePrice;

    @ApiModelProperty("本次采购价")
    private BigDecimal price;

    @ApiModelProperty("变动后采购价格")
    private BigDecimal afterAveragePrice;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
