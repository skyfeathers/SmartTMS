package net.lab1024.tms.common.module.business.material.cost.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Getter
@AllArgsConstructor
public enum CostItemTypeEnum implements BaseEnum {
    PAY(1, "应付"),

    RECEIVE(50, "应收"),


    ;

    private Integer value;

    private String desc;

}
