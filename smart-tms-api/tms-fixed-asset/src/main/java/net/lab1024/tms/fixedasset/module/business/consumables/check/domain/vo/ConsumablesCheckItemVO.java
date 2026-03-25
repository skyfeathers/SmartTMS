package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.fixedasset.module.business.consumables.check.constants.ConsumablesCheckStatusEnum;

import java.time.LocalDateTime;

/**
 * 盘点易耗品
 *
 * @author lidoudou
 * @date 2023/3/24 上午11:21
 */
@Data
public class ConsumablesCheckItemVO {

    @ApiModelProperty("自增ID")
    private Long consumablesId;

    @ApiModelProperty("易耗材编码")
    private String consumablesNo;

    @ApiModelProperty("易耗材名称")
    private String consumablesName;

    @ApiModelProperty("库存数量")
    private Integer stockCount;

    @ApiModelProperty("分类")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    private Long itemId;

    @ApiModelPropertyEnum(desc = "盘点状态", value = ConsumablesCheckStatusEnum.class)
    private Integer status;

    @ApiModelProperty("盘点数量")
    private Integer count;

    @ApiModelProperty("盘点人")
    private String employeeName;

    @ApiModelProperty("盘点时间")
    private LocalDateTime checkTime;

    @ApiModelProperty("备注")
    private String remark;
}
