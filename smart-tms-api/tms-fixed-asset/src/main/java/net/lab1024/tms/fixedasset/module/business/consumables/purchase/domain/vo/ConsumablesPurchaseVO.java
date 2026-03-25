package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;

import java.time.LocalDateTime;

/**
 * 易耗品采购
 *
 * @author lidoudou
 * @date 2023/4/12 下午5:45
 */
@Data
public class ConsumablesPurchaseVO {

    @TableId(type = IdType.AUTO)
    private Long purchaseId;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("公司名称")
    private String enterpriseName;

    @ApiModelProperty("所属位置")
    private Long locationId;

    @ApiModelProperty("所属位置名称")
    private String locationName;

    @ApiModelProperty("采购编码")
    private String purchaseNo;

    @ApiModelProperty("资产来源-数据字典（ASSET-SOURCE）")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String sourceId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
