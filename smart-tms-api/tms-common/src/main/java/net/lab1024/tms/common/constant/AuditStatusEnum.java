package net.lab1024.tms.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 审核状态
 *
 * @author lidoudou
 * @date 2022/6/21 上午10:25
 */
@AllArgsConstructor
@Getter
public enum AuditStatusEnum implements BaseEnum {

    /**
     * 1 待审核
     */
    WAIT_AUDIT(1, "待审核"),

    /**
     * 2 审核通过
     */
    AUDIT_PASS(2, "审核通过"),

    /**
     * 3 审核驳回
     */
    REJECT(3, "审核驳回"),
    ;

    private final Integer value;

    private final String desc;
}
