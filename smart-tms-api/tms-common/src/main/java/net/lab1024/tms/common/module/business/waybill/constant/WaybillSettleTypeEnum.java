package net.lab1024.tms.common.module.business.waybill.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 结算类型
 *
 * @author zhuoda
 * @Date 2022-07-13
 */
public enum WaybillSettleTypeEnum implements BaseEnum {

    DRIVER(10, "司机"),

    FLEET(20, "车队"),

    ;

    private Integer value;

    private String desc;

    WaybillSettleTypeEnum(Integer value, String desc) {
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
