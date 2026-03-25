package net.lab1024.tms.admin.module.business.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperDTO;
import net.lab1024.tms.admin.module.business.shipper.domain.dto.ShipperPrincipalDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/7/29 18:14
 */
@Data
public class ShipperListVO extends ShipperDTO {

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("客服负责人")
    private List<ShipperPrincipalDTO> shipperPrincipalList;

    @ApiModelProperty("开票抬头")
    private String invoiceName;

    @ApiModelProperty("校验标识")
    private Boolean verifyFlag;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后一次跟进时间")
    private LocalDateTime lastTrackTime;

    @ApiModelProperty("最后一次跟进内容")
    private String lastTrackContent;

    @ApiModelProperty("所属公司")
    private String enterpriseName;
}
