package net.lab1024.tms.fixedasset.module.business.purchase.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.DictValueVoDeserializer;
import net.lab1024.tms.common.common.serializer.DictValueVoSerializer;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:05
 */
@Data
public class PurchaseVO {

    @ApiModelProperty("采购ID")
    private Long purchaseId;

    @ApiModelProperty("采购编码")
    private String purchaseNo;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("资产来源-数据字典（ASSET-SOURCE）")
    @JsonSerialize(using = DictValueVoSerializer.class)
    @JsonDeserialize(using = DictValueVoDeserializer.class)
    private String sourceId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("资产清单明细")
    private List<AssetVO> assetList;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
