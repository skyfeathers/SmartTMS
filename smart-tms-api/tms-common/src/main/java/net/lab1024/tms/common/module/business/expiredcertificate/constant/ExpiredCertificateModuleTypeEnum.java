package net.lab1024.tms.common.module.business.expiredcertificate.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 到期证件 业务类型 枚举类
 *
 * @author listen
 * @date 2022/07/19 10:20
 */
@Getter
@AllArgsConstructor
public enum ExpiredCertificateModuleTypeEnum implements BaseEnum {

    DRIVER(1, "司机模块"),
    SHIPPER(2, "货主模块"),
    VEHICLE(3, "车辆模块"),
    BRACKET(4, "挂车模块"),
    ;

    private final Integer value;

    private final String desc;
}
