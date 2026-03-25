package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeIdCardBackBO {

    /**
     * 签发机关
     */
    private String issueAuthority;

    /**
     * 有效期
     */
    private String validPeriod;
}