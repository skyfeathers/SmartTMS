package net.lab1024.tms.fixedasset.module.business.requisitionrevert.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 类型
 *
 * @author lidoudou
 * @date 2023/3/21 下午2:45
 */
@Getter
@AllArgsConstructor
public enum RequisitionRevertTypeEnum implements BaseEnum {

    REQUISITION(1, "领用"),

    REVERT(2, "退还"),
    ;

    private final Integer value;

    private final String desc;
}
