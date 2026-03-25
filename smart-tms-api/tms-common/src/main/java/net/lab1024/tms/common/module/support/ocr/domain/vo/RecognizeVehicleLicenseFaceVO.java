package net.lab1024.tms.common.module.support.ocr.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeVehicleLicenseFaceVO {


    @ApiModelProperty("住址")
    private String address;

    @ApiModelProperty("发动机编号")
    private String engineNumber;

    @ApiModelProperty("发证日期")
    private LocalDate issueDate;

    @ApiModelProperty("品牌型号")
    private String model;

    @ApiModelProperty("所有人")
    private String owner;

    @ApiModelProperty("车牌号")
    private String licensePlateNumber;

    @ApiModelProperty("注册日期")
    private LocalDate registrationDate;

    @ApiModelProperty("使用性质")
    private String useNature;

    @ApiModelProperty("车型")
    private String vehicleType;

    @ApiModelProperty("车辆识别号码")
    private String vinCode;

    @ApiModelProperty("发证机关")
    private String issueAuthority;
}