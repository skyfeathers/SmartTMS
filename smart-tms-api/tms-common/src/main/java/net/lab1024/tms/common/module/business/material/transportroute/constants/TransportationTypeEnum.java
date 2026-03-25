package net.lab1024.tms.common.module.business.material.transportroute.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 运输类型
 *
 * @author lidoudou
 * @date 2022/7/11 下午5:46
 */
@AllArgsConstructor
@Getter
public enum TransportationTypeEnum implements BaseEnum {

    CONTAINER_TRANSPORT(1002998, "集装箱运输"),

    GENERAL_GOODS_TRANSPORT(1002996, "普货运输"),
    ;

    private Integer value;

    private String desc;

}
