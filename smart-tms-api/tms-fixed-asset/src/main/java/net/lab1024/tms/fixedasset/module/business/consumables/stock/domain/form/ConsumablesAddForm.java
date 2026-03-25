package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotNull;

/**
 * 易耗品新建
 *
 * @author lidoudou
 * @date 2023/4/12 上午10:05
 */
@Data
public class ConsumablesAddForm {

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("分类ID")
    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @ApiModelProperty("易耗材名称")
    @NotNull(message = "易耗材名称不能为空")
    private String consumablesName;

    @ApiModelProperty("库存预警值")
    @NotNull(message = "库存预警值不能为空")
    private Integer stockWarningValue;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;
}
