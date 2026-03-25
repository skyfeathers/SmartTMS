package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeIdCardFaceBO {

    private String name;

    /**
     * "女"
     */
    private String sex;

    /**
     * 汉
     */
    private String ethnicity;

    /**
     * 2006年10月2日
     */
    private String birthDate;

    /**
     * 上海市西藏南路-瞿溪路弘辉名苑
     */
    private String address;

    /**
     * 371002200610020000
     */
    private String idNumber;
}