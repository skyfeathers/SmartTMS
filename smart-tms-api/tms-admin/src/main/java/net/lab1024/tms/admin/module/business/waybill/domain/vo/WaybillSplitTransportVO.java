package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2024/5/14 6:19 下午
 */
@Data
public class WaybillSplitTransportVO {

    @ApiModelProperty("分段id")
    private Long splitTransportId;

    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("司机id")
    private Long driverId;

    @ApiModelProperty("司机名称")
    private String driverName;

    @ApiModelProperty("司机手机号")
    private String driverTelephone;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("市")
    private Integer city;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区县")
    private Integer district;

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String address;


    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}