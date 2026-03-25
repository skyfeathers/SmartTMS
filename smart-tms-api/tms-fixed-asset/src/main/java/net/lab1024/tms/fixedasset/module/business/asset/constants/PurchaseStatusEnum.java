package net.lab1024.tms.fixedasset.module.business.asset.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 资产采购状态
 *
 * @author lidoudou
 * @date 2023/3/18 下午5:24
 */
@Getter
@AllArgsConstructor
public enum PurchaseStatusEnum implements BaseEnum {

    DRAFT(1, "草稿"),

    COMPLETE(2, "已完成"),

    CANCEL(3, "已取消"),
    ;

    private final Integer value;

    private final String desc;
}
