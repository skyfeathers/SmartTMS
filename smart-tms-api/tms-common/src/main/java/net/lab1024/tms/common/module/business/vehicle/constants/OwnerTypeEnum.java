package net.lab1024.tms.common.module.business.vehicle.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 所属人性质
 *
 * @author lidoudou
 * @date 2022/6/25 下午3:13
 */
@AllArgsConstructor
@Getter
public enum OwnerTypeEnum implements BaseEnum {
    /**
     * 1 个人
     */
    PERSONAL(1, "个人"),

    /**
     * 2 企业
     */
    ENTERPRISE(2, "企业"),

    ;

    private final Integer value;

    private final String desc;
}
