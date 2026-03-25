package net.lab1024.tms.common.module.business.oilcard.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * @author yandy
 * @description:
 * @date 2022/7/14 7:57 下午
 */
@AllArgsConstructor
@Getter
public enum OilCardTypeEnum implements BaseEnum {

    MASTER(1, "主卡"),

    SLAVE(2, "副卡"),
    ;

    private Integer value;

    private String desc;

}