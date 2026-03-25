package net.lab1024.tms.common.module.business.driver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 驾驶证准驾车型
 *
 * @author lihaifan
 * @date 2022/7/9 11:27
 */
@AllArgsConstructor
@Getter
public enum VehicleClassEnum implements BaseEnum {
    A1(1,"A1"),
    A2(2,"A2"),
    A3(3,"A3"),
    B1(4,"B1"),
    B2(5,"B2"),
    C1(6,"C1"),
    C2(7,"C2"),
    C3(8,"C3"),
    C4(9,"C4"),
    C5(10,"C5"),
    D(11,"D"),

    ;

    private Integer value;

    private String desc;
}
