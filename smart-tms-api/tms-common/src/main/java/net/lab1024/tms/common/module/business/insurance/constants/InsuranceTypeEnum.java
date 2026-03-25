package net.lab1024.tms.common.module.business.insurance.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/***
 * 保险类型
 *
 * @author lidoudou
 * @date 2022/6/21 下午3:44
 */
@AllArgsConstructor
@Getter
public enum InsuranceTypeEnum implements BaseEnum {

    /**
     * 1 商业险
     */
    COMMERCIAL_INSURANCE(1, "商业险"),

    /**
     * 2 交强险
     */
    COMPULSORY_TRAFFIC_INSURANCE(2, "交强险"),

    /**
     * 3 超赔险
     */
    OVERPAYMENT_INSURANCE(3, "超赔险"),

    /**
     * 4 雇主责任险
     */
    EMPLOYER_RESPONSIBILITY_INSURANCE(4, "雇主责任险"),

    /**
     * 5 车船税
     */
    CAR_BOAT_TAX(5, "车船税"),

    /**
     * 6 驾乘险
     */
    DRIVER_INSURANCE(6, "驾乘险"),

    ;

    private final Integer value;

    private final String desc;
}
