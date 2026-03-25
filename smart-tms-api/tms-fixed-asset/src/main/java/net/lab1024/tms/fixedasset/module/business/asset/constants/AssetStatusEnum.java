package net.lab1024.tms.fixedasset.module.business.asset.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 资产状态
 *
 * @author lidoudou
 * @date 2023/3/15 下午4:59
 */
@Getter
@AllArgsConstructor
public enum AssetStatusEnum implements BaseEnum {

    USED(1, "在用"),

    UNUSED(5, "闲置"),

    BORROW(10, "借用"),

    REQUISITION(15, "领用"),

    ALLOCATION(20, "调拨中"),

    REPAIR(25, "维修中"),

    TO_BE_REQUISITION(30, "领用待确认"),

    TO_BE_BORROW(35, "借用待确认"),

    CANCEL(40, "作废");

    private final Integer value;

    private final String desc;
}
