package net.lab1024.tms.common.module.support.ocr.domain.bo;

import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/7/19 2:04 下午
 */
@Data
public class RecognizeDrivingLicenseBO {

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别 "女"
     */
    private String sex;

    /**
     * 驾驶证号 130789198706231234
     */
    private String licenseNumber;

    /**
     * 国籍 中国
     */
    private String nationality;

    /**
     * 住址 吉林省延吉市延龙路1727号
     */
    private String address;

    /**
     * 出生日期1987-06-23
     */
    private String birthDate;

    /**
     * 初次领证日期 2016-06-21
     */
    private String initialIssueDate;

    /**
     * 认证 吉林省延边朝鲜族自治州公安局交通警察支队
     */
    private String issueAuthority;

    /**
     * 准驾车型 C1
     */
    private String approvedType;

    /**
     * 有效期 开始日期 2016-06-21
     */
    private String validFromDate;

    /**
     * 有效期 2016-06-21至2022-06-21
     */
    private String validPeriod;
}