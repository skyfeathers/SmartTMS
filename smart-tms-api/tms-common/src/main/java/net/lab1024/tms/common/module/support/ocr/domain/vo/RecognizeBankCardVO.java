package net.lab1024.tms.common.module.support.ocr.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/8/30 10:38 上午
 */
@Data
public class RecognizeBankCardVO {

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNumber;

    @ApiModelProperty("有效期 YYYY/MM")
    private String validToDate;

    @ApiModelProperty("卡种")
    private String cardType;
}