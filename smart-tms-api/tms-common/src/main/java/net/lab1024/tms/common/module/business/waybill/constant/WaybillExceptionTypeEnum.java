package net.lab1024.tms.common.module.business.waybill.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 运单异常类型
 *
 * @author lihaifan
 * @date 2022/12/3 14:22
 */
@Getter
@AllArgsConstructor
public enum WaybillExceptionTypeEnum implements BaseEnum {

    LOAD_EXCEPTION(10,"装货地点位置异常"),

    UNLOAD_EXCEPTION(20,"卸货地点位置异常"),

    ;

    private Integer value;

    private String desc;
}
