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
public class RecognizeIdCardBackVO {

    @ApiModelProperty("有效期开始时间")
    private LocalDate validPeriodStartDate;

    @ApiModelProperty("出生日期")
    private LocalDate validPeriodEndDate;

    @ApiModelProperty("是否长期")
    private Boolean idCardEndlessFlag;
}