package net.lab1024.tms.common.module.business.esign.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 合同签署状态
 *
 * @author lihaifan
 * @date 2022/9/17 10:18
 */
@AllArgsConstructor
@Getter
public enum ESignResultEnum implements BaseEnum {

    SUCCESS(2, "签署完成"),

    FAIL(3, "失败"),

    REFUSE(4, "拒签"),

    ;

    private Integer value;

    private String desc;
}
