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
public class RecognizeIdCardFaceVO {

    @ApiModelProperty("姓名")
    private String name;


    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("民族")
    private String ethnicity;


    @ApiModelProperty("出生日期")
    private LocalDate birthDate;


    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("身份证号")
    private String idNumber;
}