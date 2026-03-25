package net.lab1024.tms.common.module.support.ocr.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 行驶证背面
 *
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeVehicleLicenseBackVO {

    @ApiModelProperty("档案编号")
    private String recordNumber;

    @ApiModelProperty("号牌号码")
    private String licensePlateNumber;

    @ApiModelProperty("核载人数")
    private String passengerCapacity;

    @ApiModelProperty("整备质量")
    private String curbWeight;

    @ApiModelProperty("总质量")
    private String totalWeight;

    @ApiModelProperty("核定载质量")
    private String permittedWeight;

    @ApiModelProperty("外廓尺寸")
    private String overallDimension;

    @ApiModelProperty("牵引总质量")
    private String tractionWeight;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("检验记录")
    private String inspectionRecord;

    @ApiModelProperty("柴油")
    private String energySign;
}