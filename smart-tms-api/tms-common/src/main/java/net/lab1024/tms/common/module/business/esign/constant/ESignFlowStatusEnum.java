package net.lab1024.tms.common.module.business.esign.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 流程状态
 *
 * @author lihaifan
 * @date 2022/9/17 10:21
 */
@AllArgsConstructor
@Getter
public enum ESignFlowStatusEnum implements BaseEnum {

    SUCCESS(2, "已完成"),

    REVOKE(3, "已撤销"),

    OVERDUE(5, "已过期"),

    REFUSE(7, "已拒签"),

    ;

    private Integer value;

    private String desc;
}
