package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/8/30 10:38 上午
 */
@Data
public class RecognizeBankCardBO {

    /**
     * 银行
     */
    private String bankName;
    /**
     * 银行卡号
     */
    private String cardNumber;

    /**
     * 有效期至
     */
    private String validToDate;

    /**
     * 卡种
     */
    private String cardType;
}