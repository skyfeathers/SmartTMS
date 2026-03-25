package net.lab1024.tms.admin.module.business.oilcard.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;

@Data
public class OilCardEnterpriseVO extends EnterpriseVO {

    @ApiModelProperty("油卡ID")
    private Long oilCardId;
}
