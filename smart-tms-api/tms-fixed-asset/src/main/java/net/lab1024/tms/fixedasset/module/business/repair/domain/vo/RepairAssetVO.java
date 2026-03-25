package net.lab1024.tms.fixedasset.module.business.repair.domain.vo;

import lombok.Data;
import net.lab1024.tms.fixedasset.module.business.asset.domain.vo.AssetVO;

@Data
public class RepairAssetVO extends AssetVO {

    private Long repairId;

    private Integer assetStatus;
}
