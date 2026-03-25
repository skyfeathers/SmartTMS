package net.lab1024.tms.common.module.business.waybill.constant;

import net.lab1024.tms.common.common.enumeration.BaseEnum;

/**
 * 凭证类型
 *
 * @author zhuoda
 * @Date 2022-07-13
 */
public enum WaybillVoucherTypeEnum implements BaseEnum {
    LOAD(1, "装货"),
    UNLOAD(2, "卸货"),

    LOAD_CONTAINER(101, "装箱"),
    UNLOAD_CONTAINER(102, "卸箱"),
    ;

    private Integer value;

    private String desc;

    WaybillVoucherTypeEnum(Integer value, String desc) {
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
