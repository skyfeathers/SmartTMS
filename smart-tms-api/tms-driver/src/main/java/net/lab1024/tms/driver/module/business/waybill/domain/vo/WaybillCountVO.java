package net.lab1024.tms.driver.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 运单数量和运输完成运单数量
 *
 * @author zhaoxinyang
 * @Date 2023-09-01 10:50
 */
@Data
public class WaybillCountVO {

    @ApiModelProperty("总运单数量")
    private Integer waybillCount;

    @ApiModelProperty("运输完成运单数量")
    private Integer waybillCompleteCount;

}
