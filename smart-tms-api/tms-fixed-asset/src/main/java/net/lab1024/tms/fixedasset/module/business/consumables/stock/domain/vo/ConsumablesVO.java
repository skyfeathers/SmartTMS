package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存列表展示
 *
 * @author lidoudou
 * @date 2023/4/12 上午11:29
 */
@Data
public class ConsumablesVO {

    @ApiModelProperty("自增ID")
    private Long consumablesId;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("易耗材编码")
    private String consumablesNo;

    @ApiModelProperty("分类")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("易耗材名称")
    private String consumablesName;

    @ApiModelProperty("平均价")
    private BigDecimal averagePrice;

    @ApiModelProperty("总数量")
    private Integer stockCount;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("库存预警值")
    private Integer stockWarningValue;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
