package net.lab1024.tms.common.module.business.material.category.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 分类 枚举
 *
 * @author listen
 * @date 2021/08/05 15:26
 */
@Getter
@AllArgsConstructor
public enum CategoryTypeEnum implements BaseEnum {

    FIXED_ASSET(1, "固定资产分类"),

    CONSUMABLES(2, "耗材分类"),

    ;

    private final Integer value;

    private final String desc;
}
