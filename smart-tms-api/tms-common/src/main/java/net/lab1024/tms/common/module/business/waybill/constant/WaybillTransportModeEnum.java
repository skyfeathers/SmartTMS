package net.lab1024.tms.common.module.business.waybill.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;
/**
 * 运输方式
 *
 * @author lidoudou
 * @date 2023/5/9 上午8:55
 */
public enum WaybillTransportModeEnum implements BaseEnum {

    LONG_DISTANCE(10, "长途"),

    SHORT_BARGE_INNER(20, "短驳（内部）"),

    SHORT_BARGE_OUTSIDE(30, "短驳（外部）"),

    ADDITIONAL(40, "补单"),
    ;

    private Integer value;

    private String desc;

    WaybillTransportModeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
