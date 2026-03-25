package net.lab1024.tms.common.module.business.expiredcertificate.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 到期证件 状态 枚举类
 *
 * @author listen
 * @date 2022/07/19 10:20
 */
@Getter
@AllArgsConstructor
public enum ExpiredCertificateStatusEnum implements BaseEnum {

    EXPIRED(0, "已到期"),
    EXPIRED_3_DAYS(3, "3天内到期"),
    EXPIRED_7_DAYS(7, "7天内到期"),
    EXPIRED_15_DAYS(15, "15天内到期"),
    EXPIRED_30_DAYS(30, "30天内到期"),
    UNEXPIRED(99, "未到期"),
    ;

    private final Integer value;

    private final String desc;
}
