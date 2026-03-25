package net.lab1024.tms.common.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */
@Data
public class WaybillTraceVO {

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("位置")
    private String location;

    @ApiModelProperty("位置")
    private LocalDateTime createTime;

}
