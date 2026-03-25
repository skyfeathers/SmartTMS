package net.lab1024.tms.common.module.support.baidumap.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 逆地理位置编码
 *
 * @author yandy
 * @description:
 * @date 2022/12/16 4:05 下午
 */
@Data
public class BaiduReverseGeocodingQueryForm {

    @ApiModelProperty("经度")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @ApiModelProperty("维度")
    @NotNull(message = "维度不能为空")
    private BigDecimal latitude;
}