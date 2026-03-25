package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货主关联的公司信息
 *
 * @author lidoudou
 * @date 2022/8/18 下午5:34
 */
@Data
public class ShipperEnterpriseVO {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("企业ID")
    private Long enterpriseId;

    @ApiModelProperty("企业名称")
    private String enterpriseName;
}
